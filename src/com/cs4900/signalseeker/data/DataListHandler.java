package com.cs4900.signalseeker.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class DataListHandler extends DefaultHandler {
	private static final String CLASSTAG = DataListHandler.class
			.getSimpleName();
	Handler dhandler = null;
	DataList _list;
	DataEntry _entry;
	String _lastElementName = "";
	StringBuilder sb = null;
	Context _context;

	public DataListHandler(Context c, Handler progresshandler) {
		this._context = c;
		if (progresshandler != null) {
			this.dhandler = progresshandler;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = ("Processing Data List");
			this.dhandler.sendMessage(msg);
		}
	}

	public DataList getList() {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("Fetching List");
		if (this.dhandler != null) {
			this.dhandler.sendMessage(msg);
		}
		return this._list;
	}

	@Override
	public void startDocument() throws SAXException {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("Starting Docment");
		if (this.dhandler == null) {
			this.dhandler.sendMessage(msg);
		}

		this._list = new DataList(this._context);
		this._entry = new DataEntry();
	}

	@Override
	public void endDocument() throws SAXException {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = ("End of Document");
		if (this.dhandler != null) {
			this.dhandler.sendMessage(msg);
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
				if (this.dhandler != null) {
					this.dhandler.sendMessage(msg);
				}

				this._entry = new DataEntry();

			}
		} catch (Exception ee) {

		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		if (localName.equals("datum")) {
			// add our product to the list!
			this._list.addDataEntry(this._entry);
			Message msg = new Message();
			msg.what = 0;
			msg.obj = ("Storing Product # " + this._entry.getId());
			if (this.dhandler != null) {
				this.dhandler.sendMessage(msg);
			}

			return;
		}

		if (localName.equals("id")) {
			this._entry.setId(Integer.parseInt(this.sb.toString()));
			return;
		}
		if (localName.equals("latitude")) {
			this._entry.setLatitude(Double.parseDouble(this.sb.toString()));
			return;
		}
		if (localName.equals("location")) {
			this._entry.setLocation(this.sb.toString());
			return;
		}
		if (localName.equals("cell")) {
			this._entry.setCell(Integer.parseInt(this.sb.toString()));
			return;
		}
		if (localName.equals("wifi")) {
			this._entry.setWifi(Integer.parseInt(this.sb.toString()));
			return;
		}
		if (localName.equals("carrier")) {
			this._entry.setCarrier(this.sb.toString());
			return;
		}
		if (localName.equals("longitude")) {
			this._entry.setLongitude(Double.parseDouble(this.sb.toString()));
			return;
		}
	}
	
	@Override
    public void characters(char ch[], int start, int length) {
        String theString = new String(ch, start, length);
       // Log.d(Constants.LOGTAG, " " + CatalogListHandler.CLASSTAG + "characters[" + theString + "]");
        this.sb.append(theString);
    }
}
