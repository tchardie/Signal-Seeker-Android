package com.cs4900.signalseeker;

import java.util.List;

import com.cs4900.signalseeker.data.*;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DataListView extends ListActivity {
	List<DataEntry> list;
	DataAdapter dataAdapter;
	private TextView empty;
	private Button backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.setContentView(R.layout.datalistview);

		empty = (TextView) findViewById(R.id.empty);

		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(empty);
	}

	public void onResume() {
		super.onResume();
		list = DataList.parse(this).getAllDataEntries();
		dataAdapter = new DataAdapter(DataListView.this, list, 2);
		setListAdapter(dataAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent(this, DataPoint.class);
		intent.putExtras(list.get((int) id).toBundle());
		startActivity(intent);
	}

	public void onBackPressed() {
		Intent i = new Intent(this, EntryPage.class);
		startActivity(i);
		return;
	}
}
