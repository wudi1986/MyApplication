package com.yktx.check.bean;

import java.io.Serializable;

public class TaskGetImageBean implements Serializable{


	/** 最后一张图片地址, 若无则为null */
	private String path;
	/** 最后一张图片来源, 1为123服务器, 2为七牛, 若无则为null */
	private int imageSource;
	/** 创建时间 */
	private long cTime;
	/** 图片创建日期 */
	private String checkDate;
	/** 定位城市, 若无则为0 */
	private String city;
	/** 图片Id */
	private String imgId;
	/**  第几天打卡 */
	private int checkDayNum;


	
	public int getCheckDayNum() {
		return checkDayNum;
	}

	public void setCheckDayNum(int checkDayNum) {
		this.checkDayNum = checkDayNum;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getImageSource() {
		return imageSource;
	}
	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	public long getcTime() {
		return cTime;
	}
	public void setcTime(long cTime) {
		this.cTime = cTime;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}





}
