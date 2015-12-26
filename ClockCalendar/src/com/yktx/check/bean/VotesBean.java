package com.yktx.check.bean;

import java.io.Serializable;

public class VotesBean implements Serializable{
	private String name;
	private String userId;
	@Override
	public String toString() {
		return "VotesBean [name=" + name + ", userId=" + userId + "]";
	}
	public VotesBean(String name, String userId) {
		super();
		this.name = name;
		this.userId = userId;
	}
	public VotesBean() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
