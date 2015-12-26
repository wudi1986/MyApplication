package com.yktx.check.bean;

import java.io.Serializable;

public class CreateTaskBean implements Serializable{
	/** 用户Id */
	private String id;
	/**  任务标题   */
	private String userId;
	/** 任务说明 */
	private String buildingId;
	/** 是否隐私, 1为隐私, 0为不隐私 */
	private String streak;
	/** 是否限时, 1为限时, 0为不限时*/
	private String description;
	/** 限时开始时间, 格式为HH:MM*/
	private String privateFlag;
	/** 限时结束时间, 格式为HH:MM*/
	private String timeLimitFlag;
	/** 颜色 */
	private String beginTime;
	/** 封面的BASE64编码 */
	private String endTime;
	/** 此次操作是否在联网状态下记录, 1为是 0为不是*/
	private String color;
	/** 无网下创建此任务的时间 */
	private String coverPath;
	public CreateTaskBean() {
		super();
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
	public String getStreak() {
		return streak;
	}
	public void setStreak(String streak) {
		this.streak = streak;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCoverPath() {
		return coverPath;
	}
	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}
	@Override
	public String toString() {
		return "CreateTaskBean [id=" + id + ", userId=" + userId
				+ ", buildingId=" + buildingId + ", streak=" + streak
				+ ", description=" + description + ", privateFlag="
				+ privateFlag + ", timeLimitFlag=" + timeLimitFlag
				+ ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", color=" + color + ", coverPath=" + coverPath + "]";
	}
	

}
