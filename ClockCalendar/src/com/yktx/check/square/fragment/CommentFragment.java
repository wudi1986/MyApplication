package com.yktx.check.square.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yktx.check.ClockTaskDynamicActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.ClockCommentAdapter;
import com.yktx.check.adapter.ClockCommentAdapter.BuildingInfoOnClick;
import com.yktx.check.bean.ByIdDetailBean;
import com.yktx.check.bean.CommentListBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class CommentFragment extends BaseFragment implements ServiceListener{
	private XListView xListView;
	boolean isConn, isReflush;
	public String jobid, createUserID, taskId,userID;
	RelativeLayout huifuLayout;
	ArrayList<CommentsBean> newList = new ArrayList<CommentsBean>(10);
	ClockCommentAdapter adapter;
	private RelativeLayout mTitleContent;
	AddCommentDialog dialog;
	private TextView group_info_frist_item_title,
	group_info_frist_item_content;
	private ImageView addTask;
	ByIdDetailBean byIdDetailBean;
	private RelativeLayout building_dialog_Layout,group_info_frist_item_titleLayout;
	private ImageView AnimImage;
	private TextView AnimContent, AnimVoteText;
	private ProgressBar  comment_ProgressBar;


	ClockTaskDynamicActivity mActivity;

	public SharedPreferences settings;

	public CommentFragment(ClockTaskDynamicActivity activity){
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_clock_comment, container,
				false);

		settings = mActivity.getSharedPreferences("clock", mActivity.MODE_PRIVATE);
		userID = settings.getString("userid", null);

		xListView = (XListView) view.findViewById(R.id.comment_XListView);
		comment_ProgressBar = (ProgressBar) view.findViewById(R.id.comment_ProgressBar);
		huifuLayout = (RelativeLayout) view.findViewById(R.id.huifuLayout);
		jobid = mActivity.getIntent().getStringExtra("jobid");
		createUserID = mActivity.getIntent().getStringExtra("createUserID");
		taskId = mActivity.getIntent().getStringExtra("taskId");
		//		in.putExtra("taskId", taskItemBean.getTaskId());
		group_info_frist_item_title = (TextView) view.findViewById(R.id.group_info_frist_item_title);
		group_info_frist_item_content = (TextView) view.findViewById(R.id.group_info_frist_item_content);
		addTask = (ImageView) view.findViewById(R.id.addTask);

		building_dialog_Layout = (RelativeLayout) view.findViewById(R.id.building_dialog_Layout);
		group_info_frist_item_titleLayout = (RelativeLayout) view.findViewById(R.id.group_info_frist_item_titleLayout);
		AnimImage = (ImageView) view.findViewById(R.id.building_dialog_image);
		AnimContent = (TextView) view.findViewById(R.id.building_dialog_Text1);
		AnimVoteText = (TextView) view.findViewById(R.id.building_dialog_voteContent);


		adapter = new ClockCommentAdapter(mActivity);
		adapter.setBuildingInfoOnClick(buildingInfoOnClick);
		xListView.setAdapter(adapter);
		xListView.setXListViewListener(listener);
		xListView.setIsShow(true);
		xListView.setPullGoHome(false);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		getByIdConn();
		addTask.setVisibility(View.GONE);
		AnimImage
		.setImageResource(R.drawable.guangchang_bd_pinglun);
		AnimContent.setText("评论成功!");
		AnimVoteText.setText("你将得到气球 +2");
		AnimVoteText.setVisibility(View.VISIBLE);

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
		huifuLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog = new AddCommentDialog(mActivity, null);
				dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {

					@Override
					public void onClickSuccess(String content) {
						// TODO Auto-generated method stub
						addComentConn(null, content);
					}
				});
				dialog.show();
			}
		});

		return view;
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

	private void getByIdConn() {
		//		loadingView.setVisibility(View.VISIBLE);
		StringBuffer sb = new StringBuffer();
		sb.append("?taskId=");
		sb.append(taskId);

		Service.getService(Contanst.HTTP_GETBYIDTASK, null, sb.toString(),
				CommentFragment.this).addList(null).request(UrlParams.GET);
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
		Service.getService(Contanst.HTTP_GETCOMMENT, null, sb.toString(),
				CommentFragment.this).addList(null).request(UrlParams.GET);
	}
	// 添加评论
	public void addComentConn(CommentsBean commentsBean, String text) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("jobId", jobid));
			params.add(new BasicNameValuePair("userId", userID));
			if (commentsBean == null) {
				params.add(new BasicNameValuePair("type", "1"));
				params.add(new BasicNameValuePair("pCommentId", "-1"));
				params.add(new BasicNameValuePair("repliedUserId ",
						createUserID));
			} else {
				params.add(new BasicNameValuePair("type", "2"));
				params.add(new BasicNameValuePair("pCommentId", commentsBean
						.getCommentId()));
				params.add(new BasicNameValuePair("repliedUserId", commentsBean
						.getUserId()));
			}

			params.add(new BasicNameValuePair("text", text));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATECOMMENT, null, null,
				CommentFragment.this).addList(params)
				.request(UrlParams.POST);
	}

	BuildingInfoOnClick buildingInfoOnClick = new BuildingInfoOnClick() {

		@Override
		public void clickComment(int position) {
			// TODO Auto-generated method stub
			// mClickPosition = position;
			final CommentsBean commentsBean = newList.get(position);
			String name = null;
			// if (commentsBean.getCommentType() == 2) {
			name = commentsBean.getName();
			// }
			dialog = new AddCommentDialog(mActivity, name);
			dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {

				@Override
				public void onClickSuccess(String content) {
					// TODO Auto-generated method stub
					addComentConn(commentsBean, content);
				}
			});
			dialog.show();

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

				case Contanst.GETCOMMENT:
					CommentListBean commentListBean = (CommentListBean) msg.obj;
					// 刷新附近列表
					if (isReflush) {
						newList.clear();
						currentPage = commentListBean.getCurrentPage();
						totalCount = commentListBean.getTotalCount();
						totalPage = commentListBean.getTotalPage();
						newList = commentListBean.getListData();
						Tools.getLog(Tools.d, "aaa", "================newList.sice=================="+newList.size());
						if (newList.size() == 0) {
							Tools.getLog(Tools.d, "aaa", "================没有数据==================");
							onLoad();
							comment_ProgressBar.setVisibility(View.GONE);
							return;
						}
						adapter.setList(newList);
						xListView.setPullLoadEnable(true);
						adapter.notifyDataSetChanged();
					} else {
						currentPage = commentListBean.getCurrentPage();
						totalCount = commentListBean.getTotalPage();
						totalPage = commentListBean.getTotalPage();
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
				case Contanst.CREATECOMMENT:
					isReflush = true;
					animAlertStart();
					Conn(1);
					break;
				case Contanst.GETBYIDTASK:
					byIdDetailBean = (ByIdDetailBean) msg.obj;
					showTitle();
					//					SelectClockStateConn(null, null);//分页获取当前数据
					isReflush = true;
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



	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		isConn = false;
		isReflush = false;
	}

	public void showTitle(){
		group_info_frist_item_title.setText(byIdDetailBean.getTitle());
		String description = byIdDetailBean.getDescription();
		group_info_frist_item_content.setVisibility(View.GONE);
		//		if(description != null && description.length() != 0){
		//			group_info_frist_item_content.setText(description);
		//			group_info_frist_item_content.setVisibility(View.VISIBLE);
		//		}else{
		//			group_info_frist_item_content.setVisibility(View.GONE);
		//		}
	}
	public void animAlertStart() {
		Tools.getLog(Tools.i, "aaa", "开始动画：");
		int height = building_dialog_Layout.getHeight();
		Tools.getLog(Tools.i, "aaa", "animAlertStart height ============= "
				+ height);
		TranslateAnimation animationStart = new TranslateAnimation(0, 0,
				height, 0);

		animationStart.setDuration(500L);// 设置动画持续时间
		building_dialog_Layout.setAnimation(animationStart);
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
