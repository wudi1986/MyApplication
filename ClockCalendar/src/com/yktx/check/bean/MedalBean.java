package com.yktx.check.bean;

import java.io.Serializable;

public class MedalBean implements Serializable{
	
	/** //勋章路径 */
	private String picPath;
	/** 勋章来源, 1为123服务器, 2为七牛 */
	private String imageSource;
	/** 勋章等级 */
	private String level;
	/** 勋章所属building */
	private String buildingId;
	public MedalBean(String picPath, String imageSource, String level,
			String buildingId) {
		super();
		this.picPath = picPath;
		this.imageSource = imageSource;
		this.level = level;
		this.buildingId = buildingId;
	}
	public MedalBean() {
		super();
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getImageSource() {
		return imageSource;
	}
	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	
	
}
