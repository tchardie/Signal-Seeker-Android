package com.cs4900.signalseeker.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.cs4900.signalseeker.Constants;

import android.R.integer;
import android.content.Context;
import android.util.Log;

public class DataList {
	private static final String CLASSTAG = DataList.class.getSimpleName();
	private Context context = null;
	private List<DataEntry> userList;

	public DataList(Context c) {
		this.context = c;
		this.userList = new Vector<DataEntry>(0);
	}

	int addUserEntry(DataEntry userEntry) {
		this.userList.add(userEntry);
		return this.userList.size();
	}

	DataEntry getUserEntry(int location) {
		return this.userList.get(location);
	}

	public List<DataEntry> getAllUserEntries() {
		return this.userList;
	}

	int getUserEntryCount() {
		return this.userList.size();
	}

	public void replace(DataEntry newUserEntry) {
		try {
			DataList newlist = new DataList(this.context);
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ue = getUserEntry(i);
				if (ue.getId() == newUserEntry.getId()) {
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG
							+ "Replacing UserEntry");
					newlist.addUserEntry(newUserEntry);
				} else {
					newlist.addUserEntry(ue);
				}
			}
			this.userList = newlist.userList;
			persist();
		} catch (Exception e) {

		}
	}

	public void delete(DataEntry UserEntry) {
		try {
			DataList newlist = new DataList(this.context);
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ue = getUserEntry(i);
				if (ue.getId() == (UserEntry.getId())) {
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG
							+ "Deleting UserEntry");

				} else {
					newlist.addUserEntry(ue);
				}
			}
			this.userList = newlist.userList;
			persist();
		} catch (Exception e) {

		}
	}

	// Under the online mode, the product id is created on the server not on the
	// client.
	// This method needs to be updated accordingly later...
	public void create(DataEntry UserEntry) {
		try {
			int maxid = 0;
			DataList newlist = new DataList(this.context);
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ue = getUserEntry(i);
				if (ue.getId() > maxid)
					maxid = ue.getId();

				newlist.addUserEntry(ue);
			}
			UserEntry.setId(new Integer(maxid + 1));
			newlist.addUserEntry(UserEntry);

			this.userList = newlist.userList;
			persist();
		} catch (Exception e) {

		}
	}

	// Write to the XML file
	public void persist() {
		try {
			FileOutputStream fos = this.context.openFileOutput(
					Constants.USER_XML_FILE, Context.MODE_PRIVATE);
			fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
					.getBytes());
			fos.write("<data>\n".getBytes());
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ce = getUserEntry(i);
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

			// return our new Userlist
			return clHandler.getList();
		} catch (Exception e) {
			Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG
					+ "Error parsing User list xml file: " + e.getMessage());
			return null;
		}
	}

}
