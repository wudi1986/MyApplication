package com.yktx.check.bean;

import java.io.Serializable;
/**
 * 查询building的基本信息 
 * @author Administrator
 *
 */

public class BasicInfoBean implements Serializable{
	private String title;
	private String creatorName;
	private String creatorId;
	private String jobCountToday;
	private String taskCount;
	private String time_limit_flag;
	private String begin_time;
	private String end_time;
	private String description;
	private String added;
	private String myTaskId;
	private String totalDateCount;
	private String currentStreak;
	private String totalCheckCount;//当前用户此卡的累计打卡次数
	private String manCountToday;//当前用户此卡的累计打卡次数
	private String totalJobCount;//历史总卡数
	
	
	@Override
	public String toString() {
		return "BasicInfoBean [title=" + title + ", creatorName=" + creatorName
				+ ", creatorId=" + creatorId + ", jobCountToday="
				+ jobCountToday + ", taskCount=" + taskCount
				+ ", time_limit_flag=" + time_limit_flag + ", begin_time="
				+ begin_time + ", end_time=" + end_time + ", description="
				+ description + ", added=" + added + ", myTaskId=" + myTaskId
				+ ", totalDateCount=" + totalDateCount + ", currentStreak="
				+ currentStreak + "]";
	}
	
	
	public String getTotalJobCount() {
		return totalJobCount;
	}


	public void setTotalJobCount(String totalJobCount) {
		this.totalJobCount = totalJobCount;
	}


	public String getManCountToday() {
		return manCountToday;
	}

	public void setManCountToday(String manCountToday) {
		this.manCountToday = manCountToday;
	}

	public String getTotalCheckCount() {
		return totalCheckCount;
	}

	public void setTotalCheckCount(String totalCheckCount) {
		this.totalCheckCount = totalCheckCount;
	}

	public String getTotalDateCount() {
		return totalDateCount;
	}
	public void setTotalDateCount(String totalDateCount) {
		this.totalDateCount = totalDateCount;
	}
	public String getCurrentStreak() {
		return currentStreak;
	}
	public void setCurrentStreak(String currentStreak) {
		this.currentStreak = currentStreak;
	}
	public String getTitle() {
		return title.trim();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getJobCountToday() {
		return jobCountToday;
	}
	public void setJobCountToday(String jobCountToday) {
		this.jobCountToday = jobCountToday;
	}
	public String getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(String taskCount) {
		this.taskCount = taskCount;
	}
	public String getTime_limit_flag() {
		return time_limit_flag;
	}
	public void setTime_limit_flag(String time_limit_flag) {
		this.time_limit_flag = time_limit_flag;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAdded() {
		return added;
	}
	public void setAdded(String added) {
		this.added = added;
	}
	public String getMyTaskId() {
		return myTaskId;
	}
	public void setMyTaskId(String myTaskId) {
		this.myTaskId = myTaskId;
	}
	
	

}
