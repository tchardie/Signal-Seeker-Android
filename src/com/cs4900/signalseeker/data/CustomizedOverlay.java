package com.cs4900.signalseeker.data;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cs4900.signalseeker.Constants;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomizedOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

	private Context context;

	public CustomizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}

	public CustomizedOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
		populate();
	}

	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	public int size() {
		return mapOverlays.size();
	}

	protected boolean onTap(int index) {
		try {
			OverlayItem item = mapOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		} catch (Exception e) {
			Log.i(Constants.LOGTAG + ": " + CustomizedOverlay.class, "Failed to load New Data Point page" + e.getMessage() + "]");
		}
		return false;
	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}
}
