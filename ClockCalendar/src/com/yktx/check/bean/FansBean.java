package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FansBean implements Serializable{
	private int currentPage;
	private int totalCount;
	private int totalPage;
	private int pageLimit;
	private ArrayList<FansItemBean> listData = new ArrayList<FansItemBean>();
	
	
	
	
	@Override
	public String toString() {
		return "FansBean [currentPage=" + currentPage + ", totalCount="
				+ totalCount + ", totalPage=" + totalPage + ", pageLimit="
				+ pageLimit + ", listData=" + listData + "]";
	}
	public FansBean(int currentPage, int totalCount, int totalPage,
			int pageLimit, ArrayList<FansItemBean> listData) {
		super();
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.pageLimit = pageLimit;
		this.listData = listData;
	}
	public FansBean() {
		super();
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
	public ArrayList<FansItemBean> getListData() {
		return listData;
	}
	public void setListData(ArrayList<FansItemBean> listData) {
		this.listData = listData;
	}
	
	

}
