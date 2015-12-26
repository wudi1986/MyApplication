package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentListBean implements Serializable{
	private int currentPage;
	private int totalCount;
	private int totalPage;

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
	public ArrayList<CommentsBean> getListData() {
		return listData;
	}
	public void setListData(ArrayList<CommentsBean> listData) {
		this.listData = listData;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	private int pageLimit;
	private ArrayList<CommentsBean> listData;
	private int nextPage;
	private int prevPage;

	public CommentListBean(int currentPage, int totalCount, int totalPage,
			int pageLimit, ArrayList<CommentsBean> listData, int nextPage,
			int prevPage) {
		super();
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.pageLimit = pageLimit;
		this.listData = listData;
		this.nextPage = nextPage;
		this.prevPage = prevPage;
	}

	public CommentListBean() {
		super();
	}

	@Override
	public String toString() {
		return "CommentListBean [currentPage=" + currentPage + ", totalCount="
				+ totalCount + ", totalPage=" + totalPage + ", pageLimit="
				+ pageLimit + ", listData=" + listData + ", nextPage="
				+ nextPage + ", prevPage=" + prevPage + "]";
	}

}
