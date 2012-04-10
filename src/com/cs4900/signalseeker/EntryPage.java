package com.cs4900.signalseeker;

import com.depot.cs4900.Constants;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EntryPage extends MapActivity {
	
	//starting location
	private double lat = 30.8469348;
	private double lon = -83.2893965;

	private MapView mapView;
	private MapController mapController;

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

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, R.string.menu_new).setIcon(
				android.R.drawable.btn_plus);
		return true;
	}

	public boolean onMenuItemSelected(int id, MenuItem item) {
		Intent intent = new Intent(Constants.INTENT_ACTION_NEW_DATA_ENTRY);
		startActivity(intent);
		return true;
	}

}