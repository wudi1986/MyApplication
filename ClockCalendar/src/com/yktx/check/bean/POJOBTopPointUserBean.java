package com.yktx.check.bean;

import java.util.List;

public class POJOBTopPointUserBean {
	private TopPointUserBean topPointUserBean;
	private int index;
	private int offSet;
	
	
	
	public POJOBTopPointUserBean() {
		super();
	}

	public POJOBTopPointUserBean(TopPointUserBean topPointUserBean, int index,
			int offSet) {
		super();
		this.topPointUserBean = topPointUserBean;
		this.index = index;
		this.offSet = offSet;
	}
	
	@Override
	public String toString() {
		return "POJOBTopPointUserBean [topPointUserBean=" + topPointUserBean
				+ ", index=" + index + ", offSet=" + offSet + "]";
	}
	public TopPointUserBean getTopPointUserBean() {
		return topPointUserBean;
	}
	public void setTopPointUserBean(TopPointUserBean topPointUserBean) {
		this.topPointUserBean = topPointUserBean;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}
	
}
