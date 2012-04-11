package com.cs4900.signalseeker.data;

import android.os.Bundle;

public class DataEntry {
	private String address;
	private double latitude;
	private String location;
	private int cell;
	private int wifi;
	private String carrier;
	private int id;
	private double longitude;
	
	public DataEntry(){
		
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCell() {
		return cell;
	}

	public void setCell(int cell) {
		this.cell = cell;
	}

	public int getWifi() {
		return wifi;
	}

	public void setWifi(int wifi) {
		this.wifi = wifi;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String toXMLString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("<datum>");
		sb.append("<address>"+this.address+"</address>");
		sb.append("<latitude type=\"decimal\">" + this.latitude + "</latitude>");
		sb.append("<location>" + this.location + "</location>");
		sb.append("<cell type=\"integer\">" + this.cell + "</cell>");
		sb.append("<wifi type=\"integer\">" + this.wifi + "</wifi>");
		sb.append("<carrier>" + this.carrier + "</carrier>");
		sb.append("<id type=\"integer\">"+this.id+"<id>");
		sb.append("<longitude type=\"decimal\">" + this.longitude + "</longitude>");
		sb.append("</datum>");
		return sb.toString() + "\n";
	}
	
	public Bundle toBundle() {
		Bundle b = new Bundle();
		b.putString("address", this.address);
		b.putDouble("latitude", this.latitude);
		b.putString("location", this.location);
		b.putInt("cell", this.cell);
		b.putInt("wifi", this.wifi);
		b.putString("carrier", this.carrier);
		b.putInt("id", this.id);
		b.putDouble("longitude", this.longitude);
		
		return b;
	}
	
	public static DataEntry fromBundle(Bundle b) {
		DataEntry de = new DataEntry();
		de.setAddress(b.getString("address"));
		de.setLatitude(b.getDouble("latitude"));
		de.setLocation(b.getString("location"));
		de.setCell(b.getInt("cell"));
		de.setWifi(b.getInt("wifi"));
		de.setCarrier(b.getString("carrier"));
		de.setId(b.getInt("id"));
		de.setLongitude(b.getDouble("longitude"));
		
		return de;
	}
	
}
