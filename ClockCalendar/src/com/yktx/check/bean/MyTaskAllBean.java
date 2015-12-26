package com.yktx.check.bean;

import java.io.Serializable;
import java.util.List;

public class MyTaskAllBean implements Serializable{
	private List<MyTaskBean> ongoingTasks;
	private List<MyTaskBean> suspendTasks;
	@Override
	public String toString() {
		return "MyTaskAllBean [ongoingTasks=" + ongoingTasks
				+ ", suspendTasks=" + suspendTasks + "]";
	}
	public MyTaskAllBean(List<MyTaskBean> ongoingTasks,
			List<MyTaskBean> suspendTasks) {
		super();
		this.ongoingTasks = ongoingTasks;
		this.suspendTasks = suspendTasks;
	}
	public MyTaskAllBean() {
		super();
	}
	public List<MyTaskBean> getOngoingTasks() {
		return ongoingTasks;
	}
	public void setOngoingTasks(List<MyTaskBean> ongoingTasks) {
		this.ongoingTasks = ongoingTasks;
	}
	public List<MyTaskBean> getSuspendTasks() {
		return suspendTasks;
	}
	public void setSuspendTasks(List<MyTaskBean> suspendTasks) {
		this.suspendTasks = suspendTasks;
	}
	

}
