package com.cs4900.signalseeker;

import java.util.List;

import com.cs4900.signalseeker.data.CustomizedOverlay;
import com.cs4900.signalseeker.data.DataEntry;
import com.cs4900.signalseeker.data.DataList;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
	double lat = 30.8469348;
	double lon = -83.2893965;

	private MapView mapView;
	private MapController mapController;
	private EditText locationText;
	private Button submitSignalButton;
	private List<DataEntry> list;
	private GeoPoint point;

	LocationManager lm;
	LocationListener ll;
	Location location;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// make starting point
		point = new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);

		// center map on the point
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(point);

		submitSignalButton = (Button) findViewById(R.id.entry_add_button);

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		submitSignalButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					Intent intent = new Intent(EntryPage.this,
							NewDataPoint.class);
					intent.putExtra("lat", location.getLatitude());
					intent.putExtra("lon", location.getLongitude());
					startActivity(intent);

				} catch (Exception e) {
					Log.i(Constants.LOGTAG + ": " + EntryPage.CLASSTAG,
							"Failed to load New Data Point page"
									+ e.getMessage() + "]");
				}
			}
		});

	}

	public void onResume() {
		super.onResume();
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.point);
		CustomizedOverlay itemizedOverlay = new CustomizedOverlay(drawable,
				this);
		list = DataList.parse(this).getAllDataEntries();
		OverlayItem overlayItem = new OverlayItem(point, "1", "2");
		itemizedOverlay.addOverlay(overlayItem);

		// mapOverlays.add(itemizedOverlay);

		for (int i = 0; i < list.size(); i++) {
			DataEntry d = list.get(i);
			GeoPoint p = new GeoPoint((int) (d.getLatitude() * 1e6),
					(int) (d.getLongitude() * 1e6));
			OverlayItem o = new OverlayItem(p, d.getLocation(),
					String.valueOf(d.getWifi()));
			itemizedOverlay.addOverlay(o);
		}

		mapOverlays.add(itemizedOverlay);

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