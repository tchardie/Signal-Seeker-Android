package com.cs4900.signalseeker;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import java.util.Collections;
import java.util.Comparator;

import com.cs4900.signalseeker.data.DataEntry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Custom adapter for "Review" model objects.
 * 
 * @author charliecollins
 */
public class DataAdapter extends BaseAdapter {

	private static final String CLASSTAG = DataAdapter.class.getSimpleName();
	private final Context context;
	private final List<DataEntry> list;

	@SuppressWarnings("unchecked")
	public DataAdapter(Context context, List<DataEntry> list, int order) {
		this.context = context;
		this.list = list;

		Collections.sort(list, new Comparator() {

			public int compare(Object o1, Object o2) {
				DataEntry ce1 = (DataEntry) o1;
				DataEntry ce2 = (DataEntry) o2;
				return ce1.get_location().compareToIgnoreCase(ce2.get_location());
			}

		});

		Log.v(Constants.LOGTAG, " " + DataAdapter.CLASSTAG
				+ " list size - " + this.list.size());
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return this.list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		DataEntry re = this.list.get(position);
		return new listListView(this.context, re.get_location().toString(),
				String.valueOf(re.get_latitude()), String.valueOf(re.get_longitude()));
	}

	/**
	 * listListView that adapter returns as it's view item per row.
	 * 
	 * @author zhiguang xu
	 */
	private final class listListView extends LinearLayout {

		private TextView location;
		private TextView lat;

		public listListView(Context context, String location, String lat,
				String lon) {

			super(context);
			setOrientation(LinearLayout.VERTICAL);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 3, 5, 0);

			this.location = new TextView(context);
			this.location.setText(location);
			this.location.setTextSize(16f);
			this.location.setTextColor(Color.WHITE);
			this.addView(this.location, params);

			this.lat = new TextView(context);
			this.lat.setText(String.valueOf(lat)+", "+String.valueOf(lon));
			this.lat.setTextSize(8f);
			this.lat.setTextColor(Color.WHITE);
			this.addView(this.lat, params);
		}
	}
}
