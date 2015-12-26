package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class UserFollowingJobBean implements Serializable {
	// 头像来源, 1为123服务器, 2为七
	private int imageSource;
	// 头像路径
	private String avartar_path;
	// 用户名
	private String name;
	// 用户Id
	private String userId;
	// 共打卡次数
	private int totalJobCount;
	// 用户积分
	private int point;
	// 最后一卡日期
	private String dateDisplay;
	
	
	private ArrayList<TaskItemBean> jobs;

	
	public String getDateDisplay() {
		return dateDisplay;
	}

	public void setDateDisplay(String dateDisplay) {
		this.dateDisplay = dateDisplay;
	}

	public int getImageSource() {
		return imageSource;
	}

	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
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

	public int getTotalJobCount() {
		return totalJobCount;
	}

	public void setTotalJobCount(int totalJobCount) {
		this.totalJobCount = totalJobCount;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public ArrayList<TaskItemBean> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<TaskItemBean> jobs) {
		this.jobs = jobs;
	}
}
