package com.cs4900.signalseeker.data;

import android.os.Bundle;

public class DataEntry {
	private String address = "";
	private double latitude;
	private double longitude;
    private int id;
    private String location = "";
    private int wifi;
    private int cell;
    private String carrier;
	
	public DataEntry() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
   
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getWifi() {
		return wifi;
	}

	public void setWifi(int wifi) {
		this.wifi = wifi;
	}

	public int getCell() {
		return cell;
	}

	public void setCell(int cell) {
		this.cell = cell;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String toXMLString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("<user>");
        sb.append("<address>" + this.address + "</address>");
        sb.append("<id type=\"integer\">"+this.id+"</id>");
        sb.append("</user>");
        return sb.toString() + "\n";
    }
	
	public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("address", this.address);
        b.putDouble("latitude", latitude);
        b.putDouble("longitude", longitude);
        b.putString("location", location);
        b.putInt("wifi", wifi);
        b.putInt("cell", cell);
        b.putString("carrier", carrier);
        b.putInt("id", this.id);
        
        return b;
    }
	
	public static DataEntry fromBundle(Bundle b) {
        DataEntry ue = new DataEntry();
        ue.setAddress(b.getString("address"));
        ue.setLatitude(b.getDouble("latitude"));
        ue.setLongitude(b.getDouble("longitude"));
        ue.setLocation(b.getString("location"));
        ue.setWifi(b.getInt("wifi"));
        ue.setCell(b.getInt("cell"));
        ue.setCarrier(b.getString("carrier"));
        ue.setId(b.getInt("i"));

        return ue;
    }
    
    
}
