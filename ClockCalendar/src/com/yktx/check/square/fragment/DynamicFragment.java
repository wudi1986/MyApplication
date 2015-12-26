package com.yktx.check.square.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.clock.service.MyReceiver;
import com.clock.service.MyReceiver.onRefreshDynamic;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockTaskDynamicActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.DynamicFragmentListViewAdapter;
import com.yktx.check.adapter.DynamicFragmentListViewAdapter.onMyItemClick;
import com.yktx.check.bean.HotTestBean;
import com.yktx.check.bean.MsgToUserBean;
import com.yktx.check.bean.MsgToUserListBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;
/**
 * 消息（个人动态）
 * */
@SuppressLint("NewApi")
public class DynamicFragment  extends BaseFragment implements ServiceListener{


	/** 第几页 */
	public int currentPage;
	/** 总数 */
	public int totalCount;
	/** 总页数 */	
	public int totalPage;
	/** 一页多少条数据 */
	public int pageLimit = 10;
	/** 数据集合 */
	public String listData;
	/** 当前时间 */
	public long reflashTime;

	String longitude = "0.0", latitude = "0.0";

	XListView xListView;
	boolean isConn, isReflush;
	String userID;
	long send_time;
	ArrayList<MsgToUserListBean> msgToUserListBeans = new ArrayList<MsgToUserListBean>(10);
	DynamicFragmentListViewAdapter adapter;
	int recommendListSize = 0;
	HotTestBean toChatBean;
	RelativeLayout loadingView,main_upLayout;
	ImageView imageListNull;
	private SharedPreferences settings;
	private Editor mEditor;
	private ProgressBar hot_or_near_ProgressBar;
	
	private Activity mContext;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = DynamicFragment.this.getActivity();
		settings = mContext.getSharedPreferences("clock", mContext.MODE_PRIVATE);
		mEditor = settings.edit();
		userID = settings.getString("userid", null);
		longitude = settings.getString("longitude", null);
		latitude = settings.getString("latitude", null);
		send_time = 0;
		if (!isConn) {
			isConn = true;
			isReflush = true;
			getMsgConn(1);
		}
		View view = inflater.inflate(R.layout.hot_or_near_activity, container,
				false);

		hot_or_near_ProgressBar = (ProgressBar) view.findViewById(R.id.hot_or_near_ProgressBar);
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		imageListNull = (ImageView) view.findViewById(R.id.imageListNull);
		imageListNull.setImageResource(R.drawable.wudongtai);
		main_upLayout = (RelativeLayout) view.findViewById(R.id.main_upLayout);
		main_upLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		imageListNull.setVisibility(View.GONE);
		xListView = (XListView) view.findViewById(R.id.xListView);
		xListView.setXListViewListener(listener);
		adapter = new DynamicFragmentListViewAdapter(mContext,userID);
		adapter.setMyItemClick(myItemClick);
		xListView.setAdapter(adapter);
		//		xListView.setIXListViewOnGoHome(iXListViewOnGoHome);
		xListView.setIsShow(true);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		//		xListView.setFooterBackground(getResources().getColor(R.color.meibao_color_15));
		xListView.setOnItemClickListener(null);
		MyReceiver.setDynamic(thisdynamic);
		return view;
	}
	onMyItemClick myItemClick = new onMyItemClick() {

		@Override
		public void itemClick(int position) {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.d, "aaa", "消息的点击。。。。。=====");
			MsgToUserListBean msgToUserListBean = msgToUserListBeans.get(position);
			String type = msgToUserListBean.getType();
			if(type.equals("3")||type.equals("5")){//点赞详情


				Intent in = new Intent(mContext, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 0);//默认选中   0打气，1评论
				in.putExtra("jobid", msgToUserListBean.getJobId());
				in.putExtra("taskId", msgToUserListBean.getTaskId());
				 in.putExtra("createUserID", msgToUserListBean.getUserId());
				mContext.startActivity(in);
			}else if(type.equals("6")){//点击关注     我关注了。。。
				Intent in = new Intent(mContext,
						ClockMyActivity.class);
				mContext.startActivity(in);
			}else if(type.equals("7")){//点击关注      别人关注了。。。
				Intent in = new Intent(mContext, ClockOtherUserActivity.class);
				in.putExtra("userid", msgToUserListBean.getUserId());
				mContext.startActivity(in);
			}else if(type.equals("12")){//今日精品
				Intent in = new Intent(mContext, GroupMainFragmentActivity.class);
//				in.putExtra("isDynamic", true);
				mContext.startActivity(in);
			}else if(type.equals("14")){//今日精品
				mContext.finish();
			}else if(type.equals("15")){//今日精品
				Intent in = new Intent(mContext, TaskInfoActivity.class);
				in.putExtra("taskid", msgToUserListBean.getTaskId());
				in.putExtra("userid", msgToUserListBean.getUserId());
				mContext.startActivity(in);
			}else if(type.equals("16")){//今日精品
				
			}else{
				Intent in = new Intent(mContext, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 1);//默认选中   0打气，1评论
				in.putExtra("jobid", msgToUserListBean.getJobId());
				in.putExtra("createUserID", msgToUserListBean.getUserId());
				in.putExtra("taskId", msgToUserListBean.getTaskId());
				mContext.startActivity(in);
			}
			//				else{
			//				Intent in = new Intent(mContext,
			//						ClockVoteActivity.class);
			//				in.putExtra("jobid", msgToUserListBean.getJobId());
			//				in.putExtra("taskId", msgToUserListBean.getTaskId());
			//				mContext.startActivity(in);
			//			}
		}
	}; 
	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			getMsgConn(1);
			isReflush = true;
			isConn = true;

		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			isReflush = false;

			if (currentPage * 10 >= totalCount) {
				// Toast.makeText(NewFragment.this.mContext, "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			getMsgConn(currentPage + 1);
			isConn = true;
		}
	};
	private	 void getMsgConn(int currentPage){
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		sb.append("&currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);

		Service.getService(Contanst.HTTP_GET_MSGTOUSER, null, sb.toString(),
				DynamicFragment.this).addList(null).request(UrlParams.GET);
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "getJOSNdataSuccessgetJOSNdataSuccess");
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
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				// 刷新附近列表
				if (isReflush) {
					currentPage = 1;
					msgToUserListBeans.clear();
					MsgToUserBean bean = (MsgToUserBean) msg.obj;
					currentPage = bean.getCurrentPage();
					totalCount = bean.getTotalCount();
					totalPage = bean.getTotalPage();
					msgToUserListBeans = bean.getListData();

					if (msgToUserListBeans.size() == 0) {
						//						imageListNull.setVisibility(View.VISIBLE);

						hot_or_near_ProgressBar.setVisibility(View.GONE);
						onLoad();
						xListView.setPullLoadEnable(false);
						return;
					}
					//					imageListNull.setVisibility(View.GONE);
					//					send_time = msgToUserListBeans.get(msgToUserListBeans.size()-1).getCheck_time();
					adapter.setList(msgToUserListBeans);
					//					adapter.setDistance(latitude, longitude);
					xListView.setPullLoadEnable(true);
					adapter.notifyDataSetChanged();

				} else {
					currentPage++;
					MsgToUserBean bean = (MsgToUserBean) msg.obj;
					msgToUserListBeans.addAll(bean.getListData());
					adapter.setList(msgToUserListBeans);
					//					send_time = msgToUserListBeans.get(newList.size()-1).getCheck_time ();
					//					adapter.setDistance(latitude, longitude);
					adapter.notifyDataSetChanged();
				}

				onLoad();
				if (totalCount <= 10 || currentPage * 10 >= totalCount) {
					xListView.setIsShow(false);
				} else {
					xListView.setIsShow(true);
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Tools.getLog(Tools.i, "aaa", "message = " + message);
				break;
			}
		}
	};
	private void onLoad() {
		if(loadingView.getVisibility() != View.GONE){
			loadingView.setVisibility(View.GONE);
		}
		xListView.stopRefresh();
		xListView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Tools.getLog(Tools.d, "ccc", "=========消息：返回了啊！！！！！=========");
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		
//		mEditor.putBoolean("isDynamicVisity", isVisibleToUser);
//		mEditor.commit();
	}
	onRefreshDynamic thisdynamic = new onRefreshDynamic() {

		@Override
		public void setRefresh() {
			// TODO Auto-generated method stub
			isConn = true;
			isReflush = true;
			getMsgConn(1);
		}
	}; 
}
