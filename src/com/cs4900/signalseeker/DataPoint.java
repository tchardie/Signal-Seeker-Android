package com.cs4900.signalseeker;

import java.util.HashMap;

import org.apache.http.client.ResponseHandler;

import com.cs4900.signalseeker.data.DataEntry;
import com.cs4900.signalseeker.data.DataList;
import com.cs4900.signalseeker.network.HTTPRequestHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataPoint extends Activity {

	private static final String CLASSTAG = DataPoint.class.getSimpleName();

	DataEntry entry;
	DataList dataList;

	TextView location;
	EditText latitude;
	EditText longitude;
	EditText wifi;
	EditText carrier;
	EditText cell;
	Button update;

	private ProgressDialog progressDialog;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOGTAG, " " + DataPoint.CLASSTAG + " update/delete worker thread done.");
			progressDialog.dismiss();

			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.datapoint);

		location = (TextView) findViewById(R.id.update_location);
		latitude = (EditText) findViewById(R.id.update_latitude);
		longitude = (EditText) findViewById(R.id.update_longitude);
		wifi = (EditText) findViewById(R.id.update_wifi);
		carrier = (EditText) findViewById(R.id.update_carrier);
		cell = (EditText) findViewById(R.id.update_cell);
		update = (Button) findViewById(R.id.Update);

		update.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					updateDataPoint();
				} catch (Exception e) {
				}
			}
		});
	}

	public void onResume() {
		super.onResume();

		entry = DataEntry.fromBundle(getIntent().getExtras());
		location.setText(entry.getLocation());
		latitude.setText(String.valueOf(entry.getLatitude()));
		longitude.setText(String.valueOf(entry.getLongitude()));
		wifi.setText(String.valueOf(entry.getWifi()));
		carrier.setText(entry.getCarrier());
		cell.setText(String.valueOf(entry.getCell()));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, R.string.delete).setIcon(android.R.drawable.ic_menu_delete);
		return true;
	}

	public boolean onMenuItemSelected(int id, MenuItem item) {
		dataList = DataList.parse(DataPoint.this);
		deleteDataPoint();
		Intent intent = new Intent(this, DataListView.class);
		startActivity(intent);
		return true;
	}

	public void updateDataPoint() {

		final ResponseHandler<String> responseHandler = HTTPRequestHelper.getResponseHandlerInstance(this.handler);

		final HashMap<String, String> params = new HashMap<String, String>();
		if (!location.getText().toString().equals("")) {
			params.put("location", location.getText().toString());
		}
		if (!latitude.getText().toString().equals("")) {
			params.put("latitude", latitude.getText().toString());
		}
		if (!longitude.getText().toString().equals("")) {
			params.put("longitude", longitude.getText().toString());
		}
		if (!wifi.getText().toString().equals("")) {
			params.put("wifi", wifi.getText().toString());
		}
		if (!carrier.getText().toString().equals("")) {
			params.put("carrier", carrier.getText().toString());
		}
		if (!cell.getText().toString().equals("")) {
			params.put("cell", cell.getText().toString());
		}

		this.progressDialog = ProgressDialog.show(this, " Working...", " Updating Signal Point", true, false);

		entry.setLocation(location.getText().toString());
		entry.setLatitude(Double.parseDouble(latitude.getText().toString()));
		entry.setLongitude(Double.parseDouble(longitude.getText().toString()));
		entry.setWifi(Integer.parseInt(wifi.getText().toString()));
		entry.setCarrier(carrier.getText().toString());
		entry.setCell(Integer.parseInt(cell.getText().toString()));

		dataList = DataList.parse(DataPoint.this);
		dataList.replace(entry);

		// update dataPoint on the server in a separate thread for
		// ProgressDialog/Handler
		// when complete send "empty" message to handler
		new Thread() {
			@Override
			public void run() {
				// networking stuff ...
				HTTPRequestHelper helper = new HTTPRequestHelper(responseHandler);
				helper.performPut(HTTPRequestHelper.MIME_TEXT_PLAIN, "http://signalseeker.herokuapp.com/data/" + entry.getId() + ".xml", null, null,
						null, params);
			}
		}.start();

		finish();
	}

	public void deleteDataPoint() {

		Log.v(Constants.LOGTAG, " " + DataPoint.CLASSTAG + " deleteDataPoint");

		// Get ready to send the HTTP DELETE request to update the Points data
		// the server
		final ResponseHandler<String> responseHandler = HTTPRequestHelper.getResponseHandlerInstance(this.handler);

		this.progressDialog = ProgressDialog.show(this, " Working...", " Deleting Signal Point", true, false);

		dataList = DataList.parse(DataPoint.this);
		dataList.delete(entry);

		// delete data point on the server in a separate thread for
		// ProgressDialog/Handler
		// when complete send "empty" message to handler
		new Thread() {
			@Override
			public void run() {
				// networking stuff ...
				HTTPRequestHelper helper = new HTTPRequestHelper(responseHandler);
				helper.performDelete(HTTPRequestHelper.MIME_TEXT_PLAIN, "http://signalseeker.herokuapp.com/data/" + entry.getId() + ".xml", null,
						null, null, null);
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
}
