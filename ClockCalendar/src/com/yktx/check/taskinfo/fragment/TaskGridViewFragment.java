package com.yktx.check.taskinfo.fragment;

import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.R;
import com.yktx.check.adapter.TaskGridViewAdapter;
import com.yktx.check.bean.CustomDate;
import com.yktx.check.bean.GetByTaskIdCameraBean;
import com.yktx.check.bean.TaskIdCameraBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.TaskInfoDialog;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.BaseFragment;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.DateUtil;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

@SuppressLint("ValidFragment")
public class TaskGridViewFragment extends BaseFragment implements
		ServiceListener {

	TaskGridViewAdapter adapter;
	GridView gridView;
	LinkedHashMap<String, GetByTaskIdCameraBean> curMap = new LinkedHashMap<String, GetByTaskIdCameraBean>(
			10);
	String taskID;
	/** 最后的照片key */
	String lastKey;

	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zw_touxiang)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	/** 当前用户的task 创建时间， 日历从次日开始显示 */
	CustomDate createDate;
	/**
	 * 点击进来对应的userID
	 */
	String getUserID;
	String userHeaderUrl;
	String getUserName, taskDate, mCity;
	int userHeaderSource;
	int totalDateCount, imageCount;

	long createTime;
	String buildingId;
	int manCountToday;

	TaskInfoDialog taskInfoDialog;

	int createTimeYear, createTimeMonth, checkTimeYear, checkTimeMonth;
	boolean isClockSuccess = false;

	/**
	 * 是否最早日期
	 */
	boolean isEarliest;

	RelativeLayout layout;
	Activity mContext;
	@SuppressLint("NewApi")
	public TaskGridViewFragment(Activity context,String userId,long createTime,String taskID) {
		super();
		this.mContext = context;
		this.createTime = createTime;
		getUserID = userId;
		this.taskID = taskID;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = (RelativeLayout) inflater.inflate(
				R.layout.task_gridview_activity, null);
		gridView = (GridView) layout
				.findViewById(R.id.pull_refresh_grid);
		initView();
		return layout;
	}

	protected void initView() {
		// TODO Auto-generated method stub

		String createTimeStr[] = TimeUtil.getyyyyMM(createTime).split(":");
		createTimeYear = Integer.parseInt(createTimeStr[0]);
		createTimeMonth = Integer.parseInt(createTimeStr[1]);

		String checkTimeStr[] = TimeUtil.getyyyyMM(System.currentTimeMillis())
				.split(":");
		checkTimeYear = Integer.parseInt(checkTimeStr[0]);
		checkTimeMonth = Integer.parseInt(checkTimeStr[1]);

		Conn(checkTimeYear+":"+checkTimeMonth);
		
	}

	boolean isNewUser;

	public void Conn(String date) {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(getUserID);
		sb.append("&taskId=");
		sb.append(taskID);
//		sb.append("&date=");
//		sb.append(date);
		Service.getService(Contanst.HTTP_TASK_INMONTH, null, sb.toString(),
				TaskGridViewFragment.this).addList(null).request(UrlParams.GET);
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:

				switch (msg.arg1) {

				case Contanst.TASK_INMONTH:
					// createDate = new CustomDate(2014,);
					TaskIdCameraBean bean = (TaskIdCameraBean) msg.obj;
					// manCountToday = bean.getManCountToday();
					// buildingId = bean.getBuildingId();
					lastKey = bean.getLastKey();
					// long createTime = bean.getTaskCTime();
					String date = null;
//					if (checkTimeYear == createTimeYear
//							&& checkTimeMonth <= createTimeMonth) {
						// 已经是最早的了。
						date = TimeUtil.getYYMMDD(createTime);
//					} else {
//						if (checkTimeMonth < 10) {
//							date = checkTimeYear + "-0" + checkTimeMonth
//									+ "-01";
//						} else {
//							date = checkTimeYear + "-" + checkTimeMonth + "-01";
//						}
//					}

					Tools.getLog(Tools.i, "aaa",
							"date ============= " + date.toString());
					long lastDateTime = DateUtil.getDate(date, 7);
					String lastWeekDate = TimeUtil.getYYMMDD(lastDateTime);

					String array[] = lastWeekDate.split("-");
					
					CustomDate createDate = new CustomDate(
							Integer.parseInt(array[0]),
							Integer.parseInt(array[1]),
							Integer.parseInt(array[2]));

					CustomDate lastSunday = DateUtil.getNextSunday(createDate);
					Tools.getLog(Tools.i, "aaa", "arrays ============= "
							+ lastSunday.toString());
					adapter = new TaskGridViewAdapter(mContext, getUserID, taskID, getUserID);
					gridView.setAdapter(adapter);
					adapter.setCreateDate(lastSunday);
					LinkedHashMap<String, GetByTaskIdCameraBean> cMap = bean
							.getMapData();
					cMap.putAll(curMap);
					curMap = cMap;
					adapter.setList(curMap);
					Tools.getLog(Tools.i, "aaa", "arrays ============= "
							+ curMap.size());
				
					adapter.notifyDataSetChanged();
					break;
				}

				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Tools.getLog(Tools.d, "aaa", message);
				switch (msg.arg1) {

				case Contanst.TASK_INMONTH:
					break;
				}
				break;
			}
		}
	};

}
