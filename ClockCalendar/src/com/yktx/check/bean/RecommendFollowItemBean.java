package com.yktx.check.bean;

import java.io.Serializable;

public class RecommendFollowItemBean implements Serializable{
	/** 卡Id */
	private String taskId;
	/** 标题 */
	private String title;
	/** 坚持天数 */
	private int totalDateCount;
	/** 勋章来源 */
	private int badgeSource;
	/** 勋章路径 */
	private String badgePath;
	/** buildingId */
	private String buildingId;
	
	
	
	public RecommendFollowItemBean() {
		super();
	}
	public RecommendFollowItemBean(String taskId, String title,
			int totalDateCount, int badgeSource, String badgePath,
			String buildingId) {
		super();
		this.taskId = taskId;
		this.title = title;
		this.totalDateCount = totalDateCount;
		this.badgeSource = badgeSource;
		this.badgePath = badgePath;
		this.buildingId = buildingId;
	}
	@Override
	public String toString() {
		return "RecommendFollowItemBean [taskId=" + taskId + ", title=" + title
				+ ", totalDateCount=" + totalDateCount + ", badgeSource="
				+ badgeSource + ", badgePath=" + badgePath + ", buildingId="
				+ buildingId + "]";
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTitle() {
		return title;
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
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	


}
