package com.cs4900.signalseeker;

import java.util.List;

import com.cs4900.signalseeker.data.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class DataListView extends Activity {
	List<DataEntry> list;
	Spinner spinner;
	private DataList dataList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datalistview);

		spinner = (Spinner) findViewById(R.id.data_spinner);
		list = DataList.parse(this).getAllDataEntries();
	}

	public void onResume() {
		super.onResume();

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// for (int i = 0; i < list.size(); i++) {
		// adapter.add(list.get(i).getAddress().toString());
		// }
		//
		// spinner.setAdapter(adapter);

	}
}
