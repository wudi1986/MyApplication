package com.yktx.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yktx.check.adapter.FansFragmentAdapter;
import com.yktx.check.adapter.FansFragmentAdapter.OnFansFragmentItemClick;
import com.yktx.check.adapter.FansFragmentAdapter.setFansTypeLisitener;
import com.yktx.check.bean.FansBean;
import com.yktx.check.bean.FansItemBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.fragment.ClockFollowFragment;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClockFansActivity extends BaseActivity implements ServiceListener{
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

	String thisJobUserID,userName;
//	SharedPreferences settings;
	boolean isConn, isReflush;

	private FansBean fansBean;
	private ArrayList<FansItemBean> fansItemBeans = new ArrayList<FansItemBean>();

	private FansFragmentAdapter adapter;

	private XListView listView;
	private ImageView imageListNull,mBack,mAdd;
	private TextView mTitle;
	private boolean isOther;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.activity_clock_fans);
		imageListNull = (ImageView) findViewById(R.id.sizeNullImage);
		isOther = getIntent().getBooleanExtra("isother", false);
		thisJobUserID = getIntent().getStringExtra("thisjobuserid");
		if(thisJobUserID == null){
			thisJobUserID = userID;
		}
		if (!isConn) {
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			conn(1);
		}
		listView = (XListView) findViewById(R.id.clock_my_fragment_listview);
		adapter = new FansFragmentAdapter(mContext,true);
		mBack = (ImageView) findViewById(R.id.title_item_leftImage);
		mAdd = (ImageView) findViewById(R.id.title_item_rightImage);
		mTitle = (TextView) findViewById(R.id.title_item_content);
		if(isOther){
			imageListNull.setImageResource(R.drawable.zhanwei_fensi_taren);
		}else{
			imageListNull.setImageResource(R.drawable.zhanwei_fensi_ziji);
		}
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
//		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
//		swingBottomInAnimationAdapter.setAbsListView(listView);
		listView.setAdapter(adapter);
		listView.setIsShow(true);
		listView.setPullGoHome(false);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(listener);
		adapter.setfansType(fansTypeLisitener);//是否关注的回调
		adapter.setOnFansFragmentItemClick(onFansFragmentItemClick);//点击某条item 的
		mAdd.setVisibility(View.GONE);
		if(isOther){
			mTitle.setText("Ta的粉丝");
		}else{
			mTitle.setText("我的粉丝");
		}
		
//		LvHeightUtil.setListViewHeightBasedOnChildren(listView);
//		setRightAdapter(listView);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String clickfansID = fansItemBeans.get(arg2-1).getUserId();
				if(userID.equals(clickfansID)){
					Intent in = new Intent(mContext,
							ClockMyActivity.class);
					mContext.startActivity(in);
				}else{
					Intent in = new Intent(mContext, ClockOtherUserActivity.class);
					in.putExtra("userid",clickfansID);
					mContext.startActivity(in);
				}
			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	String fansName,fansID;
	setFansTypeLisitener fansTypeLisitener = new setFansTypeLisitener() {
		
		@Override
		public void setFansType(int position, boolean isfan) {
			// TODO Auto-generated method stub
			if(isConn){
				return;
			}
			isConn = true;
			FansItemBean itemBean = fansItemBeans.get(position);
			fansName = itemBean.getUserName();
			fansID = itemBean.getUserId();
			Tools.getLog(Tools.d, "aaa", "guanzhu:"+isfan);
			if(isfan){
				connUnFollow();
//				fansItemBeans.get(position).setRelation(0);
			}else{
//				fansItemBeans.get(position).setRelation(2);
				connFollow();
			}
		}
	};
OnFansFragmentItemClick onFansFragmentItemClick = new OnFansFragmentItemClick() {
		
		@Override
		public void itemClick(int position) {
			// TODO Auto-generated method stub
			String clickfansID = fansItemBeans.get(position).getUserId();
			if(userID.equals(clickfansID)){
				Intent in = new Intent(mContext,
						ClockMyActivity.class);
				mContext.startActivity(in);
			}else{
				Intent in = new Intent(mContext, ClockOtherUserActivity.class);
				in.putExtra("userid",clickfansID);
				mContext.startActivity(in);
			}
		}
	};
	public void connFollow(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", fansID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_FOLLOW, null,
				null, ClockFansActivity.this)
				.addList(params).request(UrlParams.POST);
	}
	public void connUnFollow(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", fansID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_UNFOLLOW, null,
				null, ClockFansActivity.this)
				.addList(params).request(UrlParams.POST);
	} 
	private void conn(int currentPage){
		StringBuffer sb  = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&userId=");
		sb.append(thisJobUserID);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_GETFANS, null,
				sb.toString(), ClockFansActivity.this)
				.addList(null).request(UrlParams.GET);
	}
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
				// Toast.makeText(NewFragment.this.getActivity(), "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			conn(currentPage + 1);
			Tools.getLog(Tools.i, "ccc","加载中的的currentPage：===="+currentPage);
			isConn = true;
		}
	};
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

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.USER_GETFANS:
					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						fansItemBeans.clear();
						fansBean = (FansBean) msg.obj;
						currentPage = fansBean.getCurrentPage();
						totalCount = fansBean.getTotalCount();
						totalPage = fansBean.getTotalPage();
						fansItemBeans = fansBean.getListData();
						Tools.getLog(Tools.d, "aaa", "个人中心："+fansItemBeans.toString());
						if (fansItemBeans.size() == 0) {
							imageListNull.setVisibility(View.VISIBLE);
							onLoad();
							adapter.notifyDataSetChanged();
							return;
						}
						imageListNull.setVisibility(View.GONE);
						//						send_time = newList.get(newList.size()-1).getCheck_time();
						adapter.setList(fansItemBeans);
						//						adapter.setDistance(latitude, longitude);
						listView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();

					} else {
						currentPage++;
						Tools.getLog(Tools.i, "ccc","联网中的currentPage：===="+currentPage);
						fansBean = (FansBean) msg.obj;
						fansItemBeans.addAll(fansBean.getListData());
						adapter.setList(fansItemBeans);
						//						send_time = newList.get(newList.size()-1).getCheck_time ();
						//						adapter.setDistance(latitude, longitude);
						adapter.notifyDataSetChanged();
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
				case Contanst.USER_FOLLOW:
					Toast.makeText(mContext, "加入关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = false;
					isReflush = true;
					conn(1);
//					adapter.setList(fansItemBeans);
//					adapter.notifyDataSetChanged();
					break;
				case Contanst.USER_UNFOLLOW:
					Toast.makeText(mContext, "取消关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = false;
					isReflush = true;
					conn(1);
//					adapter.setList(fansItemBeans);
//					adapter.notifyDataSetChanged();
					break;
				
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.USER_FOLLOW:
					isConn = false;
//					conn(1);
//					adapter.setList(fansItemBeans);
//					adapter.notifyDataSetChanged();
					break;
				case Contanst.USER_UNFOLLOW:
					Toast.makeText(mContext, "取消关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = false;
//					conn(1);
//					adapter.setList(fansItemBeans);
//					adapter.notifyDataSetChanged();
					break;
				}
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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
