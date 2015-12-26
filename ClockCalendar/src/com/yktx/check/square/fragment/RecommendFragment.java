package com.yktx.check.square.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.R;
import com.yktx.check.adapter.RecommendFragmentAdapter;
import com.yktx.check.bean.HotTestBean;
import com.yktx.check.bean.HotTestListBean;
import com.yktx.check.bean.LatestBean;
import com.yktx.check.bean.MostDatesTaskBean;
import com.yktx.check.bean.TopPointUserBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

/**
 * 榜单 Fragment的界面
 * 
 * @author wudi
 */
public class RecommendFragment extends BaseFragment implements ServiceListener {

	XListView xListView;
	boolean isConn, isReflush;
	String userID;
	long send_time;
	ArrayList<HotTestBean> hotList = new ArrayList<HotTestBean>(10);
	ArrayList<TopPointUserBean> topPointUserBeans = new ArrayList<TopPointUserBean>(10);
	ArrayList<LatestBean> latestBeans = new ArrayList<LatestBean>(10);
	RecommendFragmentAdapter adapter;
	int recommendListSize = 0;
	HotTestBean toChatBean;
	RelativeLayout loadingView;
	ImageView imageListNull;
	private SharedPreferences settings;
	ArrayList<MostDatesTaskBean> newList = new ArrayList<MostDatesTaskBean>(10);
	private ProgressBar hot_or_near_ProgressBar;
	
	private Activity mContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = RecommendFragment.this.getActivity();
		settings = mContext.getSharedPreferences("clock", mContext.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		send_time = 0;
		//		if (!isConn) {
		//			isConn = true;
		//			isReflush = true;
		//			connHot(1, 0);
		//		}
		GetTopPointUserConn();
		GetLatestBuildingConn();
		GetTodayBoutiqueJob();
		connHot(1, 0);

		//		connRecommendList();
		View view = inflater.inflate(R.layout.hot_or_near_activity, container,
				false);

		hot_or_near_ProgressBar = (ProgressBar) view.findViewById(R.id.hot_or_near_ProgressBar);	
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		imageListNull = (ImageView) view.findViewById(R.id.imageListNull);
		xListView = (XListView) view.findViewById(R.id.xListView);
		xListView.setXListViewListener(listener);
		adapter = new RecommendFragmentAdapter(mContext,userID);
		xListView.setAdapter(adapter);
		// xListView.setIXListViewOnGoHome(iXListViewOnGoHome);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		xListView.setIsShow(false);
		//		xListView.setFooterBackground(getResources().getColor(R.color.meibao_color_15));
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				//				GroupMainFragmentActivity.ReflushItem = 2;  
				//				GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
				//				NewFragment.isNewLoadAgain = true;
				//				HotTestBean bean = hotList.get(position-1);
				//				Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
				//				in.putExtra("buildingId", bean.getBuildingId());
				//				if(position == hotList.size()+2 ){//这是因为点击了最新创建的title
				//					Tools.getLog(Tools.d, "aaa", "直接返回！！！！！！！！！");
				//					return;
				//				}
				//				int type = adapter.getItemViewType(position-1);
				//				Tools.getLog(Tools.d, "aaa", "点击位置"+position);
				//				Tools.getLog(Tools.d, "aaa", "点击位置的哇状态"+type);
				//				Tools.getLog(Tools.d, "aaa", "hot的shulian"+type);

				//				Intent in = new Intent(mContext, ClockGroupInfoActivity.class);
				//				if(type == adapter.HOTTASK){
				//					HotTestBean hotTestBean= hotList.get(position-2);
				//					Tools.getLog(Tools.d, "aaa", "hotTestBean数据"+hotTestBean.toString());
				//					in.putExtra("buildingId",hotTestBean.getBuildingId());
				//				}else if(type == adapter.NEWITEM){
				//					String buildingID = latestBeans.get(position-2-hotList.size()-1).getBuildingId();
				//					if(buildingID == null){
				//						return;
				//					}
				//					in.putExtra("buildingId", buildingID);
				//				}else if(type == adapter.TOPPOINTUSER||type == adapter.TODAYHOTJOB){
				//					return;
				//				}
				//				mContext.startActivity(in);
			}
		});

		return view;
	}


	private void connHot(int currentPage, long send_time) {
		StringBuffer sb  = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_GETHOTTEST, null,
				sb.toString(), RecommendFragment.this)
				.addList(null).request(UrlParams.GET);
	}

	//	private void connRecommendList() {
	//
	//		List<NameValuePair> params1 = new ArrayList<NameValuePair>();
	//		try {
	//			params1.add(new BasicNameValuePair("currentPage", "1"));
	//			params1.add(new BasicNameValuePair("pageLimit", pageLimit + ""));
	//			params1.add(new BasicNameValuePair("longitude", longitude));
	//			params1.add(new BasicNameValuePair("latitude", latitude));
	//			params1.add(new BasicNameValuePair("type", "3"));
	//			params1.add(new BasicNameValuePair("user_id", userID));
	//			params1.add(new BasicNameValuePair("send_time", "0"));
	//
	//		} catch (Exception e) {
	//
	//		}
	//		Service.getService(Contanst.HTTP_HOMEPAGE, null,
	//				GroupMainFragmentActivity.MAIN_RECOMMEND_HTTP,
	//				RecommendFragment.this).addList(params1)
	//				.request(UrlParams.POST);
	//	}

	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			//			if (isConn) {
			//				return;
			//			}
			//			//			connRecommendList();
			//			connHot(1, 0);
			//			isReflush = true;
			//			isConn = true;
			GetTopPointUserConn();
		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			isReflush = false;
			if (currentPage * 10 >= totalCount) {
				onLoad();
				return;
			}
			connHot(currentPage + 1, send_time);
			isConn = true;
		}
	};

	// IXListViewOnGoHome iXListViewOnGoHome = new IXListViewOnGoHome() {
	//
	// @Override
	// public void onGoHome() {
	// // TODO Auto-generated method stub
	// if (onGoHomeListener != null) {
	// onGoHomeListener.goHome();
	// }
	// onLoad();
	//
	// }
	// };

	/**
	 * 进群接口
	 */
	//	private void intoGroup(String group_id, String group_name) {
	//		Tools.getLog(Tools.i, "aaa", "intoGroup  group_name ============ "
	//				+ group_name);
	//		Tools.getLog(Tools.i, "aaa",
	//				"(GroupApplication.getInstance()).getCurSP() ============ "
	//						+ (GroupApplication.getInstance()).getCurSP());
	//		// TelephonyManager tm = (TelephonyManager) CameraActivity.this
	//		// .getSystemService(TELEPHONY_SERVICE);
	//		// final String Imei = tm.getDeviceId();
	//
	//		if (userID == null || userID.length() == 0) {
	//			SharedPreferences setting = GroupApplication.getInstance()
	//					.getSharedPreferences(
	//							(GroupApplication.getInstance()).getCurSP(), 0);
	//			userID = setting.getString("userID", null);
	//
	//		}
	//
	//		List<NameValuePair> params = new ArrayList<NameValuePair>();
	//		try {
	//			params.add(new BasicNameValuePair("longitude", longitude));
	//			params.add(new BasicNameValuePair("latitude", latitude));
	//			params.add(new BasicNameValuePair("user_id", userID));
	//			if (group_id != null && group_id.length() > 0) {
	//
	//				params.add(new BasicNameValuePair("group_id", group_id));
	//			}
	//			if (group_name != null && group_name.length() > 0) {
	//				params.add(new BasicNameValuePair("group_name", group_name));
	//			}
	//			// params.add(new BasicNameValuePair("token", Imei));
	//
	//			Tools.getLog(Tools.i, "aaa", "params ============ " + params);
	//
	//		} catch (Exception e) {
	//
	//		}
	//		Service.getService(Contanst.HTTP_INTOGROUP, null, null,
	//				RecommendFragment.this).addList(params).request(UrlParams.POST);
	//	}

	public void GetTopPointUserConn(){
		StringBuffer sb  = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_BUILDING_GETTOPPOINTUSER, null,
				sb.toString(), RecommendFragment.this)
				.addList(null).request(UrlParams.GET);
	}

	public void GetLatestBuildingConn(){
		StringBuffer sb  = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_BUILDING_GETLATESTBUILDING, null,
				sb.toString(), RecommendFragment.this)
				.addList(null).request(UrlParams.GET);
	}
	public void GetTodayBoutiqueJob(){
		StringBuffer sb  = new StringBuffer();
		//		sb.append("?currentPage=");
		//		sb.append(currentPage);
		//		sb.append("&pageLimit=");
		//		sb.append(pageLimit);
		sb.append("?userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_GET_MOST_DATES_TASK, null,
				sb.toString(), RecommendFragment.this)
				.addList(null).request(UrlParams.GET);
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "getJOSNdataSuccessgetJOSNdataSuccess");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		if (sccmsg != null
				&& sccmsg.equals(GroupMainFragmentActivity.MAIN_RECOMMEND_HTTP)) {
			msg.arg2 = 1;
		} else if (sccmsg != null
				&& sccmsg.equals(GroupMainFragmentActivity.MAIN_HOT_HTTP)) {
			msg.arg2 = 2;
		}
		mHandler.sendMessage(msg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yktx.snake.conn.ServiceListener#getJOSNdataFail(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	boolean isHotList,isTopPointUserBeans,isLatestBeans, isNewList;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:

				if (msg.arg1 == Contanst.GETHOTTEST) {

					HotTestListBean bean = (HotTestListBean) msg.obj;
					currentPage = bean.getCurrentPage();
					totalCount = bean.getTotalCount();
					totalPage = bean.getTotalPage();

					hotList = bean.getListData();
					//					for(int i = 0;i<hotList.size();i++){
					//						if(i>5){
					//							hotList.remove(i);
					//							i--;//对条删除
					//						}
					//					}
					isHotList = true;
					adapter.setHotList(hotList);
					Tools.getLog(Tools.d, "aaa", "hotList的数量："+hotList.size());
					adapter.notifyDataSetChanged();
				}else if(msg.arg1 == Contanst.BUILDING_GETTOPPOINTUSER){
					topPointUserBeans = (ArrayList<TopPointUserBean>) msg.obj;

					isTopPointUserBeans = true;
					adapter.setTopPointUserBeans(topPointUserBeans);
					adapter.notifyDataSetChanged();
				}else if(msg.arg1 == Contanst.BUILDING_GETLATESTBUILDING){
					latestBeans = (ArrayList<LatestBean>) msg.obj;
					isLatestBeans = true;
					Tools.getLog(Tools.d, "aaa", "完成接口！！！！！！！");
					adapter.setLatestBeans(latestBeans);
					adapter.notifyDataSetChanged();
					onLoad();
				}else if (msg.arg1 == Contanst.GET_MOST_DATES_TASK) {
					newList = (ArrayList<MostDatesTaskBean>) msg.obj;
					isNewList = true;
					adapter.setTodayBoutiqueJobList(newList);
					adapter.notifyDataSetChanged();
				}
				isConnOver();
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;

				hot_or_near_ProgressBar.setVisibility(View.GONE);
				switch(msg.arg1){
				case Contanst.GETHOTTEST:
					isHotList = true;
					break;
				case Contanst.BUILDING_GETTOPPOINTUSER:
					isTopPointUserBeans = true;
					break;
				case Contanst.BUILDING_GETLATESTBUILDING:
					isLatestBeans = true;
					break;
				case Contanst.GET_MOST_DATES_TASK:
					isNewList = true;

					break;
				}

				isConnOver();
				//				if (msg.arg1 == Contanst.GETHOTTEST) {
				////					xListView.setAdapter(adapter);
				//					adapter.notifyDataSetChanged();
				//
				//					Tools.getLog(Tools.i, "aaa", "message = " + message);
				//					onLoad();
				//					
				//				} 
				break;
			}
		}

	};

	private boolean isConnOver(){
		if(isHotList && isTopPointUserBeans && isLatestBeans && isNewList){
			onLoad();
			return true;
		} else {
			return false;
		}
	}

	private void onLoad() {
		if(loadingView.getVisibility() != View.GONE){
			loadingView.setVisibility(View.GONE);
		}
		xListView.stopRefresh();
		xListView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
}
