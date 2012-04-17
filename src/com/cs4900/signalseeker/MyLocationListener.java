package com.cs4900.signalseeker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener{

	public void onLocationChanged(Location location, Context context) {
		location.getLatitude();
		location.getLongitude();
		
	}
	
	public Location getLoc(Location location){
		return location;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
	
}
