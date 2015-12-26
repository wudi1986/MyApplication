package com.yktx.check.bean;

import java.io.Serializable;

public class GetUserBean implements Serializable{
	private String exsit;
	private CreateUserBean createUserBean;
	@Override
	public String toString() {
		return "GetUserBean [exsit=" + exsit + ", createUserBean="
				+ createUserBean + "]";
	}
	public GetUserBean() {
		super();
	}
	public String getExsit() {
		return exsit;
	}
	public void setExsit(String exsit) {
		this.exsit = exsit;
	}
	public CreateUserBean getCreateUserBean() {
		return createUserBean;
	}
	public void setCreateUserBean(CreateUserBean createUserBean) {
		this.createUserBean = createUserBean;
	}
	

}
