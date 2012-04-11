package com.cs4900.signalseeker.data;

import android.R.integer;
import android.os.Bundle;

public class DataEntry {
	private String _address = "";
	private double _latitude;
	private double _longitude;
    private int _id;
    private String _location = "";
    private int _wifi;
    private int _cell;
    private String _carrier;
	
	public DataEntry() {

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
   
	public String get_address() {
		return _address;
	}

	public void set_address(String _address) {
		this._address = _address;
	}

	public double get_latitude() {
		return _latitude;
	}

	public void set_latitude(double _latitude) {
		this._latitude = _latitude;
	}

	public double get_longitude() {
		return _longitude;
	}

	public void set_longitude(double _longitude) {
		this._longitude = _longitude;
	}
	
	public String get_location() {
		return _location;
	}

	public void set_location(String _location) {
		this._location = _location;
	}

	public int get_wifi() {
		return _wifi;
	}

	public void set_wifi(int _wifi) {
		this._wifi = _wifi;
	}

	public int get_cell() {
		return _cell;
	}

	public void set_cell(int _cell) {
		this._cell = _cell;
	}

	public String get_carrier() {
		return _carrier;
	}

	public void set_carrier(String _carrier) {
		this._carrier = _carrier;
	}

	public String toXMLString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("<user>");
        sb.append("<address>" + this._address + "</address>");
        sb.append("<id type=\"integer\">"+this._id+"</id>");
        sb.append("</user>");
        return sb.toString() + "\n";
    }
	
	public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("address", this._address);
        b.putDouble("latitude", _latitude);
        b.putDouble("longitude", _longitude);
        b.putString("location", _location);
        b.putInt("wifi", _wifi);
        b.putInt("cell", _cell);
        b.putString("carrier", _carrier);
        b.putInt("id", this._id);
        
        return b;
    }
	
	public static DataEntry fromBundle(Bundle b) {
        DataEntry ue = new DataEntry();
        ue.set_address(b.getString("address"));
        ue.set_latitude(b.getDouble("latitude"));
        ue.set_longitude(b.getDouble("longitude"));
        ue.set_location(b.getString("location"));
        ue.set_wifi(b.getInt("wifi"));
        ue.set_cell(b.getInt("cell"));
        ue.set_carrier(b.getString("carrier"));
        ue.set_id(b.getInt("i"));

        return ue;
    }
    
    
}
