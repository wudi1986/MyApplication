package com.yktx.check.bean;

import java.util.ArrayList;

public class Group {

	private String title;

	private ArrayList<TaskItemBean> itemBeans = new ArrayList<TaskItemBean>(10);

	public ArrayList<TaskItemBean> getItemBeans() {
		return itemBeans;
	}

	public void setItemBeans(ArrayList<TaskItemBean> itemBeans) {
		this.itemBeans = itemBeans;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
