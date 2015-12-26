package com.yktx.check.bean;

import java.io.Serializable;
/**
 * 分页获取最热打卡（每一条）
 * @author Administrator
 *
 */

public class HotTestBean implements Serializable{
	private String title;
	private String jobCountToday;
	private String buildingId;
	public HotTestBean(String title, String jobCountToday, String buildingId) {
		super();
		this.title = title;
		this.jobCountToday = jobCountToday;
		this.buildingId = buildingId;
	}
	
	@Override
	public String toString() {
		return "HotTestBean [title=" + title + ", jobCountToday="
				+ jobCountToday + ", buildingId=" + buildingId + "]";
	}

	public HotTestBean() {
		super();
	}
	public String getTitle() {
		return title.trim();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJobCountToday() {
		return jobCountToday;
	}
	public void setJobCountToday(String jobCountToday) {
		this.jobCountToday = jobCountToday;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	

}
