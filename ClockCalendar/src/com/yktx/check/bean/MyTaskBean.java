package com.yktx.check.bean;

import java.io.Serializable;

public class MyTaskBean implements Serializable{
	private String taskId;
	private String buildingId;
	private String title;
	private String status;
	private String currentStreak;
	private String totalCheckCount;
	private String lastCheckTime;
	private String manCountToday;
	private String level;
	private String description;
	private String totalDateCount;
	private String point;
	/** //勋章图片地址, 若无勋章则为null  */
	private String badgePath;
	/** 勋章来源, 1为123服务器, 2为七牛 */
	private String badgeSource;
	/** 正在坚持人数  */
	private int taskCount;
	/**  今日打卡次数 */
	private int jobCountToday;
	/**  当前等级进度, 0-6 */
	private int progress;
	private int privateFlag;
	



	@Override
	public String toString() {
		return "MyTaskBean [taskId=" + taskId + ", buildingId=" + buildingId
				+ ", title=" + title + ", status=" + status
				+ ", currentStreak=" + currentStreak + ", totalCheckCount="
				+ totalCheckCount + ", lastCheckTime=" + lastCheckTime
				+ ", manCountToday=" + manCountToday + ", level=" + level
				+ ", description=" + description + ", totalDateCount="
				+ totalDateCount + ", point=" + point + ", badgePath="
				+ badgePath + ", badgeSource=" + badgeSource + ", taskCount="
				+ taskCount + ", jobCountToday=" + jobCountToday + "]";
	}


	public int getPrivateFlag() {
		return privateFlag;
	}


	public void setPrivateFlag(int privateFlag) {
		this.privateFlag = privateFlag;
	}


	public int getProgress() {
		return progress;
	}


	public void setProgress(int progress) {
		this.progress = progress;
	}


	public int getTaskCount() {
		return taskCount;
	}


	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}


	public int getJobCountToday() {
		return jobCountToday;
	}


	public void setJobCountToday(int jobCountToday) {
		this.jobCountToday = jobCountToday;
	}


	public String getBadgePath() {
		return badgePath;
	}


	public void setBadgePath(String badgePath) {
		this.badgePath = badgePath;
	}


	public String getBadgeSource() {
		return badgeSource;
	}


	public void setBadgeSource(String badgeSource) {
		this.badgeSource = badgeSource;
	}


	public String getPoint() {
		return point;
	}


	public void setPoint(String point) {
		this.point = point;
	}
	public String getTotalDateCount() {
		return totalDateCount;
	}


	public void setTotalDateCount(String totalDateCount) {
		this.totalDateCount = totalDateCount;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}


	public MyTaskBean() {
		super();
	}
	
	public String getManCountToday() {
		return manCountToday;
	}

	public void setManCountToday(String manCountToday) {
		this.manCountToday = manCountToday;
	}

	public String getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(String lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getTitle() {
		return title.trim();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrentStreak() {
		return currentStreak;
	}
	public void setCurrentStreak(String currentStreak) {
		this.currentStreak = currentStreak;
	}
	public String getTotalCheckCount() {
		return totalCheckCount;
	}
	public void setTotalCheckCount(String totalCheckCount) {
		this.totalCheckCount = totalCheckCount;
	}
	

}
