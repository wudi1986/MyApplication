package com.yktx.check.taskinfo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.LayoutParams;

import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMainActivity;
import com.yktx.check.ClockSetActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.MyexpandableListAdapter;
import com.yktx.check.adapter.MyexpandableListAdapter.SharedThisJob;
import com.yktx.check.adapter.MyexpandableListAdapter.TaskInfoOnClick;
import com.yktx.check.adapter.MyexpandableListAdapter.giveUpFlagClick;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.Group;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.dialog.GiveUpJobDialog;
import com.yktx.check.dialog.TaskInfoDialog;
import com.yktx.check.dialog.TaskInfoDialog.onCLickClockSuccess;
import com.yktx.check.listview.PinnedHeaderExpandableListView;
import com.yktx.check.listview.PinnedHeaderExpandableListView.IXListViewListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.setClickHeadViewlistener;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.BaseFragment;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

@SuppressLint("ValidFragment")
public class TaskInfoFragment extends BaseFragment implements ServiceListener,ExpandableListView.OnChildClickListener,
ExpandableListView.OnGroupClickListener, OnHeaderUpdateListener{
	TaskInfoActivity taskInfoActivity;
	View view;
	boolean isConn, isReflush = true;
	
	private ByTaskIdBean byTaskIdBean;
	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	private ArrayList<ByDateBean> byDateBeanList = new ArrayList<ByDateBean>();
	private  ByIdDetailBean byIdDetailBean;
	

	private PinnedHeaderExpandableListView expandablelist;
	private MyexpandableListAdapter myExpandableListAdapter;

	private RelativeLayout loadingView,clock_main_alertLayout,expandablelist_Layout;
	private TextView clock_main_alertText,shareTitle;
	private ImageView leftImage;
	
	private String mTaskId,thisJobUserid,userID;
	String today;
	
	private boolean isOther;
	
	QiQiuUtils qiQiuUtils;
	FrameLayout donghua;
	int mClickPosition;
	
	public SharedPreferences settings;
	
	private MyUMSDK myShare;
	
	
	

	public TaskInfoFragment(TaskInfoActivity activity,String taskid,String userid,boolean isother,ByIdDetailBean byIdDetailBean){
		taskInfoActivity = activity;
		mTaskId = taskid;
		thisJobUserid = userid;
		isOther = isother;
		this.byIdDetailBean = byIdDetailBean;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = taskInfoActivity.getLayoutInflater().inflate(R.layout.taskinfo_fragment, null);
		expandablelist = (PinnedHeaderExpandableListView) view.findViewById(R.id.expandablelist);

		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		
		clock_main_alertLayout = (RelativeLayout) view.findViewById(R.id.clock_main_alertLayout);
		clock_main_alertText = (TextView) view.findViewById(R.id.clock_main_alertText);
		shareTitle = (TextView) view.findViewById(R.id.shareTitle);
		leftImage = (ImageView) view.findViewById(R.id.leftImage);
		
		expandablelist_Layout  = (RelativeLayout) view.findViewById(R.id.expandablelist_Layout);
		donghua = new FrameLayout(taskInfoActivity);
		expandablelist_Layout.addView(donghua);
		qiQiuUtils = new QiQiuUtils(donghua, taskInfoActivity);
		
		myShare = new MyUMSDK(taskInfoActivity);
		
		settings = taskInfoActivity.getSharedPreferences("clock", taskInfoActivity.MODE_PRIVATE);
		userID = settings.getString("userid", null);

		expandablelist.setXListViewListener(listener);
		expandablelist.setIsShow(true);
		expandablelist.setPullLoadEnable(true);
		expandablelist.setPullRefreshEnable(false);
		
		
		myExpandableListAdapter = new MyexpandableListAdapter(taskInfoActivity,thisJobUserid);
		myExpandableListAdapter.setTaskInfoOnClick(taskInfoOnClick);
		myExpandableListAdapter.setGiveUpFlagClick(upFlagClick);
		myExpandableListAdapter.setSharedThisJob(sharedThisJob);
		myExpandableListAdapter.isOtherTaksInfo(isOther);
		expandablelist.setAdapter(myExpandableListAdapter);
		expandablelist.setIsShow(true);
		expandablelist.setOnHeaderUpdateListener(this);
		expandablelist.setOnChildClickListener(this);
		expandablelist.setOnGroupClickListener(this);
		expandablelist.setClickHeadView(clickHeadViewlistener);

		expandablelist
		.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView arg0,
					View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		getByTaskIdConn(1);
		return view;
	}
	IXListViewListener listener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (isConn) {
				return;
			}
			getByTaskIdConn(1);
			isReflush = true;
			isConn = true;

		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.i, "aaa", "加载更多数据");
			if (isConn) {
				return;
			}
			isReflush = false;

			if (currentPage * 10 >= totalCount) {
				onLoad();
				return;
			}
			getByTaskIdConn(currentPage + 1);
			isConn = true;
		}
	};

	private void getByTaskIdConn(int currentPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);
		sb.append("&userId=");
		sb.append(thisJobUserid);
		sb.append("&currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append("10");

		Service.getService(Contanst.HTTP_GETBYTASKID, null, sb.toString(),
				TaskInfoFragment.this).addList(null).request(UrlParams.GET);
	}
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
					TaskInfoFragment.this).addList(params).request(UrlParams.POST);
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
					TaskInfoFragment.this).addList(params).request(UrlParams.POST);
		}
		//删除Job
		private void GiveUpJob(String jobid) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("jobId", jobid));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Service.getService(Contanst.HTTP_DELETEJOB, null, null,
					TaskInfoFragment.this).addList(params).request(UrlParams.POST);
		}
		private void deleteTaskConn() {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("taskId", mTaskId));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Service.getService(Contanst.HTTP_TASK_DELETE, null, null,
					TaskInfoFragment.this).addList(params).request(UrlParams.POST);

		}


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
				case Contanst.GETBYTASKID:
					byTaskIdBean = (ByTaskIdBean) msg.obj;
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
							onLoad();
							// return;
						}
						// adapter.setList(newList);
						// infoListView.setPullLoadEnable(true);
						// adapter.notifyDataSetChanged();

						setExpandableList();
						// myExpandableListAdapter.setList(newList);
						expandablelist.setPullLoadEnable(true);
						// myExpandableListAdapter.notifyDataSetChanged();
						// // 展开所有group
						// for (int i = 0, count = expandablelist.getCount(); i
						// < count; i++) {
						// expandablelist.expandGroup(i);
						// }

					} else {
						currentPage++;
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						newList.addAll(bean.getListData());
						// adapter.setList(newList);
						// adapter.notifyDataSetChanged();

						setExpandableList();
					}

					onLoad();
					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
						expandablelist.setIsShow(false);
					} else {
						expandablelist.setIsShow(true);
					}
					break;
					
				case Contanst.CREATECOMMENT:
					ArrayList<CommentsBean> commentsBeans = (ArrayList<CommentsBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"评论返回:" + commentsBeans.toString());
					TaskItemBean itemBean = newList.get(mClickPosition);
					newList.get(mClickPosition).setCommentCount(
							itemBean.getCommentCount() + 1);
					newList.get(mClickPosition).setComments(commentsBeans);
					Tools.getLog(Tools.d, "aaa",
							"评论数量:"
									+ newList.get(mClickPosition)
									.getCommentCount());
					leftImage.setImageResource(R.drawable.guangchang_bd_pinglun);
					shareTitle.setText("评论成功!");
					clock_main_alertText.setText("你将得到气球 +2");
					clock_main_alertText.setVisibility(View.VISIBLE);
					animAlertStart();
					setExpandableList();
					break;
				case Contanst.CREATEVOTE:
					isConn = false; 
					ArrayList<VotesBean> votesBeans = (ArrayList<VotesBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"点赞成功返回:" + votesBeans.toString());
					TaskItemBean itemBean1 = newList.get(mClickPosition);
					newList.get(mClickPosition).setVoteCount(
							(itemBean1.getVoteCount() + 1));
					newList.get(mClickPosition).setVoted("1");
					newList.get(mClickPosition).setVotes(votesBeans);
					newList.get(mClickPosition).setClickMore(true);
					if(!userID.equals(thisJobUserid)){
						leftImage.setImageResource(R.drawable.guangchang_bd_daqichenggong);
						shareTitle.setText("打气成功!");
						clock_main_alertText.setText("你将得到气球 +1");
						clock_main_alertText.setVisibility(View.VISIBLE);
						animAlertStart();
					}

					setExpandableList();
					break;
					
				case Contanst.DELETEJOB:
					if(isToday){
						for(int i = 0;i<byDateBeanList.size();i++){
							if(byDateBeanList.get(i).getTaskId().equals(mTaskId)){
								if(byDateBeanList.get(i).getJobCount() == 1){
									byDateBeanList.get(i).setJobCount(byDateBeanList.get(i).getJobCount()-1);
									if(ClockMainActivity.curMap.get(today).equals("2")){
										ClockMainActivity.curMap.put(today, "1");
									}
								}else if(byDateBeanList.get(i).getJobCount() > 1){
									byDateBeanList.get(i).setJobCount(byDateBeanList.get(i).getJobCount()-1);
								}

							}
						}
					}


					getByTaskIdConn(1);
					isReflush = true;
					isConn = true;
					break;
				case Contanst.TASK_DELETE:
					taskInfoActivity.finish();
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				break;
			}
		}
	};
	
	public void setExpandableList() {
		myExpandableListAdapter.setList(newList);

		// 这句话不能注释掉，设置数据要不expandableListView 不能展开
		myExpandableListAdapter.notifyDataSetChanged();

		for (int i = 0, count = expandablelist.getCount(); i < count; i++) {
			expandablelist.expandGroup(i);
		}

		Tools.getLog(Tools.i, "aaa",
				"expandablelist myExpandableListAdapter  ======== "
						+ myExpandableListAdapter.getGroupCount());
		myExpandableListAdapter.notifyDataSetChanged();
	}
	
	private void onLoad() {
		if (loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
		// infoListView.stopRefresh();
		expandablelist.stopLoadMore();
		isConn = false;
		isReflush = false;
	}
	
	
	public void animAlertStart() {
		int height = clock_main_alertLayout.getHeight();
		TranslateAnimation animationStart = new TranslateAnimation(0, 0,
				height, 0);

		animationStart.setDuration(500L);// 设置动画持续时间
		clock_main_alertLayout.startAnimation(animationStart);
		animationStart.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				clock_main_alertLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				clock_main_alertLayout.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int height = clock_main_alertLayout.getHeight();
						Animation mAnimation = new TranslateAnimation(0, 0, 0,
								height);
						mAnimation.setDuration(250L);
						// clock_main_alertLayout.setAnimation(mAnimation);
						clock_main_alertLayout.startAnimation(mAnimation);

						mAnimation
						.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								clock_main_alertLayout
								.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								clock_main_alertLayout
								.setVisibility(View.GONE);

							}
						});
					}
				}, 3000L);

			}
		});

	}
	private void showdialogFinish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(taskInfoActivity, R.style.CustomDiaLog_by_SongHang));
		builder.setTitle("提示");
		builder.setMessage("是否确认删除？之前打卡数据将无法恢复");
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// deleteDialog();
				deleteTaskConn();
			}
		});
		builder.setNeutralButton("暂停", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(taskInfoActivity, ClockSetActivity.class);
				if (byIdDetailBean != null) {
					intent.putExtra("byid", byIdDetailBean);
				}
				startActivityForResult(intent, 111);
			}
		});
		builder.setNegativeButton("返回", null);
		builder.show();
	}
	
	public void UpJobDialogShow(final TaskItemBean taskItemBean) {
		new AlertDialog.Builder(taskInfoActivity).setTitle("取消打卡")
		.setMessage("确定删除这条打卡内容吗？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				GiveUpJob(taskItemBean.getJobId());
			}
		})
		.setNegativeButton("返回", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();
	}
	
	AddCommentDialog dialog;
	TaskInfoOnClick taskInfoOnClick = new TaskInfoOnClick() {

		@Override
		public void clickVote(String jobid, int groupPosition,
				int childPosition, String type,int x, int y) {
			// TODO Auto-generated method stub
			if(isConn){
				Tools.getLog(Tools.d, "aaa", "点赞 不要点了啊！");
				return;
			}
			isConn = true;
			int index = 0;
			// i=1 第0个是表格
			for (int i = 1; i < groupPosition; i++) {
				index += myExpandableListAdapter.getChildrenCount(i);
			}
			qiQiuUtils.startFly((int)(x+6*BaseActivity.DENSITY), (int)(y-54*BaseActivity.DENSITY));
			mClickPosition = index + childPosition;
			if (type.equals("0")) {
				addVoteConn(jobid);
			} else {
				isConn = false; 
				leftImage
				.setImageResource(R.drawable.guangchang_bd_daqichenggong);
				shareTitle.setText("你已经为Ta打气加油过了哦！");
				//				AnimVoteText.setText("你将得到气球 +1");
				clock_main_alertText.setVisibility(View.GONE);
				animAlertStart();
			}
		}

		@Override
		public void clickComment(final TaskItemBean taskItemBean,
				int groupPosition, int childPosition, final int itemBeanIndex) {
			// TODO Auto-generated method stub
			int index = 0;
			for (int i = 1; i < groupPosition; i++) {
				index += myExpandableListAdapter.getChildrenCount(i);
			}
			String name = null;
			if (itemBeanIndex != -1) {
				name = taskItemBean.getComments().get(itemBeanIndex).getName();
			}
			mClickPosition = index + childPosition;
			dialog = new AddCommentDialog(taskInfoActivity, name);
			dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {

				@Override
				public void onClickSuccess(String content) {
					// TODO Auto-generated method stub
					addComentConn(taskItemBean, content, itemBeanIndex);
				}
			});
			dialog.show();
		}
	};
	GiveUpJobDialog upJobDialog;
	boolean isToday;
	giveUpFlagClick upFlagClick = new giveUpFlagClick() {
		@Override
		public void setGiveUp(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub

			int index = 0;
			// i=1 第0个是表格
			for (int i = 1; i < groupPosition; i++) {
				index += myExpandableListAdapter.getChildrenCount(i);
			}


			TaskItemBean taskItemBean = newList.get(index + childPosition);
			String today = TimeUtil.getYYMMDD(System.currentTimeMillis());
			String curTitle = TimeUtil.getYYMMDD(taskItemBean.getCheck_time());
			if(curTitle.equals(today)){
				isToday = true;
			}else{
				isToday = false;
			}

			UpJobDialogShow(taskItemBean);
		}
	};
	
	boolean isShareImage;
	Bitmap shareBitmap;
	String shareTaskUrl = "", shareTaskStr;
	TaskInfoDialog taskDialog;
	String sharedialogStr;
	boolean isAlone;
	SharedThisJob sharedThisJob = new SharedThisJob() {

		@Override
		public void thisJob(TaskItemBean item) {
			// TODO Auto-generated method stub
			shareTaskUrl = "http://123.57.5.108:8087/architect/share?jobId="
					+ item.getJobId();
			// Tools.getLog(Tools.d, "aaa", "item 输出：==="+item.toString());
			String Signature = item.getSignature();
			String Quantity = item.getQuantity();
			StringBuffer sb = new StringBuffer();
			boolean isContentHave = false;

			if(isOther){
				//				if(ClockOtherUserActivity.mUserName != null){
				//					sb.append("@"+ClockOtherUserActivity.mUserName+" ");
				//				}
				String name = byIdDetailBean.getUserName();
				if(name != null){
					sb.append("@"+name+" ");
				}
			}
			sb.append("#" + byIdDetailBean.getTitle() + "#");
			sb.append("Day" + byIdDetailBean.getTotalDateCount() + " ");

			if (Signature != null && Signature.length() != 0) {
				sb.append(Signature);
				isContentHave = true;
			}
			if (Quantity != null && Quantity.length() != 0) {
				if (isContentHave) {
					sb.append(";");
				}
				sb.append(Quantity);
			}
			if (item.getPicCount().equals("0")) {
				isShareImage = false;
				shareBitmap = null;
			} else {
				String imageUrl = item.getAllPath();
				String ImageUrlArray[] = imageUrl.split(",");
				String getAllSource[] = item.getAllSource().split(",");

				ImageLoader.getInstance().loadImage(
						Tools.getImagePath(Integer.parseInt(getAllSource[0]))
						+ ImageUrlArray[0], new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								shareBitmap = loadedImage;
								isShareImage = true;
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});
			}
			sharedialogStr = sb.toString();
			isAlone = true; 
			taskDialog = new TaskInfoDialog(taskInfoActivity, true);
			taskDialog.setOnClickClockSuccess(mCLickClockSuccess);
			taskDialog.show();
		}
	};
	private onCLickClockSuccess mCLickClockSuccess = new onCLickClockSuccess() {
		//详情分享的dialog 
		@Override
		public void onClickSuccess(int index) {
			// TODO Auto-generated method stub
			//			MobclickAgent.onEvent(mContext, "jobshareClick");

			switch (index) {
			case 0: // 新浪
				if (isShareImage){

					//					if(isOther){
					if(isAlone){
						MobclickAgent.onEvent(taskInfoActivity, "detailJobshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(taskInfoActivity, "detailTaskshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}

					//					}else{
					//						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
					//								shareBitmap, false);
					//					}
				}else{
					//					if(isOther){
					if(isAlone){
						MobclickAgent.onEvent(taskInfoActivity, "detailJobshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(taskInfoActivity, "detailTaskshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

					//					}else{
					//						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
					//								shareBitmap, false);
					//					}
				}

				Toast.makeText(taskInfoActivity, "分享成功！", Toast.LENGTH_SHORT).show();
				taskDialog.dismiss();
				break;
			case 1: // 朋友圈
				//				MobclickAgent.onEvent(mContext, "mainmodalWeChatclick");
				if (isShareImage)
					if(isAlone){
						MobclickAgent.onEvent(taskInfoActivity, "detailTaskshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, shareBitmap, false,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(taskInfoActivity, "detailJobshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, shareBitmap, false,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}

				else
					if(isAlone){
						MobclickAgent.onEvent(taskInfoActivity, "detailTaskshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, null, false,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(taskInfoActivity, "detailJobshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, null, false,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

				taskDialog.dismiss();
				break;
			case 2: // QQ空间
				MobclickAgent.onEvent(taskInfoActivity, "mainmodalQQclick");
				if (isShareImage)
					if(isAlone){
						MobclickAgent.onEvent(taskInfoActivity, "detailTaskshareQQclick");
						myShare.qzeroUMShared(taskInfoActivity, sharedialogStr, "打卡7",
								shareTaskUrl, shareBitmap,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(taskInfoActivity, "detailJobshareQQclick");
						myShare.qzeroUMShared(taskInfoActivity, sharedialogStr, "打卡7",
								shareTaskUrl, shareBitmap,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

				else
					if(isAlone){
						MobclickAgent.onEvent(taskInfoActivity, "detailTaskshareQQclick");
						myShare.qzeroUMShared(taskInfoActivity, sharedialogStr, "打卡7",
								shareTaskUrl, null,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(taskInfoActivity, "detailJobshareQQclick");
						myShare.qzeroUMShared(taskInfoActivity, sharedialogStr, "打卡7",
								shareTaskUrl, null,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

				taskDialog.dismiss();
				break;

			case 3: // 邀请
				//				inviteDialog();
				break;

			case 4: // 设置
				Intent intent = new Intent(taskInfoActivity, ClockSetActivity.class);
				if (byIdDetailBean != null) {
					intent.putExtra("byid", byIdDetailBean);
				}
				startActivityForResult(intent, 111);
				taskDialog.dismiss();
				break;

			case 5: // 删除
				showdialogFinish();
				taskDialog.dismiss();
				break;

			}

		}
	};
	setClickHeadViewlistener clickHeadViewlistener = new setClickHeadViewlistener() {

		@Override
		public void clickHead(int position) {
			// TODO Auto-generated method stub
//			Tools.getLog(Tools.d, "aaa", "点击的position：" + position
//					+ " lastTwoJobBean =========== "
//					+ lastTwoJobBean.getLastThrees().size());
			if (position == -1) {
				if (byIdDetailBean.getBuildingId() != null) {
					Intent in = new Intent(taskInfoActivity,
							ClockGroupInfoFragmentActivity.class);
					in.putExtra("buildingId", byIdDetailBean.getBuildingId());
					taskInfoActivity.startActivity(in);
				}
			}
			return;
		}
	};




	@Override
	public View getPinnedHeader() {
		// TODO Auto-generated method stub
		View headerView = (ViewGroup) taskInfoActivity.getLayoutInflater().inflate(
				R.layout.group, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return headerView;
	}

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		// TODO Auto-generated method stub
		if (firstVisibleGroupPos < 0) {
			return;
//		} else if (firstVisibleGroupPos == 0) {
//			Group firstVisibleGroup = (Group) myExpandableListAdapter
//					.getGroup(firstVisibleGroupPos);
//			TextView textView = (TextView) headerView.findViewById(R.id.group);
//			TextView textView1 = (TextView) headerView.findViewById(R.id.day);
//			textView1.setText("Day");
//			textView.setText(firstVisibleGroup.getTitle());
//			textView.setVisibility(View.GONE);
//			textView1.setVisibility(View.GONE);

		} else {
			Group firstVisibleGroup = (Group) myExpandableListAdapter
					.getGroup(firstVisibleGroupPos);
			TextView textView = (TextView) headerView.findViewById(R.id.group);
			TextView textView1 = (TextView) headerView.findViewById(R.id.day);
			textView1.setText("Day");
			textView.setVisibility(View.VISIBLE);
			textView1.setVisibility(View.VISIBLE);
			textView.setText(firstVisibleGroup.getTitle().substring(5));
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		// TODO Auto-generated method stub
		return false;
	}

}
