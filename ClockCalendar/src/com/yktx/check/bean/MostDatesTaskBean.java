package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MostDatesTaskBean implements Serializable{
	private String taskId;
	private int point;
	private String title;
	private int imageSource;
	private int checkDateCount;
	private String avartar_path;
	private String name;
	private String userId;
	private int badgeSource;
	private String badgePath;
	
	private String buildingId;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageSource() {
		return imageSource;
	}

	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}

	public int getCheckDateCount() {
		return checkDateCount;
	}

	public void setCheckDateCount(int checkDateCount) {
		this.checkDateCount = checkDateCount;
	}

	public String getAvartar_path() {
		return avartar_path;
	}

	public void setAvartar_path(String avartar_path) {
		this.avartar_path = avartar_path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBadgeSource() {
		return badgeSource;
	}

	public void setBadgeSource(int badgeSource) {
		this.badgeSource = badgeSource;
	}

	public String getBadgePath() {
		return badgePath;
	}

	public void setBadgePath(String badgePath) {
		this.badgePath = badgePath;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public MostDatesTaskBean() {
		super();
	}
}
