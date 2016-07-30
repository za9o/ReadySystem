/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus;

/**
 *
 * @author Torjus
 */
public class GPSValues {

    int gpsid;
    String latitude;
    String longitude;
    String gpsname;

    public int getGPSID() {
        return gpsid;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    public String getGpsName() {
        return gpsname;
    }

    public void setGPSID(int gpsid) {
        this.gpsid = gpsid;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public void setGPSName(String gpsname) {
        this.gpsname = gpsname;
    }

    @Override
    public String toString() {
        return "GPS: " + gpsid + " latitude: " + latitude + " longitude: " + longitude;
    }

}
