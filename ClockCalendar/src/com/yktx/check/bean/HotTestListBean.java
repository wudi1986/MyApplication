package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 分页获取最热打卡
 * @author Administrator
 *
 */

public class HotTestListBean implements Serializable{
	private int currentPage;
	private int totalCount;
	private int totalPage;
	private String pageLimit;
	private ArrayList<HotTestBean> listData;
	private String nextPage;
	private String prevPage;
	@Override
	public String toString() {
		return "HotTestListBean [currentPage=" + currentPage + ", totalCount="
				+ totalCount + ", totalPage=" + totalPage + ", pageLimit="
				+ pageLimit + ", listData=" + listData + ", nextPage="
				+ nextPage + ", prevPage=" + prevPage + "]";
	}
	public HotTestListBean(int currentPage, int totalCount,
			int totalPage, String pageLimit,
			ArrayList<HotTestBean> listData, String nextPage, String prevPage) {
		super();
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.pageLimit = pageLimit;
		this.listData = listData;
		this.nextPage = nextPage;
		this.prevPage = prevPage;
	}
	public HotTestListBean() {
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
	public String getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(String pageLimit) {
		this.pageLimit = pageLimit;
	}
	public ArrayList<HotTestBean> getListData() {
		return listData;
	}
	public void setListData(ArrayList<HotTestBean> listData) {
		this.listData = listData;
	}
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(String prevPage) {
		this.prevPage = prevPage;
	}
	
}
