package com.cs4900.signalseeker;

import com.cs4900.signalseeker.data.DataEntry;
import com.cs4900.signalseeker.data.DataList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewDataPoint extends Activity {
	double latitude;
	double longitude;

	protected static final String CLASSTAG = NewDataPoint.class.getSimpleName();

	private DataEntry newDataEntry;
	private DataList dataList;

	private EditText locationEditText;
	private TextView latitudeTextView;
	private TextView longitudeTextView;
	private TextView carrierTextView;
	private TextView wifiTextView;
	private Spinner cellSpinner;
	private Button submitButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newdatapoint);

		newDataEntry = new DataEntry();

		initializeNewDataPoint();

		// Location view
		locationEditText = (EditText) findViewById(R.id.new_location);

		// Latitude view
		latitudeTextView = (TextView) findViewById(R.id.new_latitude);
		latitudeTextView.setText("" + getIntent().getDoubleExtra("latitude", 0));

		// Longitude view
		longitudeTextView = (TextView) findViewById(R.id.new_longitude);
		longitudeTextView.setText("" + getIntent().getDoubleExtra("longitude", 0));

		// Wifi signal strength view
		wifiTextView = (TextView) findViewById(R.id.new_wifi);
		wifiTextView.setText("" + newDataEntry.getWifi());

		// Carrier name view
		carrierTextView = (TextView) findViewById(R.id.new_carrier);
		carrierTextView.setText(newDataEntry.getCarrier());

		// Carrier signal strength view
		cellSpinner = (Spinner) findViewById(R.id.new_cell_spinner);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.cell_signal_array,
				android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cellSpinner.setAdapter(arrayAdapter);

		// Submit button view
		submitButton = (Button) findViewById(R.id.submit_button);
		submitButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					if (locationEditText.getText().toString().equals("")) {
						Context context = getApplicationContext();
						CharSequence text = "There are empty fields. Cannot submit!";
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
					else {
						newDataEntry.setLocation(locationEditText.getText().toString());
						newDataEntry.setCell(cellSpinner.getSelectedItemPosition());
						dataList = DataList.parse(NewDataPoint.this);
						dataList.create(newDataEntry);
						finish();
					}

				} catch (Exception e) {
					Log.i(Constants.LOGTAG + ": " + EntryPage.CLASSTAG, "Failed to Submit new Data Point" + e.getMessage() + "]");
				}
			}
		});

	}

	public void initializeNewDataPoint() {

		newDataEntry.setLatitude(getIntent().getDoubleExtra("latitude", 0));
		newDataEntry.setLongitude(getIntent().getDoubleExtra("longitude", 0));

		// Wifi signal strength
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		newDataEntry.setWifi(wifiManager.calculateSignalLevel(0, 5));

		// Carrier name
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		newDataEntry.setCarrier(telephonyManager.getNetworkOperatorName());

	}
}