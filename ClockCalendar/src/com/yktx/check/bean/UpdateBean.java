package com.yktx.check.bean;

import java.io.Serializable;

public class UpdateBean implements Serializable {

	private String taskLevel;
	/** 用户积分 */
	private String userPoint;
	/** 打卡时间 */
	private String checkTime;
	public String getTaskLevel() {
		return taskLevel;
	}
	public void setTaskLevel(String taskLevel) {
		this.taskLevel = taskLevel;
	}
	public String getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(String userPoint) {
		this.userPoint = userPoint;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	
	
}
