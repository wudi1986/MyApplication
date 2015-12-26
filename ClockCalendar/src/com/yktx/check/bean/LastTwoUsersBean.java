package com.yktx.check.bean;

import java.io.Serializable;

public class LastTwoUsersBean implements Serializable{
	private int todayUserCount;
	private int totalManCount; //总人数
	private String users;
	private String buildingId;
	public LastTwoUsersBean(int todayUserCount, int totalManCount,
			String users, String buildingId) {
		super();
		this.todayUserCount = todayUserCount;
		this.totalManCount = totalManCount;
		this.users = users;
		this.buildingId = buildingId;
	}
	public LastTwoUsersBean() {
		super();
	}
	@Override
	public String toString() {
		return "LastTwoUsersBean [todayUserCount=" + todayUserCount
				+ ", totalManCount=" + totalManCount + ", users=" + users
				+ ", buildingId=" + buildingId + "]";
	}
	public int getTodayUserCount() {
		return todayUserCount;
	}
	public void setTodayUserCount(int todayUserCount) {
		this.todayUserCount = todayUserCount;
	}
	public int getTotalManCount() {
		return totalManCount;
	}
	public void setTotalManCount(int totalManCount) {
		this.totalManCount = totalManCount;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	

}
