package com.cs4900.signalseeker.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cs4900.signalseeker.Constants;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DataListHandler extends DefaultHandler {
	private static final String CLASSTAG = DataListHandler.class
			.getSimpleName();
	Handler uhandler = null;
	DataList _list;
	DataEntry _entry;
	String _lastElementName = "";
	StringBuilder sb = null;
	Context _context;

	public DataListHandler(Context c, Handler progresshandler) {
		this._context = c;
		if (progresshandler != null) {
			this.uhandler = progresshandler;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = ("Processing User List");
			this.uhandler.sendMessage(msg);
		}
	}

	public DataList getList() {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("Fetching List");
		if (this.uhandler != null) {
			this.uhandler.sendMessage(msg);
		}
		return this._list;
	}

	@Override
	public void startDocument() throws SAXException {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("Starting Document");
		if (this.uhandler != null) {
			this.uhandler.sendMessage(msg);
		}

		// initialize our UserLIst object - this will hold our parsed
		// contents
		this._list = new DataList(this._context);

		// initialize the UserEntry object
		this._entry = new DataEntry();

	}

	@Override
	public void endDocument() throws SAXException {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("End of Document");
		if (this.uhandler != null) {
			this.uhandler.sendMessage(msg);
		}

	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		try {
			this.sb = new StringBuilder("");

			if (localName.equals("datum")) {
				// create a new item

				Message msg = new Message();
				msg.what = 0;
				msg.obj = (localName);
				if (this.uhandler != null) {
					this.uhandler.sendMessage(msg);
				}

				this._entry = new DataEntry();

			}
		} catch (Exception ee) {
			Log.d(Constants.LOGTAG, " " + DataListHandler.CLASSTAG
					+ ee.getStackTrace().toString());
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		if (localName.equals("datum")) {
			// add our entry to the list!
			this._list.addUserEntry(this._entry);
			Message msg = new Message();
			msg.what = 0;
			msg.obj = ("Storing entry # " + this._entry.get_id());
			if (this.uhandler != null) {
				this.uhandler.sendMessage(msg);
			}

			return;
		}

		if (localName.equals("id")) {
			this._entry.set_id(Integer.parseInt(this.sb.toString()));
			return;
		}
		if (localName.equals("address")) {
			this._entry.set_address(this.sb.toString());
			return;
		}
		if(localName.equals("latitude")) {
			this._entry.set_latitude(Double.parseDouble(this.sb.toString()));
			return;
		}
		if(localName.equals("longitude")) {
			this._entry.set_longitude(Double.parseDouble(this.sb.toString()));
			return;
		}
		if(localName.equals("wifi")) {
			this._entry.set_wifi(Integer.parseInt(this.sb.toString()));
			return;
		}
		if(localName.equals("cell")) {
			this._entry.set_cell(Integer.parseInt(this.sb.toString()));
			return;
		}
		if(localName.equals("carrier")) {
			this._entry.set_carrier(this.sb.toString());
			return;
		}
		if(localName.equals("location")) {
			this._entry.set_location(this.sb.toString());
			return;
		}

	}

	@Override
	public void characters(char ch[], int start, int length) {
		String theString = new String(ch, start, length);
		// Log.d(Constants.LOGTAG, " " + UserListHandler.CLASSTAG +
		// "characters[" + theString + "]");
		this.sb.append(theString);
	}
}
