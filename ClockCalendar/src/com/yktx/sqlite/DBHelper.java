package com.yktx.sqlite;

//package com.sqlite.sqlite;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.bean.PerformanceBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.util.Tools;

/**
 * 
 @Title: DBHelper
 * @Package com.coupon.activity
 * @Description: TODO
 * @author wudi
 * @date 2012-8-2 2012
 * @version V1.0
 */

public class DBHelper {

	/** errorCode 成功是0 失败为-1 */

	private final String CLASSTAG = DBHelper.class.getSimpleName();

	Context mCotext;
	private final static String _DB_NAME = "WdDB";
	private final static int _DB_VERSION = 1;

	OpenHelper mDbOpenHelper;

	public DBHelper(Context context) {
		super();
		mCotext = context;
		mDbOpenHelper = new OpenHelper(context);
	}

	/**
	 * task表
	 * */
	final static String TASK_LIST_EXCEL = "TASK_LIST_EXCEL";

	/** task表 */

	final static String CREAT_TB_TASK_EXCEL = "CREATE TABLE "
			+ TASK_LIST_EXCEL
			+ "("
			+ " TASK_ID TEXT Integer NOT NULL,"
			+ " TITLE TEXT varchar(50),"
			+ " GIVE_UP_FLAG TEXT varchar," // 是否是放弃打卡, 1为是 0为不是
			+ " TOTAL_DATE_COUNT TEXT Integer," // 打卡天数
			+ " COLOR TEXT Integer," // 颜色
			+ " ALL_DATE TEXT varchar(50),"// 所有天数 “，”隔开
			+ " CURRENT_STREAK TEXT Integer," // 当前连续打卡天数
			+ " TIME_LIMIT_FLAG TEXT Integer," // 限时打卡
			+ " CHECK_TIME TEXT varchar(50)," // 当日最后两次打卡的时间,
			// 格式为HH:mm,若jobCount为2则用","分隔,
			// 若jobCount为0或大于2则不不显示
			+ " JOB_COUNT TEXT Integer," // 当日打卡次数
			+ " BEGIN_TIME TEXT varchar(50)," + " END_TIME TEXT varchar(50),"
			+ " STICK_FLAG TEXT Integer," // 置顶 1 置顶
			+ " STICK_TIME TEXT timestamp," // 置顶时间
			+ " CTIME TEXT timestamp," // 创建时间
			+ " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功
			+ " STATUS TEXT Integer" + ");"; // 1-正常， 2-暂停

	/**
	 * job表
	 * */
	final static String JOB_EXCEL = "JOB_EXCEL";

	/** job表 */

	final static String CREAT_TB_JOB_EXCEL = "CREATE TABLE " + JOB_EXCEL
			+ "("
			+ " JOB_ID TEXT varchar(50) NOT NULL,"
			+ " GIVE_UP_FLAG TEXT Integer," // 是否放弃
			+ " TASK_ID TEXT Integer NOT NULL,"
			+ " QUANTITY TEXT varchar(2) NOT NULL," // 数值
			+ " CHECK_DATE TEXT varchar(50)," // 打卡时间 yyyy-MM-dd
			+ " CTIME TEXT timestamp," // 创建时间
			+ " GIVE_UP_REASON TEXT varchar(50)," // 放弃理由
			+ " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功
			+ " PIC_COUNT TEXT Integer NOT NULL," // 图片数量
			+ " CHECK_TIME TEXT timestamp," // 打卡时间 时间戳
			+ " ALL_PATH TEXT varchar(50)," // 打卡图片
			+ " SIGNATURE TEXT varchar(50)," // 打卡写的话
			+ " STATUS TEXT Integer" + ");";

	// =========================================================

	/**
	 * 连续打卡表
	 * */
	final static String DAILY_PERFORMANCE_EXCEL = "DAILY_PERFORMANCE_EXCEL";

	/** 连续打卡表 */

	final static String CREAT_TB_DAILY_PERFORMANCE_EXCEL = "CREATE TABLE "
			+ DAILY_PERFORMANCE_EXCEL + "(" + " PERFORMANCE Integer NOT NULL,"
			+ " RECORD_DATE varchar(50)" + ");";

	/** 提醒表表名 */
	final static String ALARM_EXCEL = "ALARM_EXCEL";

	/** 提醒表 */
	final static String CREATE_TB_ALARM_EXCEL = "CREATE TABLE ALARM_EXCEL"
			+ "(id integer primary key autoincrement,"
			+ "alarm_id varchar(50) NOT NULL," + // 闹表Id, 客户端生成(必须)
			"taskId varchar(50) NOT NULL," + // 对应的taskId, (必须)
			"days varchar(50)," + // 需要提醒的星期, 以","分隔, 例如"1,2,4,6,7"
			"time varchar(50)," + // 提醒时间, 格式为HH:MM
			"ringtone varchar(50)," + // 提醒铃声, 根据平台对应不同铃声
			"vibrationFlag varchar(50)," + // 是否震动, 1为是 0为不是(必须)
			"remark varchar(50)," + // 备注
			"status varchar(50)," + // 闹表状态, 1为开启, 2为关闭(必须)
			"pickervisity varchar(50)," + // 自己加的Timerpicker是否显示 1 为不显示 2 为显示
			"title varchar(50));";

	// =========================================================
	/***
	 * 添加task list
	 */
	public int insertTaskList(ArrayList<ByDateBean> list) {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);

			if (mDb != null) {
				mDb.beginTransaction();

				mDb.delete(TASK_LIST_EXCEL, "", null);

				try {
					// JSONArray result = new JSONArray(list);

					for (int i = 0; i < list.size(); i++) {
						// JSONObject item = result.getJSONObject(i);

						ContentValues insertValues = new ContentValues();
						ByDateBean bean = list.get(i);
						/**
						 * + "(" + " TASK_ID TEXT Integer NOT NULL," +
						 * " TITLE TEXT varchar(50)," +
						 * " GIVE_UP_FLAG TEXT varchar," // 是否是放弃打卡, 1为是 0为不是 +
						 * " TOTAL_DATE_COUNT TEXT Integer," // 打卡天数 +
						 * " COLOR TEXT Integer," // 颜色 +
						 * " ALL_DATE TEXT varchar(50),"// 所有天数 “，”隔开 +
						 * " CURRENT_STREAK TEXT Integer," // 当前连续打卡天数 +
						 * " TIME_LIMIT_FLAG TEXT Integer," // 限时打卡 +
						 * " CHECK_TIME TEXT varchar(50)," // 当日最后两次打卡的时间, //
						 * 格式为HH:mm,若jobCount为2则用","分隔, // 若jobCount为0或大于2则不不显示
						 * + " JOB_COUNT TEXT Integer," // 当日打卡次数 +
						 * " BEGIN_TIME TEXT varchar(50)," +
						 * " END_TIME TEXT varchar(50)," +
						 * " STICK_FLAG TEXT Integer," // 置顶 1 置顶 +
						 * " STICK_TIME TEXT timestamp," // 置顶时间 +
						 * " CTIME TEXT timestamp," // 创建时间 +
						 * " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功 +
						 * " STATUS TEXT Integer" + ");"; // 1-正常， 2-暂停
						 * 
						 * */
						insertValues.put("TASK_ID", bean.getTaskId());
						insertValues.put("TITLE", bean.getTitle());
						insertValues.put("GIVE_UP_FLAG", bean.getGiveUpFlag());
						insertValues.put("TOTAL_DATE_COUNT",
								bean.getTotalDateCount());
						insertValues.put("COLOR", bean.getColor());
						insertValues.put("ALL_DATE", bean.getColor());
						insertValues.put("CURRENT_STREAK",
								bean.getCurrentStreak());
						insertValues.put("TIME_LIMIT_FLAG",
								bean.getTime_limit_flag());
						insertValues.put("CHECK_TIME", bean.getCheckTime());
						insertValues.put("JOB_COUNT", bean.getJobCount());
						Tools.getLog(
								Tools.i,
								"aaa",
								"insert-------getJobCount-----------"
										+ bean.getJobCount());
						insertValues.put("BEGIN_TIME", bean.getBegin_time());
						insertValues.put("END_TIME", bean.getEnd_time());
						insertValues.put("STICK_FLAG", bean.getStickFlag());
						insertValues.put("STICK_TIME", bean.getStickTime());
						// insertValues.put("CTIME", bean.getc);
						insertValues.put("ISCONN_SUCCESS",
								bean.getIsConnSuccess());
						insertValues.put("STATUS", bean.getStatus());

						mDb.insertOrThrow(TASK_LIST_EXCEL, null, insertValues);
						Tools.getLog(Tools.i, "aaa", "insert------------------"
								+ bean.getTitle());
					}

					mDb.setTransactionSuccessful();

				} catch (SQLException e) {
					errorCode--;
					Log.e("kkk",
							"insertProductsList SQLException:" + e.getMessage());

				} finally {
					mDb.endTransaction();
					mDb.close();
					// returnExcelCount(PRODUCT_LIST_EXCEL, null);
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;

	}

	/** 获得全部的alarm提醒的数据 */
	public ArrayList<ByDateBean> getTaskList() {
		ArrayList<ByDateBean> taskList = new ArrayList<ByDateBean>();
		synchronized (DBInstance.getInstance()) {

			Cursor c = null;
			SQLiteDatabase mDb = openDatabase(false);

			if (mDb != null) {
				try {
					/**
					 * + "(" + " TASK_ID TEXT Integer NOT NULL," +
					 * " TITLE TEXT varchar(50)," +
					 * " GIVE_UP_FLAG TEXT varchar," // 是否是放弃打卡, 1为是 0为不是 +
					 * " TOTAL_DATE_COUNT TEXT Integer," // 打卡天数 +
					 * " COLOR TEXT Integer," // 颜色 +
					 * " ALL_DATE TEXT varchar(50),"// 所有天数 “，”隔开 +
					 * " CURRENT_STREAK TEXT Integer," // 当前连续打卡天数 +
					 * " TIME_LIMIT_FLAG TEXT Integer," // 限时打卡 +
					 * " CHECK_TIME TEXT varchar(50)," // 当日最后两次打卡的时间, //
					 * 格式为HH:mm,若jobCount为2则用","分隔, // 若jobCount为0或大于2则不不显示 +
					 * " JOB_COUNT TEXT Integer," // 当日打卡次数 +
					 * " BEGIN_TIME TEXT varchar(50)," +
					 * " END_TIME TEXT varchar(50)," +
					 * " STICK_FLAG TEXT Integer," // 置顶 1 置顶 +
					 * " STICK_TIME TEXT timestamp," // 置顶时间 +
					 * " CTIME TEXT timestamp," // 创建时间 +
					 * " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功 +
					 * " STATUS TEXT Integer" + ");"; // 1-正常， 2-暂停
					 */
					c = mDb.query(TASK_LIST_EXCEL, null, null, null, null,
							null, null);

					c.moveToFirst();

					for (int i = 0; i < c.getCount(); i++) {

						ByDateBean bean = new ByDateBean();
						bean.setTaskId(c.getString(0));
						bean.setTitle(c.getString(1));
						bean.setGiveUpFlag(c.getString(2));
						bean.setTotalDateCount(c.getInt(3));
						bean.setColor(c.getInt(4));
						bean.setAllDate(c.getString(5));
						bean.setCurrentStreak(c.getInt(6));
						bean.setTime_limit_flag(c.getInt(7));
						bean.setCheckTime(c.getString(8));
						bean.setJobCount(c.getInt(9));
						bean.setBegin_time(c.getString(10));
						bean.setEnd_time(c.getString(11));
						bean.setStickFlag(c.getInt(12));
						bean.setStickTime(c.getLong(13));
						bean.setIsConnSuccess(c.getInt(15));
						bean.setStatus(c.getInt(16));
						taskList.add(bean);

						c.moveToNext();
					}

				} catch (SQLException e) {

					Log.e(CLASSTAG + " read table exception : ", e.getMessage());

				} finally {
					if (c != null && !c.isClosed()) {
						c.close();
						c = null;
					}
					mDb.close();
					mDb = null;
				}
			}
		}

		return taskList;
	}

	/***
	 * 添加打卡未成功的job
	 */
	public int insertFailJob(TaskItemBean taskItemBean) {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);

			if (mDb != null) {
				/** ��ʼ���� */
				mDb.beginTransaction();

				try {
					// JSONArray result = new JSONArray(list);

					// JSONObject item = result.getJSONObject(i);

					ContentValues insertValues = new ContentValues();
					TaskItemBean bean = taskItemBean;
					/**
					 * + " JOB_ID TEXT varchar(50) NOT NULL," +
					 * " GIVE_UP_FLAG TEXT Integer," // 是否放弃 +
					 * " TASK_ID TEXT Integer NOT NULL," +
					 * " QUANTITY TEXT varchar(2) NOT NULL," // 数值 +
					 * " CHECK_DATE TEXT varchar(50)," // 打卡时间 yyyy-MM-dd +
					 * " CTIME TEXT timestamp," // 创建时间 +
					 * " GIVE_UP_REASON TEXT varchar(50)," // 放弃理由 +
					 * " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功 +
					 * " PIC_COUNT TEXT Integer NOT NULL," //图片数量 +
					 * " CHECK_TIME TEXT timestamp," // 打卡时间 时间戳 +
					 * " ALL_PATH TEXT varchar(50)," // 打卡图片 +
					 * " SIGNATURE TEXT varchar(50)," // 打卡写的话 +
					 * " STATUS TEXT Integer" + ");";
					 * 
					 * */
					insertValues.put("JOB_ID", bean.getJobId());
					insertValues.put("GIVE_UP_FLAG", "0");
					insertValues.put("TASK_ID", bean.getTaskId());
					insertValues.put("QUANTITY", bean.getQuantity());
					insertValues.put("GIVE_UP_REASON", "无");
					insertValues.put("ISCONN_SUCCESS", 0);
					insertValues.put("PIC_COUNT", bean.getPicCount());
					insertValues.put("CHECK_TIME", bean.getCheck_time());
					insertValues.put("ALL_PATH", bean.getAllPath());
					insertValues.put("SIGNATURE", bean.getSignature());

					mDb.insertOrThrow(JOB_EXCEL, null, insertValues);
					Tools.getLog(Tools.i, "aaa", "JOB_ID------------------"
							+ bean.getJobId());
					mDb.setTransactionSuccessful();

				} catch (SQLException e) {
					errorCode--;
					Log.e("kkk",
							"insertProductsList SQLException:" + e.getMessage());

				} finally {
					mDb.endTransaction();
					mDb.close();
					// returnExcelCount(PRODUCT_LIST_EXCEL, null);
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;

	}

	/** 获得全部的打卡失败的数据 */
	public ArrayList<TaskItemBean> getFialJobList() {
		ArrayList<TaskItemBean> connFileJobList = new ArrayList<TaskItemBean>();
		synchronized (DBInstance.getInstance()) {

			Cursor c = null;
			SQLiteDatabase mDb = openDatabase(false);

			if (mDb != null) {
				try {
					/**
					 * JOB_ID TEXT varchar(50) NOT NULL," +
					 * " GIVE_UP_FLAG TEXT Integer," // 是否放弃 +
					 * " TASK_ID TEXT Integer NOT NULL," +
					 * " QUANTITY TEXT varchar(2) NOT NULL," // 数值 +
					 * " CHECK_DATE TEXT varchar(50)," // 打卡时间 yyyy-MM-dd +
					 * " CTIME TEXT timestamp," // 创建时间 +
					 * " GIVE_UP_REASON TEXT varchar(50)," // 放弃理由 +
					 * " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功 +
					 * " PIC_COUNT TEXT Integer NOT NULL," // 图片数量 +
					 * " CHECK_TIME TEXT timestamp," // 打卡时间 时间戳 +
					 * " ALL_PATH TEXT varchar(50)," // 打卡图片 +
					 * " SIGNATURE TEXT varchar(50)," // 打卡写的话 +
					 * " STATUS TEXT Integer" + ");";
					 * */
					c = mDb.query(JOB_EXCEL, null, null, null, null, null, null);

					c.moveToFirst();

					for (int i = 0; i < c.getCount(); i++) {

						TaskItemBean bean = new TaskItemBean();
						bean.setJobId(c.getString(0));
						bean.setGive_up_flag(c.getString(1));
						bean.setTaskId(c.getString(2));
						bean.setQuantity(c.getString(3));
						bean.setDate(c.getString(4));
						bean.setTime(c.getString(5));
						bean.setGive_up_reason(c.getString(6));
						bean.setIsConnSucces(c.getInt(7));
						bean.setPicCount(c.getString(8));
						bean.setCheck_time(c.getLong(9));
						bean.setAllPath(c.getString(10));
						bean.setSignature(c.getString(11));
						connFileJobList.add(bean);
						c.moveToNext();
					}

				} catch (SQLException e) {

					Log.e(CLASSTAG + " read table exception : ", e.getMessage());

				} finally {
					if (c != null && !c.isClosed()) {
						c.close();
						c = null;
					}
					mDb.close();
					mDb = null;
				}
			}
		}

		return connFileJobList;
	}

	/** 获得全部的alarm提醒的数据 */
	public TaskItemBean getFialJobBean(String jobId) {
		TaskItemBean bean = new TaskItemBean();
		synchronized (DBInstance.getInstance()) {

			Cursor c = null;
			SQLiteDatabase mDb = openDatabase(false);
			
			if (mDb != null) {
				try {
					/**
					 * JOB_ID TEXT varchar(50) NOT NULL," +
					 * " GIVE_UP_FLAG TEXT Integer," // 是否放弃 +
					 * " TASK_ID TEXT Integer NOT NULL," +
					 * " QUANTITY TEXT varchar(2) NOT NULL," // 数值 +
					 * " CHECK_DATE TEXT varchar(50)," // 打卡时间 yyyy-MM-dd +
					 * " CTIME TEXT timestamp," // 创建时间 +
					 * " GIVE_UP_REASON TEXT varchar(50)," // 放弃理由 +
					 * " ISCONN_SUCCESS TEXT Integer," // 是否聯網成功 +
					 * " PIC_COUNT TEXT Integer NOT NULL," // 图片数量 +
					 * " CHECK_TIME TEXT timestamp," // 打卡时间 时间戳 +
					 * " ALL_PATH TEXT varchar(50)," // 打卡图片 +
					 * " SIGNATURE TEXT varchar(50)," // 打卡写的话 +
					 * " STATUS TEXT Integer" + ");";
					 * */
					c = mDb.query(JOB_EXCEL, null, "JOB_ID='"+jobId+"'", null, null, null, null);

					c.moveToFirst();
					bean.setJobId(c.getString(0));
					bean.setGive_up_flag(c.getString(1));
					bean.setTaskId(c.getString(2));
					bean.setQuantity(c.getString(3));
					bean.setDate(c.getString(4));
					bean.setTime(c.getString(5));
					bean.setGive_up_reason(c.getString(6));
					bean.setIsConnSucces(c.getInt(7));
					bean.setPicCount(c.getString(8));
					bean.setCheck_time(c.getLong(9));
					bean.setAllPath(c.getString(10));
					bean.setSignature(c.getString(11));
					c.moveToNext();

				} catch (SQLException e) {

					Log.e(CLASSTAG + " read table exception : ", e.getMessage());

				} finally {
					if (c != null && !c.isClosed()) {
						c.close();
						c = null;
					}
					mDb.close();
					mDb = null;
				}
			}
		}

		return bean;
	}
	
	public int deleteFialJobBean() {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);

			if (mDb != null) {
				mDb.beginTransaction();

				// mDb.delete(SEARCH_HISTORY_LIST_EXCEL, "", null);

				try {
					mDb.delete(JOB_EXCEL, "", null);
					mDb.setTransactionSuccessful();

				} catch (SQLException e) {
					errorCode--;
					Log.e("kkk",
							"JOB_EXCEL SQLException:" + e.getMessage());

				} finally {
					mDb.endTransaction();
					mDb.close();
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;

	}

	// public ArrayList<GroupListBean> getNameList(ArrayList<GroupListBean>
	// list) {
	//
	// GroupListBean bean = null;
	// synchronized (DBInstance.getInstance()) {
	//
	// Cursor c = null;
	// list.clear();
	//
	// SQLiteDatabase mDb = openDatabase(false);
	//
	// if (mDb != null) {
	// try {
	//
	// c = mDb.query(TASK_LIST_EXCEL, null, null, null,
	// null, null, "GROUP_TIME DESC");
	//
	// c.moveToFirst();
	//
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// bean = new GroupListBean();
	// bean.setGroupID(c.getString(0));
	// bean.setGroupName(c.getString(1));
	// bean.setGroupPeopleCount(c.getString(2));
	// bean.setGroupTime(c.getLong(3));
	// bean.setDistance(c.getString(4));
	// // if (Contanst.STORE_DEBUG) {
	// // Tools.getLog(Tools.i, "++++++++++++++++++",
	// // c.getString(0) + "+"
	// // + c.getString(1) + "+"
	// // + c.getString(2));
	// // }
	// list.add(bean);
	// c.moveToNext();
	//
	// }
	//
	// } catch (SQLException e) {
	//
	// Log.e(CLASSTAG + " read table exception : ", e.getMessage());
	//
	// } finally {
	// if (c != null && !c.isClosed()) {
	// c.close();
	// c = null;
	// }
	// mDb.close();
	// mDb = null;
	// }
	// }
	// }
	// return list;
	//
	// }

	/**
	 * 获取首页历史list
	 * 
	 * @param list
	 * @return
	 */
	// public ArrayList<GroupListBean>
	// getMainHistoryList(ArrayList<GroupListBean> list) {
	//
	// GroupListBean bean = null;
	// synchronized (DBInstance.getInstance()) {
	//
	// Cursor c = null;
	// list.clear();
	//
	// SQLiteDatabase mDb = openDatabase(false);
	//
	// if (mDb != null) {
	// try {
	//
	// c = mDb.query(JOB_EXCEL, null, null, null,
	// null, null, "GROUP_TIME DESC");
	//
	// c.moveToFirst();
	//
	// for (int i = 0; i < c.getCount(); i++) {
	// // * @"GROUP_ID":"1",
	// // * @"GROUP_NAME":"jerry",
	// // * @"GROUP_ONLINE_NUMBER":"1",
	// // * @"GROUP_AT_COUNT":"1",
	// // * @"GROUP_TIME":"1413192168142",
	// // * @"GROUP_DISTANCE":"0.06km",;";
	// bean = new GroupListBean();
	// bean.setGroupID(c.getString(0));
	// bean.setGroupName(c.getString(1));
	// bean.setAtNum(c.getString(2));
	// bean.setGroupPeopleCount(c.getString(3));
	// bean.setGroupTime(c.getLong(4));
	// bean.setDistance(c.getString(5));
	// list.add(bean);
	// c.moveToNext();
	//
	// }
	//
	// } catch (SQLException e) {
	//
	// Log.e(CLASSTAG + " read table exception : ", e.getMessage());
	//
	// } finally {
	// if (c != null && !c.isClosed()) {
	// c.close();
	// c = null;
	// }
	// mDb.close();
	// mDb = null;
	// }
	// }
	// }
	// return list;
	//
	// }

	/***
	 * 添加连续打卡列表
	 */
	public int insertDailyPerformanceList(ArrayList<PerformanceBean> list) {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);

			if (mDb != null) {
				mDb.beginTransaction();

				mDb.delete(DAILY_PERFORMANCE_EXCEL, "", null);

				try {
					// JSONArray result = new JSONArray(list);

					for (int i = 0; i < list.size(); i++) {
						// JSONObject item = result.getJSONObject(i);

						ContentValues insertValues = new ContentValues();
						PerformanceBean bean = list.get(i);
						/**
						 * + " PERFORMANCE Integer NOT NULL," +
						 * " RECORD_DATE varchar(50)" + ");";
						 * 
						 * */
						insertValues.put("PERFORMANCE", bean.getPerformance());
						insertValues.put("RECORD_DATE", bean.getRecord_date());

						mDb.insertOrThrow(DAILY_PERFORMANCE_EXCEL, null,
								insertValues);
						Tools.getLog(Tools.i, "aaa", "insert------------------"
								+ bean.getRecord_date());
					}

					mDb.setTransactionSuccessful();

				} catch (SQLException e) {
					errorCode--;
					Log.e("kkk",
							"insertProductsList SQLException:" + e.getMessage());

				} finally {
					mDb.endTransaction();
					mDb.close();
					// returnExcelCount(PRODUCT_LIST_EXCEL, null);
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;

	}

	/**
	 * 获取历史记录的GroupId集合
	 * 
	 * @return
	 */
	public ArrayList<String> getGroupIdList() {

		ArrayList<String> groupIdList = new ArrayList<String>();
		synchronized (DBInstance.getInstance()) {

			Cursor c = null;
			SQLiteDatabase mDb = openDatabase(false);

			if (mDb != null) {
				try {

					c = mDb.query(ALARM_EXCEL, null, null, null, null, null,
							"GROUP_TIME DESC");

					c.moveToFirst();

					for (int i = 0; i < c.getCount(); i++) {

						// bean = new GroupListBean();
						// bean.setGroupID(c.getString(0));
						// bean.setGroupName(c.getString(1));
						// bean.setGroupPeopleCount(c.getString(2));
						// bean.setGroupTime(c.getLong(3));
						// bean.setDistance(c.getString(4));
						groupIdList.add(c.getString(0));
						c.moveToNext();
					}

				} catch (SQLException e) {

					Log.e(CLASSTAG + " read table exception : ", e.getMessage());

				} finally {
					if (c != null && !c.isClosed()) {
						c.close();
						c = null;
					}
					mDb.close();
					mDb = null;
				}
			}
		}
		return groupIdList;

	}

	/***
	 * 删除搜索历史
	 */
	public int deleteSearchList() {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);

			if (mDb != null) {
				mDb.beginTransaction();

				// mDb.delete(SEARCH_HISTORY_LIST_EXCEL, "", null);

				try {
					mDb.delete(ALARM_EXCEL, "", null);
					mDb.setTransactionSuccessful();

				} catch (SQLException e) {
					errorCode--;
					Log.e("kkk",
							"deleteSearchList SQLException:" + e.getMessage());

				} finally {
					mDb.endTransaction();
					mDb.close();
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;
	}

	/** 添加提醒的数据 */
	public int insertAlarmList(ArrayList<MoreAlertTimeBean> list) {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);
			/**
			 * "(alarm_id varchar(50) NOT NULL,"+ //闹表Id, 客户端生成(必须)
			 * "taskId varchar(50) NOT NULL," +//对应的taskId, (必须)
			 * "days varchar(50)" +// 需要提醒的星期, 以","分隔, 例如"1,2,4,6,7"
			 * "time varchar(50)," + //提醒时间, 格式为HH:MM "ringtone varchar(50),"
			 * +// 提醒铃声, 根据平台对应不同铃声 "vibrationFlag varchar(50)," + //是否震动, 1为是
			 * 0为不是(必须) "remark varchar(50)," +// 备注 "status varchar(50),"
			 * +//闹表状态, 1为开启, 2为关闭(必须) "pickervisity varchar(50));";
			 * //自己加的Timerpicker是否显示 1 为不显示 2 为显示
			 */
			if (mDb != null) {
				mDb.beginTransaction();
				Tools.getLog(Tools.d, "taskid:==", list.get(0).getTaskId());
				mDb.delete(ALARM_EXCEL, "taskId=?", new String[] { list.get(0)
						.getTaskId() });
				try {
					for (int i = 0; i < list.size(); i++) {
						ContentValues insertValues = new ContentValues();
						MoreAlertTimeBean bean = list.get(i);

						insertValues.put("alarm_id", bean.getId());
						insertValues.put("taskId", bean.getTaskId());
						insertValues.put("days", bean.getDays());
						insertValues.put("time", bean.getTime());
						insertValues.put("ringtone", bean.getRingtone());
						insertValues.put("vibrationFlag",
								bean.getVibrationFlag());
						insertValues.put("remark", bean.getRemark());
						insertValues.put("status", bean.getStatus());
						insertValues
								.put("pickervisity", bean.getPickervisity());
						insertValues.put("title", bean.getTitle());

						mDb.insertOrThrow(ALARM_EXCEL, null, insertValues);
						Tools.getLog(Tools.i, "aaa",
								"addAlarmList------------------" + i);
					}
					mDb.setTransactionSuccessful();
				} catch (Exception e) {
					// TODO: handle exception
					errorCode--;
					Log.e("kkk", "addAlarmList:" + e.getMessage());
				} finally {
					mDb.endTransaction();
					mDb.close();
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;

	}

	/** 获得alarm提醒的数据 */
	public ArrayList<MoreAlertTimeBean> getAlarmList(String taskId) {
		ArrayList<MoreAlertTimeBean> alarmList = new ArrayList<MoreAlertTimeBean>();
		synchronized (DBInstance.getInstance()) {

			Cursor c = null;
			SQLiteDatabase mDb = openDatabase(false);

			if (mDb != null) {
				try {
					/**
					 * "(alarm_id varchar(50) NOT NULL,"+ //闹表Id, 客户端生成(必须)
					 * "taskId varchar(50) NOT NULL," +//对应的taskId, (必须)
					 * "days varchar(50)" +// 需要提醒的星期, 以","分隔, 例如"1,2,4,6,7"
					 * "time varchar(50)," + //提醒时间, 格式为HH:MM
					 * "ringtone varchar(50)," +// 提醒铃声, 根据平台对应不同铃声
					 * "vibrationFlag varchar(50)," + //是否震动, 1为是 0为不是(必须)
					 * "remark varchar(50)," +// 备注 "status varchar(50),"
					 * +//闹表状态, 1为开启, 2为关闭(必须) "pickervisity varchar(50));";
					 * //自己加的Timerpicker是否显示 1 为不显示 2 为显示
					 */
					c = mDb.query(ALARM_EXCEL, null, "taskId=?",
							new String[] { taskId }, null, null, null);

					c.moveToFirst();

					for (int i = 0; i < c.getCount(); i++) {

						MoreAlertTimeBean bean = new MoreAlertTimeBean();
						bean.setU_id(c.getInt(0));
						bean.setId(c.getString(1));
						bean.setTaskId(c.getString(2));
						bean.setDays(c.getString(3));
						bean.setTime(c.getString(4));
						bean.setRingtone(c.getString(5));
						bean.setVibrationFlag(c.getString(6));
						bean.setRemark(c.getString(7));
						bean.setStatus(c.getString(8));
						bean.setPickervisity(c.getString(9));
						bean.setTitle(c.getString(10));
						alarmList.add(bean);
						c.moveToNext();
					}

				} catch (SQLException e) {

					Log.e(CLASSTAG + " read table exception : ", e.getMessage());

				} finally {
					if (c != null && !c.isClosed()) {
						c.close();
						c = null;
					}
					mDb.close();
					mDb = null;
				}
			}
		}

		return alarmList;
	}

	/** 获得全部的alarm提醒的数据 */
	public ArrayList<MoreAlertTimeBean> getAllAlarmList() {
		ArrayList<MoreAlertTimeBean> alarmList = new ArrayList<MoreAlertTimeBean>();
		synchronized (DBInstance.getInstance()) {

			Cursor c = null;
			SQLiteDatabase mDb = openDatabase(false);

			if (mDb != null) {
				try {
					/**
					 * "(alarm_id varchar(50) NOT NULL,"+ //闹表Id, 客户端生成(必须)
					 * "taskId varchar(50) NOT NULL," +//对应的taskId, (必须)
					 * "days varchar(50)" +// 需要提醒的星期, 以","分隔, 例如"1,2,4,6,7"
					 * "time varchar(50)," + //提醒时间, 格式为HH:MM
					 * "ringtone varchar(50)," +// 提醒铃声, 根据平台对应不同铃声
					 * "vibrationFlag varchar(50)," + //是否震动, 1为是 0为不是(必须)
					 * "remark varchar(50)," +// 备注 "status varchar(50),"
					 * +//闹表状态, 1为开启, 2为关闭(必须) "pickervisity varchar(50));";
					 * //自己加的Timerpicker是否显示 1 为不显示 2 为显示
					 */
					c = mDb.query(ALARM_EXCEL, null, null, null, null, null,
							null);

					c.moveToFirst();

					for (int i = 0; i < c.getCount(); i++) {

						MoreAlertTimeBean bean = new MoreAlertTimeBean();
						bean.setU_id(c.getInt(0));
						bean.setId(c.getString(1));
						bean.setTaskId(c.getString(2));
						bean.setDays(c.getString(3));
						bean.setTime(c.getString(4));
						bean.setRingtone(c.getString(5));
						bean.setVibrationFlag(c.getString(6));
						bean.setRemark(c.getString(7));
						bean.setStatus(c.getString(8));
						bean.setPickervisity(c.getString(9));
						bean.setTitle(c.getString(10));
						alarmList.add(bean);
						c.moveToNext();
					}

				} catch (SQLException e) {

					Log.e(CLASSTAG + " read table exception : ", e.getMessage());

				} finally {
					if (c != null && !c.isClosed()) {
						c.close();
						c = null;
					}
					mDb.close();
					mDb = null;
				}
			}
		}

		return alarmList;
	}

	/** 修改提醒 */
	public int updateAlarmList(ArrayList<MoreAlertTimeBean> list) {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);
			/**
			 * "(alarm_id varchar(50) NOT NULL,"+ //闹表Id, 客户端生成(必须)
			 * "taskId varchar(50) NOT NULL," +//对应的taskId, (必须)
			 * "days varchar(50)" +// 需要提醒的星期, 以","分隔, 例如"1,2,4,6,7"
			 * "time varchar(50)," + //提醒时间, 格式为HH:MM "ringtone varchar(50),"
			 * +// 提醒铃声, 根据平台对应不同铃声 "vibrationFlag varchar(50)," + //是否震动, 1为是
			 * 0为不是(必须) "remark varchar(50)," +// 备注 "status varchar(50),"
			 * +//闹表状态, 1为开启, 2为关闭(必须) "pickervisity varchar(50));";
			 * //自己加的Timerpicker是否显示 1 为不显示 2 为显示
			 */
			if (mDb != null) {
				mDb.beginTransaction();

				try {

					for (int i = 0; i < list.size(); i++) {
						ContentValues insertValues = new ContentValues();
						MoreAlertTimeBean bean = list.get(i);

						insertValues.put("alarm_id", bean.getId());
						insertValues.put("taskId", bean.getTaskId());
						insertValues.put("days", bean.getDays());
						insertValues.put("time", bean.getTime());
						insertValues.put("ringtone", bean.getRingtone());
						insertValues.put("vibrationFlag",
								bean.getVibrationFlag());
						insertValues.put("remark", bean.getRemark());
						insertValues.put("status", bean.getStatus());
						insertValues
								.put("pickervisity", bean.getPickervisity());

						mDb.update(ALARM_EXCEL, insertValues, "alarm_id=?",
								new String[] { bean.getId() });
					}
					mDb.setTransactionSuccessful();
				} catch (Exception e) {
					// TODO: handle exception
					errorCode--;
					Log.e("kkk", "updateAlarmList:" + e.getMessage());
				} finally {
					mDb.endTransaction();
					mDb.close();
				}

			} else {
				errorCode--;
			}
		}
		return errorCode;
	}

	/** 删除提醒的某一条 */
	public int deleteAlarm() {
		int errorCode = 0;
		synchronized (DBInstance.getInstance()) {
			SQLiteDatabase mDb = openDatabase(true);
			if (mDb != null) {
				mDb.beginTransaction();

				try {
					mDb.delete(ALARM_EXCEL, null, null);
					mDb.setTransactionSuccessful();
					Tools.getLog(Tools.d, "aaa", "删除提醒数据库");
				} catch (Exception e) {
					// TODO: handle exception
					errorCode--;
					Log.e("kkk", "deleteAlarmList:" + e.getMessage());
				} finally {
					mDb.endTransaction();
					mDb.close();
				}
			} else {
				errorCode--;
			}
		}
		return errorCode;
	}

	// ///////////////////////////////////////////////////////////////////
	// DB MANAGER
	// ////////////////////////////////////////////////////////////////////
	private SQLiteDatabase openDatabase(boolean writable) {

		SQLiteDatabase db = null;

		if (mDbOpenHelper != null) {
			mDbOpenHelper.close();
		}

		try {
			if (writable) {
				db = mDbOpenHelper.getWritableDatabase();
			} else {
				db = mDbOpenHelper.getReadableDatabase();
			}
		} catch (SQLiteFullException e) {
			e.printStackTrace();
		} catch (SQLiteDiskIOException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return db;
	}

	public void createTable() {

	}

	private class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			super(context, _DB_NAME, null, _DB_VERSION);
			Tools.getLog(Tools.i, "kkk", "OpenHelper");
		}

		public void onCreate(SQLiteDatabase db) {

			db.beginTransaction();

			try {
				Tools.getLog(Tools.i, "aaa", "showTable");
				db.execSQL(CREAT_TB_TASK_EXCEL);
				db.execSQL(CREAT_TB_JOB_EXCEL);
				db.execSQL(CREAT_TB_DAILY_PERFORMANCE_EXCEL);
				db.execSQL(CREATE_TB_ALARM_EXCEL);
				Tools.getLog(Tools.i, "aaa", "TableSuccess");
				db.setTransactionSuccessful();

			} catch (Exception ex) {

				ex.printStackTrace();

			} finally {

				if (db != null) {

					db.endTransaction();
				}
			}
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.beginTransaction();

			try {

				db.execSQL("DROP TABLE IF EXISTS " + CREAT_TB_TASK_EXCEL);
				db.execSQL("DROP TABLE IF EXISTS " + CREAT_TB_JOB_EXCEL);
				db.execSQL("DROP TABLE IF EXISTS "
						+ CREAT_TB_DAILY_PERFORMANCE_EXCEL);
				db.execSQL("DROP TABLE IF EXISTS " + ALARM_EXCEL);
				db.setTransactionSuccessful();

			} catch (Exception ex) {

				ex.printStackTrace();

			} finally {

				if (db != null) {
					db.endTransaction();
					onCreate(db);
				}
			}
		}
	}
}
