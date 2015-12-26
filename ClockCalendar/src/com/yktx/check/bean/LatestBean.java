package com.yktx.check.bean;

import java.io.Serializable;

public class LatestBean implements Serializable{
	private String avartarPath;
	private String title;
	private String name;
	private String userId;
	private String buildingId;
	private int imageSource;
	public int getImageSource() {
		return imageSource;
	}
	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	public LatestBean(String avartarPath, String title, String name,
			String userId, String buildingId) {
		super();
		this.avartarPath = avartarPath;
		this.title = title;
		this.name = name;
		this.userId = userId;
		this.buildingId = buildingId;
	}
	public LatestBean() {
		super();
	}
	@Override
	public String toString() {
		return "LatestBean [avartarPath=" + avartarPath + ", title=" + title
				+ ", name=" + name + ", userId=" + userId + ", buildingId="
				+ buildingId + "]";
	}
	public String getAvartarPath() {
		return avartarPath;
	}
	public void setAvartarPath(String avartarPath) {
		this.avartarPath = avartarPath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

}
