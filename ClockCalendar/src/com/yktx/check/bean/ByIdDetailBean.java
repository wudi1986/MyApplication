package com.yktx.check.bean;

import java.io.Serializable;

public class ByIdDetailBean implements Serializable{
	/** taskId */
	private String id;
	/** 用户Id */
	private String userId;
	/** 所属buildingId */
	private String buildingId;
	/** 标题 */
	private String title;
	/**描述    */
	private String description;
	/** 是否隐私, 1为隐私, 0为不隐私 */
	private String privateFlag;
	/** 是否限时, 1为限时, 0为不限时 */
	private String timeLimitFlag;
	/** 限时开始时间, 格式为HH:MM */
	private String beginTime;
	/** 限时结束时间, 格式为HH:MM */
	private String endTime;
	/** 是否置顶, 1为是, 0为不是 */
	private String stickFlag;
	/**  置顶时间 */
	private String stickTime;
	/** 颜色 */
	private String color;
	/** 提醒时间, 格式为HH:MM, 若不提醒则为-1 */
	private String alertTime;
	/** 状态, 1为正常, 2为暂停 */
	private String status;
	/** 累计打卡次数 */
	private String totalCheckCount;
	/** 累计打卡天数*/
	private String totalDateCount;
	/** 气球数 */
	private String point;
	/** //勋章图片地址, 若无勋章则为null  */
	private String badgePath;
	/** 勋章来源, 1为123服务器, 2为七牛 */
	private int badgeSource;

	private int autoShareFlag;
	
	private String userName;
	
	private long cTime;
	/** 当前等级进度, 0-6 */
	private int progress;
	
	



	@Override
	public String toString() {
		return "ByIdDetailBean [id=" + id + ", userId=" + userId
				+ ", buildingId=" + buildingId + ", title=" + title
				+ ", description=" + description + ", privateFlag="
				+ privateFlag + ", timeLimitFlag=" + timeLimitFlag
				+ ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", stickFlag=" + stickFlag + ", stickTime=" + stickTime
				+ ", color=" + color + ", alertTime=" + alertTime + ", status="
				+ status + ", totalCheckCount=" + totalCheckCount
				+ ", totalDateCount=" + totalDateCount + ", point=" + point
				+ ", badgePath=" + badgePath + ", badgeSource=" + badgeSource
				+ ", autoShareFlag=" + autoShareFlag + ", userName=" + userName
				+ ", cTime=" + cTime + "]";
	}

	public ByIdDetailBean() {
		super();
	}

	public ByIdDetailBean(String id, String userId, String buildingId,
			String title, String description, String privateFlag,
			String timeLimitFlag, String beginTime, String endTime,
			String stickFlag, String stickTime, String color, String alertTime,
			String status, String totalCheckCount, String totalDateCount,
			String point, String badgePath, int badgeSource,
			int autoShareFlag, String userName, long cTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.buildingId = buildingId;
		this.title = title;
		this.description = description;
		this.privateFlag = privateFlag;
		this.timeLimitFlag = timeLimitFlag;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.stickFlag = stickFlag;
		this.stickTime = stickTime;
		this.color = color;
		this.alertTime = alertTime;
		this.status = status;
		this.totalCheckCount = totalCheckCount;
		this.totalDateCount = totalDateCount;
		this.point = point;
		this.badgePath = badgePath;
		this.badgeSource = badgeSource;
		this.autoShareFlag = autoShareFlag;
		this.userName = userName;
		this.cTime = cTime;
	}

	
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAutoShareFlag() {
		return autoShareFlag;
	}

	public void setAutoShareFlag(int autoShareFlag) {
		this.autoShareFlag = autoShareFlag;
	}
	public String getBadgePath() {
		return badgePath;
	}

	public void setBadgePath(String badgePath) {
		this.badgePath = badgePath;
	}

	public int getBadgeSource() {
		return badgeSource;
	}

	public void setBadgeSource(int badgeSource) {
		this.badgeSource = badgeSource;
	}
	
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrivateFlag() {
		return privateFlag;
	}
	public void setPrivateFlag(String privateFlag) {
		this.privateFlag = privateFlag;
	}
	public String getTimeLimitFlag() {
		return timeLimitFlag;
	}
	public void setTimeLimitFlag(String timeLimitFlag) {
		this.timeLimitFlag = timeLimitFlag;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStickFlag() {
		return stickFlag;
	}
	public void setStickFlag(String stickFlag) {
		this.stickFlag = stickFlag;
	}
	public String getStickTime() {
		return stickTime;
	}
	public void setStickTime(String stickTime) {
		this.stickTime = stickTime;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
