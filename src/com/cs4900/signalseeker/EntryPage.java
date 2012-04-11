package com.cs4900.signalseeker;

import com.cs4900.signalseeker.Constants;
import com.cs4900.signalseeker.R;
import com.cs4900.signalseeker.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EntryPage extends MapActivity {
	
	//starting location
	private double lat = 30.8469348;
	private double lon = -83.2893965;

	private MapView mapView;
	private MapController mapController;
	private EditText location_text;
	private Button submit_button;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//make starting point 
		GeoPoint point = new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		
		//center map on the point
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(point);
		
		location_text = (EditText)findViewById(R.id.new_location);
		submit_button = (Button)findViewById(R.id.entry_add_button);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, R.string.menu_settings).setIcon(android.R.drawable.ic_menu_manage);
		return true;
	}

	public boolean onMenuItemSelected(int id, MenuItem item) {
		//Intent intent = new Intent(Constants.INTENT_ACTION_SETTINGS);
		//startActivity(intent);
		
		Intent intent = new Intent(EntryPage.this, DataListView.class);
		startActivity(intent);
		return true;
	}

}