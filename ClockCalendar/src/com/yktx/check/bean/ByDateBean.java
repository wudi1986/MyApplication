package com.yktx.check.bean;

import java.io.Serializable;

import com.duguang.baseanimation.ui.listivew.deletelistview.SlideView;

public class ByDateBean implements Serializable {
	/** Task的主键Id */
	private String taskId;
	/** 是否是放弃打卡, 1为是 0为不是 */
	private String giveUpFlag;
	/** Task标题 */
	private String title;
	/** 累计打卡天数 */
	private int totalDateCount;
	/** 颜色 */
	private int color;
	/** 在范围内所有打过卡的日期 */
	private String allDate = "";
	/** 当前连续打卡天数 */
	private int currentStreak;
	/** 当日最后两次打卡的时间, 格式为HH:mm,若jobCount为2则用","分隔, 若jobCount为0或大于2则不不显示 */
	private String checkTime;
	/** 当日打卡次数 */
	private int jobCount;
	/** 是否置顶, 1为置顶, 0为不是 */
	private int stickFlag;
	/** 置顶时间 */
	private long stickTime;

	private String alertTime;

	private int isConnSuccess = 1;

	private String begin_time;
	
	private String end_time;
	
	private int time_limit_flag;
	/** 上次打卡的数值*/
	private String lastQuantity;

	/** 是否分享到微博 */
	private int autoShareFlag;
	/** bar */
	private int progress;

	/** 勋章标识 */ 
	private int badgeSource;
	
	/** 勋章路径*/
	private String badgePath;
	
	
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getBadgeSource() {
		return badgeSource;
	}

	public void setBadgeSource(int badgeSource) {
		this.badgeSource = badgeSource;
	}

	public String getBadgePath() {
		return badgePath;
	}

	public void setBadgePath(String badgePath) {
		this.badgePath = badgePath;
	}

	public int getAutoShareFlag() {
		return autoShareFlag;
	}

	public void setAutoShareFlag(int autoShareFlag) {
		this.autoShareFlag = autoShareFlag;
	}

	/**
	 *  1-正常，2暂停
	 */
	
	private int status = 1;
	/** 打卡说明 */
	private String description;
	/** 累计打卡次数 */
	private int totalCheckCount;
	/** 此卡是否隐私, 1为隐私, 0为不隐私 */
	private int privateFlag;

	/** 上次打卡单位, 若为"0"则没有单位 */
	private String lastUnit;
	

	public String getLastUnit() {
		return lastUnit;
	}

	public void setLastUnit(String lastUnit) {
		this.lastUnit = lastUnit;
	}

	@Override
	public String toString() {
		return "ByDateBean [taskId=" + taskId + ", giveUpFlag=" + giveUpFlag
				+ ", title=" + title + ", totalDateCount=" + totalDateCount
				+ ", color=" + color + ", allDate=" + allDate
				+ ", currentStreak=" + currentStreak + ", checkTime="
				+ checkTime + ", jobCount=" + jobCount + ", stickFlag="
				+ stickFlag + ", stickTime=" + stickTime + ", isConnSuccess="
				+ isConnSuccess + ", begin_time=" + begin_time + ", end_time="
				+ end_time + ", time_limit_flag=" + time_limit_flag
				+ ", status=" + status + "]";
	}
	
	
	public int getPrivateFlag() {
		return privateFlag;
	}


	public void setPrivateFlag(int privateFlag) {
		this.privateFlag = privateFlag;
	}


	public int getTotalCheckCount() {
		return totalCheckCount;
	}


	public void setTotalCheckCount(int totalCheckCount) {
		this.totalCheckCount = totalCheckCount;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastQuantity() {
		return lastQuantity;
	}

	public void setLastQuantity(String lastQuantity) {
		this.lastQuantity = lastQuantity;
	}
	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getTime_limit_flag() {
		return time_limit_flag;
	}

	public void setTime_limit_flag(int time_limit_flag) {
		this.time_limit_flag = time_limit_flag;
	}

	public int getIsConnSuccess() {
		return isConnSuccess;
	}

	public void setIsConnSuccess(int isConnSuccess) {
		this.isConnSuccess = isConnSuccess;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getGiveUpFlag() {
		return giveUpFlag;
	}

	public void setGiveUpFlag(String giveUpFlag) {
		this.giveUpFlag = giveUpFlag;
	}

	public String getTitle() {
		return title.trim();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotalDateCount() {
		return totalDateCount;
	}

	public void setTotalDateCount(int totalDateCount) {
		this.totalDateCount = totalDateCount;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getStickFlag() {
		return stickFlag;
	}

	public void setStickFlag(int stickFlag) {
		this.stickFlag = stickFlag;
	}

	public String getAllDate() {
		return allDate;
	}

	public void setAllDate(String allDate) {
		this.allDate = allDate;
	}

	public int getCurrentStreak() {
		return currentStreak;
	}

	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public int getJobCount() {
		return jobCount;
	}

	public void setJobCount(int jobCount) {
		this.jobCount = jobCount;
	}

	public long getStickTime() {
		return stickTime;
	}

	public void setStickTime(long stickTime) {
		this.stickTime = stickTime;
	}

	public ByDateBean() {
		super();
	}
	
	
}
