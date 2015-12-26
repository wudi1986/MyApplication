package com.yktx.view;

/**
 * 
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yktx.check.BaseActivity;
import com.yktx.check.R;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.CalendarViewBuilder;
import com.yktx.check.util.Contanst;
import com.yktx.check.widget.CalendarView;
import com.yktx.check.widget.CalendarView.CallBack;
import com.yktx.check.widget.CalendarViewPagerLisenter;
import com.yktx.check.widget.CustomViewPagerAdapter;

/**  
 * @author 作者 :  wudi
 * @version 创建时间：2014年8月13日 下午4:59:34  
 * 类说明  */
/**
 * @author Administrator
 * 
 */
public class TaskCaledarView extends LinearLayout implements ServiceListener {

	Activity mContext;
	// GoodsListView goodsListView;
	/**
	 * @param context
	 */
	LinearLayout homeSale;
	/**
	 * 详情页面单个task打卡日期
	 */
	public static String [] infoDateArray;

	String userid;
	String userName = null;
	public ImageView chat_head, chat_photo, zanbutton, gatback;
	public TextView chat_name_item, chat_distance_item, chat_time_item;

	private GridView gat_gridView;

	public ViewPager viewPager;
	CustomViewPagerAdapter<CalendarView> viewPagerAdapter;
	private CalendarView[] views;
	private CalendarViewBuilder builder = new CalendarViewBuilder();

	public TaskCaledarView(Activity mContext, CallBack callback, String taskID) {
		super(mContext);
		this.mContext = mContext;
		this.taskID = taskID;
		conn();
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		homeSale = (LinearLayout) mInflater.inflate(R.layout.calendar_view,
				null);
		viewPager = (ViewPager) homeSale.findViewById(R.id.viewpager);
		views = builder.createMassCalendarViews(mContext, 3, callback);

		viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		viewPager.getLayoutParams().height = BaseActivity.ScreenWidth * 6 / 7;
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(498);
		viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(
				viewPagerAdapter));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.setMargins((int) (12 * BaseActivity.DENSITY), 0,
				(int) (12 * BaseActivity.DENSITY), 0);
		homeSale.setLayoutParams(layoutParams);
		// getBestView(mContext, chatID);
	}

	String taskID;

	public LinearLayout getBestView() {

		return homeSale;

	}

	private void conn() {
		// TODO Auto-generated method stub

		Service.getService(Contanst.HTTP_GET_ALL_CHECKDATE, null, taskID,
				TaskCaledarView.this).addList(null).request(UrlParams.GET);
	}

	// private void init(final Activity mContext, final ChatListBean chatBean2)
	// {
	// initPhotoZan(chatBean, holdView);
	// }

	public interface OnClickBack {
		public void getClick();
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
				case Contanst.GET_ALL_CHECKDATE:
					infoDateArray = (String[]) msg.obj;
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:// 通过Id获得详细信息的
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.GET_ALL_CHECKDATE:
					
					break;
				}
				break;
			}
		}
	};
}
