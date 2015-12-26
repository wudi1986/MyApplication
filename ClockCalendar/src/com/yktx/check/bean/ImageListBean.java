package com.yktx.check.bean;

import java.io.Serializable;

public class ImageListBean implements Serializable{
	private String ImageUrl;
	private boolean isCheck;
	public ImageListBean() {
		super();
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public boolean getIsCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

}
