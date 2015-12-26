package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MsgToUserBean implements Serializable{
	private int currentPage;
	private int totalCount;
	private int totalPage;
	private int pageLimit;
	private ArrayList<MsgToUserListBean> listData;
	public MsgToUserBean(int currentPage, int totalCount, int totalPage,
			int pageLimit, ArrayList<MsgToUserListBean> listData) {
		super();
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.pageLimit = pageLimit;
		this.listData = listData;
	}
	public MsgToUserBean() {
		super();
	}
	@Override
	public String toString() {
		return "MsgToUserBean [currentPage=" + currentPage + ", totalCount="
				+ totalCount + ", totalPage=" + totalPage + ", pageLimit="
				+ pageLimit + ", listData=" + listData + "]";
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
	public ArrayList<MsgToUserListBean> getListData() {
		return listData;
	}
	public void setListData(ArrayList<MsgToUserListBean> listData) {
		this.listData = listData;
	}
	
	
}
