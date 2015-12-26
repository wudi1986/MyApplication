package com.yktx.check.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 */
public class TimeUtil {
	public static String getwelcometime() {
		String time = "";
		Date d = new Date();
		if (d.getHours() >= 0 && d.getHours() < 3) {
			time = "凌晨";
		} else if (d.getHours() >= 3 && d.getHours() < 5) {
			time = "黎明";
		} else if (d.getHours() >= 5 && d.getHours() < 6) {
			time = "拂晓";
		} else if (d.getHours() >= 6 && d.getHours() < 7) {
			time = "清晨";
		} else if (d.getHours() >= 7 && d.getHours() < 8) {
			time = "早晨";
		} else if (d.getHours() >= 8 && d.getHours() < 11) {
			time = "上午";
		} else if (d.getHours() >= 11 && d.getHours() < 13) {
			time = "中午";
		} else if (d.getHours() >= 13 && d.getHours() < 16) {
			time = "下午";
		} else if (d.getHours() >= 16 && d.getHours() < 17) {
			time = "黄昏";
		} else if (d.getHours() >= 17 && d.getHours() < 18) {
			time = "傍晚";
		} else if (d.getHours() >= 18 && d.getHours() < 23) {
			time = "晚上";
		} else if (d.getHours() >= 23 && d.getHours() < 24) {
			time = "午夜";
		}
		return time;
	}

	public static long getUnixLong(String time) {
		try {

			// Date或者String转化为时间戳

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			// String time="1970-01-06 11:45:55";

			Date date = format.parse(time);

			System.out.print("Format To times:" + date.getTime());
			return date.getTime();
		} catch (Exception ex) {
			return 0;
		}
	}

	public static String getDateTime(Date sDate, String forma) {
		if (sDate == null)
			return "";
		try {
			return new SimpleDateFormat(forma).format(sDate);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getDateTime(Date sDate) {
		if (sDate == null)
			return "";
		try {
			return new SimpleDateFormat("yyyy-MM-dd").format(sDate);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getDate(String s, String forma) {
		if (s == null || s.equals("")) {
			return "";
		}
		try {
			return TimeUtil.getDateTime(TimeUtil.format(s, forma), forma);
		} catch (Exception ex) {
			return "";
		}
	}

	public static Date format(String s, String forma) {
		Date lastdt = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(forma);
			lastdt = sdf.parse(s);
			return lastdt;
		} catch (Exception e) {
			return new Date();
		}
	}

	public static Date format(String s) {
		Date lastdt = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			lastdt = sdf.parse(s);
			return lastdt;
		} catch (Exception e) {
			return new Date();
		}
	}

	public static Date format(Date s, String forma) {
		Date lastdt = null;
		String date = getDateTime(s, forma);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(forma);
			lastdt = sdf.parse(date);
			return lastdt;
		} catch (Exception e) {
			System.out.println("s");
			return new Date();
		}
	}

	public static String getbeforeDTime(int days, String forma) {
		Date dateresult = new Date();
		DateFormat df = new SimpleDateFormat(forma);
		try {
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, days);
			dateresult = cal.getTime();
		} catch (Exception e) {
			System.out.println("exception" + e.toString());
		}
		return df.format(dateresult);
	}

	public static int getNumericDatePeriod(Date sDate) {
		int iTime = 0;
		try {
			Date date1 = new Date();
			iTime = (int) ((sDate.getTime() - date1.getTime()) / 1000L);
		} catch (Exception ex) {
		}
		return iTime;
	}

	public static int getYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static String getUTCFullDateTime(Date date) {
		try {
			Calendar gmtlocal = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+8"));
			gmtlocal.setTime(date);

			SimpleDateFormat sf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");
			sf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			return sf.format(gmtlocal.getTime());
		} catch (Exception ex) {
			return "";
		}
	}

	public static Date getGMT8FullDateTime(String sDate) {
		try {
			String formater = "yyyy-MM-dd'T'HH:mm:ss'Z'";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);

			Calendar gmtlocal = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+0"));
			gmtlocal.setTime(date);

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			sf.setTimeZone(TimeZone.getTimeZone("GMT+16"));
			return sf.parse(sf.format(gmtlocal.getTime()));
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String getGMT8FullDateString(String sDate) {
		try {
			String formater = "yyyy-MM-dd'T'HH:mm:ss'Z'";
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);
			return TimeUtil.getDateTime(date, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			return TimeUtil.getDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
		}
	}

	public static String getMMDD(long unixLong) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixLong);
		SimpleDateFormat dateformat = new SimpleDateFormat("MM月dd日");
		return dateformat.format(c.getTime());
	}

	public static String getyyyyMM(long unixLong) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixLong);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy:MM");
		return dateformat.format(c.getTime());
	}

	public static String getHHmm(long unixLong) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixLong);
		SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
		return dateformat.format(c.getTime());
	}
	
	public static String getMMDDHHmm(long unixLong) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(unixLong);
		SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH:mm");
		return dateformat.format(c.getTime());
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTimes(long unixLong) {
		long currentDate = System.currentTimeMillis();
		long times = currentDate / 1000 - unixLong / 1000;
		// return "侧首";
		if (times > 172800) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(unixLong);
			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
			String time = dateformat.format(c.getTime());
			String[] timeArray = time.split("-");
			int month = Integer.parseInt(timeArray[0]);
			int day = Integer.parseInt(timeArray[1]);
			time = month + "-" + day;
			return time;
		} else if (times >= 86400 && times < 172800) {
			return "昨天";
		} else if (times > 3600 && times < 86400) {
			return times / (60 * 60) + "小时前";
		} else if (times > 60 && times <= 3600) {
			return times / (60) + "分钟前";
		} else {
			return "刚刚";
		}
	}
	public static String getAttentionTimes(long unixLong){
		long currentDate = System.currentTimeMillis();
		long times = currentDate / 1000 - unixLong / 1000;
		if(times > 259200){
			return "提醒Ta打卡";
		}else if (times >= 172800 && times < 259200) {
			return "前天";
		}else if (times >= 86400 && times < 172800) {
			return "昨天";
		}else{
			return "今天";
		}

	}
	@SuppressLint("SimpleDateFormat")
	public static String getBuildTimes(long unixLong) {
		long currentDate = System.currentTimeMillis();
		long times = currentDate / 1000 - unixLong / 1000;
		// return "侧首";
		if (times > 86400) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(unixLong);
			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
			String time = dateformat.format(c.getTime());
			String[] timeArray = time.split("-");
			int month = Integer.parseInt(timeArray[0]);
			int day = Integer.parseInt(timeArray[1]);
			time = month + "-" + day;
			return time;
		} 
		//		else if (times >= 86400 && times < 172800) {
		//			return "昨天";
		//		} 
		else if (times > 3600 && times < 86400) {
			return times / (60 * 60) + "小时前";
		} else if (times > 60 && times <= 3600) {
			return times / (60) + "分钟前";
		} else {
			return "刚刚";
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTaskInfoTimes(long unixLong) {
		long currentDate = System.currentTimeMillis();
		long times = currentDate / 1000 - unixLong / 1000;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date date = new Date(unixLong);
		String time = formatter.format(date);
		return time;
		// return "侧首";
		//		if (times > 172800) {
		//			Calendar c = Calendar.getInstance();
		//			c.setTimeInMillis(unixLong);
		//			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
		//			String time = dateformat.format(c.getTime());
		//			String[] timeArray = time.split("-");
		//			int month = Integer.parseInt(timeArray[0]);
		//			int day = Integer.parseInt(timeArray[1]);
		//			time = month + "-" + day;

		//		} else if (times >= 86400 && times < 172800) {
		//			return "昨天";
		//		} else if (times > 3600 && times < 86400) {
		//			return times / (60 * 60) + "小时前";
		//		} else if (times > 60 && times <= 3600) {
		//			return times / (60) + "分钟前";
		//		} else {
		//			return "刚刚";
		//		}
	}

	public static int getYear(long times) {
		Calendar cld = Calendar.getInstance();
		Date date = new Date(times);
		return date.getYear();
	}

	public static String getTimes1(long unixLong) {
		long currentDate = new Date().getTime();
		long times = (currentDate - new Date(unixLong).getTime()) / 1000;
		if (times > 86400) {
			return getDate(String.valueOf(unixLong), "yyyy-MM-dd");
		} else if (times > 3600) {
			return times / (60 * 60) + "小时前";
		} else if (times > 60) {
			if (times / 60 * 60 == 0) {
				return "刚刚";
			}
			return times / (60 * 60) + "分钟前";
		} else {
			return "刚刚";
		}
	}

	public static String getYYMMDD(long unixLong) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(unixLong));

		// return getDate(String.valueOf(unixLong), "yyyy-MM-dd");
	}

	public static boolean isSameDate(long lastDate, long curDate) {

		String lastStr = getDateString(lastDate);
		String curStr = getDateString(curDate);
		// Tools.getLog(Tools.i, "aaa", "TimeUtil   lastStr = "+lastStr);
		// Tools.getLog(Tools.i, "aaa", "TimeUtil   lcurStr = "+curStr);
		// Tools.getLog(Tools.i, "aaa", "lastDate = "+lastDate);
		// Tools.getLog(Tools.i, "aaa", "curDate = "+curDate);

		if (lastStr.equals(curStr)) {
			return true;
		}
		return false;

	}

	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("出生时间大于当前时间!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;// 注意此处，如果不加1的话计算结果是错误的
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		Tools.getLog(Tools.i, "aaa", "yearNow ====== " + yearNow);
		Tools.getLog(Tools.i, "aaa", "yearBirth ====== " + yearBirth);
		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else if (monthNow < monthBirth) {
					// do nothing
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		} else {
			// monthNow<monthBirth
			// donothing
		}
		return age;
	}

	/**
	 * 获取当前时间的前一天时间
	 * 
	 * @param cl
	 * @return
	 */
	public static Calendar getBeforeDay(Calendar cl) {
		// 使用roll方法进行向前回滚
		// cl.roll(Calendar.DATE, -1);
		// 使用set方法直接进行设置
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day - 1);
		return cl;
	}

	/**
	 * 获取当前时间的后一天时间
	 * 
	 * @param cl
	 * @return
	 */
	public static Calendar getAfterDay(Calendar cl) {
		// 使用roll方法进行回滚到后一天的时间
		// cl.roll(Calendar.DATE, 1);
		// 使用set方法直接设置时间值
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day + 2);
		return cl;
	}

	public static String getDateString(long dateLong) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(dateLong));
		return date;
	}

	/**
	 * 打印时间
	 * 
	 * @param cl
	 */
	public static String printCalendar(Calendar cl) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(cl.getTime());
		return dateStr;
	}

	public static void main(String[] args) {
		Calendar cld = Calendar.getInstance();
		Date date = new Date(1385893001l);
		cld.setTime(date);
	}
}
