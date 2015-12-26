package com.yktx.check;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clock.service.AddShowPhotoService;
import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.yktx.check.adapter.MyexpandableListAdapter;
import com.yktx.check.adapter.MyexpandableListAdapter.SharedThisJob;
import com.yktx.check.adapter.MyexpandableListAdapter.TaskInfoOnClick;
import com.yktx.check.adapter.MyexpandableListAdapter.giveUpFlagClick;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.CreateJobBean;
import com.yktx.check.bean.Group;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.JobStatsBean;
import com.yktx.check.bean.LastTwoJobBean;
import com.yktx.check.bean.MsgToUserBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.dialog.AllTaskFinishDialog2;
import com.yktx.check.dialog.GiveUpJobDialog;
import com.yktx.check.dialog.TakeClockDialog;
import com.yktx.check.dialog.TakeClockSuccessDialog;
import com.yktx.check.dialog.TaskInfoDialog;
import com.yktx.check.dialog.TakeClockDialog.TaskClockDialogOnCLickClockSuccess;
import com.yktx.check.dialog.TakeClockSuccessDialog.OnCLickSuccessShare;
import com.yktx.check.dialog.TaskInfoDialog.onCLickClockSuccess;
import com.yktx.check.listview.PinnedHeaderExpandableListView;
import com.yktx.check.listview.PinnedHeaderExpandableListView.IXListViewListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.yktx.check.listview.PinnedHeaderExpandableListView.setClickHeadViewlistener;
import com.yktx.check.service.Service;
import com.yktx.check.square.fragment.BoutiqueFragment;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.ImageTool;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.CalendarView;
import com.yktx.sqlite.DBHelper;

public class TaskInfo2Activity extends BaseActivity implements ServiceListener,
ExpandableListView.OnChildClickListener,
ExpandableListView.OnGroupClickListener, OnHeaderUpdateListener {

	public static String mTaskId;
	public static String mTotalCheckCount;
	public static String mTotalDateCount;
	public static String mDescription;
	private String mTitleContent;
	private LastTwoJobBean lastTwoJobBean;
	// private XListView infoListView;
	private PinnedHeaderExpandableListView expandablelist;
	private MyexpandableListAdapter myExpandableListAdapter;
	private ByTaskIdBean byTaskIdBean;
	// private TaskInfoAdapter adapter;
	private ImageView title_item_leftImage, title_item_rightTextView,
	title_item_createJob;
	public static ByIdDetailBean byIdDetailBean;
	private TextView title_item_content;

	ArrayList<JobStatsBean> jobList = new ArrayList<JobStatsBean>(10);
	boolean isConn, isReflush = true;
	/** 一页多少条数据 */
	public int pageLimit = 10;
	/** 第几页 */
	public int currentPage;
	/** 总数 */
	public int totalCount;
	/** 总页数 */
	public int totalPage;
	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	int mClickPosition;
	AddCommentDialog dialog;
	private TextView activity_task_addUpClockJobContent,
	activity_task_insistClockJobContent;

	private RelativeLayout loadingView;

	private MyUMSDK myShare;
	private String thisJobUserid;
	TakeClockDialog taskClockDialog;
	private TextView shareTitle, clock_main_alertText;
	private ImageView leftImage;
	private RelativeLayout clock_main_alertLayout;

	private boolean isOther;
	boolean isCannotDaka;

	RelativeLayout expandablelist_Layout;
	QiQiuUtils qiQiuUtils;
	FrameLayout donghua;
	boolean isAlone;

	DBHelper dbHelper;
	private ArrayList<ByDateBean> byDateBeanList = new ArrayList<ByDateBean>();
	String today;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_task_info2);
		mTaskId = getIntent().getStringExtra("taskid");
		thisJobUserid = getIntent().getStringExtra("userid");
		isCannotDaka = getIntent().getBooleanExtra("isCannotDaka", false);
		if (thisJobUserid == null || thisJobUserid.length() == 0) {
			thisJobUserid = userID;
		}

		// mTotalCheckCount = getIntent().getStringExtra("totalCheckCount");
		// mTotalDateCount = getIntent().getStringExtra("totalDateCount");
		// mTitleContent = getIntent().getStringExtra("title");
		// mDescription = getIntent().getStringExtra("description");
		Tools.getLog(Tools.d, "aaa", "mTotalDateCount" + mTotalCheckCount
				+ ",currentStreak" + mTotalDateCount);
		// infoListView = (XListView) findViewById(R.id.infoListView);
		expandablelist = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
		title_item_content = (TextView) findViewById(R.id.title_item_content);
		title_item_leftImage = (ImageView) findViewById(R.id.title_item_leftImage);
		title_item_rightTextView = (ImageView) findViewById(R.id.title_item_rightImage);
		title_item_createJob = (ImageView) findViewById(R.id.title_item_createJob);

		activity_task_insistClockJobContent = (TextView) findViewById(R.id.activity_task_insistClockJobContent);
		activity_task_addUpClockJobContent = (TextView) findViewById(R.id.activity_task_addUpClockJobContent);

		clock_main_alertLayout = (RelativeLayout) findViewById(R.id.clock_main_alertLayout);
		clock_main_alertText = (TextView) findViewById(R.id.clock_main_alertText);
		shareTitle = (TextView) findViewById(R.id.shareTitle);
		leftImage = (ImageView) findViewById(R.id.leftImage);

		expandablelist_Layout  = (RelativeLayout) findViewById(R.id.expandablelist_Layout);
		donghua = new FrameLayout(mContext);
		expandablelist_Layout.addView(donghua);
		qiQiuUtils = new QiQiuUtils(donghua, mContext);

		loadingView = (RelativeLayout) findViewById(R.id.loadingView);
		dbHelper = new DBHelper(mContext);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		today = TimeUtil.getYYMMDD(System.currentTimeMillis());
		byDateBeanList = dbHelper.getTaskList();
		myShare = new MyUMSDK(mContext);
		isOther = getIntent().getBooleanExtra("isother", false);
		if(isCannotDaka){
			title_item_createJob.setVisibility(View.GONE);
		}

		if (isOther || !thisJobUserid.equals(userID)) {
			isOther = true;
			title_item_rightTextView.setVisibility(View.GONE);
			title_item_createJob.setVisibility(View.GONE);
		}
		getByIdConn();// 个人信息

		// getMsgConn();
		// activity_task_insistClockJobContent.setText(mTotalDateCount);//
		// 修改坚持天数
		// activity_task_addUpClockJobContent.setText(mTotalCheckCount);//
		// 修改累计打卡次数
		// adapter = new TaskInfoAdapter(this);
		// infoListView.setAdapter(adapter);
		expandablelist.setXListViewListener(listener);
		expandablelist.setIsShow(true);
		// infoListView.setPullGoHome(false);
		expandablelist.setPullLoadEnable(true);
		expandablelist.setPullRefreshEnable(false);
		// title_item_content.setText(mTitleContent);

	}

	private void GiveUpJob(String jobid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("jobId", jobid));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_DELETEJOB, null, null,
				TaskInfo2Activity.this).addList(params).request(UrlParams.POST);
	}

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
			taskDialog = new TaskInfoDialog(TaskInfo2Activity.this, true);
			taskDialog.setOnClickClockSuccess(mCLickClockSuccess);
			taskDialog.show();
		}
	};
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
			dialog = new AddCommentDialog(mContext, name);
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
	private int curTabIndex;
	TaskInfoDialog taskDialog;
	String sharedialogStr;
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
						MobclickAgent.onEvent(mContext, "detailJobshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(mContext, "detailTaskshareWeiboclick");
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
						MobclickAgent.onEvent(mContext, "detailJobshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(mContext, "detailTaskshareWeiboclick");
						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
								shareBitmap, true,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

					//					}else{
					//						myShare.sinaUMShared(sharedialogStr, shareTaskUrl,
					//								shareBitmap, false);
					//					}
				}

				Toast.makeText(mContext, "分享成功！", Toast.LENGTH_SHORT).show();
				taskDialog.dismiss();
				break;
			case 1: // 朋友圈
				//				MobclickAgent.onEvent(mContext, "mainmodalWeChatclick");
				if (isShareImage)
					if(isAlone){
						MobclickAgent.onEvent(mContext, "detailTaskshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, shareBitmap, false,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(mContext, "detailJobshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, shareBitmap, false,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}

				else
					if(isAlone){
						MobclickAgent.onEvent(mContext, "detailTaskshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, null, false,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(mContext, "detailJobshareWeChatclick");
						myShare.friendsterUMShared("打卡7", sharedialogStr,
								shareTaskUrl, null, false,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

				taskDialog.dismiss();
				break;
			case 2: // QQ空间
				MobclickAgent.onEvent(mContext, "mainmodalQQclick");
				if (isShareImage)
					if(isAlone){
						MobclickAgent.onEvent(mContext, "detailTaskshareQQclick");
						myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
								shareTaskUrl, shareBitmap,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(mContext, "detailJobshareQQclick");
						myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
								shareTaskUrl, shareBitmap,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

				else
					if(isAlone){
						MobclickAgent.onEvent(mContext, "detailTaskshareQQclick");
						myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
								shareTaskUrl, null,4);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job	
					}else{
						MobclickAgent.onEvent(mContext, "detailJobshareQQclick");
						myShare.qzeroUMShared(mContext, sharedialogStr, "打卡7",
								shareTaskUrl, null,3);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
					}

				taskDialog.dismiss();
				break;

			case 3: // 邀请
				//				inviteDialog();
				break;

			case 4: // 设置
				Intent intent = new Intent(mContext, ClockSetActivity.class);
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

	private void showdialogFinish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, R.style.CustomDiaLog_by_SongHang));
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
				Intent intent = new Intent(mContext, ClockSetActivity.class);
				if (byIdDetailBean != null) {
					intent.putExtra("byid", byIdDetailBean);
				}
				startActivityForResult(intent, 111);
			}
		});
		builder.setNegativeButton("返回", null);
		builder.show();
	}

	boolean isShareImage;
	Bitmap shareBitmap;
	String shareTaskUrl = "", shareTaskStr;

	//	public void inviteDialog() {
	//		AlertDialog.Builder builder = new AlertDialog.Builder(
	//				new ContextThemeWrapper(mContext,
	//						R.style.CustomDiaLog_by_SongHang));
	//		builder.setItems(new String[] { "新浪微博", "朋友圈", "QQ空间", "微信好友", "qq好友",
	//				"返回" }, new AlertDialog.OnClickListener() {
	//
	//			@Override
	//			public void onClick(DialogInterface dialog, int which) {
	//				// TODO Auto-generated method stub
	//				switch (which) {
	//				case 0:
	//					if (isShareImage)
	//						myShare.sinaUMShared(shareTaskStr, shareTaskUrl,
	//								shareBitmap, false);
	//					else
	//						myShare.sinaUMShared(shareTaskStr, shareTaskUrl, null,
	//								false);
	//					break;
	//				case 1:
	//					if (isShareImage)
	//						myShare.friendsterUMShared("打卡7", shareTaskStr,
	//								shareTaskUrl, shareBitmap, false);
	//					else
	//						myShare.friendsterUMShared("打卡7", shareTaskStr,
	//								shareTaskUrl, null, false);
	//					break;
	//				case 2:
	//					if (isShareImage)
	//						myShare.qzeroUMShared(mContext, shareTaskStr, "打卡7",
	//								shareTaskUrl, shareBitmap);
	//					else
	//						myShare.qzeroUMShared(mContext, shareTaskStr, "打卡7",
	//								shareTaskUrl, null);
	//					break;
	//				case 3:
	//					if (isShareImage)
	//						myShare.weixinUMShared(shareTaskStr, "", shareTaskUrl,
	//								shareBitmap, false);
	//					else
	//						myShare.weixinUMShared(shareTaskStr, "", shareTaskUrl,
	//								null, false);
	//					break;
	//				case 4:
	//					if (isShareImage)
	//						myShare.qqUMShared(mContext, shareTaskStr, "打卡7",
	//								shareTaskUrl, shareBitmap, false);
	//					else
	//						myShare.qqUMShared(mContext, shareTaskStr, "打卡7",
	//								shareTaskUrl, null, false);
	//					break;
	//
	//				default:
	//					break;
	//				}
	//			}
	//		});
	//		builder.show();
	//	}

	private void deleteTaskConn() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("taskId", mTaskId));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_TASK_DELETE, null, null,
				TaskInfo2Activity.this).addList(params).request(UrlParams.POST);

	}

	// private void deleteDialog() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// new ContextThemeWrapper(this, R.style.CustomDiaLog_by_SongHang));
	// builder.setTitle("提示");
	// builder.setMessage("");
	// builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	//
	// }
	// });
	// builder.setNegativeButton("取消", null);
	// builder.show();
	// }

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		title_item_leftImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_item_rightTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				shareTaskUrl = "http://123.57.5.108:8087/architect/taskAchievement?taskId="
						+ mTaskId;
				isAlone = false;
				taskDialog = new TaskInfoDialog(TaskInfo2Activity.this, false);
				taskDialog.setOnClickClockSuccess(mCLickClockSuccess);
				sharedialogStr = "#" + byIdDetailBean.getTitle() + "#已累计坚持"
						+ byIdDetailBean.getTotalDateCount() + "天";
				taskDialog.show();
				isShareImage = false;
				shareBitmap = null;

			}
		});
		title_item_createJob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 打卡
				if (byIdDetailBean.getTimeLimitFlag() == "1") {
					String today = TimeUtil.getYYMMDD(System
							.currentTimeMillis());
					String startTime = today + " "
							+ byIdDetailBean.getBeginTime() + ":00";
					String endTime = today + " " + byIdDetailBean.getEndTime()
							+ ":00";
					Tools.getLog(Tools.d, "aaa", "start:" + startTime);
					Tools.getLog(Tools.d, "aaa", "end:" + endTime);
					if (TimeUtil.getUnixLong(startTime) >= System
							.currentTimeMillis()
							|| TimeUtil.getUnixLong(endTime) <= System
							.currentTimeMillis()) {
						shareTitle.setText("打卡失败！");
						leftImage.setImageResource(R.drawable.home_dakashibai);
						clock_main_alertText.setText("请在时间内打卡。");
						Message msg = new Message();
						msg.what = Contanst.CreateJobFail;
						mHandler.sendMessage(msg);
						animAlertStart();
						return;
					}
				}
				taskClockDialog = new TakeClockDialog(TaskInfo2Activity.this);
				taskClockDialog.setOnClickClockSuccess(cLickClockSuccess);
				taskClockDialog.setTaskNameStr(byIdDetailBean.getTitle());
				taskClockDialog.show();
			}
		});
	}

	public ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(3);// 相册选取的集合的名字
	String jobid;
	boolean isShowAllTast;
	int imageNum = 0;// 上传图片数量

	String signature,quantity;
	TaskClockDialogOnCLickClockSuccess cLickClockSuccess = new TaskClockDialogOnCLickClockSuccess() {

		@Override
		public void onClickSuccess(String content, String num, String unit,				 
				ArrayList<ImageListBean> list) {
			filenames = list;

			signature = content;
			quantity = num;
			connBaiduLocation();

		}
	};

	private Vibrator vibrator;

	private void jobSound() {
		vibrator.vibrate(500); // 重复两次上面的pattern 如果只想震动一次，index设为
		SoundPool soundPool;
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

		soundPool.load(this, R.raw.a6, 1);
		soundPool.play(1, 1, 1, 0, 0, 1);
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

	public void UpJobDialogShow(final TaskItemBean taskItemBean) {
		// upJobDialog= new GiveUpJobDialog(mContext);
		// upJobDialog.show();
		// upJobDialog.mTitle.setText("取消打卡");
		// upJobDialog.giveUpJob_dialog_content
		// .setText("确定取消打卡将删除最新的一次\n打卡内容");

		// upJobDialog.mClose.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// upJobDialog.dismiss();
		// }
		// });
		// upJobDialog.mSuccess.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// GiveUpJob(taskItemBean.getJobId());
		// upJobDialog.dismiss();
		// }
		// });
		new AlertDialog.Builder(mContext).setTitle("取消打卡")
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

	private void getByTaskIdConn(int currentPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);
		sb.append("&userId=");
		sb.append(userID);
		sb.append("&currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append("10");

		Service.getService(Contanst.HTTP_GETBYTASKID, null, sb.toString(),
				TaskInfo2Activity.this).addList(null).request(UrlParams.GET);
	}

	public void getLastTwoJobsConn() {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);
		Service.getService(Contanst.HTTP_GETLASTTHREE, null, sb.toString(),
				TaskInfo2Activity.this).addList(null).request(UrlParams.GET);
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
				TaskInfo2Activity.this).addList(params).request(UrlParams.POST);
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
				TaskInfo2Activity.this).addList(params).request(UrlParams.POST);
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
				TaskInfo2Activity.this).addList(params).request(UrlParams.POST);
	}

	public void SelectClockStateConn(String beginDate, String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);

		Service.getService(Contanst.HTTP_GETSTATISTIC, null, sb.toString(),
				TaskInfo2Activity.this).addList(null).request(UrlParams.GET);

	}

	private void getByIdConn() {
		loadingView.setVisibility(View.VISIBLE);
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(mTaskId);

		Service.getService(Contanst.HTTP_GETBYIDTASK, null, sb.toString(),
				TaskInfo2Activity.this).addList(null).request(UrlParams.GET);
	}

	//	private void getMsgConn() {
	//		StringBuffer sb = new StringBuffer();
	//		sb.append("?userId=");
	//		sb.append(userID);
	//		sb.append("&currentPage=");
	//		sb.append("1");
	//		sb.append("&pageLimit=");
	//		sb.append(pageLimit);
	//
	//		Service.getService(Contanst.HTTP_GET_MSGTOUSER, null, sb.toString(),
	//				TaskInfoActivity.this).addList(null).request(UrlParams.GET);
	//	}

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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {

				case Contanst.BAIDU_LOCATION:
					createJobConn((String) msg.obj);
					break;

				case Contanst.GETSTATISTIC:
					// jobList = (ArrayList<JobStatsBean>) msg.obj;
					curTabIndex = 1;
					getLastTwoJobsConn();
					break;
				case Contanst.GETLASTTHREE:
					lastTwoJobBean = (LastTwoJobBean) msg.obj;
					getByTaskIdConn(1);
					break;
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
				case Contanst.GETBYIDTASK:
					byIdDetailBean = (ByIdDetailBean) msg.obj;
					showTitle(byIdDetailBean);
					SelectClockStateConn(null, null);// 分页获取当前数据
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
				case Contanst.CANCELVOTE:
					isConn = false; 
					// 取消点赞
					TaskItemBean itemBean2 = newList.get(mClickPosition);
					for (int i = 0; i < itemBean2.getVotes().size(); i++) {
						if (itemBean2.getVotes().get(i).getUserId()
								.equals(userID)) {
							itemBean2.getVotes().remove(i);
						}
					}
					newList.get(mClickPosition).setVoteCount(
							itemBean2.getVoteCount() - 1);
					newList.get(mClickPosition).setVoted("0");
					newList.get(mClickPosition).setClickMore(true);
					setExpandableList();
					break;
				case Contanst.GET_MSGTOUSER:
					MsgToUserBean msgBean = (MsgToUserBean) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"=================Contanst.GET_MSGTOUSER:==="
									+ msgBean.toString());
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
					TaskInfo2Activity.this.finish();
					break;
				case Contanst.CREATEJOB:
					createJobBean = (CreateJobBean) msg.obj;
					for(int i = 0;i<byDateBeanList.size();i++){
						if(byDateBeanList.get(i).getTaskId().equals(createJobBean.getTaskId())){
							byDateBeanList.get(i).setJobCount(byDateBeanList.get(i).getJobCount()+1);
						}
					}
					Tools.getLog(Tools.i, "aaa", "打卡成功！！！！！！！！");
					if (filenames.size() > 0) {
						Intent intent_baonew = new Intent(
								TaskInfo2Activity.this,
								AddShowPhotoService.class);
						intent_baonew.putExtra("state",
								AddShowPhotoService.AddShowPhotoRun);
						intent_baonew.putExtra("productid", userID);
						intent_baonew.putExtra("list", filenames);
						intent_baonew.putExtra("uuid", jobid);
						intent_baonew.putExtra("type", 1);
						startService(intent_baonew);
					}
					//					if(taskClockDialog != null ){
					//						taskClockDialog.dismiss();
					//					}
					// boolean isWeiboOpen;
					// isWeiboOpen = settings.getBoolean("isWeiboOpen", false);
					// if (isWeiboOpen) {
					// final MyUMSDK myUMSDK = new MyUMSDK(
					// TaskInfoActivity.this);
					// final StringBuffer sb = new StringBuffer();
					// boolean isContentHave = false;
					// sb.append("#" + byIdDetailBean.getTitle() + "#");
					// sb.append("Day1");
					// String Signature = createJobBean.getSignature();
					// String Quantity = createJobBean.getQuantity();
					// if (Signature != null && Signature.length() != 0) {
					// sb.append(Signature);
					// isContentHave = true;
					// }
					// if (Quantity != null && Quantity.length() != 0) {
					// if (isContentHave) {
					// sb.append(";");
					// }
					// sb.append(Quantity);
					//
					// }
					// if (!url.equals("")) {
					//
					// ImageLoader.getInstance().loadImage(url,
					// new ImageLoadingListener() {
					//
					// @Override
					// public void onLoadingStarted(
					// String imageUri, View view) {
					// // TODO Auto-generated method stub
					//
					// }
					//
					// @Override
					// public void onLoadingFailed(
					// String imageUri, View view,
					// FailReason failReason) {
					// // TODO Auto-generated method stub
					//
					// }
					//
					// @Override
					// public void onLoadingComplete(
					// String imageUri, View view,
					// Bitmap loadedImage) {
					// // TODO Auto-generated method stub
					//
					// myUMSDK.sinaUMShared(sb.toString(),
					// "http://123.57.5.108:8087/architect/share?jobId="
					// + jobid,
					// loadedImage, false);
					// }
					//
					// @Override
					// public void onLoadingCancelled(
					// String imageUri, View view) {
					// // TODO Auto-generated method stub
					//
					// }
					// });
					//
					// } else {
					//
					// myUMSDK.sinaUMShared(sb.toString(),
					// "http://123.57.5.108:8087/architect/share?jobId="
					// + jobid, null, false);
					// }
					// }
					TakeClockSuccessDialog clockSuccessDialog;

					int index = 0;

					if (signature != null && signature.length() != 0) {
						index = 1;
					}
					if (quantity != null && quantity.length() != 0) {
						index = 1;
					}
					if (imageNum == 0) {
						clockSuccessDialog = new TakeClockSuccessDialog(
								mContext, index,
								createJobBean.getManCountToday(),createJobBean.getBuildingId(),
								byIdDetailBean.getTitle(),createJobBean.getCheckDateCount());
					} else {
						clockSuccessDialog = new TakeClockSuccessDialog(
								mContext, 2,
								createJobBean.getManCountToday(),createJobBean.getBuildingId(),
								byIdDetailBean.getTitle(),createJobBean.getCheckDateCount());
					}
					clockSuccessDialog
					.setOnCLickSuccessShare(onCLickSuccessShare);
					clockSuccessDialog
					.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							// TODO Auto-generated method stub
							Tools.getLog(Tools.d, "aaa", "clockSuccessDialog ===================== setOnDismissListener");
							updateDateColor();
							//							if (isShowAllTast) {
							//								AllTaskFinishDialog2 allDialog = new AllTaskFinishDialog2(
							//										TaskInfoActivity.this);
							//								allDialog.show();
							//								
							//								allDialog.shareImage.setOnClickListener(new OnClickListener() {
							//									
							//									@Override
							//									public void onClick(View arg0) {
							//										// TODO Auto-generated method stub
							//										MobclickAgent.onEvent(mContext, "mainhomepageshareclick");
							//										Intent in = new Intent(mContext, ShareAchievementActivity.class);
							//										// in.putExtra("text", sb.toString());
							////										in.putExtra("date", mClickDate.toString());
							//										in.putExtra("userid", userID);
							//										startActivity(in);
							//										overridePendingTransition(R.anim.slide_alpha_in_right,
							//												R.anim.slide_alpha_out_left);
							//									}
							//								});
							//								isShowAllTast = false;
							//							}
						}
					});
					clockSuccessDialog.show();
					getByIdConn();// 个人信息
					loadingView.setVisibility(View.VISIBLE);
					isReflush = true;
					isConn = true;
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:// 通过Id获得详细信息的
				String message = (String) msg.obj;
				switch (msg.arg1) {

				case Contanst.BAIDU_LOCATION:
					createJobConn(null);
					break;

				case Contanst.GETSTATISTIC:
					getLastTwoJobsConn();
					break;
				case Contanst.GETLASTTHREE:
					getByTaskIdConn(1);
					break;
				case Contanst.GETBYTASKID:
					onLoad();
					break;
				case Contanst.GETBYIDTASK:
					SelectClockStateConn(null, null);// 分页获取当前数据
					break;
				}
				break;
			}
		}
	};

	CreateJobBean createJobBean;
	OnCLickSuccessShare onCLickSuccessShare = new OnCLickSuccessShare() {
		//拍照打卡的Dialog
		@Override
		public void onClickSuccess(final int shareID) {
			// TODO Auto-generated method stub

			final MyUMSDK myUMSDK = new MyUMSDK(TaskInfo2Activity.this);
			final StringBuffer sb = new StringBuffer();
			boolean isContentHave = false;
			sb.append("#" + byIdDetailBean.getTitle() + "#");
			sb.append("Day1");
			String Signature = createJobBean.getSignature();
			String Quantity = createJobBean.getQuantity();
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
			Tools.getLog(Tools.d, "aaa", "shareID:" + shareID);
			final String shareUrl = "http://123.57.5.108:8087/architect/share?jobId="
					+ jobid;
			if (imageNum > 0) {
				String url = "";
				for (int i = 0; i < filenames.size(); i++) {
					if (filenames.get(i).getIsCheck()) {
						url = FileURl.LOAD_FILE
								+ filenames.get(i).getImageUrl();
					}
				}
				//				MobclickAgent.onEvent(mContext, "homepageshareClick");
				//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				ImageLoader.getInstance().loadImage(url,
						new ImageLoadingListener() {

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
							View view, Bitmap mShareImageBitmap) {
						// TODO Auto-generated method stub

						switch (shareID) {
						case 0: // sina
							// if (imageNum == 0) {
							// myUMSDK.sinaUMShared(sb.toString(),
							// shareUrl, null, false);
							// } else {
							MobclickAgent.onEvent(mContext, "mainmodalWeiboclick");
							myUMSDK.sinaUMShared(sb.toString(),
									shareUrl, mShareImageBitmap, false,1);
							// }
							break;
						case 1: // 朋友圈
							// if (imageNum == 0) {
							MobclickAgent.onEvent(mContext, "mainmodalWeChatclick");
							myUMSDK.friendsterUMShared("打卡7",
									sb.toString(), shareUrl,
									mShareImageBitmap, false,1);
							// } else {
							// myUMSDK.friendsterUMShared("打卡7",
							// sb.toString(), shareUrl, null,
							// false);
							// }
							break;
						case 2: // QQ空间
							// if (imageNum == 0){
							MobclickAgent.onEvent(mContext, "mainmodalQQclick");
							int i = myUMSDK.qzeroUMShared(mContext,
									sb.toString(), "打卡7", shareUrl,
									mShareImageBitmap,1);
							Tools.getLog(Tools.d, "aaa", "i:" + i);
							// }else{
							//
							// int i1 = myUMSDK.qzeroUMShared(mContext,
							// sb.toString(), "打卡7", shareUrl,
							// null);
							// Tools.getLog(Tools.d, "aaa", "i:"+i1);
							// }

							break;
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri,
							View view) {
						// TODO Auto-generated method stub

					}
				});

			} else {

				switch (shareID) {
				case 0: // sina
					// if (imageNum == 0) {
					myUMSDK.sinaUMShared(sb.toString(), shareUrl, null, false,1);
					// } else {
					// myUMSDK.sinaUMShared(sb.toString(),
					// shareUrl, mShareImageBitmap,
					// false);
					// }
					break;
				case 1: // 朋友圈
					// if (imageNum == 0) {
					myUMSDK.friendsterUMShared("打卡7", sb.toString(), shareUrl,
							null, false,1);
					// } else {
					// myUMSDK.friendsterUMShared("打卡7",
					// sb.toString(), shareUrl, null,
					// false);
					// }
					break;
				case 2: // QQ空间
					// if (imageNum == 0){
					int i = myUMSDK.qzeroUMShared(mContext, sb.toString(),
							"打卡7", shareUrl, null,1);
					Tools.getLog(Tools.d, "aaa", "i:" + i);
					// }else{
					//
					// int i1 = myUMSDK.qzeroUMShared(mContext,
					// sb.toString(), "打卡7", shareUrl,
					// null);
					// Tools.getLog(Tools.d, "aaa", "i:"+i1);
					// }

					break;
				}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5668 && resultCode == RESULT_OK) {
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = MyUMSDK.mController.getConfig()
					.getSsoHandler(requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
			return;
		}
		Tools.getLog(Tools.d, "aaa", "设置的返回！！！！！！！！");
		if (requestCode == 111) {
			if (resultCode == 222) {
				finish();
				return;
			}
			getByIdConn();
			return;
		}

		Tools.getLog(Tools.i, "aaa", "Activity.RESULT_OK === "
				+ Activity.RESULT_OK);
		Tools.getLog(Tools.i, "aaa", "resultCode === " + resultCode);
		Bitmap bitmap = null;
		String ImageUrl = null;
		if (resultCode == Activity.RESULT_OK) {
			filenames = taskClockDialog.getFilenames();
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Tools.getLog(Tools.i, "aaa",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			if (requestCode == taskClockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
				Bitmap photo = null;
				// 根据需要，也可以加上Option这个参数
				try {
					String path = FileURl.ImageFilePath + "/"
							+ taskClockDialog.cameraPhotoName;

					int degree = ImageTool.getBitmapDegree(path);
					Tools.getLog(Tools.i, "aaa", "degree ============ "
							+ degree);

					FileInputStream in = new FileInputStream(path);

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 10;

					photo = ImageTool.rotateBitMap(
							BitmapFactory.decodeStream(in, null, options),
							degree);

					// TODO Auto-generated catch block
					if (ImageTool.saveBitmapToAlbum(TaskInfo2Activity.this,
							photo)) {
						taskClockDialog.reflashCanmera();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					ImageLoader.getInstance().clearDiskCache();
					ImageLoader.getInstance().clearMemoryCache();
				}

				// }
			} else {
				Tools.getLog(Tools.d, "aaa", "返回相册选着的照片！！！！");
				List<String> list = (List<String>) data
						.getSerializableExtra("selectimage");

				for (int j = 0; j < list.size(); j++) {

					for (int i = filenames.size() - 1; i >= 0; i--) {
						if (!filenames.get(i).getIsCheck()) {
							filenames.remove(i);
							break;
						}
					}
					ImageListBean bean = new ImageListBean();
					bean.setImageUrl(list.get(j));
					bean.setCheck(true);
					filenames.add(0, bean);
				}

				taskClockDialog.reflashList(filenames);
			}
			Tools.getLog(Tools.d, "aaa", "图片的转换：" + ImageUrl);
		}
		if (requestCode == taskClockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
			Tools.getLog(Tools.d, "aaa", "从相册选择完图片的情况：" + filenames.toString());
			for (int i = filenames.size() - 1; i >= 3; i--) {
				filenames.remove(i);
			}
		}

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

	@Override
	public View getPinnedHeader() {
		View headerView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.group, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return headerView;
	}

	public void showTitle(ByIdDetailBean byIdDetailBean) {
		title_item_content.setText("卡片详情");
		activity_task_insistClockJobContent.setText(byIdDetailBean
				.getTotalDateCount());// 修改坚持天数
		activity_task_addUpClockJobContent.setText(byIdDetailBean
				.getTotalCheckCount());// 修改累计打卡次数
		mDescription = byIdDetailBean.getDescription();
		// 为了备注可以显示出来
		myExpandableListAdapter = new MyexpandableListAdapter(this,thisJobUserid);
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
	}

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {

		if (firstVisibleGroupPos < 0) {
			return;
		} else if (firstVisibleGroupPos == 0) {
			Group firstVisibleGroup = (Group) myExpandableListAdapter
					.getGroup(firstVisibleGroupPos);
			TextView textView = (TextView) headerView.findViewById(R.id.group);
			TextView textView1 = (TextView) headerView.findViewById(R.id.day);
			textView1.setText("Day");
			textView.setText(firstVisibleGroup.getTitle());
			textView.setVisibility(View.GONE);
			textView1.setVisibility(View.GONE);

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

	private void createJobConn(String city) {
		// TODO Auto-generated method stub

		imageNum = 0;
		String url = "";
		for (int i = 0; i < filenames.size(); i++) {
			if (filenames.get(i).getIsCheck()) {
				url = FileURl.LOAD_FILE + filenames.get(i).getImageUrl();
				imageNum++;

			}
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			jobid = Tools.getUuid();
			params.add(new BasicNameValuePair("jobId", jobid));
			params.add(new BasicNameValuePair("taskId", byIdDetailBean.getId()));
			if (signature != null && signature.length() != 0) {
				params.add(new BasicNameValuePair("signature", signature));
			}
			if (quantity != null && quantity.length() != 0) {
				params.add(new BasicNameValuePair("quantity", quantity));
			}

			params.add(new BasicNameValuePair("giveUpFlag", "0"));
			params.add(new BasicNameValuePair("picNum", imageNum + ""));
			params.add(new BasicNameValuePair("checkTime", System
					.currentTimeMillis() + ""));
			params.add(new BasicNameValuePair("onlineFlag", "1"));
			params.add(new BasicNameValuePair("clientLocalTime", System
					.currentTimeMillis() + ""));
			params.add(new BasicNameValuePair("currentGiveUpFlag", "0"));

			if (city != null) {
				params.add(new BasicNameValuePair("city", city));
				params.add(new BasicNameValuePair("longitude", longitude));
				params.add(new BasicNameValuePair("latitude", latitude));
			}
			Tools.getLog(Tools.i, "aaa", "params ======================= "
					+ params.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATEJOB, null, null,
				TaskInfo2Activity.this).addList(params).request(UrlParams.POST);
		if(!settings.getBoolean("sound", false)){
			jobSound();
		}


	}

	String latitude, longitude;

	private void connBaiduLocation() {
		latitude = settings.getString("latitude", "-1");
		longitude = settings.getString("longitude", "-1");
		if (latitude.equals("-1")) {
			createJobConn(null);
		} else {
			Service.getService(Contanst.HTTP_BAIDU_LOCATION, null,
					latitude + "," + longitude, TaskInfo2Activity.this)
					.addList(null).request(UrlParams.GET);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		vibrator.cancel();
	}

	setClickHeadViewlistener clickHeadViewlistener = new setClickHeadViewlistener() {

		@Override
		public void clickHead(int position) {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.d, "aaa", "点击的position：" + position
					+ " lastTwoJobBean =========== "
					+ lastTwoJobBean.getLastThrees().size());
			if (position == -1) {
				if (byIdDetailBean.getBuildingId() != null) {
					Intent in = new Intent(mContext,
							ClockGroupInfoFragmentActivity.class);
					in.putExtra("buildingId", byIdDetailBean.getBuildingId());
					mContext.startActivity(in);
				}
			}
			return;
		}
	};
	private void updateDateColor() {
		int total = 0;
		for (int i = 0; i < byDateBeanList.size(); i++) {
			Tools.getLog(Tools.d, "aaa", "byDateBeanList.get(i).getJobCount()====="+byDateBeanList.get(i).getJobCount());

			if (byDateBeanList.get(i).getJobCount() > 0) {
				total++;
			}
		}
		if (total == 0) {
			ClockMainActivity.curMap.put(today, "0");
		} else if (total == byDateBeanList.size()) {
			// 今天的卡全部打完
			if(ClockMainActivity.curMap == null){
				return;
			}

			if ( !ClockMainActivity.curMap.get(today).equals("2")) {
				ClockMainActivity.curMap.put(today, "2");
				AllTaskFinishDialog2 allDialog = new AllTaskFinishDialog2(
						TaskInfo2Activity.this);
				allDialog.show();
				allDialog.shareImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MobclickAgent.onEvent(mContext, "mainhomepageshareclick");
						Intent in = new Intent(mContext, ShareAchievementActivity.class);
						// in.putExtra("text", sb.toString());
						//						in.putExtra("date", mClickDate.toString());
						in.putExtra("userid", userID);
						startActivity(in);
						overridePendingTransition(R.anim.slide_alpha_in_right,
								R.anim.slide_alpha_out_left);
					}
				});
			}

		} else {
			ClockMainActivity.curMap.put(today, "1");
		}
	}


}
