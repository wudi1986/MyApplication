package com.yktx.check.square.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.BaseActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.AttentionExpandableListAdapter;
import com.yktx.check.adapter.AttentionExpandableListAdapter.AttentionOtherTakeClock;
import com.yktx.check.adapter.AttentionExpandableListAdapter.TaskInfoOnClick;
import com.yktx.check.adapter.AttentionRecommendFragmentListViewAdapter;
import com.yktx.check.adapter.AttentionRecommendFragmentListViewAdapter.setFansTypeLisitener;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.RecommendFollowBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.UserFollowingBean;
import com.yktx.check.bean.UserFollowingJobBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.listener.IntoUserCenterListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView;
import com.yktx.check.listview.PinnedHeaderExpandableListView.IXListViewListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.setClickHeadViewlistener;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewOnGoHome;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

/**
 * 关注Fragment的界面
 * 
 * @author wudi
 */
public class AttentionFragment2 extends BaseFragment implements
ServiceListener, ExpandableListView.OnChildClickListener,
ExpandableListView.OnGroupClickListener, OnHeaderUpdateListener {

	PinnedHeaderExpandableListView expandablelist;
	boolean isConn, isReflush;
	String userID;
	// long send_time;
	ArrayList<UserFollowingJobBean> newList = new ArrayList<UserFollowingJobBean>(
			10);
	AttentionExpandableListAdapter adapter;
	AttentionRecommendFragmentListViewAdapter attentionRecommendFragmentListViewAdapter;
	RelativeLayout loadingView;
	// ImageView imageListNull;
	SharedPreferences settings;
	int mClickPosition;
	AddCommentDialog dialog;

	public static boolean isNewLoadAgain = true;
	private int newItem = 0;
	private XListView xListView;
	ArrayList<RecommendFollowBean> recommendFollowBeans = new ArrayList<RecommendFollowBean>();
	View view;
	private RelativeLayout building_dialog_Layout;
	private ImageView AnimImage;
	private TextView AnimContent, AnimVoteText;
	private ProgressBar attention_frament_ProgressBar;

	RelativeLayout attention_frament_Layout;
	QiQiuUtils qiQiuUtils;
	FrameLayout donghua;
	private Activity mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = AttentionFragment2.this.getActivity();
		settings = mContext.getSharedPreferences("clock",
				mContext.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		longitude = settings.getString("longitude", null);
		latitude = settings.getString("latitude", null);
		pageLimit = 3;
		// send_time = 0;
		if (!isConn) {
			isConn = true;
			isReflush = true;
			isNewLoadAgain = false;
			conn(1);
		}
		view = inflater.inflate(R.layout.attention_fragment, container,
				false);
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		// imageListNull = (ImageView) view.findViewById(R.id.imageListNull);
		// imageListNull.setImageResource(R.drawable.zhanwei_guanzhu_ziji);
		expandablelist = (PinnedHeaderExpandableListView) view
				.findViewById(R.id.expandablelist);
		attention_frament_ProgressBar = (ProgressBar) view.findViewById(R.id.attention_frament_ProgressBar);

		attention_frament_Layout  = (RelativeLayout) view.findViewById(R.id.attention_frament_Layout);
		donghua = new FrameLayout(mContext);
		attention_frament_Layout.addView(donghua);
		qiQiuUtils = new QiQiuUtils(donghua, mContext);

		expandablelist.setXListViewListener(listener);
		adapter = new AttentionExpandableListAdapter(
				AttentionFragment2.this.mContext,userID);
		adapter.setTaskInfoOnClick(taskInfoOnClick);
		adapter.setAttentionOtherTakeClock(attentionOtherTakeClock);
		expandablelist.setAdapter(adapter);
		// xListView.setIXListViewOnGoHome(iXListViewOnGoHome);
		expandablelist.setIsShow(true);
		expandablelist.setPullLoadEnable(true);
		// xListView.setBackGround(mContent.getResources().getColor(R.color.meibao_color_15));
		//		expandablelist.setOnHeaderUpdateListener(this);
		expandablelist.setOnChildClickListener(this);
		expandablelist.setOnGroupClickListener(this);
		expandablelist.setmFooterViewBackground(mContext.getResources().getColor(R.color.meibao_color_15));
		expandablelist.setClickHeadView(clickHeadViewlistener);
		building_dialog_Layout = (RelativeLayout) view.findViewById(R.id.building_dialog_Layout);
		AnimImage = (ImageView) view.findViewById(R.id.building_dialog_image);
		AnimContent = (TextView) view.findViewById(R.id.building_dialog_Text1);
		AnimVoteText = (TextView) view.findViewById(R.id.building_dialog_voteContent);
		expandablelist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					newItem = expandablelist.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		return view;
	}
	
	public void initXListView(){
		xListView = (XListView) view.findViewById(R.id.xListView);
		attentionRecommendFragmentListViewAdapter = 
				new AttentionRecommendFragmentListViewAdapter(mContext,userID,false);
		attentionRecommendFragmentListViewAdapter.thisetFansTypeLisitener(fansTypeLisitener);
		xListView.setXListViewListener(xListViewlistener);
		xListView.setAdapter(attentionRecommendFragmentListViewAdapter);
		//		xListView.setIXListViewOnGoHome(iXListViewOnGoHome);
		xListView.setIsShow(true);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		xListView.setVisibility(View.VISIBLE);

		//		xListView.setFooterBackground(getResources().getColor(R.color.meibao_color_15));
		xListView.setOnItemClickListener(null);
	}

	com.yktx.check.listview.XListView.IXListViewListener xListViewlistener = new 
			com.yktx.check.listview.XListView.IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			//			getMsgConn(1);
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
				// Toast.makeText(NewFragment.this.mContent, "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			//			getMsgConn(currentPage + 1);
			isConn = true;
		}
	};

	setClickHeadViewlistener clickHeadViewlistener = new setClickHeadViewlistener() {

		@Override
		public void clickHead(int position) {
			// TODO Auto-generated method stub
			//			Tools.getLog(Tools.d, "aaa", "点击的position：" + position
			//					+ " lastTwoJobBean =========== "
			//					+ lastTwoJobBean.getLastThrees().size());
			//			if (position == -1) {
			//				if (byIdDetailBean.getBuildingId() != null) {
			//					Intent in = new Intent(mContext,
			//							ClockGroupInfoActivity.class);
			//					in.putExtra("buildingId", byIdDetailBean.getBuildingId());
			//					mContext.startActivity(in);
			//				}
			//			}
			return;
		}
	};

	int thisgroupPosition,thischildPosition;
	TaskInfoOnClick taskInfoOnClick = new TaskInfoOnClick() {
		// 添加评论

		@Override
		public void clickComment(final TaskItemBean taskItemBean, int groupPosition,
				int childPosition, int itemBeanIndex) {
			// TODO Auto-generated method stub
			//			mClickPosition = position;
			if(isConn){
				return;
			}
			isConn = true;
			thisgroupPosition = groupPosition;
			thischildPosition = childPosition;
			dialog = new AddCommentDialog(mContext, null);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {

				@Override
				public void onClickSuccess(String content) {
					// TODO Auto-generated method stub
					addComentConn(taskItemBean.getJobId(), content);
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

		// 添加点赞
		@Override
		public void clickVote(String jobid, int groupPosition,
				int childPosition, String type,int x, int y) {
			// TODO Auto-generated method stub
			if(isConn){
				return;
			}
			isConn = true;

			thisgroupPosition = groupPosition;
			thischildPosition = childPosition;
			//			mClickPosition = position;
			qiQiuUtils.startFly((int)(x+6*BaseActivity.DENSITY), (int)(y-54*BaseActivity.DENSITY));
			if (type.equals("0")) {
				addVoteConn(jobid);
			} else {
				isConn = false;
				AnimImage
				.setImageResource(R.drawable.guangchang_bd_daqichenggong);
				AnimContent.setText("你已经为Ta打气加油过了哦！");
				//				AnimVoteText.setText("你将得到气球 +1");
				AnimVoteText.setVisibility(View.GONE);
				animAlertStart();
			}
		}
	};

	public void addComentConn(String jobid, String text) {
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
				AttentionFragment2.this).addList(params)
				.request(UrlParams.POST);
	}

	// 添加点赞
	public void addVoteConn(String jobid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId", userID));
			params.add(new BasicNameValuePair("jobId", jobid));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATEVOTE, null, null,
				AttentionFragment2.this).addList(params)
				.request(UrlParams.POST);
	}

	// 取消点赞
	public void CancelVoteConn(String jobid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId", userID));
			params.add(new BasicNameValuePair("jobId", jobid));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CANCELVOTE, null, null,
				AttentionFragment2.this).addList(params)
				.request(UrlParams.POST);
	}

	public void connAttentionTakeClock(String OtherUserId){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId", OtherUserId));
			params.add(new BasicNameValuePair("curUserId", userID));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_MESSAGE_REMIND, null, null,
				AttentionFragment2.this).addList(params)
				.request(UrlParams.POST);
		Tools.getLog(Tools.d, "ccc", "params======"+params.toString());
	}

	private void conn(int currentPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		Tools.getLog(Tools.d, "ccc", "第：" + newItem + "====");
		// 判断加载多少条
		if (isNewLoadAgain) {
			int LoadNum = 0;
			if (newItem < 3) {
				LoadNum = 3;
			} else {
				LoadNum = newItem / 3 * 3 + 3;
			}
			sb.append(LoadNum);
		} else {
			Tools.getLog(Tools.d, "ccc", "加载" + 3 + "====");
			sb.append(pageLimit);
		}
		Tools.getLog(Tools.d, "ccc", "加载" + newItem + "====");
		sb.append("&userId=");
		sb.append(userID);
		Service.getService(Contanst.HTTP_USER_GETFOLLOWINGJOB, null,
				sb.toString(), AttentionFragment2.this).addList(null)
				.request(UrlParams.GET);
	}

	public void connRecommendFollow(){
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(userID);

		Service.getService(Contanst.HTTP_USER_GETRECOMMENDFOLLOW, null,
				sb.toString(), AttentionFragment2.this).addList(null)
				.request(UrlParams.GET);
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
				null, AttentionFragment2.this)
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
				null, AttentionFragment2.this)
				.addList(params).request(UrlParams.POST);
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
			Tools.getLog(Tools.i, "ccc", "加载中的的totalCount：====" + totalCount);
			Tools.getLog(Tools.i, "ccc", "11111加载中的的currentPage：===="
					+ currentPage);
			if (currentPage * 3 >= totalCount) {
				// Toast.makeText(NewFragment.this.mContent, "亲，没有更多信息了",
				// Toast.LENGTH_SHORT).show();
				onLoad();
				return;
			}
			conn(currentPage + 1);
			Tools.getLog(Tools.i, "ccc", "22222加载中的的currentPage：===="
					+ currentPage);
			isConn = true;
		}
	};

	IntoUserCenterListener intoUserCenter = new IntoUserCenterListener() {

		@Override
		public void getIntoUserCenter(String userID) {
			// TODO Auto-generated method stub
			// Intent in = new Intent(NewFragment.this.mContent,
			// UserCenterActivity.class);
			// in.putExtra("userID", userID);
			// NewFragment.this.startActivity(in);
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
	String fansName;
	setFansTypeLisitener fansTypeLisitener = new setFansTypeLisitener() {

		@Override
		public void setFansType(int position, boolean isfan) {
			// TODO Auto-generated method stub
			//			if(isConn){
			//				return;
			//			}
			//			isConn = true;
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
	AttentionOtherTakeClock attentionOtherTakeClock = new AttentionOtherTakeClock() {

		@Override
		public void setAttention(String userid,String name) {
			// TODO Auto-generated method stub
			connAttentionTakeClock(userid);
			fansName = name;
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

				if (msg.arg1 == Contanst.USER_GETFOLLOWINGJOB) {

					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						newList.clear();
						UserFollowingBean bean = (UserFollowingBean) msg.obj;
						currentPage = bean.getCurrentPage();
						totalCount = bean.getTotalCount();
						totalPage = bean.getTotalPage();
						newList = bean.getListData();
						if (newList.size() == 0) {
							// imageListNull.setVisibility(View.VISIBLE);
							//							onLoad();
							attention_frament_ProgressBar.setVisibility(View.GONE);
							expandablelist.setVisibility(View.GONE);
							initXListView();
							connRecommendFollow();
							//							expandablelist.setPullLoadEnable(false);
							//							adapter.notifyDataSetChanged();
							return;
						}
						// imageListNull.setVisibility(View.GONE);
						setExpandableList();
						expandablelist.setPullLoadEnable(true);
						adapter.setTixingPosition(-1);
						adapter.notifyDataSetChanged();

					} else {
						currentPage++;
						UserFollowingBean bean = (UserFollowingBean) msg.obj;
						newList.addAll(bean.getListData());
						Tools.getLog(Tools.d,"kkk", "newList ============ " + newList.size());
						setExpandableList();
					}
					onLoad();
					if (totalCount <= 3 || currentPage * 3 >= totalCount) {
						expandablelist.setIsShow(false);
					} else {
						expandablelist.setIsShow(true);
					}
					// 判断是否为本页点入building 页
					// Tools.getLog(Tools.d,
					// "aaa","type："+GroupMainFragmentActivity.ReflushItem+"，页数："+newItem);
					// if(GroupMainFragmentActivity.ReflushItem == 1){
					// xListView.setSelection(newItem);
					// }
					isNewLoadAgain = false;// 刷新成功就不可以刷
				} else if (msg.arg1 == Contanst.CREATECOMMENT) {
					isConn = false;
					ArrayList<CommentsBean> commentsBeans = (ArrayList<CommentsBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"评论返回:" + commentsBeans.toString());
					//					 TaskItemBean itemBean = newList.get(mClickPosition);
					final TaskItemBean taskItemBean = newList.get(thisgroupPosition)
							.getJobs().get(thischildPosition);
					newList.get(thisgroupPosition).getJobs().get(thischildPosition).setCommentCount(
							taskItemBean.getCommentCount() + 1);
					//					 newList.get(mClickPosition).setComments(commentsBeans);
					//					 Tools.getLog(Tools.d, "aaa",
					//					 "评论数量:"
					//					 + newList.get(mClickPosition)
					//					 .getCommentCount());

					AnimImage
					.setImageResource(R.drawable.guangchang_bd_pinglun);
					AnimContent.setText("评论成功!");
					AnimVoteText.setText("你将得到气球 +2");
					AnimVoteText.setVisibility(View.VISIBLE);
					animAlertStart();
					setExpandableList();
				} else if (msg.arg1 == Contanst.CREATEVOTE) {
					isConn = false;
					ArrayList<VotesBean> votesBeans = (ArrayList<VotesBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"点赞成功返回:" + votesBeans.toString());
					final TaskItemBean taskItemBean = newList.get(thisgroupPosition)
							.getJobs().get(thischildPosition);
					newList.get(thisgroupPosition).getJobs().get(thischildPosition).setVoteCount(
							(taskItemBean.getVoteCount() + 1));
					newList.get(thisgroupPosition).getJobs().get(thischildPosition).setVoted("1");
					newList.get(thisgroupPosition).getJobs().get(thischildPosition).setVotes(votesBeans);
					if(!newList.get(thisgroupPosition).getUserId().equals(userID)){
						AnimImage
						.setImageResource(R.drawable.guangchang_bd_daqichenggong);
						AnimContent.setText("打气成功!");
						AnimVoteText.setText("你将得到气球 +1");
						AnimVoteText.setVisibility(View.VISIBLE);
						animAlertStart();
					}


					setExpandableList();
				} else if (msg.arg1 == Contanst.CANCELVOTE) {
					// 取消点赞
					isConn = false;
					final TaskItemBean taskItemBean = newList.get(thisgroupPosition)
							.getJobs().get(thischildPosition);
					newList.get(thisgroupPosition).getJobs().get(thischildPosition).setVoteCount(
							taskItemBean.getVoteCount() - 1);
					newList.get(thisgroupPosition).getJobs().get(thischildPosition).setVoted("0");
					setExpandableList();
				}else if(msg.arg1 == Contanst.USER_GETRECOMMENDFOLLOW){
					recommendFollowBeans = (ArrayList<RecommendFollowBean>) msg.obj;
					attentionRecommendFragmentListViewAdapter.setList(recommendFollowBeans);
					xListView.setAdapter(attentionRecommendFragmentListViewAdapter);
					if (loadingView.getVisibility() != View.GONE) {
						loadingView.setVisibility(View.GONE);
					}
				}else if(msg.arg1 == Contanst.USER_FOLLOW){
					Toast.makeText(mContext, "加入关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = false;
					attentionRecommendFragmentListViewAdapter.setList(recommendFollowBeans);
					attentionRecommendFragmentListViewAdapter.notifyDataSetChanged();
				}else if(msg.arg1 == Contanst.USER_UNFOLLOW){
					Toast.makeText(mContext, "取消关注,"+fansName, Toast.LENGTH_SHORT).show();
					isConn = false;
					attentionRecommendFragmentListViewAdapter.setList(recommendFollowBeans);
					attentionRecommendFragmentListViewAdapter.notifyDataSetChanged();
				}else if(msg.arg1 == Contanst.MESSAGE_REMIND){
					Toast.makeText(mContext, "提醒"+ fansName+"打卡成功!", Toast.LENGTH_SHORT).show();
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				if (msg.arg1 == Contanst.GETNEWEST) {
					expandablelist.setPullLoadEnable(false);
					adapter.notifyDataSetChanged();
					String message = (String) msg.obj;
					Tools.getLog(Tools.i, "aaa", "message = " + message);
					onLoad();
				}
				break;
			}
		}

	};

	public void setExpandableList() {
		adapter.setList(newList);

		// 这句话不能注释掉，设置数据要不expandableListView 不能展开
		adapter.notifyDataSetChanged();

		for (int i = 0, count = expandablelist.getCount(); i < count; i++) {
			expandablelist.expandGroup(i);
		}

		Tools.getLog(
				Tools.i,
				"aaa",
				"expandablelist myExpandableListAdapter  ======== "
						+ adapter.getGroupCount());
		adapter.notifyDataSetChanged();
	}

	private void onLoad() {
		if (loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
		expandablelist.stopRefresh();
		expandablelist.stopLoadMore();
		isConn = false;
		isReflush = false;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Tools.getLog(Tools.d, "ccc", "=========最新：返回了啊！！！！！=========");
		Tools.getLog(Tools.d, "aaa",
				"===================11111====================");
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
		if (isNewLoadAgain) {
			Tools.getLog(Tools.d, "aaa",
					"===================22222====================");
			isConn = true;
			isReflush = true;
			conn(1);
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}

	@Override
	public View getPinnedHeader() {
		// TODO Auto-generated method stub
		View headerView = (ViewGroup) mContext.getLayoutInflater()
				.inflate(R.layout.group_attention, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		Tools.getLog(Tools.d,"kkk", "getPinnedHeader");
		return headerView;
	}

	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageOnFail(R.anim.loading_image_animation)
	.showImageOnLoading(null).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(200))
	.cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	// .displayer(new RoundedBitmapDisplayer(4))
	.build();

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		if (firstVisibleGroupPos < 0) {
			return;
		}


		UserFollowingJobBean firstVisibleGroup = (UserFollowingJobBean) adapter
				.getGroup(firstVisibleGroupPos);
		LinearLayout groupLayout = (LinearLayout) headerView
				.findViewById(R.id.groupLayout);
		groupLayout.setBackgroundResource(R.drawable.kongtu);
		ImageView header = (ImageView) headerView
				.findViewById(R.id.userHeadImage);
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(firstVisibleGroup.getImageSource())
				+ firstVisibleGroup.getAvartar_path()+(firstVisibleGroup.getImageSource() == 2?"?imageMogr2/thumbnail/70x70":""), header,
				headOptions);
		TextView textView = (TextView) headerView.findViewById(R.id.userName);
		TextView textView1 = (TextView) headerView.findViewById(R.id.day);
		textView.setVisibility(View.GONE);
		textView1.setVisibility(View.GONE);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		// TODO Auto-generated method stub
		return false;
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
