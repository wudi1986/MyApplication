package com.yktx.check.bean;

import java.io.Serializable;

public class RecommendBean implements Serializable{
	private String id;
	private String time_limit_flag;
	private String begin_time;
	private String end_time;
	private String title;
	private String description;
	private String attCount;
	private String jobCount;
	
	
	public String getJobCount() {
		return jobCount;
	}
	public void setJobCount(String jobCount) {
		this.jobCount = jobCount;
	}
	public RecommendBean() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getAttCount() {
		return attCount;
	}
	public void setAttCount(String attCount) {
		this.attCount = attCount;
	}
	@Override
	public String toString() {
		return "Recommend [id=" + id + ", time_limit_flag=" + time_limit_flag
				+ ", begin_time=" + begin_time + ", end_time=" + end_time
				+ ", title=" + title + ", description=" + description
				+ ", attCount=" + attCount + "]";
	}
	

}
