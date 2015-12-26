package com.yktx.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yktx.check.adapter.AttentionRecommendFragmentListViewAdapter;
import com.yktx.check.adapter.AttentionRecommendFragmentListViewAdapter.setFansTypeLisitener;
import com.yktx.check.bean.RecommendFollowBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.AttentionFragment2;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClockSearchActivity extends BaseActivity implements ServiceListener{
	public static EditText groupSearchInput;
	TextView title_item_leftText;
	ArrayList<RecommendFollowBean> recommendFollowBeans = new ArrayList<RecommendFollowBean>();
	private ListView groupSearchListView;
	AttentionRecommendFragmentListViewAdapter adapter;
	private boolean isNetWork;
	RelativeLayout loadingView;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_search);
		groupSearchInput = (EditText) findViewById(R.id.groupSearchInput);
		title_item_leftText = (TextView) findViewById(R.id.title_item_leftText);
		groupSearchListView = (ListView) findViewById(R.id.groupSearchListView);
		loadingView = (RelativeLayout) findViewById(R.id.loadingView);
		connRecommendFollow();

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		timer = new Timer();
		adapter = new AttentionRecommendFragmentListViewAdapter(mContext, userID,true);
		adapter.thisetFansTypeLisitener(fansTypeLisitener);
		adapter.setEditText(groupSearchInput);
		groupSearchListView.setAdapter(adapter);
//		clockApplication.openKeybord(groupSearchInput, mContext);
	}
	String fansName;
	setFansTypeLisitener fansTypeLisitener = new setFansTypeLisitener() {

		@Override
		public void setFansType(int position, boolean isfan) {
			// TODO Auto-generated method stub
			if(isNetWork){
				return;
			}
			isNetWork = true;
			fansName = recommendFollowBeans.get(position).getName();
			if(isfan){
				connUnFollow(recommendFollowBeans.get(position).getUserId());
				recommendFollowBeans.get(position).setRelation(1);
			}else{
				connFollow(recommendFollowBeans.get(position).getUserId());
				recommendFollowBeans.get(position).setRelation(2);
			}
		}
	}; 

	Timer timer;
	private static final int TIMER_SEARCH = 101;
	String searchStr;
	private final int SearchTime = 1000;
	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		title_item_leftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clockApplication.closeKeybord(groupSearchInput, mContext);
				finish();
				overridePendingTransition(R.anim.fade, R.anim.hold);

			}
		});	
		groupSearchInput.addTextChangedListener(new TextWatcher() {
			private int selectionStart;
			private int selectionEnd;
			private CharSequence temp;
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				searchStr = groupSearchInput.getText().toString();
				selectionStart = groupSearchInput.getSelectionStart();
				selectionEnd = groupSearchInput.getSelectionEnd();

				final int textLength = groupSearchInput.getText().length();
				timer.cancel();
				timer = new Timer();
				/**
				 * 延时一秒执行
				 */
				timer.schedule(new TimerTask() {
					public void run() {
						Message msg = new Message();
						msg.what = TIMER_SEARCH;
						msg.arg1 = textLength;
						mHandler.sendMessage(msg);
					}
				}, SearchTime);
			}
		});
		groupSearchInput.setOnKeyListener(new OnKeyListener() {//回车的监听

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_ENTER){  

					//					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(mContext.INPUT_METHOD_SERVICE);  
					//
					//					if(imm.isActive()){  
					//
					//						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
					//
					//					}  
					Tools.getLog(Tools.d, "aaa", "回车监听！");
					return true;  

				}  

				return false;  
			}
		});

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.fade, R.anim.hold);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void connRecommendFollow(){
		if (loadingView.getVisibility() == View.GONE) {
			loadingView.setVisibility(View.VISIBLE);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);

		Service.getService(Contanst.HTTP_USER_GETRECOMMENDFOLLOW, null,
				sb.toString(), ClockSearchActivity.this).addList(null)
				.request(UrlParams.GET);
	}
	public void ConnSearch(String searchStr){
		if (loadingView.getVisibility() == View.GONE) {
			loadingView.setVisibility(View.VISIBLE);
		}
		Tools.getLog(Tools.i, "aaa", "搜索关键字：========"+searchStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId", userID));
			params.add(new BasicNameValuePair("keyWord", searchStr));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_SEARCH, null,
				null, ClockSearchActivity.this)
				.addList(params).request(UrlParams.POST);
	}
	public void connFollow(String thisJobUsedID){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", thisJobUsedID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_FOLLOW, null,
				null, ClockSearchActivity.this)
				.addList(params).request(UrlParams.POST);
	}
	public void connUnFollow(String thisJobUsedID){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", thisJobUsedID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_UNFOLLOW, null,
				null, ClockSearchActivity.this)
				.addList(params).request(UrlParams.POST);
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
				switch (msg.arg1) {
				case Contanst.USER_SEARCH:
					isNetWork = false;
					recommendFollowBeans = (ArrayList<RecommendFollowBean>) msg.obj;
					adapter.setList(recommendFollowBeans);
					adapter.setIsSearch(true);
					adapter.notifyDataSetChanged();
					if (loadingView.getVisibility() != View.GONE) {
						loadingView.setVisibility(View.GONE);
					}
					break;
				case Contanst.USER_FOLLOW:
					Toast.makeText(mContext, "加入关注,"+fansName, Toast.LENGTH_SHORT).show();
					isNetWork = false;
					adapter.setList(recommendFollowBeans);
					adapter.notifyDataSetChanged();
					break;
				case Contanst.USER_UNFOLLOW:
					Toast.makeText(mContext, "取消关注,"+fansName, Toast.LENGTH_SHORT).show();
					isNetWork = false;
					adapter.setList(recommendFollowBeans);
					adapter.notifyDataSetChanged();
					break;
				case Contanst.USER_GETRECOMMENDFOLLOW:
					recommendFollowBeans = (ArrayList<RecommendFollowBean>) msg.obj;
					adapter.setList(recommendFollowBeans);
					adapter.setIsSearch(false);
					adapter.notifyDataSetChanged();
					if (loadingView.getVisibility() != View.GONE) {
						loadingView.setVisibility(View.GONE);
					}
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				break;
			case TIMER_SEARCH:

				if (msg.arg1 > 0) {
					isNetWork = true;
					ConnSearch(searchStr);
				}
				break;
			}
		}
	};

}
