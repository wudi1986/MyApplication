package com.yktx.check.square.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.NewFragmentAdapter;
import com.yktx.check.adapter.NewFragmentAdapter.BuildingIsHaveImage;
import com.yktx.check.adapter.NewFragmentAdapter.BuildingOnClick;
import com.yktx.check.adapter.NewFragmentListViewAdapter.OnNewFragmentItemClick;
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
 * 最新Fragment的界面
 * 
 * @author wudi
 */
public class NewFragment extends BaseFragment implements ServiceListener {

	XListView xListView;
	boolean isConn, isReflush;
	String userID;
	long send_time;
	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	NewFragmentAdapter adapter;
	RelativeLayout loadingView,main_upLayout,building_dialog_Layout;
	//	ImageView imageListNull;
	SharedPreferences settings;
	int mClickPosition;
	AddCommentDialog dialog;

	public static  boolean isNewLoadAgain = true;
	private boolean isNewVisity = false;
	private int newItem = 0;
	private ProgressBar hot_or_near_ProgressBar;

	QiQiuUtils qiQiuUtils;
	FrameLayout donghua;

	private Activity mContext;

	private ImageView AnimImage;

	private TextView AnimContent, AnimVoteText;

	int type = 0;// 有没有图 0 无图（默认） 1 有图

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = NewFragment.this.getActivity();
		settings = getActivity().getSharedPreferences("clock", getActivity().MODE_PRIVATE);
		userID = settings.getString("userid", null);
		longitude = settings.getString("longitude", null);
		latitude = settings.getString("latitude", null);
		send_time = 0;
		if (!isConn) {
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			isNewLoadAgain = false;
			conn(1,0);
		}
		View view = inflater.inflate(R.layout.new_fragment, container,
				false);
		hot_or_near_ProgressBar = (ProgressBar) view.findViewById(R.id.hot_or_near_ProgressBar);
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);

		main_upLayout = (RelativeLayout) view.findViewById(R.id.main_upLayout);
		donghua = new FrameLayout(mContext);
		main_upLayout.addView(donghua);
		qiQiuUtils = new QiQiuUtils(donghua, mContext);

		xListView = (XListView) view.findViewById(R.id.xListView);
		xListView.setXListViewListener(listener);
		adapter = new NewFragmentAdapter(NewFragment.this.getActivity(),userID);
		adapter.setIntoUserCenter(intoUserCenter);
		adapter.setBuildingOnClick(buildingOnClick);
		adapter.setBuildingIsHaveImage(buildingIsHaveImage);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
		swingBottomInAnimationAdapter.setAbsListView(xListView);

		building_dialog_Layout = (RelativeLayout) view.findViewById(R.id.building_dialog_Layout);
		AnimImage = (ImageView) view.findViewById(R.id.building_dialog_image);
		AnimContent = (TextView) view.findViewById(R.id.building_dialog_Text1);
		AnimVoteText = (TextView) view.findViewById(R.id.building_dialog_voteContent);


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

	BuildingIsHaveImage buildingIsHaveImage = new BuildingIsHaveImage() {

		@Override
		public void clickType(int types) {
			// TODO Auto-generated method stub
			if(isConn || type == types){
				return;
			}
			if(loadingView.getVisibility() == View.GONE){
				loadingView.setVisibility(View.VISIBLE);
			}

			type = types;
			Tools.getLog(Tools.d, "aaa", "===================22222====================");
			isConn = true;
			isReflush = true;
			isNewLoadAgain = false;
			conn(1,0);

		}
	};

	BuildingOnClick buildingOnClick = new BuildingOnClick() {

		@Override
		public void clickVote(String jobid, int position, String type, int x, int y) {
			// TODO Auto-generated method stub

			qiQiuUtils.startFly((int)(x+6*BaseActivity.DENSITY), (int)(y-54*BaseActivity.DENSITY));
			if(isConn){
				return;
			}
			isConn = true;
			mClickPosition = position;

			if(type.equals("0")){
				addVoteConn(jobid);
			}else{
				AnimImage
				.setImageResource(R.drawable.guangchang_bd_daqichenggong);
				AnimContent.setText("你已经为Ta打气加油过了哦！");
				//				AnimVoteText.setText("你将得到气球 +1");
				AnimVoteText.setVisibility(View.GONE);
				animAlertStart();
				isConn = false;
			}
		}

		@Override
		public void clickComment(final TaskItemBean taskItemBean, int position,
				final int itemBeanIndex) {
			// TODO Auto-generated method stub
			if(isConn){
				return;
			}
			isConn = true;
			mClickPosition = position;
			String name = null;
			if (itemBeanIndex != -1) {
				name = taskItemBean.getComments().get(itemBeanIndex).getName();
			}
			dialog = new AddCommentDialog(mContext, name);
			dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {

				@Override
				public void onClickSuccess(String content) {
					// TODO Auto-generated method stub
					addComentConn(taskItemBean, content, itemBeanIndex);
				}
			});
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					isConn = false;
				}
			});
			dialog.show();
		}
	}; 

	OnNewFragmentItemClick itemClick = new OnNewFragmentItemClick() {

		@Override
		public void itemClick(int position) {
			// TODO Auto-generated method stub
			//			GroupMainFragmentActivity.ReflushItem = 1;  
			//			newItem = position;
			isNewLoadAgain = true;
			//			GroupMainFragmentActivity.isReflush = false;//让主activity 刷新fragment
			TaskItemBean bean = newList.get(position);
			Intent in = new Intent(getActivity(), ClockGroupInfoFragmentActivity.class);
			in.putExtra("buildingId", bean.getBuildingId());
			getActivity().startActivity(in);
		}
	};

	// 添加评论
	public void addComentConn(TaskItemBean taskItemBean, String text,
			int itemBeanIndex) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("jobId", taskItemBean.getJobId()));
			params.add(new BasicNameValuePair("userId", userID));
			if (itemBeanIndex == -1) {
				params.add(new BasicNameValuePair("type", "1"));
				params.add(new BasicNameValuePair("pCommentId", "-1"));
				params.add(new BasicNameValuePair("repliedUserId ",
						taskItemBean.getUserId()));
			} else {
				params.add(new BasicNameValuePair("type", "2"));
				params.add(new BasicNameValuePair("pCommentId", taskItemBean
						.getComments().get(itemBeanIndex).getCommentId()));
				params.add(new BasicNameValuePair("repliedUserId", taskItemBean
						.getComments().get(itemBeanIndex).getUserId()));
			}

			params.add(new BasicNameValuePair("text", text));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATECOMMENT, null, null,
				NewFragment.this).addList(params)
				.request(UrlParams.POST);
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
				NewFragment.this).addList(params).request(UrlParams.POST);
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
				NewFragment.this).addList(params).request(UrlParams.POST);
	}
	private void conn(int currentPage, long send_time) {
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
		sb.append("&withPicFlag=");
		sb.append(type+"");
		Service.getService(Contanst.HTTP_BUILDING_VIEWTODAYCARD, null,
				sb.toString(), NewFragment.this)
				.addList(null).request(UrlParams.GET);
	}

	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			conn(1, 0);
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
			conn(currentPage + 1, send_time);
			Tools.getLog(Tools.i, "ccc","加载中的的currentPage：===="+currentPage);
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

				if (msg.arg1 == Contanst.BUILDING_VIEWTODAYCARD) {

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

							hot_or_near_ProgressBar.setVisibility(View.GONE);
							return;
						}
						//						imageListNull.setVisibility(View.GONE);
						send_time = newList.get(newList.size()-1).getCheck_time();
						adapter.setList(newList,type);
						//						adapter.setDistance(latitude, longitude);
						xListView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();

					} else {
						currentPage++;
						Tools.getLog(Tools.i, "ccc","联网中的currentPage：===="+currentPage);
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						newList.addAll(bean.getListData());
						adapter.setList(newList,type);
						send_time = newList.get(newList.size()-1).getCheck_time ();
						//						adapter.setDistance(latitude, longitude);
						adapter.notifyDataSetChanged();
					}

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

					AnimImage
					.setImageResource(R.drawable.guangchang_bd_pinglun);
					AnimContent.setText("评论成功!");
					AnimVoteText.setText("你将得到气球 +2");
					AnimVoteText.setVisibility(View.VISIBLE);
					animAlertStart();
					adapter.setList(newList,type);
					adapter.notifyDataSetChanged();
					isConn = false; 
				}else if(msg.arg1 == Contanst.CREATEVOTE){
					isConn = false;
					ArrayList<VotesBean> votesBeans = (ArrayList<VotesBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa", "点赞成功返回:"+votesBeans.toString());
					TaskItemBean itemBean = newList.get(mClickPosition);
					newList.get(mClickPosition).setVoteCount((itemBean.getVoteCount()+1));
					newList.get(mClickPosition).setVoted("1");
					newList.get(mClickPosition).setVotes(votesBeans);
					if(!itemBean.getUserId().equals(userID)){
						AnimImage
						.setImageResource(R.drawable.guangchang_bd_daqichenggong);
						AnimContent.setText("打气成功!");
						AnimVoteText.setText("你将得到气球 +1");
						AnimVoteText.setVisibility(View.VISIBLE);
						animAlertStart();
					}

					adapter.setList(newList,type);
					adapter.notifyDataSetChanged();
					isConn = false; 
				}else if(msg.arg1 == Contanst.CANCELVOTE){
					isConn = false;
					//取消点赞
					TaskItemBean itemBean = newList.get(mClickPosition);
					for(int i = 0;i<itemBean.getVotes().size();i++){
						if(itemBean.getVotes().get(i).getUserId().equals(userID)){
							itemBean.getVotes().remove(i);
						}
					}
					newList.get(mClickPosition).setVoteCount(itemBean.getVoteCount()-1);
					newList.get(mClickPosition).setVoted("0");
					adapter.setList(newList,type);
					adapter.notifyDataSetChanged();
					isConn = false; 
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
		Tools.getLog(Tools.d, "aaa", "===================22222====================");
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

			isConn = true;
			isReflush = true;
			conn(1, 0);
		}
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
	public void animAlertStart() {
		Tools.getLog(Tools.i, "aaa", "开始动画：");
		int height = building_dialog_Layout.getHeight();
		Tools.getLog(Tools.i, "aaa", "animAlertStart height ============= "
				+ height);
		TranslateAnimation animationStart = new TranslateAnimation(0, 0,
				height, 0);

		animationStart.setDuration(500L);// 设置动画持续时间
		building_dialog_Layout.startAnimation(animationStart);
		animationStart.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				building_dialog_Layout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				building_dialog_Layout.setVisibility(View.VISIBLE);
				Tools.getLog(Tools.i, "aaa", "onAnimationEnd");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int height = building_dialog_Layout.getHeight();
						Animation mAnimation = new TranslateAnimation(0, 0, 0,
								height);
						mAnimation.setDuration(250L);
						// building_dialog_Layout.setAnimation(mAnimation);
						building_dialog_Layout.startAnimation(mAnimation);

						mAnimation
						.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.i, "aaa",
										"onAnimationStartonAnimationStartonAnimationStartonAnimationStart");
								building_dialog_Layout
								.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.i, "aaa",
										"onAnimationEndonAnimationEnd");

								building_dialog_Layout
								.setVisibility(View.GONE);

							}
						});
					}
				}, 3000L);

			}
		});

	}
}
