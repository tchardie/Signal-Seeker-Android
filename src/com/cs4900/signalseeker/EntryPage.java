package com.cs4900.signalseeker;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.os.Bundle;

public class EntryPage extends MapActivity {
	double lat = 30.8469348;
	double lon = -83.2893965;

	MapView mapView;
	MapController mapController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		GeoPoint point = new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(point);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}