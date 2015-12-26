package com.yktx.check.square.fragment;

import java.util.ArrayList;

import com.yktx.check.ClockTaskDynamicActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.ClockVoteAdapter;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.CommentListBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class VoteFragment extends BaseFragment implements ServiceListener{
	private XListView xListView;
	
	boolean isConn, isReflush;
	public String jobid, taskId,createUserID,userID;
	ClockVoteAdapter adapter;
	CommentListBean commentListBean;
	ArrayList<CommentsBean> newList = new ArrayList<CommentsBean>(10);
	private RelativeLayout mTitleContent,group_info_frist_item_titleLayout;

	private TextView group_info_frist_item_title,
	group_info_frist_item_content;
	private ImageView addTask;
	ByIdDetailBean byIdDetailBean;

	private ProgressBar vote_ProgressBar;
	
	ClockTaskDynamicActivity mActivity;
	
	public SharedPreferences settings;
	
	
	@SuppressLint("ValidFragment")
	public VoteFragment(ClockTaskDynamicActivity activity){
		mActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_clock_vote, container,
				false);
		settings = mActivity.getSharedPreferences("clock", mActivity.MODE_PRIVATE);
		
		userID = settings.getString("userid", null);
		
		xListView = (XListView) view.findViewById(R.id.vote_XListView);
		addTask = (ImageView) view.findViewById(R.id.addTask);
		vote_ProgressBar = (ProgressBar) view.findViewById(R.id.vote_ProgressBar);
		group_info_frist_item_title = (TextView) view.findViewById(R.id.group_info_frist_item_title);
		group_info_frist_item_content = (TextView) view.findViewById(R.id.group_info_frist_item_content);
		group_info_frist_item_titleLayout = (RelativeLayout) view.findViewById(R.id.group_info_frist_item_titleLayout);
		
		jobid = mActivity.getIntent().getStringExtra("jobid");
		taskId = mActivity.getIntent().getStringExtra("taskId");
		createUserID = mActivity.getIntent().getStringExtra("createUserID");
		
		
		adapter = new ClockVoteAdapter(mActivity);
		xListView.setAdapter(adapter);
		xListView.setXListViewListener(listener);
		xListView.setIsShow(true);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		// Conn(1);
		getByIdConn();
		addTask.setVisibility(View.GONE);
		
		group_info_frist_item_titleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mActivity,
						TaskInfoActivity.class);
				in.putExtra("taskid",taskId);
				in.putExtra("userid", createUserID);
				Tools.getLog(Tools.d, "aaa", "ClockCommentActivity=======createUserID:"+createUserID);
				mActivity.startActivity(in);
			}
		});
		
		return view;
	}
	
	private void getByIdConn() {
		// loadingView.setVisibility(View.VISIBLE);
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(taskId);

		Service.getService(Contanst.HTTP_GETBYIDTASK, null, sb.toString(),
				VoteFragment.this).addList(null).request(UrlParams.GET);
	}

	public void Conn(int currentPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&userId=");
		sb.append(userID);
		sb.append("&jobId=");
		sb.append(jobid);
		Service.getService(Contanst.HTTP_GETVOTE, null, sb.toString(),
				VoteFragment.this).addList(null).request(UrlParams.GET);
	}
	
	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			Conn(1);
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
			Tools.getLog(Tools.d, "ccc", "currentPage====="+currentPage);
			Tools.getLog(Tools.d, "ccc", "totalCount====="+totalCount);

			if (currentPage * 10 >= totalCount) {
				
				// Toast.makeText(NewFragment.this.getActivity(), "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			Conn(currentPage + 1);
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
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:

				switch (msg.arg1) {

				case Contanst.GETVOTE:
					commentListBean = (CommentListBean) msg.obj;
					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						newList.clear();
						currentPage = commentListBean.getCurrentPage();
						totalCount = commentListBean.getTotalCount();
						totalPage = commentListBean.getTotalPage();
						newList = commentListBean.getListData();
						
						Tools.getLog(Tools.d, "ccc", "currentPage====="+currentPage);
						Tools.getLog(Tools.d, "ccc", "totalCount====="+totalCount);

						if (newList.size() == 0) {
							onLoad();
							vote_ProgressBar.setVisibility(View.GONE);
							return;
						}
						adapter.setList(newList);
						xListView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();

					} else {
						currentPage++;
						CommentListBean bean = (CommentListBean) msg.obj;
						newList.addAll(commentListBean.getListData());
						adapter.setList(newList);
						adapter.notifyDataSetChanged();
					}
					onLoad();
					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
						xListView.setIsShow(false);
					} else {
						xListView.setIsShow(true);
					}
					break;
				case Contanst.GETBYIDTASK:
					byIdDetailBean = (ByIdDetailBean) msg.obj;
					showTitle();
					isReflush = true;
					// SelectClockStateConn(null, null);//分页获取当前数据
					Conn(1);
					break;
				}

				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Tools.getLog(Tools.d, "aaa", message);
				break;
			}
		}
	};
	
	public void showTitle() {
		if (byIdDetailBean != null) {
			if(byIdDetailBean.getTitle()!= null)
				group_info_frist_item_title.setText(byIdDetailBean.getTitle());
			String description = byIdDetailBean.getDescription();
			group_info_frist_item_content.setVisibility(View.GONE);
			//			if (description != null && description.length() != 0) {
			//				group_info_frist_item_content.setText(description);
			//				group_info_frist_item_content.setVisibility(View.VISIBLE);
			//			} else {
			//				group_info_frist_item_content.setVisibility(View.GONE);
			//			}
		}
	}
	
	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}

}
