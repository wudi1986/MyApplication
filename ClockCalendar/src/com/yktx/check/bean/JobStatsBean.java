package com.yktx.check.bean;

import java.io.Serializable;

public class JobStatsBean implements Serializable {
	/** 是否放弃, 1为是 0为不是 */
	private String give_up_flag;
	/** 当天所有打卡的数值, 以","分隔 */
	private String quantity;
	/** 日期 */
	private String check_date;
	private String checkTime;

	/** 当天一共打卡次数 */
	private int jobCount;

	public JobStatsBean(String give_up_flag, String quantity,
			String check_date, int jobCount) {
		super();
		this.give_up_flag = give_up_flag;
		this.quantity = quantity;
		this.check_date = check_date;
		this.jobCount = jobCount;
	}

	public JobStatsBean() {
		super();
	}

	@Override
	public String toString() {
		return "JobStatsBean [give_up_flag=" + give_up_flag + ", quantity="
				+ quantity + ", check_date=" + check_date + ", jobCount="
				+ jobCount + "]";
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getGive_up_flag() {
		return give_up_flag;
	}
	public void setGive_up_flag(String give_up_flag) {
		this.give_up_flag = give_up_flag;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getCheck_date() {
		return check_date;
	}
	public void setCheck_date(String check_date) {
		this.check_date = check_date;
	}
	public int getJobCount() {
		return jobCount;
	}
	public void setJobCount(int jobCount) {
		this.jobCount = jobCount;
	}
	
	

}
