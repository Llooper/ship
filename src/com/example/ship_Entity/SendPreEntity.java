package com.example.ship_Entity;

import android.graphics.Bitmap;


//define all data used in SendPre list
public class SendPreEntity {
	
	private Bitmap SendPre_picture;
	
	private String SendPre_detail;
	private float SendPre_gps_la = 0;
	private float SendPre_gps_lo = 0;

	private int unget = 0;

	public Bitmap getSendPre_picture() {
		return SendPre_picture;
	}

	public void setSendPre_picture(Bitmap SendPre_picture) {
		this.SendPre_picture = SendPre_picture;
	}

	public String getSendPre_detail() {
		return SendPre_detail;
	}

	public void setSendPre_detail(String SendPre_detail) {
		this.SendPre_detail = SendPre_detail;
	}

	public float getSendPre_gps_la() {
		return SendPre_gps_la;
	}

	public void setSendPre_gps_la(float sendPre_gps_la) {
		SendPre_gps_la = sendPre_gps_la;
	}

	public float getSendPre_gps_lo() {
		return SendPre_gps_lo;
	}

	public void setSendPre_gps_lo(float sendPre_gps_lo) {
		SendPre_gps_lo = sendPre_gps_lo;
	}

	public int getUnget() {
		return unget;
	}

	public void setUnget(int unget) {
		this.unget = unget;
	}
}
