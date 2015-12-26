package com.yktx.check.bean;

import java.io.Serializable;

public class PerformanceBean implements Serializable{
	/** //当日表现, 1表示打了一部分卡, 2表示全部完成*/
	private int performance;
	/** //记录的日期*/
	private String record_date;
	public PerformanceBean() {
		super();
	}
	public int getPerformance() {
		return performance;
	}
	public void setPerformance(int performance) {
		this.performance = performance;
	}
	public String getRecord_date() {
		return record_date;
	}
	public void setRecord_date(String record_date) {
		this.record_date = record_date;
	}
	@Override
	public String toString() {
		return "PerformanceBean [performance=" + performance + ", record_date="
				+ record_date + "]";
	}
	
	
}
