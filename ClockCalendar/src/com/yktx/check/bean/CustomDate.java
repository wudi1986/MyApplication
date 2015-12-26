package com.yktx.check.bean;

import java.io.Serializable;
import java.util.Calendar;

import com.yktx.check.util.DateUtil;

/**
 * 自定义的日期类
 * 
 * @author huang
 * 
 */
public class CustomDate implements Serializable {

	private static final long serialVersionUID = 1L;
	public int year;
	public int month;
	public int day;
	public int week;

	/**
	 * 根据年月日时分秒计算毫秒
	 */

	public String getDate() {
		return year + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);
	}

	public static long DateToUnixTime(int year, int month, int day, int hour,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		// SimpleDateFormat

		return cal.getTime().getTime();
	}

	public static long DateToUnixTime(CustomDate date) {
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonth() - 1, date.getDay());
		// SimpleDateFormat

		return cal.getTime().getTime();
	}

	public CustomDate(int year, int month, int day) {
		if (month > 12) {
			month = 1;
			year++;
		} else if (month < 1) {
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public CustomDate() {
		this.year = DateUtil.getYear();
		this.month = DateUtil.getMonth();
		this.day = DateUtil.getCurrentMonthDay();
	}

	public static CustomDate modifiDayForObject(CustomDate date, int day) {
		CustomDate modifiDate = new CustomDate(date.year, date.month, day);

		return modifiDate;
	}

	@Override
	public String toString() {

		String monthStr = month+"";
		String datStr = day+"";
		
		if(month < 10){
			monthStr = "0"+monthStr;
		}
		if(day < 10){
			datStr = "0"+datStr;
		}
		return year + "-" + monthStr + "-" + datStr;
	
//		return year + "-" + month + "-" + day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

}
