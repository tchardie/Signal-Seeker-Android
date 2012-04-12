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
	DataList list;
	DataEntry entry;
	String lastElementName = "";
	StringBuilder sb = null;
	Context context;

	public DataListHandler(Context c, Handler progresshandler) {
		this.context = c;
		if (progresshandler != null) {
			this.uhandler = progresshandler;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = ("Processing Data List");
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
		return this.list;
	}

	@Override
	public void startDocument() throws SAXException {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("Starting Document");
		if (this.uhandler != null) {
			this.uhandler.sendMessage(msg);
		}

		// initialize our DataLIst object - this will hold our parsed
		// contents
		this.list = new DataList(this.context);

		// initialize the DataEntry object
		this.entry = new DataEntry();

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

				this.entry = new DataEntry();

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
			this.list.addDataEntry(this.entry);
			Message msg = new Message();
			msg.what = 0;
			msg.obj = ("Storing entry # " + this.entry.getId());
			if (this.uhandler != null) {
				this.uhandler.sendMessage(msg);
			}

			return;
		}

		if (localName.equals("id")) {
			this.entry.setId(Integer.parseInt(this.sb.toString()));
			return;
		}
		
		if(localName.equals("latitude")) {
			this.entry.setLatitude(Double.parseDouble(this.sb.toString()));
			return;
		}
		if(localName.equals("longitude")) {
			this.entry.setLongitude(Double.parseDouble(this.sb.toString()));
			return;
		}
		if(localName.equals("wifi")) {
			this.entry.setWifi(Integer.parseInt(this.sb.toString()));
			return;
		}
		if(localName.equals("cell")) {
			this.entry.setCell(Integer.parseInt(this.sb.toString()));
			return;
		}
		if(localName.equals("carrier")) {
			this.entry.setCarrier(this.sb.toString());
			return;
		}
		if(localName.equals("location")) {
			this.entry.setLocation(this.sb.toString());
			return;
		}

	}

	@Override
	public void characters(char ch[], int start, int length) {
		String theString = new String(ch, start, length);
		// Log.d(Constants.LOGTAG, " " + DataListHandler.CLASSTAG +
		// "characters[" + theString + "]");
		this.sb.append(theString);
	}
}
