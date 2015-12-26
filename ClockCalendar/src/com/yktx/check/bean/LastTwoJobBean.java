package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class LastTwoJobBean implements Serializable{
	private ArrayList<LastThree> jobs;
	private int totalJobCount;
	@Override
	public String toString() {
		return "LastTwoJobBean [lastThrees=" + jobs + ", totalJobCount="
				+ totalJobCount + "]";
	}
	public LastTwoJobBean(ArrayList<LastThree> lastThrees, int totalJobCount) {
		super();
		this.jobs = lastThrees;
		this.totalJobCount = totalJobCount;
	}
	public LastTwoJobBean() {
		super();
	}
	public ArrayList<LastThree> getLastThrees() {
		return jobs;
	}
	public void setLastThrees(ArrayList<LastThree> lastThrees) {
		this.jobs = lastThrees;
	}
	public int getTotalJobCount() {
		return totalJobCount;
	}
	public void setTotalJobCount(int totalJobCount) {
		this.totalJobCount = totalJobCount;
	}
	

}
