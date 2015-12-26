package com.yktx.check.square.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.AttentionFragmentListViewAdapter;
import com.yktx.check.adapter.AttentionFragmentListViewAdapter.BuildingOnClick;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.listener.IntoUserCenterListener;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.listview.XListView.IXListViewOnGoHome;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

/**
 * 关注Fragment的界面
 * 
 * @author wudi
 */
public class AttentionFragment extends BaseFragment implements ServiceListener {

	XListView xListView;
	boolean isConn, isReflush;
	String userID;
	//	long send_time;
	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	AttentionFragmentListViewAdapter adapter;
	RelativeLayout loadingView;
	//	ImageView imageListNull;
	SharedPreferences settings;
	int mClickPosition;
	AddCommentDialog dialog;

	public static  boolean isNewLoadAgain = true;
	private boolean isNewVisity = false;
	private int newItem = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		settings = getActivity().getSharedPreferences("clock", getActivity().MODE_PRIVATE);
		userID = settings.getString("userid", null);
		longitude = settings.getString("longitude", null);
		latitude = settings.getString("latitude", null);
		//		send_time = 0;
		Tools.getLog(Tools.d, "aaa", "===================11111====================");
		if (!isConn) {
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			isNewLoadAgain = false;
			conn(1);
		}
		View view = inflater.inflate(R.layout.hot_or_near_activity, container,
				false);
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		//		imageListNull = (ImageView) view.findViewById(R.id.imageListNull);
		//		imageListNull.setImageResource(R.drawable.zhanwei_guanzhu_ziji);
		xListView = (XListView) view.findViewById(R.id.xListView);
		xListView.setXListViewListener(listener);
		adapter = new AttentionFragmentListViewAdapter(AttentionFragment.this.getActivity(),false,userID,1);
		adapter.setIntoUserCenter(intoUserCenter);
//				adapter.setBuildingOnClick(buildingOnClick);
//		adapter.setOnNewFragmentItemClick(itemClick);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
		swingBottomInAnimationAdapter.setAbsListView(xListView);


		xListView.setAdapter(swingBottomInAnimationAdapter);
		// xListView.setIXListViewOnGoHome(iXListViewOnGoHome);
		xListView.setIsShow(true);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		//		xListView.setBackGround(getActivity().getResources().getColor(R.color.meibao_color_15));
		xListView.setFooterBackground(getActivity().getResources().getColor(R.color.meibao_color_15));
		xListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					newItem = xListView.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		return view;
	}


//	OnNewFragmentItemClick itemClick = new OnNewFragmentItemClick() {
//
//		@Override
//		public void itemClick(int position) {
//			// TODO Auto-generated method stub
//			//			GroupMainFragmentActivity.ReflushItem = 1;  
//			//			newItem = position;
//			isNewLoadAgain = true;
//			//			GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
//			TaskItemBean bean = newList.get(position);
//			Intent in = new Intent(getActivity(), ClockGroupInfoActivity.class);
//			in.putExtra("buildingId", bean.getBuildingId());
//			getActivity().startActivity(in);
//		}
//	};
//		BuildingOnClick buildingOnClick = new BuildingOnClick() {
//			//添加点赞
//			@Override
//			public void clickVote(String jobid, int position, String type) {
//				// TODO Auto-generated method stub
//				mClickPosition = position;
//				if(type.equals("0")){
//					addVoteConn(jobid);
//				}else{
//					CancelVoteConn(jobid);
//				}
//			}
//			//添加评论
//			@Override
//			public void clickComment(final String jobid, int position) {
//				// TODO Auto-generated method stub
//				mClickPosition = position;
//				dialog = new AddCommentDialog(getActivity(),null);
//				dialog.setCanceledOnTouchOutside(true);
//				dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {
//	
//					@Override
//					public void onClickSuccess(String content) {
//						// TODO Auto-generated method stub
//						addComentConn(jobid, content);
//					}
//				});
//				dialog.show();
//			}
//		}; 
	
		public void addComentConn(String jobid,String text){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("userId", userID));
				params.add(new BasicNameValuePair("text", text));
				params.add(new BasicNameValuePair("jobId", jobid));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Service.getService(Contanst.HTTP_CREATECOMMENT, null, null,
					AttentionFragment.this).addList(params).request(UrlParams.POST);
		}
		//添加点赞
		public void addVoteConn(String jobid){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("userId", userID));
				params.add(new BasicNameValuePair("jobId", jobid));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Service.getService(Contanst.HTTP_CREATEVOTE, null, null,
					AttentionFragment.this).addList(params).request(UrlParams.POST);
		}
		//取消点赞
		public void CancelVoteConn(String jobid){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("userId", userID));
				params.add(new BasicNameValuePair("jobId", jobid));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Service.getService(Contanst.HTTP_CANCELVOTE, null, null,
					AttentionFragment.this).addList(params).request(UrlParams.POST);
		}
	private void conn(int currentPage) {
		StringBuffer sb  = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		Tools.getLog(Tools.d, "ccc", "第："+newItem+"====");
		//判断加载多少条
		if(isNewLoadAgain){
			int LoadNum = 0;
			if(newItem <10){
				LoadNum = 10;
			}else{
				LoadNum = newItem/10*10+10;
			}
			sb.append(LoadNum);
		}else{
			Tools.getLog(Tools.d, "ccc", "加载"+10+"====");
			sb.append(pageLimit);
		}
		Tools.getLog(Tools.d, "ccc", "加载"+newItem+"====");
		sb.append("&userId=");
		sb.append(userID);
		sb.append("&curUserId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_GETBUILDINGFOLLOWING, null,
				sb.toString(), AttentionFragment.this)
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
			Tools.getLog(Tools.i, "ccc","加载中的的totalCount：===="+totalCount);
			Tools.getLog(Tools.i, "ccc","11111加载中的的currentPage：===="+currentPage);
			if (currentPage * 10 >= totalCount) {
				// Toast.makeText(NewFragment.this.getActivity(), "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			conn(currentPage + 1);
			Tools.getLog(Tools.i, "ccc","22222加载中的的currentPage：===="+currentPage);
			isConn = true;
		}
	};

	IntoUserCenterListener intoUserCenter = new IntoUserCenterListener() {

		@Override
		public void getIntoUserCenter(String userID) {
			// TODO Auto-generated method stub
			//			Intent in = new Intent(NewFragment.this.getActivity(),
			//					UserCenterActivity.class);
			//			in.putExtra("userID", userID);
			//			NewFragment.this.startActivity(in);
		}
	};

	IXListViewOnGoHome iXListViewOnGoHome = new IXListViewOnGoHome() {

		@Override
		public void onGoHome() {
			// TODO Auto-generated method stub
			if (onGoHomeListener != null) {
				onGoHomeListener.goHome();
			}
			onLoad();

		}
	};

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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:

				if (msg.arg1 == Contanst.USER_GETBUILDINGFOLLOWING) {

					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						newList.clear();
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						currentPage = bean.getCurrentPage();
						totalCount = bean.getTotalCount();
						totalPage = bean.getTotalPage();
						newList = bean.getListData();
						if (newList.size() == 0) {
							//							imageListNull.setVisibility(View.VISIBLE);
							onLoad();
							xListView.setFooterBackground(getActivity().getResources().getColor(R.color.white));
							xListView.setPullLoadEnable(false);
							adapter.notifyDataSetChanged();
							return;
						}
						//						imageListNull.setVisibility(View.GONE);
						adapter.setList(newList);
						adapter.setDistance(latitude, longitude);
						xListView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();

					} else {
						currentPage++;
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						newList.addAll(bean.getListData());
						adapter.setList(newList);
						adapter.setDistance(latitude, longitude);
						adapter.notifyDataSetChanged();
					}
					xListView.setFooterBackground(0xfffafafa);
					onLoad();
					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
						xListView.setIsShow(false);
					} else {
						xListView.setIsShow(true);
					}
					//判断是否为本页点入building 页
					//					Tools.getLog(Tools.d, "aaa","type："+GroupMainFragmentActivity.ReflushItem+"，页数："+newItem);
					//					if(GroupMainFragmentActivity.ReflushItem == 1){
					//						xListView.setSelection(newItem);
					//					}
					isNewLoadAgain = false;//刷新成功就不可以刷  
				}else if(msg.arg1 == Contanst.CREATECOMMENT){
					ArrayList<CommentsBean> commentsBeans = (ArrayList<CommentsBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa", "评论返回:"+commentsBeans.toString());
					TaskItemBean itemBean = newList.get(mClickPosition);
					newList.get(mClickPosition).setCommentCount(itemBean.getCommentCount()+1);
					newList.get(mClickPosition).setComments(commentsBeans);
					Tools.getLog(Tools.d, "aaa", "评论数量:"+newList.get(mClickPosition).getCommentCount());
					adapter.setList(newList);
					adapter.notifyDataSetChanged();
				}else if(msg.arg1 == Contanst.CREATEVOTE){
					ArrayList<VotesBean> votesBeans = (ArrayList<VotesBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa", "点赞成功返回:"+votesBeans.toString());
					TaskItemBean itemBean = newList.get(mClickPosition);
					newList.get(mClickPosition).setVoteCount((itemBean.getVoteCount()+1));
					newList.get(mClickPosition).setVoted("1");
					newList.get(mClickPosition).setVotes(votesBeans);
					adapter.setList(newList);
					adapter.notifyDataSetChanged();
				}else if(msg.arg1 == Contanst.CANCELVOTE){
					//取消点赞
					TaskItemBean itemBean = newList.get(mClickPosition);
					for(int i = 0;i<itemBean.getVotes().size();i++){
						if(itemBean.getVotes().get(i).getUserId().equals(userID)){
							itemBean.getVotes().remove(i);
						}
					}
					newList.get(mClickPosition).setVoteCount(itemBean.getVoteCount()-1);
					newList.get(mClickPosition).setVoted("0");
					adapter.setList(newList);
					adapter.notifyDataSetChanged();
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				if (msg.arg1 == Contanst.GETNEWEST) {
					xListView.setAdapter(adapter);
					xListView.setPullLoadEnable(false);
					adapter.notifyDataSetChanged();
					String message = (String) msg.obj;
					Tools.getLog(Tools.i, "aaa", "message = " + message);
					onLoad();
				}
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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		isNewVisity = isVisibleToUser;
		//		if (isNewLoadAgain||isNewVisity) {
		//			Tools.getLog(Tools.d, "aaa", "===================22222====================");
		//			isConn = true;
		//			isReflush = true;
		//			conn(1, 0);
		//		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Tools.getLog(Tools.d, "ccc", "=========最新：返回了啊！！！！！=========");
		Tools.getLog(Tools.d, "aaa", "===================11111====================");
		MobclickAgent.onPageStart("MainScreen"); //统计页面
		if (isNewLoadAgain) {
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			conn(1);
		}
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
}
