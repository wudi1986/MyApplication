//package com.yktx.check;
//
//import java.util.ArrayList;
//
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.clock.service.MyReceiver;
//import com.clock.service.MyReceiver.onRefreshDynamic;
//import com.umeng.analytics.MobclickAgent;
//import com.yktx.check.adapter.ClockVoteAdapter;
//import com.yktx.check.bean.ByIdDetailBean;
//import com.yktx.check.bean.CommentListBean;
//import com.yktx.check.bean.CommentsBean;
//import com.yktx.check.conn.ServiceListener;
//import com.yktx.check.conn.UrlParams;
//import com.yktx.check.listview.XListView;
//import com.yktx.check.listview.XListView.IXListViewListener;
//import com.yktx.check.service.Service;
//import com.yktx.check.util.Contanst;
//import com.yktx.check.util.Tools;
//
///**
// * 
// * 点赞
// * 
// * @author Administrator
// * 
// */
//public class ClockVoteActivity extends BaseActivity implements ServiceListener {
//	public static ClockVoteActivity clockVoteActivity;
//	private XListView xListView;
//	private ImageView mBack, mAdd;
//	private TextView mTitle;
//	/** 一页多少条数据 */
//	public int pageLimit = 10;
//	/** 第几页 */
//	public int currentPage;
//	/** 总数 */
//	public int totalCount;
//	/** 总页数 */
//	public int totalPage;
//	boolean isConn, isReflush;
//	public String jobid, taskId,createUserID;
//	ClockVoteAdapter adapter;
//	CommentListBean commentListBean;
//	ArrayList<CommentsBean> newList = new ArrayList<CommentsBean>(10);
//	private RelativeLayout mTitleContent,group_info_frist_item_titleLayout;
//
//	private TextView group_info_frist_item_title,
//	group_info_frist_item_content;
//	private ImageView addTask;
//	ByIdDetailBean byIdDetailBean;
//
//	private ProgressBar vote_ProgressBar;
//
//	@Override
//	protected void findViews() {
//		// TODO Auto-generated method stub
//		setContentView(R.layout.activity_clock_vote);
//		xListView = (XListView) findViewById(R.id.vote_XListView);
//		mBack = (ImageView) findViewById(R.id.title_item_leftImage);
//		mAdd = (ImageView) findViewById(R.id.title_item_rightImage);
//		mTitle = (TextView) findViewById(R.id.title_item_content);
//		//		mTitleContent = (RelativeLayout) findViewById(R.id.vote_title);
//		group_info_frist_item_titleLayout = (RelativeLayout) findViewById(R.id.group_info_frist_item_titleLayout);
//		//		mTitleContent = (RelativeLayout) findViewById(R.id.vote_title);
//		jobid = getIntent().getStringExtra("jobid");
//		taskId = getIntent().getStringExtra("taskId");
//		createUserID = getIntent().getStringExtra("createUserID");
//
//		group_info_frist_item_title = (TextView) findViewById(R.id.group_info_frist_item_title);
//		group_info_frist_item_content = (TextView) findViewById(R.id.group_info_frist_item_content);
//		addTask = (ImageView) findViewById(R.id.addTask);
//		vote_ProgressBar = (ProgressBar) findViewById(R.id.vote_ProgressBar);
//		clockVoteActivity = this;
//	}
//
//	@Override
//	protected void init() {
//		// TODO Auto-generated method stub
//		mTitleContent
//		.setBackgroundColor(getResources().getColor(R.color.white));
//		mTitle.setText("打气");
//		mAdd.setVisibility(View.GONE);
//		adapter = new ClockVoteAdapter(mContext);
//		xListView.setAdapter(adapter);
//		xListView.setXListViewListener(listener);
//		xListView.setIsShow(true);
//		xListView.setPullGoHome(false);
//		xListView.setPullLoadEnable(false);
//		xListView.setPullRefreshEnable(true);
//		// Conn(1);
//		getByIdConn();
//		addTask.setVisibility(View.GONE);
//
//	}
//
//	@Override
//	protected void setListeners() {
//		// TODO Auto-generated method stub
//
//		mBack.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
//		group_info_frist_item_titleLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent in = new Intent(ClockVoteActivity.this,
//						TaskInfoActivity.class);
//				in.putExtra("taskid",taskId);
//				in.putExtra("userid", createUserID);
//				Tools.getLog(Tools.d, "aaa", "ClockCommentActivity=======createUserID:"+createUserID);
//				ClockVoteActivity.this.startActivity(in);
//			}
//		});
//
//	}
//
//	IXListViewListener listener = new IXListViewListener() {
//
//		@Override
//		public void onRefresh() {
//			// TODO Auto-generated method stub
//			if (isConn) {
//				return;
//			}
//			Conn(1);
//			isReflush = true;
//			isConn = true;
//
//		}
//
//		@Override
//		public void onLoadMore() {
//			// TODO Auto-generated method stub
//			if (isConn) {
//				return;
//			}
//			isReflush = false;
//
//			if (currentPage * 10 >= totalCount) {
//				// Toast.makeText(NewFragment.this.getActivity(), "亲，没有更多信息了",
//				// Toast.LENGTH_SHORT).show();
//				onLoad();
//				return;
//			}
//			Conn(pageLimit + 1);
//			isConn = true;
//		}
//	};
//
//	private void getByIdConn() {
//		// loadingView.setVisibility(View.VISIBLE);
//		StringBuffer sb = new StringBuffer();
//		sb.append("?taskId=");
//		sb.append(taskId);
//
//		Service.getService(Contanst.HTTP_GETBYIDTASK, null, sb.toString(),
//				ClockVoteActivity.this).addList(null).request(UrlParams.GET);
//	}
//
//	public void Conn(int currentPage) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("?currentPage=");
//		sb.append(currentPage);
//		sb.append("&pageLimit=");
//		sb.append(pageLimit);
//		sb.append("&userId=");
//		sb.append(userID);
//		sb.append("&jobId=");
//		sb.append(jobid);
//		Service.getService(Contanst.HTTP_GETVOTE, null, sb.toString(),
//				ClockVoteActivity.this).addList(null).request(UrlParams.GET);
//	}
//
//	@Override
//	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
//		// TODO Auto-generated method stub
//		Message msg = new Message();
//		msg.what = Contanst.BEST_INFO_OK;
//		msg.obj = bean;
//		msg.arg1 = connType;
//		mHandler.sendMessage(msg);
//	}
//
//	@Override
//	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
//		// TODO Auto-generated method stub
//		Message msg = new Message();
//		msg.what = Contanst.BEST_INFO_FAIL;
//		msg.obj = errmsg;
//		msg.arg1 = connType;
//		mHandler.sendMessage(msg);
//	}
//
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case Contanst.BEST_INFO_OK:
//
//				switch (msg.arg1) {
//
//				case Contanst.GETVOTE:
//					commentListBean = (CommentListBean) msg.obj;
//					// 刷新附近列表
//					if (isReflush) {
//						currentPage = 1;
//						newList.clear();
//						currentPage = commentListBean.getCurrentPage();
//						totalCount = commentListBean.getTotalPage();
//						totalPage = commentListBean.getTotalPage();
//						newList = commentListBean.getListData();
//
//						if (newList.size() == 0) {
//							onLoad();
//							vote_ProgressBar.setVisibility(View.GONE);
//							return;
//						}
//						adapter.setList(newList);
//						xListView.setPullLoadEnable(true);
//						adapter.notifyDataSetChanged();
//
//					} else {
//						currentPage++;
//						CommentListBean bean = (CommentListBean) msg.obj;
//						newList.addAll(commentListBean.getListData());
//						adapter.setList(newList);
//						adapter.notifyDataSetChanged();
//					}
//					onLoad();
//					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
//						xListView.setIsShow(false);
//					} else {
//						xListView.setIsShow(true);
//					}
//					break;
//				case Contanst.GETBYIDTASK:
//					byIdDetailBean = (ByIdDetailBean) msg.obj;
//					showTitle();
//					// SelectClockStateConn(null, null);//分页获取当前数据
//					Conn(1);
//					break;
//				}
//
//				break;
//			case Contanst.BEST_INFO_FAIL:
//				String message = (String) msg.obj;
//				Tools.getLog(Tools.d, "aaa", message);
//				break;
//			}
//		}
//	};
//
//	private void onLoad() {
//		xListView.stopRefresh();
//		xListView.stopLoadMore();
//		isConn = false;
//		isReflush = false;
//	}
//
//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
//		MobclickAgent.onResume(this); // 统计时长
//	}
//
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
//		// onPageEnd 在onPause
//		// 之前调用,因为 onPause 中会保存信息
//		MobclickAgent.onPause(this);
//	}
//
//	public void showTitle() {
//		if (byIdDetailBean != null) {
//			if(byIdDetailBean.getTitle()!= null)
//				group_info_frist_item_title.setText(byIdDetailBean.getTitle());
//			String description = byIdDetailBean.getDescription();
//			group_info_frist_item_content.setVisibility(View.GONE);
//			//			if (description != null && description.length() != 0) {
//			//				group_info_frist_item_content.setText(description);
//			//				group_info_frist_item_content.setVisibility(View.VISIBLE);
//			//			} else {
//			//				group_info_frist_item_content.setVisibility(View.GONE);
//			//			}
//		}
//	}
//
//	public void setRefresh() {
//		// TODO Auto-generated method stub
//		isConn = true;
//		isReflush = true;
//		Conn(1);
//	}
//}
