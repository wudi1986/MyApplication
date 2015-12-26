package com.yktx.check.bean;

import java.io.Serializable;

public class LastThree implements Serializable{
	private String avartar_path;
	private String name;
	private String task_id;
	private String picCount;
	private long check_time;
	private String quantity;
	private String signature;
	private String buildingId;
	private int imageSource;
	public int getImageSource() {
		return imageSource;
	}
	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	public LastThree(String avartar_path, String name, String task_id,
			String picCount, long check_time, String quantity,
			String signature, String buildingId) {
		super();
		this.avartar_path = avartar_path;
		this.name = name;
		this.task_id = task_id;
		this.picCount = picCount;
		this.check_time = check_time;
		this.quantity = quantity;
		this.signature = signature;
		this.buildingId = buildingId;
	}
	public LastThree() {
		super();
	}
	@Override
	public String toString() {
		return "LastThree [avartar_path=" + avartar_path + ", name=" + name
				+ ", task_id=" + task_id + ", picCount=" + picCount
				+ ", check_time=" + check_time + ", quantity=" + quantity
				+ ", signature=" + signature + ", buildingId=" + buildingId
				+ "]";
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
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getPicCount() {
		return picCount;
	}
	public void setPicCount(String picCount) {
		this.picCount = picCount;
	}
	public long getCheck_time() {
		return check_time;
	}
	public void setCheck_time(long check_time) {
		this.check_time = check_time;
	}
	public String getQuantity() {
		return quantity.trim();
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	
	
	
	
}
