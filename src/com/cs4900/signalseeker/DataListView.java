package com.cs4900.signalseeker;

import java.util.List;

import com.cs4900.signalseeker.data.*;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DataListView extends ListActivity {
	List<DataEntry> list;
	DataAdapter catalogAdapter;
	private TextView empty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.setContentView(R.layout.datalistview);
		this.empty = (TextView) findViewById(R.id.empty);
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(empty);
	}

	public void onResume() {
		super.onResume();
		// list = DataList.parse(this).getAllDataEntries();
		list = DataList.parse(this).getAllUserEntries();
		catalogAdapter = new DataAdapter(DataListView.this, list, 2);
		setListAdapter(catalogAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent(this, DataPoint.class);
		intent.putExtras(list.get((int)id).toBundle());
		startActivity(intent);
	}
}
