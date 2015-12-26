package com.yktx.check.bean;

import java.io.Serializable;

public class TopPointUserBean implements Serializable{
	private String point;
	private String avartarPath;
	private String name;
	private String userId;
	private int imageSource;
	public int getImageSource() {
		return imageSource;
	}
	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	public TopPointUserBean(String point, String avartarPath, String name,
			String userId) {
		super();
		this.point = point;
		this.avartarPath = avartarPath;
		this.name = name;
		this.userId = userId;
	}
	public TopPointUserBean() {
		super();
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getAvartarPath() {
		return avartarPath;
	}
	public void setAvartarPath(String avartarPath) {
		this.avartarPath = avartarPath;
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
	@Override
	public String toString() {
		return "TopPointUserBean [point=" + point + ", avartarPath="
				+ avartarPath + ", name=" + name + ", userId=" + userId
				+ ", imageSource=" + imageSource + "]";
	}
	
	
	
}
