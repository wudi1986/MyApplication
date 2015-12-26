package com.yktx.check.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class TaskIdCameraBean implements Serializable {

	private long taskCTime;

	private String lastKey;
	
	private int manCountToday;

	private String buildingId;

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public int getManCountToday() {
		return manCountToday;
	}

	public void setManCountToday(int manCountToday) {
		this.manCountToday = manCountToday;
	}

	LinkedHashMap<String, GetByTaskIdCameraBean> mapData = new LinkedHashMap<String, GetByTaskIdCameraBean>(
			10);

	public String getLastKey() {
		return lastKey;
	}

	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}

	public LinkedHashMap<String, GetByTaskIdCameraBean> getMapData() {
		return mapData;
	}

	public void setMapData(LinkedHashMap<String, GetByTaskIdCameraBean> mapData) {
		this.mapData = mapData;
	}

	public long getTaskCTime() {
		return taskCTime;
	}

	public void setTaskCTime(long taskCTime) {
		this.taskCTime = taskCTime;
	}

}
