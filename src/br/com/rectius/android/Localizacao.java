package br.com.rectius.android;

import android.text.format.Time;

public class Localizacao {
	
	private Double latitude;
	private Double longitude;
	private Time time;
	private String accurancy;
	private String fonte;
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public Time getTime() {
		return time;
	}
	public void setAccurancy(String accurancy) {
		this.accurancy = accurancy;
	}
	public String getAccurancy() {
		return accurancy;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	public String getFonte() {
		return fonte;
	}

}
