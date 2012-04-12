package com.cs4900.signalseeker;

import com.cs4900.signalseeker.data.DataEntry;
import com.cs4900.signalseeker.data.DataList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataPoint extends Activity {
	DataEntry entry;
	DataList dataList;

	TextView location;
	EditText latitude;
	EditText longitude;
	EditText wifi;
	EditText cell;
	EditText carrier;
	Button update;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.datapoint);

		location = (TextView) findViewById(R.id.update_location);
		latitude = (EditText) findViewById(R.id.update_latitude);
		longitude = (EditText) findViewById(R.id.update_longitude);
		carrier = (EditText) findViewById(R.id.update_carrier);
		wifi = (EditText) findViewById(R.id.update_wifi);
		cell = (EditText) findViewById(R.id.update_cell);
		update = (Button) findViewById(R.id.Update);

	}

	public void onResume() {
		super.onResume();

		entry = DataEntry.fromBundle(getIntent().getExtras());
		location.setText(entry.getLocation());
		latitude.setText(String.valueOf(entry.getLatitude()));
		longitude.setText(String.valueOf(entry.getLongitude()));
		wifi.setText(String.valueOf(entry.getWifi()));
		cell.setText(String.valueOf(entry.getCell()));
		carrier.setText(entry.getCarrier());

		update.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					update();
				} catch (Exception e) {
				}
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, R.string.delete).setIcon(
				android.R.drawable.ic_menu_delete);
		return true;
	}

	public boolean onMenuItemSelected(int id, MenuItem item) {
		dataList = DataList.parse(DataPoint.this);
		dataList.replace(entry);
		Intent intent = new Intent(this, DataListView.class);
		startActivity(intent);
		return true;
	}

	public void update() {
		entry.setLocation(location.getText().toString());
		entry.setLatitude(Double.parseDouble(latitude.getText().toString()));
		entry.setLongitude(Double.parseDouble(longitude.getText().toString()));
		entry.setWifi(Integer.parseInt(wifi.getText().toString()));
		entry.setCell(Integer.parseInt(cell.getText().toString()));
		entry.setCarrier(carrier.getText().toString());

		dataList = DataList.parse(DataPoint.this);
		dataList.replace(entry);

		finish();
	}
}
