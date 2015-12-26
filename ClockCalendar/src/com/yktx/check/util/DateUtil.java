package com.yktx.check.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

import com.yktx.check.bean.CustomDate;

public class DateUtil {

	public static String[] weekName = { "周日", "周一", "周二", "周三", "周四", "周五",
			"周六" };

	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}

	public static long getDate(String dateString, int beforeDays)  {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date inputDate;
		try {
			inputDate = dateFormat.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear - beforeDays);
			return cal.getTime().getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static CustomDate getNextSunday(CustomDate curDate) {

		Calendar c = Calendar.getInstance();
		CustomDate date = null;
		if (curDate == null) {
			c.add(Calendar.DATE, 7 - getWeekDay() + 1);
			date = new CustomDate(c.get(Calendar.YEAR),
					c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 也可将此值当参数传进来
			String pTime = curDate.getYear() + "-" + curDate.getMonth() + "-"
					+ curDate.getDay();
			Tools.getLog(Tools.i, "aaa", "curDate.getYear() ============ "
					+ curDate.getYear() + "-" + curDate.getMonth() + "-"
					+ curDate.getDay());
			try {
				c.setTime(format.parse(pTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.add(Calendar.DATE, 7 - c.get(Calendar.DAY_OF_WEEK) + 1);
			date = new CustomDate(c.get(Calendar.YEAR),
					c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		}
		return date;
	}

	public static int[] getWeekSunday(int year, int month, int day, int pervious) {
		int[] time = new int[3];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.DAY_OF_MONTH, pervious);
		time[0] = c.get(Calendar.YEAR);
		time[1] = c.get(Calendar.MONTH) + 1;
		time[2] = c.get(Calendar.DAY_OF_MONTH);
		return time;
	}

	public static int getWeekDayFromDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromString(int year, int month) {
		String dateString = year + "-" + (month > 9 ? month : ("0" + month))
				+ "-01";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	public static boolean isToday(CustomDate date) {
		if (date == null)
			return false;
		return (date.year == DateUtil.getYear()
				&& date.month == DateUtil.getMonth() && date.day == DateUtil
					.getCurrentMonthDay());
	}

	public static int getWeekofMonth(CustomDate date) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, date.getYear());
		c.set(Calendar.MONTH, date.getMonth());
		c.set(Calendar.DAY_OF_MONTH, date.getDay());
		Tools.getLog(Tools.i, "aaa", date.getYear() + "nian " + date.getMonth()
				+ "yue" + date.getDay());
		int firstDayWeek = DateUtil.getWeekDayFromDate(date.year, date.month);
		Tools.getLog(Tools.i, "kkk", "firstDayWeek ========== " + firstDayWeek);
		if ((firstDayWeek + date.getDay()) % 7 == 0) {
			return (firstDayWeek + date.getDay()) / 7 - 1;
		}
		return (firstDayWeek + date.getDay()) / 7;
	}

	public static boolean isCurrentMonth(CustomDate date) {
		return (date.year == DateUtil.getYear() && date.month == DateUtil
				.getMonth());
	}
}
