package com.yktx.check.bean;

import java.io.Serializable;
/**
 * 根据关键字模糊查询building
 * @author Administrator
 *
 */

public class SearchBean implements Serializable{
	private String time_limit_flag;
	private String taskId;
	private String title;
	private String begin_time;
	private String taskCount;
	private String end_time;
	private String description;
	@Override
	public String toString() {
		return "SearchBean [time_limit_flag=" + time_limit_flag + ", taskId="
				+ taskId + ", title=" + title + ", begin_time=" + begin_time
				+ ", taskCount=" + taskCount + ", end_time=" + end_time
				+ ", description=" + description + "]";
	}
	public SearchBean(String time_limit_flag, String taskId, String title,
			String begin_time, String taskCount, String end_time,
			String description) {
		super();
		this.time_limit_flag = time_limit_flag;
		this.taskId = taskId;
		this.title = title;
		this.begin_time = begin_time;
		this.taskCount = taskCount;
		this.end_time = end_time;
		this.description = description;
	}
	public SearchBean() {
		super();
	}
	public String getTime_limit_flag() {
		return time_limit_flag;
	}
	public void setTime_limit_flag(String time_limit_flag) {
		this.time_limit_flag = time_limit_flag;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTitle() {
		return title.trim();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(String taskCount) {
		this.taskCount = taskCount;
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
	

}
