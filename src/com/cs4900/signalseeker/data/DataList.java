package com.cs4900.signalseeker.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.cs4900.signalseeker.Constants;

import android.content.Context;
import android.util.Log;

public class DataList {
	private static final String CLASSTAG = DataList.class.getSimpleName();
	private Context context = null;
	private List<DataEntry> dataList;

	public DataList(Context c) {
		this.context = c;
		this.dataList = new Vector<DataEntry>(0);
	}

	int addDataEntry(DataEntry dataEntry) {
		this.dataList.add(dataEntry);
		return this.dataList.size();
	}

	DataEntry getDataEntry(int location) {
		return this.dataList.get(location);
	}

	public List<DataEntry> getAllDataEntries() {
		return this.dataList;
	}

	int getDataEntryCount() {
		return this.dataList.size();
	}

	public void replace(DataEntry newDataEntry) {
		Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG + "Replacing DataEntry" + newDataEntry.getId());
		try {
			DataList newlist = new DataList(this.context);
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry de = getDataEntry(i);
				if (de.getId() == newDataEntry.getId()) {
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG + "Replacing DataEntry");
					newlist.addDataEntry(newDataEntry);
				}
				else {
					newlist.addDataEntry(de);
				}
			}
			this.dataList = newlist.dataList;
			persist();
		} catch (Exception e) {

		}
	}

	public void delete(DataEntry dataEntry) {
		try {
			DataList newlist = new DataList(this.context);
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry de = getDataEntry(i);
				if (de.getId() == dataEntry.getId()) {
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG + " Deleting DataEntry");

				}
				else {
					newlist.addDataEntry(de);
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG + " Entry Not Deleted");
				}
			}
			this.dataList = newlist.dataList;
			persist();
		} catch (Exception e) {

		}
	}

	// Under the online mode, the product id is created on the server not on the
	// client.
	// This method needs to be updated accordingly later...
	public void create(DataEntry DataEntry) {
		try {
			int maxid = 0;
			DataList newlist = new DataList(this.context);
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry ue = getDataEntry(i);
				if ((ue.getId()) > maxid)
					maxid = ue.getId();

				newlist.addDataEntry(ue);
			}
			DataEntry.setId(new Integer(maxid + 1));
			newlist.addDataEntry(DataEntry);

			this.dataList = newlist.dataList;
			persist();
		} catch (Exception e) {

		}
	}

	// Write to the XML file
	public void persist() {
		try {

			/** This Region for emulator with xml file!!! **/
			FileOutputStream fos = this.context.openFileOutput(Constants.DATA_XML_FILE, Context.MODE_PRIVATE);

			/**
			 * Comment this for actual phone File file = new
			 * File("/mnt/sdcard/data.xml"); file.createNewFile();
			 * FileOutputStream fos = new FileOutputStream(file);
			 **/
			fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
			fos.write("<data>\n".getBytes());
			for (int i = 0; i < getDataEntryCount(); i++) {
				DataEntry ce = getDataEntry(i);
				fos.write(ce.toXMLString().getBytes());
			}
			fos.write("</data>\n".getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG + "Failed to write out file?" + e.getMessage());
		}
	}

	// Read from the XML file
	public static DataList parse(Context context) {
		try {
			/** This line for emulator **/
			FileInputStream fis = context.openFileInput(Constants.DATA_XML_FILE);

			/**
			 * this line for actual phone File file = new
			 * File("/mnt/sdcard/data.xml"); FileInputStream fis = new
			 * FileInputStream(file);
			 **/

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

			// return our new Datalist
			return clHandler.getList();
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG + "Error parsing Data list xml file: " + e.getMessage());
			return null;
		}
	}

}
