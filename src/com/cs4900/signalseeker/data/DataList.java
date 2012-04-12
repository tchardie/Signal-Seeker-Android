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
	private Context _context = null;
	private List<DataEntry> _userList;

	public DataList(Context c) {
		this._context = c;
		this._userList = new Vector<DataEntry>(0);
	}

	int addUserEntry(DataEntry userEntry) {
		this._userList.add(userEntry);
		return this._userList.size();
	}

	DataEntry getUserEntry(int location) {
		return this._userList.get(location);
	}

	public List<DataEntry> getAllUserEntries() {
		return this._userList;
	}

	int getUserEntryCount() {
		return this._userList.size();
	}

	public void replace(DataEntry newUserEntry) {
		try {
			DataList newlist = new DataList(this._context);
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ue = getUserEntry(i);
				if (ue.get_id() == newUserEntry.get_id()) {
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG
							+ "Replacing UserEntry");
					newlist.addUserEntry(newUserEntry);
				} else {
					newlist.addUserEntry(ue);
				}
			}
			this._userList = newlist._userList;
			persist();
		} catch (Exception e) {

		}
	}

	public void delete(DataEntry UserEntry) {
		try {
			DataList newlist = new DataList(this._context);
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ue = getUserEntry(i);
				if (ue.get_id() == (UserEntry.get_id())) {
					Log.d(Constants.LOGTAG, " " + DataList.CLASSTAG
							+ "Deleting UserEntry");

				} else {
					newlist.addUserEntry(ue);
				}
			}
			this._userList = newlist._userList;
			persist();
		} catch (Exception e) {

		}
	}

	// Under the online mode, the product id is created on the server not on the
	// client.
	// This method needs to be updated accordingly later...
	public void create(DataEntry UserEntry) {
		try {
			int max_id = 0;
			DataList newlist = new DataList(this._context);
			for (int i = 0; i < getUserEntryCount(); i++) {
				DataEntry ue = getUserEntry(i);
				if (ue.get_id() > max_id)
					max_id = ue.get_id();

				newlist.addUserEntry(ue);
			}
			UserEntry.set_id(new Integer(max_id + 1));
			newlist.addUserEntry(UserEntry);

			this._userList = newlist._userList;
			persist();
		} catch (Exception e) {

		}
	}

	// Write to the XML file
	public void persist() {
		try {
			FileOutputStream fos = this._context.openFileOutput(
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
