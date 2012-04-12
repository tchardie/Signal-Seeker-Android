package com.cs4900.signalseeker;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EntryPage extends MapActivity {

	protected static final String CLASSTAG = EntryPage.class.getSimpleName();
	// starting location
	private double lat = 30.8469348;
	private double lon = -83.2893965;

	private MapView mapView;
	private MapController mapController;
	private EditText locationText;
	private Button submitSignalButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// make starting point
		GeoPoint point = new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);

		// center map on the point
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(point);

		submitSignalButton = (Button) findViewById(R.id.entry_add_button);
		submitSignalButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {					
					Intent intent = new Intent(
							Constants.INTENT_ACTION_NEW_DATA_POINT);
					startActivity(intent);

				} catch (Exception e) {
					Log.i(Constants.LOGTAG + ": " + EntryPage.CLASSTAG, "Failed to load New Data Point page"
							+ e.getMessage() + "]");
				}
			}
		});

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, R.string.menu_settings).setIcon(
				android.R.drawable.ic_menu_manage);
		return true;
	}

	public boolean onMenuItemSelected(int id, MenuItem item) {

		// Intent intent = new Intent(Constants.INTENT_ACTION_SETTINGS);
		// startActivity(intent);
		Intent intent = new Intent(EntryPage.this, DataListView.class);
		startActivity(intent);
		return true;
	}

}