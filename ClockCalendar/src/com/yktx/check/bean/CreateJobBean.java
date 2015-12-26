package com.yktx.check.bean;

import java.io.Serializable;

public class CreateJobBean implements Serializable{
	/** 新建的打卡Id */
	private String id;
	/** 对应的任务Id */
	private String taskId;
	/** 对应的BuildingId */
	private String buildingId;
	/** 签名 */
	private String signature;
	/** 数值 */
	private String quantity;
	/** 是否放弃 */
	private String giveUpFlag;
	/** 放弃理由 */
	private String giveUpReason;
	/** 打卡时间 */
	private String checkTime;
	/** 打卡日期 */
	private String checkDate;
	/** 打卡过后, 用户当日的表现, 1表示打了一部分卡, 2表示全部完成 */
	private String performance;

	/** 今天打卡人数 */
	private int manCountToday;

	/** 今日获得分数 */
	int pointToday;
	/** 排名 */
	int rank;
	/** 此卡一共打了多少天 */
	int checkDateCount;
	
	

	public int getCheckDateCount() {
		return checkDateCount;
	}

	public void setCheckDateCount(int checkDateCount) {
		this.checkDateCount = checkDateCount;
	}

	public int getPointToday() {
		return pointToday;
	}

	public void setPointToday(int pointToday) {
		this.pointToday = pointToday;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getManCountToday() {
		return manCountToday;
	}

	public void setManCountToday(int manCountToday) {
		this.manCountToday = manCountToday;
	}

	public CreateJobBean() {
		super();
	}
	@Override
	public String toString() {
		return "CreateJobBean [id=" + id + ", taskId=" + taskId
				+ ", buildingId=" + buildingId + ", signature=" + signature
				+ ", quantity=" + quantity + ", giveUpFlag=" + giveUpFlag
				+ ", giveUpReason=" + giveUpReason + ", checkTime=" + checkTime
				+ ", checkDate=" + checkDate + ", performance=" + performance
				+ "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getSignature() {
		if(signature != null){
			return signature.trim();
		}
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getGiveUpFlag() {
		return giveUpFlag;
	}
	public void setGiveUpFlag(String giveUpFlag) {
		this.giveUpFlag = giveUpFlag;
	}
	public String getGiveUpReason() {
		return giveUpReason;
	}
	public void setGiveUpReason(String giveUpReason) {
		this.giveUpReason = giveUpReason;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getPerformance() {
		return performance;
	}
	public void setPerformance(String performance) {
		this.performance = performance;
	}
	

}
