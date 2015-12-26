package com.yktx.check.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.ClockNewBuildingAdapter;
import com.yktx.check.adapter.ClockNewBuildingAdapter.OnClickItem;
import com.yktx.check.adapter.ClockNewBuildingAdapter.addBuilding;
import com.yktx.check.bean.RecommendBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.ListViewForScrollView;

@SuppressLint("ValidFragment")
public class ClockNewBuildingFragment extends Fragment implements ServiceListener{
	private ClockNewBuildingAdapter adapter;
	private List<RecommendBean> beans;
	private onClickAdd clickAdd;
	public ListViewForScrollView listView;
	private String type;
	ScrollView parentScrollView;
	int height;
	GOTopButton goTopButton;
	RelativeLayout bgLayout;
	String userID;
	public ClockNewBuildingFragment(String type, ScrollView parentScrollView , GOTopButton goTopButton, String userID){
		this.type =type;  
		this.parentScrollView =parentScrollView;  
		this.goTopButton = goTopButton;
		this.userID = userID;
	}
	public void setClickAdd(onClickAdd clickAdd){
		this.clickAdd = clickAdd;  
	}
	
	private Activity mContext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = ClockNewBuildingFragment.this.getActivity();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		FrameLayout fl = new FrameLayout(mContext);
		fl.setLayoutParams(params);
		listView= new ListViewForScrollView(mContext, parentScrollView);
		listView.setDividerHeight(0);
		listView.setDivider(null);
		listView.setLayoutParams(params);
		listView.setLayoutParams(params);
		listView.setVerticalScrollBarEnabled(false);//不活动的时候隐藏，活动的时候也隐藏
		adapter = new ClockNewBuildingAdapter(mContext);
		adapter.setAddBuilding(building);
		adapter.setOnClickItem(onClickItem);
		listView.setAdapter(adapter);
		bgLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				R.layout.loading_view_anim2, null);
//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				// TODO Auto-generated method stub
////				if (goTopButton != null)
////					goTopButton.getTopBottom(beans.get(arg2).getTitle());
//				
//			}
//		});

		fl.addView(listView);
		fl.addView(bgLayout);
		getBuildingConn(type);
		return fl;
	}
	addBuilding building = new addBuilding() {

		@Override
		public void add(RecommendBean recommendBean) {
			// TODO Auto-generated method stub
			clickAdd.clickAdd(recommendBean);
		}
	};
	public void getBuildingConn(String type){
		String str = "?categoryId="+type+"&userId="+userID;
		Service.getService(Contanst.HTTP_GETRECOMMEND, null, str,
				ClockNewBuildingFragment.this).addList(null)
				.request(UrlParams.GET);

	}

	OnClickItem onClickItem = new OnClickItem() {

		@Override
		public void click(String id) {
			// TODO Auto-generated method stub
			Intent in = new Intent(mContext,
					ClockGroupInfoFragmentActivity.class);
			in.putExtra("buildingId", id);
			mContext.startActivity(in);
		}
	};

	public interface listenerSetHeght {
		public void setHeight(int height);
	};
	/**点击添加*/
	public interface onClickAdd{
		public void clickAdd(RecommendBean recommendBean);
	}
	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "===========getJOSNdataSuccess=============");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}
	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "==========getJOSNdataFail===========");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	} 
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETRECOMMEND:
					beans = (List<RecommendBean>) msg.obj;
					adapter.setBeans(beans);
					adapter.notifyDataSetChanged();
					bgLayout.setVisibility(View.GONE);
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.GETRECOMMEND:
					Tools.getLog(Tools.d, "aaa", "GETRECOMMEND:"+message);
					bgLayout.setVisibility(View.VISIBLE);
					getBuildingConn(type);
					break;
				}
				break;
			}
		}
	};

	public interface GOTopButton {
		void getTopBottom(String title);
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
