package com.yktx.check.bean;

import java.io.Serializable;

public class GetByTaskIdCameraBean implements Serializable {
//	 "imageCount": 1,                                      //该日图片数量
//     "lastImagePath": "FvGK25QKbfULZzSxLMTz-dSlvyVb",      //最后一张图片地址, 若无则为null
//     "lastImageSource": "2",                               //最后一张图片来源, 1为123服务器, 2为七牛, 若无则为null
//     "lastImageCTime": "1443655148173",                    //最后一张图片创建时间, 若无则为null
//     "checkDate": "2015-10-01" 
	private int imageCount;

	private String lastImagePath;

	private String checkDate;
	
	private int lastImageSource;
	
	private long lastImageCTime;
	
	/** �洢��ǰ�±꣬��������ͼ */
	private int position;
	
	

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

	public String getLastImagePath() {
		return lastImagePath;
	}

	public void setLastImagePath(String lastImagePath) {
		this.lastImagePath = lastImagePath;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public int getLastImageSource() {
		return lastImageSource;
	}

	public void setLastImageSource(int lastImageSource) {
		this.lastImageSource = lastImageSource;
	}

	public long getLastImageCTime() {
		return lastImageCTime;
	}

	public void setLastImageCTime(long lastImageCTime) {
		this.lastImageCTime = lastImageCTime;
	}

	
	
}
