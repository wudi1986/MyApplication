package com.yktx.check.bean;

import java.io.Serializable;

public class AlarmBean implements Serializable {

	private int alarmIndex;

	private String alarmTime;

	private String tastID;
	
	private String tastTitle;

	public String getTastTitle() {
		return tastTitle;
	}

	@Override
	public String toString() {
		return "AlarmBean [alarmIndex=" + alarmIndex + ", alarmTime="
				+ alarmTime + ", tastID=" + tastID + ", tastTitle=" + tastTitle
				+ "]";
	}

	public void setTastTitle(String tastTitle) {
		this.tastTitle = tastTitle;
	}

	public int getAlarmIndex() {
		return alarmIndex;
	}

	public void setAlarmIndex(int alarmIndex) {
		this.alarmIndex = alarmIndex;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getTastID() {
		return tastID;
	}

	public void setTastID(String tastID) {
		this.tastID = tastID;
	}

}
