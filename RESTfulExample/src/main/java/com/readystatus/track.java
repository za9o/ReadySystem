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
public class track {
    	String gpsid;
	String singer;

	public String getGPSID() {
		return gpsid;
	}

	public void setGPSID(String gpsid) {
		this.gpsid = gpsid;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	@Override
	public String toString() {
		return "Track [title=" + gpsid + ", singer=" + singer + "]";
	}
}
