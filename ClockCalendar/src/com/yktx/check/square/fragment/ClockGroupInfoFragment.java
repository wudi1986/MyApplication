package com.yktx.check.square.fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.clock.service.AddShowPhotoService;
import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockThisTaskUserActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity.onClockGroupInfoFragmentResult;
import com.yktx.check.adapter.ClockGroupInfoAdapter;
import com.yktx.check.adapter.ClockGroupInfoAdapter.BuildingInfoOnClick;
import com.yktx.check.adapter.ClockGroupInfoAdapter.OnClickAdd;
import com.yktx.check.bean.BasicInfoBean;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.CreateJobBean;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.TakeClockDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.dialog.TakeClockDialog.TaskClockDialogOnCLickClockSuccess;
import com.yktx.check.listview.XListView;
import com.yktx.check.listview.XListView.IXListViewListener;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.ImageTool;
import com.yktx.check.util.Tools;
/**
 * 打卡明细
 * @author Administrator
 *
 */
@SuppressLint("ValidFragment")
public class ClockGroupInfoFragment extends BaseFragment implements ServiceListener{
	ClockGroupInfoFragmentActivity clockGroupInfoFragmentActivity;

	@SuppressLint("ValidFragment")
	public ClockGroupInfoFragment(
			ClockGroupInfoFragmentActivity Activity) {
		super();
		this.clockGroupInfoFragmentActivity = Activity;
		clockGroupInfoFragmentActivity.setOnClockGroupInfoFragmentResult(clockGroupInfoFragmentResult);
	}

	private XListView xListView;
	String latitude, longitude;
	private ImageView mBack, mTitleRightImage;
	private TextView mTitle, title_item_username;
	private String buildingId;
	/** 一页多少条数据 */
	public int pageLimit = 10;
	/** 第几页 */
	public int currentPage;
	/** 总数 */
	public int totalCount;
	/** 总页数 */
	public int totalPage;
	BasicInfoBean basicInfoBean;
	ByTaskIdBean byTaskIdBean;
	boolean isConn, isReflush;
	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	ClockGroupInfoAdapter adapter;
	int mClickPosition;
	AddCommentDialog dialog;
	private final static int BuildingRequestCode = 666;
	private RelativeLayout building_dialog_Layout;
	private TextView mBuildingText;
	private ImageView mTakeClock,mInvite;
	private TakeClockDialog clockDialog;
	private LinearLayout title_item_textLayout;
	RelativeLayout loadingView;
	private ImageView imageListNull;
	private ImageView AnimImage;
	private TextView AnimContent, AnimVoteText;
	private RelativeLayout title_item_rightLayout;

	boolean isWeiboOpen;
	private ProgressBar group_info_ProgressBar;

	RelativeLayout group_info_Layout;
	QiQiuUtils qiQiuUtils;
	FrameLayout donghua;

	View view;
	String userID;

	private SharedPreferences settings;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = clockGroupInfoFragmentActivity.getLayoutInflater().inflate(R.layout.activity_clock_group_info, null);
		settings = clockGroupInfoFragmentActivity.getSharedPreferences("clock", clockGroupInfoFragmentActivity.MODE_PRIVATE);
		userID = settings.getString("userid", null);
		findViews();
		init();
		setListeners();
		return view;
	}

	protected void findViews() {
		// TODO Auto-generated method stub


		xListView = (XListView) view.findViewById(R.id.group_info_XListView);
		mBack = (ImageView) view.findViewById(R.id.title_item_back);
		mTitle = (TextView) view.findViewById(R.id.title_item_content);
		//		mTitleRightText = (TextView) findViewById(R.id.title_item_rightText);
		mTitleRightImage = (ImageView) view.findViewById(R.id.title_item_rightImage);

		title_item_username = (TextView) view.findViewById(R.id.title_item_username);
		buildingId = clockGroupInfoFragmentActivity.getIntent().getStringExtra("buildingId");

		building_dialog_Layout = (RelativeLayout) view.findViewById(R.id.building_dialog_Layout);
		mBuildingText = (TextView) view.findViewById(R.id.building_dialog_Text);

		AnimImage = (ImageView) view.findViewById(R.id.building_dialog_image);
		AnimContent = (TextView) view.findViewById(R.id.building_dialog_Text1);
		AnimVoteText = (TextView) view.findViewById(R.id.building_dialog_voteContent);
		mTakeClock = (ImageView) view.findViewById(R.id.building_dialog_takeClock);
		//		mInvite = (ImageView) findViewById(R.id.building_dialog_inviteClock);
		title_item_textLayout = (LinearLayout) view.findViewById(R.id.title_item_textLayout);
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		imageListNull = (ImageView) view.findViewById(R.id.imageListNull);
		title_item_rightLayout = (RelativeLayout) view.findViewById(R.id.title_item_rightLayout);
		group_info_ProgressBar = (ProgressBar) view.findViewById(R.id.group_info_ProgressBar);

		group_info_Layout  = (RelativeLayout) view.findViewById(R.id.group_info_Layout);
		donghua = new FrameLayout(clockGroupInfoFragmentActivity);
		group_info_Layout.addView(donghua);
		qiQiuUtils = new QiQiuUtils(donghua, clockGroupInfoFragmentActivity);


	}

	protected void init() {
		// TODO Auto-generated method stub
		building_dialog_Layout.setVisibility(View.GONE);
		isWeiboOpen = settings.getBoolean("isWeiboOpen", false);
		// animAlertStart();
		xListView.setIsShow(true);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		// xListView.setFooterBackground(getResources().getColor(R.color.white));
		//		mTitleRightText.setVisibility(View.VISIBLE);
		CreatorConn();
	}

	protected void setListeners() {
		// TODO Auto-generated method stub

		mTakeClock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i, "aaa",
						"====================打卡=================");
				clockDialog = new TakeClockDialog(clockGroupInfoFragmentActivity);
				clockDialog.setOnClickClockSuccess(cLickClockSuccess);
				clockDialog.setTaskNameStr(basicInfoBean.getTitle());
				clockDialog.show();
				building_dialog_Layout.setVisibility(View.GONE);

			}
		});
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			}
		});
		//		mBack.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				finish();
		//			}
		//		});
		//		title_item_rightLayout.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				Intent in = new Intent(mContext, ClockThisTaskUserActivity.class);
		//				in.putExtra("buildingid", buildingId);
		//				startActivity(in);
		//			}
		//		});
	}

	BuildingInfoOnClick buildingInfoOnClick = new BuildingInfoOnClick() {
		// 点赞
		@Override
		public void clickVote(String jobid, int position, String type,int x, int y) {
			// TODO Auto-generated method stub
			if(isConn){
				Tools.getLog(Tools.d, "aaa", "点赞 不要点了啊！");
				return;
			}
			isConn = true;
			mClickPosition = position;
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

		// 评论
		@Override
		public void clickComment(final TaskItemBean taskItemBean, int position,
				final int itemBeanIndex) {
			// TODO Auto-generated method stub
			mClickPosition = position;
			String name = null;
			if (itemBeanIndex != -1) {
				name = taskItemBean.getComments().get(itemBeanIndex).getName();
			}
			dialog = new AddCommentDialog(clockGroupInfoFragmentActivity, name);
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
				ClockGroupInfoFragment.this).addList(params)
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
				ClockGroupInfoFragment.this).addList(params)
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
				ClockGroupInfoFragment.this).addList(params)
				.request(UrlParams.POST);
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

	public void CreatorConn() {
		StringBuffer sb = new StringBuffer();
		if (userID != null && userID.length() != 0) {
			sb.append("?userId=");
			sb.append(userID);
		}
		sb.append("&buildingId=");
		sb.append(buildingId);

		Service.getService(Contanst.HTTP_GETBASICINFO, null, sb.toString(),
				ClockGroupInfoFragment.this).addList(null)
				.request(UrlParams.GET);

	}

	public void Conn(int currentPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&userId=");
		sb.append(userID);
		sb.append("&buildingId=");
		sb.append(buildingId);
		Service.getService(Contanst.HTTP_GETFLOOR, null, sb.toString(),
				ClockGroupInfoFragment.this).addList(null)
				.request(UrlParams.GET);
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

				case Contanst.GETBASICINFO:
					basicInfoBean = (BasicInfoBean) msg.obj;
					Tools.getLog(Tools.i, "aaa",
							"==============taskid:============="
									+ basicInfoBean.toString());
					showView();
					Conn(1);
					isConn = true;
					isReflush = true;
					break;
				case Contanst.GETFLOOR:
					byTaskIdBean = (ByTaskIdBean) msg.obj;
					group_info_ProgressBar.setVisibility(View.VISIBLE);
					// 刷新附近列表
					if (isReflush) {
						currentPage = 1;
						newList.clear();
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						currentPage = bean.getCurrentPage();
						totalCount = bean.getTotalCount();
						totalPage = bean.getTotalPage();
						newList = bean.getListData();
						Tools.getLog(Tools.d, "aaa",
								"newList数量L" + newList.size());
						if (newList.size() == 0) {
							imageListNull.setVisibility(View.VISIBLE);
							xListView.setPullLoadEnable(false);
							group_info_ProgressBar.setVisibility(View.GONE);
							onLoad();
							return;
						}
						imageListNull.setVisibility(View.GONE);
						adapter.setList(newList);
						xListView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();
					} else {
						currentPage++;
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						newList.addAll(bean.getListData());
						adapter.setList(newList);
						adapter.notifyDataSetChanged();
					}
					onLoad();
					if (totalCount <= 10 || currentPage * 10 >= totalCount) {
						xListView.setIsShow(false);
					} else {
						xListView.setIsShow(true);
					}
					Tools.getLog(Tools.d, "aaa",
							"加载完的List" + newList.toString());
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
					adapter.setList(newList);
					adapter.notifyDataSetChanged();
					// newList = adapter.getList();
					AnimImage
					.setImageResource(R.drawable.guangchang_bd_pinglun);
					AnimContent.setText("评论成功!");
					AnimVoteText.setText("你将得到气球 +2");
					AnimVoteText.setVisibility(View.VISIBLE);
					mTakeClock.setVisibility(View.GONE);
					animAlertStart();
					break;
				case Contanst.CREATEVOTE:
					isConn = false;
					// 添加点赞
					@SuppressWarnings("unchecked")
					ArrayList<VotesBean> votesBeans = (ArrayList<VotesBean>) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"点赞成功返回:" + votesBeans.toString());
					TaskItemBean itemBean1 = newList.get(mClickPosition);
					newList.get(mClickPosition).setVoteCount(
							(itemBean1.getVoteCount() + 1));
					newList.get(mClickPosition).setVoted("1");
					newList.get(mClickPosition).setVotes(votesBeans);
					newList.get(mClickPosition).setClickMore(true);
					adapter.setList(newList);
					adapter.notifyDataSetChanged();
					if(!itemBean1.getUserId().equals(userID)){
						AnimImage
						.setImageResource(R.drawable.guangchang_bd_daqichenggong);
						AnimContent.setText("打气成功!");
						AnimVoteText.setText("你将得到气球 +1");
						AnimVoteText.setVisibility(View.VISIBLE);
						mTakeClock.setVisibility(View.GONE);
						//					mInvite.setVisibility(View.GONE);
						animAlertStart();
					}
					//					mInvite.setVisibility(View.GONE);
					animAlertStart();
					// newList = adapter.getList();
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
					adapter.setList(newList);
					adapter.notifyDataSetChanged();

					// newList = adapter.getList();
					break;
				case Contanst.CREATEJOB:
					final CreateJobBean createJobBean = (CreateJobBean) msg.obj;
					Tools.getLog(Tools.i, "aaa", "打卡成功！！！！！！！！");
					if (filenames.size() > 0) {
						Intent intent_baonew = new Intent(
								clockGroupInfoFragmentActivity,
								AddShowPhotoService.class);
						intent_baonew.putExtra("state",
								AddShowPhotoService.AddShowPhotoRun);
						intent_baonew.putExtra("productid", userID);
						intent_baonew.putExtra("list", filenames);
						intent_baonew.putExtra("uuid", jobid);
						intent_baonew.putExtra("type", 1);
						clockGroupInfoFragmentActivity.startService(intent_baonew);
					}
					Conn(1);
					isReflush = true;
					isConn = true;
					break;
				case Contanst.CREATETASK:
					CreatorConn();// 刷新task的信息，获得taskid进行打卡
					// addDialog = new
					// BuildingAddDialog(ClockGroupInfoActivity.this,basicInfoBean.getTitle());
					// addDialog.setOnClickTwoButton(clickTwoButton);
					// addDialog.show();
					AnimImage.setImageResource(R.drawable.xinjian_tuijian_tixing);
					AnimContent.setText("添加成功!");
					AnimVoteText.setVisibility(View.GONE);
					mTakeClock.setVisibility(View.VISIBLE);
					//					mInvite.setVisibility(View.VISIBLE);
					animAlertStart();
					break;
				}
				break;

			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.BAIDU_LOCATION:
					createJobConn((String) msg.obj);
					break;
				case Contanst.GETBASICINFO:
					Tools.getLog(Tools.d, "aaa", "GETBASICINFO:" + message);
					break;
				case Contanst.GETFLOOR:
					imageListNull
					.setImageResource(R.drawable.zhanwei_wuwangluo);
					xListView.setAdapter(adapter);
					xListView.setPullLoadEnable(false);
					adapter.notifyDataSetChanged();
					// newList = adapter.getList();
					Tools.getLog(Tools.i, "aaa", "message = " + message);
					onLoad();
					break;

				}
				break;
			}
		}
	};

	public void showView() {
		Tools.getLog(Tools.d, "aaa", "basicInfoBean.getTitle():"
				+ basicInfoBean.getTitle());
		Tools.getLog(Tools.d, "aaa", "basicInfoBean.getDescription():"
				+ basicInfoBean.getDescription());
		adapter = new ClockGroupInfoAdapter(clockGroupInfoFragmentActivity, basicInfoBean,userID);

		adapter.setBuildingOnClick(buildingInfoOnClick);
		adapter.setOnClickAdd(onClickAdd);

		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
		swingBottomInAnimationAdapter.setAbsListView(xListView);

		xListView.setAdapter(swingBottomInAnimationAdapter);

		xListView.setXListViewListener(listener);
		// mBuildingText.setText(basicInfoBean.getTitle());//添加成功出来的提示框
		mTitle.setText("卡片");// basicInfoBean.getTitle()+""
		title_item_username.setText("创建者:" + basicInfoBean.getCreatorName());

		//		if (basicInfoBean.getAdded().equals("1")) {
		//		mTitleRightText.setText(basicInfoBean.getTaskCount());

		// title_item_username.setTextColor(getResources().getColor(R.color.meibao_color_12));
		//		} else {
		//			mTitleRightText.setText("添加");
		//			// title_item_username.setTextColor(getResources().getColor(R.color.meibao_color_1));
		//		}
		title_item_textLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (basicInfoBean.getCreatorId().equals(userID)) {
					Intent in = new Intent(clockGroupInfoFragmentActivity, ClockMyActivity.class);
					clockGroupInfoFragmentActivity.startActivity(in);
				} else {
					Intent in = new Intent(clockGroupInfoFragmentActivity,
							ClockOtherUserActivity.class);
					in.putExtra("userid", basicInfoBean.getCreatorId());
					clockGroupInfoFragmentActivity.startActivity(in);
				}
			}
		});
		//		mTitleRightText.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				
		//			}
		//		});
		TextView title = (TextView) view.findViewById(R.id.group_info_frist_item_title);
		TextView content = (TextView) view.findViewById(R.id.group_info_frist_item_content);
		TextView group_info_frist_item_taskInfo = (TextView) view.findViewById(R.id.group_info_frist_item_taskInfo);
		ImageView addTask = (ImageView) view.findViewById(R.id.addTask);
		if (basicInfoBean.getAdded().equals("1")) {
			addTask.setVisibility(View.GONE);
			group_info_frist_item_taskInfo.setVisibility(View.VISIBLE);
			// title_item_username.setTextColor(getResources().getColor(R.color.meibao_color_12));
		} else {
			addTask.setVisibility(View.VISIBLE);
			group_info_frist_item_taskInfo.setVisibility(View.GONE);
			// title_item_username.setTextColor(getResources().getColor(R.color.meibao_color_1));
		}
		addTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (onClickAdd != null) {
					onClickAdd.clickAdd();
				}
			}
		});
		group_info_frist_item_taskInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(clockGroupInfoFragmentActivity,
						TaskInfoActivity.class);
				in.putExtra("taskid",basicInfoBean.getMyTaskId());
				in.putExtra("userid", userID);
				clockGroupInfoFragmentActivity.startActivity(in);
			}
		});
		title.setText(basicInfoBean.getTitle());
		content.setText(basicInfoBean.getTaskCount()+"人  打卡"+basicInfoBean.getTotalJobCount()+"次");
	}

	OnClickAdd onClickAdd = new OnClickAdd() {

		@Override
		public void clickAdd() {
			// TODO Auto-generated method stub
			Intent intent;
			if (basicInfoBean.getAdded().equals("1")) {
				intent = new Intent(clockGroupInfoFragmentActivity, TaskInfoActivity.class);
				intent.putExtra("taskid", basicInfoBean.getMyTaskId());
				intent.putExtra("totalCheckCount",
						basicInfoBean.getTotalCheckCount());// 打卡次数
				intent.putExtra("totalDateCount",
						basicInfoBean.getTotalDateCount());
				intent.putExtra("title", basicInfoBean.getTitle());
				intent.putExtra("description", basicInfoBean.getDescription());
				startActivity(intent);
			} else {
				// intent = new Intent(mContext, ClockNewActivity.class);
				// intent.putExtra("bean", basicInfoBean);
				// startActivityForResult(intent, BuildingRequestCode);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				try {
					params.add(new BasicNameValuePair("taskId",Tools.getUuid()));// UUID.randomUUID().toString())
					params.add(new BasicNameValuePair("userId", userID));// UUID.randomUUID().toString())
					params.add(new BasicNameValuePair("title",
							basicInfoBean.getTitle()));
					params.add(new BasicNameValuePair("description",
							basicInfoBean.getDescription()));
					params.add(new BasicNameValuePair("privateFlag", "0"));
					String isCheckTime = basicInfoBean.getTime_limit_flag();
					params.add(new BasicNameValuePair("timeLimitFlag",
							isCheckTime));
					if(settings.getBoolean("isWeiboOpen", false)){
						params.add(new BasicNameValuePair("autoShareFlag", 1+""));
					} else {
						params.add(new BasicNameValuePair("autoShareFlag", 0+""));
					}
					if (isCheckTime.equals("1")) {
						params.add(new BasicNameValuePair("beginTime",
								basicInfoBean.getBegin_time()));
						params.add(new BasicNameValuePair("endTime",
								basicInfoBean.getEnd_time()));
					}
					params.add(new BasicNameValuePair("onlineFlag", 1 + ""));
					// params.add(new BasicNameValuePair("offLineCTime",
					// ""));
				} catch (Exception e) {

				}
				Service.getService(Contanst.HTTP_CREATETASK, null, null,
						ClockGroupInfoFragment.this).addList(params)
						.request(UrlParams.POST);
			}
		}
	};

	private void onLoad() {
		if (loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
		xListView.stopRefresh();
		xListView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}

	// onClickTwoButton clickTwoButton = new onClickTwoButton() {
	//
	// @Override
	// public void onClickInvite() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onClickAdd() {
	// // TODO Auto-generated method stub
	// clockDialog = new TakeClockDialog(ClockGroupInfoActivity.this);
	// clockDialog.setOnClickClockSuccess(cLickClockSuccess);
	// clockDialog.show();
	// animAlert(false);
	// }
	// };
	String jobid;
	String curContent, curNum;
	TaskClockDialogOnCLickClockSuccess cLickClockSuccess = new TaskClockDialogOnCLickClockSuccess() {

		@Override
		public void onClickSuccess(String content, String num, String unit,
				ArrayList<ImageListBean> list) {
			// TODO Auto-generated method stub
			filenames = list;
			curContent = content;
			curNum = num;
			connBaiduLocation();
		}
	};

	private void createJobConn(String city) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			jobid = Tools.getUuid();
			params.add(new BasicNameValuePair("jobId", jobid));
			params.add(new BasicNameValuePair("taskId", basicInfoBean
					.getMyTaskId()));
			if (curContent != null && curContent.length() != 0) {
				params.add(new BasicNameValuePair("signature", curContent));
			}
			if (curNum != null && curNum.length() != 0) {
				params.add(new BasicNameValuePair("quantity", curNum));
			}
			params.add(new BasicNameValuePair("giveUpFlag", "0"));
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
				ClockGroupInfoFragment.this).addList(params)
				.request(UrlParams.POST);
	}

	InputStream inputStream = null;
	boolean isTakePicture;
	// BuildingAddDialog addDialog;
	public ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(3);// 相册选取的集合的名字

	onClockGroupInfoFragmentResult clockGroupInfoFragmentResult = new onClockGroupInfoFragmentResult() {
		
		@Override
		public void setResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.i, "aaa", "Activity.RESULT_OK === "
					+ Activity.RESULT_OK);
			Tools.getLog(Tools.i, "aaa", "resultCode === " + resultCode);
			Bitmap bitmap = null;
			String ImageUrl = null;
			if (resultCode == Activity.RESULT_OK) {
				filenames = clockDialog.getFilenames();
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Tools.getLog(Tools.i, "aaa",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				if (requestCode == clockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
					isTakePicture = true;
					// if (data != null) {
					// Uri selectedImage = data.getData();
					// // Tools.getLog(Tools.d, "aaa",
					// // "imageUrl:" + selectedImage.getPath());
					// ContentResolver resolver = getContentResolver();
					Bitmap photo = null;
					// 根据需要，也可以加上Option这个参数
					try {
						// if (selectedImage != null) {
						// // // sendPicByUri(selectedImage);
						// photo = MediaStore.Images.Media.getBitmap(resolver,
						// selectedImage);
						//
						// } else {
						// Bundle extras = data.getExtras();
						// if (extras != null) {
						// // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
						// photo = extras.getParcelable("data");
						// }
						//
						// }
						// if (photo != null) {
						// // String str =
						//
						// if (photo.getWidth() > photo.getHeight()) {
						// photo = ImageTool.rotateBitMap(photo, 1);
						// }
						//
						// if (ImageTool.saveBitmapToAlbum(
						// ClockMainActivity.this, photo)) {
						// dialog.reflashCanmera();
						// }
						//
						// return;
						// }

						// File f = new File(FileURl.ImageFilePath
						// + dialog.cameraPhotoName);
						String path = FileURl.ImageFilePath + "/"
								+ clockDialog.cameraPhotoName;

						int degree = ImageTool.getBitmapDegree(path);
						Tools.getLog(Tools.i, "aaa", "degree ============ "
								+ degree);

						FileInputStream in = new FileInputStream(path);

						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 10;

						photo = ImageTool.rotateBitMap(
								BitmapFactory.decodeStream(in, null, options),
								degree);

						// Uri u = Uri
						// .parse(android.provider.MediaStore.Images.Media
						// .insertImage(getContentResolver(),
						// f.getAbsolutePath(), null, null));
						// u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
						// TODO Auto-generated catch block
						if (ImageTool.saveBitmapToAlbum(
								clockGroupInfoFragmentActivity, photo)) {
							clockDialog.reflashCanmera();
						}

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OutOfMemoryError e) {
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

					// for (int i = 0; i < list.size(); i++) {
					// ImageListBean bean = new ImageListBean();
					// bean.setImageUrl(FileURl.LOAD_FILE + list.get(i));
					// bean.setCheck(true);
					// filenames.add(0, bean);
					// }
					clockDialog.reflashList(filenames);
				}
				Tools.getLog(Tools.d, "aaa", "图片的转换：" + ImageUrl);
			}
			if (requestCode == clockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
				Tools.getLog(Tools.d, "aaa", "从相册选择完图片的情况：" + filenames.toString());
				for (int i = filenames.size() - 1; i >= 3; i--) {
					filenames.remove(i);
				}
				// mSetImage.setImage(filenames, isTakePicture);
			}
		}
	};
	

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

	private void connBaiduLocation() {
		latitude = settings.getString("latitude", "-1");
		longitude = settings.getString("longitude", "-1");
		if (latitude.equals("-1")) {
			createJobConn(null);
		} else {
			Service.getService(Contanst.HTTP_BAIDU_LOCATION, null,
					latitude + "," + longitude, ClockGroupInfoFragment.this)
					.addList(null).request(UrlParams.GET);
		}
	}








}
