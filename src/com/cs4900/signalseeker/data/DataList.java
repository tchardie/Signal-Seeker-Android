package com.cs4900.signalseeker.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cs4900.signalseeker.Constants;

import com.cs4900.signalseeker.data.*;

public class DataList {
	private static final String CLASSTAG = DataList.class.getSimpleName();
	private Context _context = null;
	private List<DataEntry> _dataList;

	public DataList(Context c) {
		this._context = c;
		this._dataList = new Vector<DataEntry>();
	}

	int addDataEntry(DataEntry dataEntry) {
		this._dataList.add(dataEntry);
		return this._dataList.size();
	}

	DataEntry getDataEntry(int location) {
		return this._dataList.get(location);
	}

	public List<DataEntry> getAllDataEntries() {
		return this._dataList;
	}

	int getDataEntryCount() {
		return this._dataList.size();
	}

	public void replace(DataEntry newDataEntry) {
		try {
			DataList newlist = new DataList(this._context);
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry ce = getDataEntry(i);
				if (ce.getId() == newDataEntry.getId()) {
					newlist.addDataEntry(newDataEntry);
				} else {
					newlist.addDataEntry(ce);
				}
			}
			this._dataList = newlist._dataList;
			persist();
		} catch (Exception e) {

		}
	}

	public void delete(DataEntry DataEntry) {
		try {
			DataList newlist = new DataList(this._context);
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry ce = getDataEntry(i);
				if (ce.getId() != DataEntry.getId()) {
					newlist.addDataEntry(ce);
				}
			}
			this._dataList = newlist._dataList;
			persist();
		} catch (Exception e) {

		}
	}

	public void create(DataEntry DataEntry) {
		try {
			int max_id = 0;
			DataList newlist = new DataList(this._context);
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry ce = getDataEntry(i);
				if (ce.getId() > max_id)
					max_id = ce.getId();

				newlist.addDataEntry(ce);
			}
			DataEntry.setId(new Integer(max_id + 1));
			newlist.addDataEntry(DataEntry);

			this._dataList = newlist._dataList;
			persist();
		} catch (Exception e) {

		}
	}

	// Write to the XML file
	public void persist() {
		try {
			FileOutputStream fos = this._context.openFileOutput(
					Constants.DATA_XML_FILE, Context.MODE_PRIVATE);
			fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
					.getBytes());
			fos.write("<data>\n".getBytes());
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry ce = getDataEntry(i);
				fos.write(ce.toXMLString().getBytes());
			}
			fos.write("</data>\n".getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG
					+ "Failed to write out file?" + e.getMessage());
		}
	}

	// Read from the XML file
	public static DataList parse(Context context) {
		try {
			
			FileInputStream fis = context
					.openFileInput(Constants.DATA_XML_FILE);

			if (fis == null) {
				return null;
			}
			// we need an input source for the sax parser
			InputSource is = new InputSource(fis);

			// create the factory
			SAXParserFactory factory = SAXParserFactory.newInstance();

			// create a parser
			SAXParser parser = factory.newSAXParser();

			// create the reader (scanner)
			XMLReader xmlreader = parser.getXMLReader();

			// instantiate our handler
			DataListHandler clHandler = new DataListHandler(context, null /*
																				 * no
																				 * progress
																				 * updates
																				 * when
																				 * reading
																				 * file
																				 */);

			// assign our handler
			xmlreader.setContentHandler(clHandler);

			// perform the synchronous parse
			xmlreader.parse(is);

			// clean up
			fis.close();

			
			return clHandler.getList();
		} catch (Exception e) {
			return null;
		}
	}
}
