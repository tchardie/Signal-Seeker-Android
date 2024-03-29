package com.cs4900.signalseeker;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.http.client.ResponseHandler;

import com.cs4900.signalseeker.data.CustomizedOverlay;
import com.cs4900.signalseeker.data.DataEntry;
import com.cs4900.signalseeker.data.DataList;
import com.cs4900.signalseeker.network.HTTPRequestHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EntryPage extends MapActivity {

	private static final String CLASSTAG = EntryPage.class.getSimpleName();
	private static final int MENU_VIEW_POINTS = Menu.FIRST;
	private static final int MENU_REFRESH_MAP = Menu.FIRST + 1;

	private MapView mapView;
	private MapController mapController;
	private Button submitSignalButton;
	private List<DataEntry> list;

	LocationManager lm;
	LocationListener ll;
	Location location;

	ProgressDialog progressDialog;

	private final Handler progressHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {

			progressDialog.dismiss();

			String bundleResult = msg.getData().getString("RESPONSE");

			if (bundleResult.startsWith("Error")) {
				Toast.makeText(EntryPage.this, bundleResult, Toast.LENGTH_LONG)
						.show();
				finish();
			}

			try {
				FileOutputStream fos = getApplication().getApplicationContext()
						.openFileOutput("data.xml", Context.MODE_PRIVATE);
				fos.write(bundleResult.getBytes());
				fos.flush();
				fos.close();

			} catch (Exception e) {
				Log.d("Signal Seeker", "Exception: " + e.getMessage());
				finish();
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.progressDialog = ProgressDialog.show(this, "Loading Map",
				"Please Wait", true, false);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);

		mapController = mapView.getController();
		mapController.setZoom(18);
		GeoPoint vsuGeoPoint = new GeoPoint((int) (30.848466 * 1E6),
				(int) (-83.289569 * 1E6));
		mapController.setCenter(vsuGeoPoint);

		submitSignalButton = (Button) findViewById(R.id.entry_add_button);

		submitSignalButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				try {
					lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					ll = new MyLocationListener();
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
							0, ll);
					location = lm
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);

					Intent intent = new Intent(EntryPage.this,
							NewDataPoint.class);
					intent.putExtra("latitude", location.getLatitude());
					intent.putExtra("longitude", location.getLongitude());
					startActivity(intent);

				} catch (Exception e) {
					Toast.makeText(EntryPage.this, "GPS Unavailable", 2000)
							.show();
					Log.i(Constants.LOGTAG + ": " + EntryPage.CLASSTAG,
							"Failed to load New Data Point page"
									+ e.getMessage() + "]");
				}
			}
		});
	}

	public void onResume() {
		super.onResume();
		try {
			populateMap();
		} catch (Exception e) {
			Log.i(Constants.LOGTAG + ": " + EntryPage.CLASSTAG, "error");
		}
	}

	private void populateMap() {

		final ResponseHandler<String> responseHandler = HTTPRequestHelper
				.getResponseHandlerInstance(this.progressHandler);

		new Thread() {

			@Override
			public void run() {
				HTTPRequestHelper helper = new HTTPRequestHelper(
						responseHandler);
				helper.performGet("http://signalseeker.herokuapp.com/data.xml",
						null, null, null);
			}
		}.start();

		mapView.getOverlays().clear();
		mapView.invalidate();
		List<Overlay> mapOverlays = mapView.getOverlays();

		// Initialize wifi signal markers
		Drawable drawable[] = new Drawable[5];
		drawable[0] = this.getResources().getDrawable(R.drawable.point0);
		drawable[1] = this.getResources().getDrawable(R.drawable.point1);
		drawable[2] = this.getResources().getDrawable(R.drawable.point2);
		drawable[3] = this.getResources().getDrawable(R.drawable.point3);
		drawable[4] = this.getResources().getDrawable(R.drawable.point4);

		// Assign the markers to overlays
		CustomizedOverlay itemizedOverlay[] = new CustomizedOverlay[5];
		itemizedOverlay[0] = new CustomizedOverlay(drawable[0], this);
		itemizedOverlay[1] = new CustomizedOverlay(drawable[1], this);
		itemizedOverlay[2] = new CustomizedOverlay(drawable[2], this);
		itemizedOverlay[3] = new CustomizedOverlay(drawable[3], this);
		itemizedOverlay[4] = new CustomizedOverlay(drawable[4], this);

		DataList dataList = DataList.parse(this);

		if (dataList == null) {
			return;
		}

		list = dataList.getAllDataEntries();

		for (int i = 0; i < list.size(); i++) {
			DataEntry dataEntry = list.get(i);
			GeoPoint geoPoint = new GeoPoint(
					(int) (dataEntry.getLatitude() * 1e6),
					(int) (dataEntry.getLongitude() * 1e6));
			OverlayItem overlayItem = new OverlayItem(geoPoint,
					dataEntry.getLocation(), "Wifi signal: "
							+ dataEntry.getWifi() + "\n" + "Latitude: "
							+ dataEntry.getLatitude() + "\n" + "Longitude: "
							+ dataEntry.getLongitude() + "\n"
							+ "Carrier name: " + dataEntry.getCarrier() + "\n"
							+ "Cell signal: " + dataEntry.getCell());
			itemizedOverlay[dataEntry.getWifi()].addOverlay(overlayItem);
		}
		
		mapOverlays.add(itemizedOverlay[0]);
		mapOverlays.add(itemizedOverlay[1]);
		mapOverlays.add(itemizedOverlay[2]);
		mapOverlays.add(itemizedOverlay[3]);
		mapOverlays.add(itemizedOverlay[4]);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, EntryPage.MENU_VIEW_POINTS, 0, R.string.menu_view_points)
				.setIcon(android.R.drawable.ic_menu_manage);
		menu.add(0, EntryPage.MENU_REFRESH_MAP, 0, R.string.menu_refresh_map)
				.setIcon(R.drawable.ic_menu_refresh);
		return true;
	}

	public boolean onMenuItemSelected(int id, MenuItem item) {

		Intent intent = null;
		switch (item.getItemId()) {
		case MENU_VIEW_POINTS:
			intent = new Intent(EntryPage.this, DataListView.class);
			startActivity(intent);
			return true;
		case MENU_REFRESH_MAP:
			populateMap();
			return true;
		}
		return super.onMenuItemSelected(id, item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}