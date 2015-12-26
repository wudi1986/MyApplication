package com.yktx.check.fragment;

import java.util.ArrayList;

import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.NewFragmentListViewAdapter;
import com.yktx.check.adapter.NewFragmentListViewAdapter.OnNewFragmentItemClick;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.MyTaskBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.BaseFragment;
import com.yktx.check.square.fragment.NewFragment;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.LvHeightUtil;
import com.yktx.check.util.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

@SuppressLint({ "ValidFragment", "NewApi" })
public class ClockMyJobListFragment extends BaseFragment implements ServiceListener{
	private Activity mContext;
	private NewFragmentListViewAdapter adapter;
	private RelativeLayout layout;
	private LayoutInflater inflater;
	private XListView listView;
//	private ImageView imageListNull;
	private ArrayList<TaskItemBean> itemBeans;

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

	boolean isConn, isReflush;

	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	String userID,thisJobUserID;
	SharedPreferences settings;
//	ScrollView parentScrollView;
//	LinearLayout offView;


	@SuppressLint("NewApi")
	public ClockMyJobListFragment(Context context,String userId) {
		super();
//		this.mContext = context;
		thisJobUserID = userId;
//		this.parentScrollView = parentScrollView;
//		this.offView = offView;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = ClockMyJobListFragment.this.getActivity();
		layout = (RelativeLayout) inflater.inflate(R.layout.clock_my_fragment, null);
//		imageListNull = (ImageView) layout.findViewById(R.id.sizeNullImage);
		settings = mContext.getSharedPreferences("clock", mContext.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		if (!isConn) {
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			conn(1);
		}
		listView = (XListView) layout
				.findViewById(R.id.clock_my_fragment_listview);
		if(userID.equals(thisJobUserID)){
			adapter = new NewFragmentListViewAdapter(mContext, true, userID,2);
		}else{
			adapter = new NewFragmentListViewAdapter(mContext, true, userID,3);
		}
		
		
		
		listView.setIsShow(true);
		listView.setPullGoHome(false);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(listener);
//		listView.setParentScrollView(parentScrollView);
//		listView.setViewGetLocationOnScreen(offView);
		
		adapter.setOnNewFragmentItemClick(itemClick);
		//		adapter.setList(itemBeans);
		//		listView.setAdapter(adapter);
		return layout;
	}
	OnNewFragmentItemClick itemClick = new OnNewFragmentItemClick() {

		@Override
		public void itemClick(int position) {
			// TODO Auto-generated method stub
			TaskItemBean myTaskBean = newList.get(position);
			Intent in = new Intent(mContext, TaskInfoActivity.class);
			in.putExtra("taskid", myTaskBean.getTaskId());
			in.putExtra("userid", thisJobUserID);
			if(!userID.equals(thisJobUserID)){
				in.putExtra("isother", true);
			}else{
				in.putExtra("isother", false);
			}

			startActivity(in);
		}
	};
	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			conn(1);
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
			conn(currentPage + 1);
			Tools.getLog(Tools.i, "ccc","加载中的的currentPage：===="+currentPage);
			isConn = true;
		}
	};

	private void conn(int currentPage) {
		StringBuffer sb  = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&userId=");
		sb.append(thisJobUserID);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_JOB_GETBYUSERID, null,
				sb.toString(), ClockMyJobListFragment.this)
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
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.JOB_GETBYUSERID:
					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						newList.clear();
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						currentPage = bean.getCurrentPage();
						totalCount = bean.getTotalCount();
						totalPage = bean.getTotalPage();
						newList = bean.getListData();
						Tools.getLog(Tools.d, "aaa", "个人中心："+newList.toString());
						listView.setAdapter(adapter);//放到这里就不会出现空图
						if (newList.size() == 0) {
//							imageListNull.setVisibility(View.VISIBLE);
							onLoad();
							adapter.notifyDataSetChanged();
							return;
						}
						
//						imageListNull.setVisibility(View.GONE);
						//						send_time = newList.get(newList.size()-1).getCheck_time();
						adapter.setList(newList);
					
						adapter.notifyDataSetChanged();
						//						adapter.setDistance(latitude, longitude);
						listView.setPullLoadEnable(true);

						SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
						swingBottomInAnimationAdapter.setAbsListView(listView);
						listView.setAdapter(swingBottomInAnimationAdapter);

					} else {
						currentPage++;
						Tools.getLog(Tools.i, "ccc","联网中的currentPage：===="+currentPage);
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						newList.addAll(bean.getListData());
						adapter.setList(newList);
						adapter.notifyDataSetChanged();
						//						send_time = newList.get(newList.size()-1).getCheck_time ();
						//						adapter.setDistance(latitude, longitude);
						
//						SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
//						swingBottomInAnimationAdapter.setAbsListView(listView);
//						listView.setAdapter(swingBottomInAnimationAdapter);
					}

					onLoad();
					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
						listView.setIsShow(false);
					} else {
						listView.setIsShow(true);
					}
					//判断是否为本页点入building 页
					//					Tools.getLog(Tools.d, "aaa","type："+GroupMainFragmentActivity.ReflushItem+"，页数："+newItem);
					//					if(GroupMainFragmentActivity.ReflushItem == 1){
					//						xListView.setSelection(newItem);
					//					}
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.JOB_GETBYUSERID:

					break;
				}
				break;
			}
		}
	};
	private void onLoad() {
		//		if(loadingView.getVisibility() != View.GONE){
		//			loadingView.setVisibility(View.GONE);
		//		}
		listView.stopRefresh();
		listView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}

}
