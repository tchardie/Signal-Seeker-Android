package com.cs4900.signalseeker;

import com.cs4900.signalseeker.data.DataEntry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataPoint extends Activity{
	DataEntry entry;
	
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
		
		location = (TextView) findViewById(R.id.location);
		latitude = (EditText) findViewById(R.id.latitude);
		longitude = (EditText) findViewById(R.id.longitude);
		carrier = (EditText) findViewById(R.id.carrier);
		wifi = (EditText) findViewById(R.id.wifi);
		cell = (EditText) findViewById(R.id.cell);
		
		update = (Button) findViewById(R.id.Update);
		
	}
	
	public void onResume(){
		super.onResume();
		
		entry = DataEntry.fromBundle(getIntent().getExtras());
		location.setText(entry.get_location());
		latitude.setText(String.valueOf(entry.get_latitude()));
		longitude.setText(String.valueOf(entry.get_longitude()));
		wifi.setText(String.valueOf(entry.get_wifi()));
		cell.setText(String.valueOf(entry.get_cell()));
		carrier.setText(entry.get_carrier());
		
		update.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					update();
				} catch (Exception e) {
				}
			}
		});
	}
	
	public void update(){
		
	}
}
