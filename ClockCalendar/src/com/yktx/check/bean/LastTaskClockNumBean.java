package com.yktx.check.bean;

import java.io.Serializable;

public class LastTaskClockNumBean implements Serializable{
	private String taskid;
	private String num;
	
	public LastTaskClockNumBean() {
		super();
	}
	@Override
	public String toString() {
		return "LastTaskClockNumBean [taskid=" + taskid + ", num=" + num + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	

}
