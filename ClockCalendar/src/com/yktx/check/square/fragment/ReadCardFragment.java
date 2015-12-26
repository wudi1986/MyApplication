package com.yktx.check.square.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chiemy.cardview.view.CardAdapter;
import com.chiemy.cardview.view.CardAdapter.OnClickVoteOrComments;
import com.chiemy.cardview.view.CardView;
import com.duguang.baseanimation.ui.listivew.deletelistview.QiQiuUtils;
import com.yktx.check.BaseActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.R;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.dialog.AddCommentDialog;
import com.yktx.check.dialog.AddCommentDialog.onCLickCommentSuccess;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public class ReadCardFragment extends BaseFragment implements ServiceListener {

	int imageLayoutHeight;

	TextView readcard_fragment_item_PageNum, readcard_fragment_item_typeAll, readcard_fragment_item_typeHaveImage,
			readcard_fragment_item_fanstypeText;

	ImageView readcard_fragment_item_fanstypeImage, readcard_fragment_item_daqiTopImage;
	RelativeLayout readcard_fragment_item;

	AddCommentDialog dialog;
	
	FrameLayout donghua;

	public static int qiqiuX, qiqiuY;
	
	private int NowPage;
	/** 是否联网 */
	boolean isConn;
	private View view;
	SharedPreferences settings;
	Editor mEditor;
	String userID, thisJobName;
	boolean isReflush;
	public static boolean isNewLoadAgain = true;
	RelativeLayout loadingView;
	ArrayList<TaskItemBean> newList = new ArrayList<TaskItemBean>(10);
	// ReadCradFragmentListViewAdapter adapter;
	private Activity context;
	private ImageView readcard_fragment_item_daqiImage, readcard_fragment_item_nextJobImage;
	private boolean isNetWork;
	int type = 0;// 有没有图 0 无图（默认） 1 有图
	boolean isFans = false;
	boolean isShowDaqi = false;

	public ReadCardFragment() {
		super();
	}

	private RelativeLayout building_dialog_Layout;
	private ImageView AnimImage;
	private TextView AnimContent, AnimVoteText;
	CardView contentLayout;
	CardAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = ReadCardFragment.this.getActivity();
		settings =context.getSharedPreferences("clock", context.MODE_PRIVATE);
		mEditor = settings.edit();
		userID = settings.getString("userid", null);
		isShowDaqi = settings.getBoolean("isshowdaqi", false);
	
		view = inflater.inflate(R.layout.readcard_fragment, container, false);
		if (!isNetWork) {
			isReflush = true;
			isNewLoadAgain = false;
			isNetWork = true;
			Conn(1, 0);
		}

		// loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		readcard_fragment_item_daqiImage = (ImageView) view.findViewById(R.id.readcard_fragment_item_daqiImage);
		readcard_fragment_item_nextJobImage = (ImageView) view.findViewById(R.id.readcard_fragment_item_nextJobImage);
		loadingView = (RelativeLayout) view.findViewById(R.id.loadingView);
		contentLayout = (CardView) view.findViewById(R.id.contentLayout);
		findByID();
		listener();
		
		donghua = new FrameLayout(context);
		readcard_fragment_item.addView(donghua);
		qiQiuUtils = new QiQiuUtils(donghua, context);
		AnimImage.setImageResource(R.drawable.guangchang_bd_daqichenggong);
		AnimContent.setText("打气成功!");
		AnimVoteText.setText("你将得到气球 +1");
		AnimVoteText.setVisibility(View.VISIBLE);
		return view;
	}

	QiQiuUtils qiQiuUtils;

	public void listener() {
		readcard_fragment_item_daqiImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if (isNetWork) {
				// return;
				// }
				// isNetWork = true;
				// if (newList.get(NowPage)
				// .getVoted().equals("0")) {
				// addVoteConn(newList.get(NowPage).getJobId());
				// } else {
				// CancelVoteConn(newList.get(NowPage).getJobId());
				// }
				
				qiQiuUtils.startFly((int)(qiqiuX-50*BaseActivity.DENSITY), (int)(qiqiuY+getStatusBarHeight()+16*BaseActivity.DENSITY));
				if (isNetWork) {
					return;
				}
				isNetWork = true;
				if (newList.get(NowPage).getVoted().equals("0")) {
					addVoteConn(newList.get(NowPage).getJobId());
				} else {
					AnimImage.setImageResource(R.drawable.guangchang_bd_daqichenggong);
					AnimContent.setText("你已经为Ta打气加油过了哦！");
					// AnimVoteText.setText("你将得到气球 +1");
					AnimVoteText.setVisibility(View.GONE);
					animAlertStart();
					isNetWork = false;
				}

			}
		});
		readcard_fragment_item_nextJobImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO Auto-generated method stub

				qiQiuUtils.release();
				if (isNetWork) {
					return;
				}
				if (NowPage + 1 >= totalCount) {
					Toast.makeText(context, "少侠，到底了！", Toast.LENGTH_SHORT).show();
					return;
				}
				int index = contentLayout.goDown();
				if (index == -1)
					return;
				NowPage ++;
				readcard_fragment_item_PageNum.setText("今日最新：" + (NowPage + 1) + "/" + totalCount);
				if(NowPage >=newList.size()){
					return;
				}
				if (newList.get(NowPage).getVoted().equals("0")) {
					readcard_fragment_item_daqiImage.setBackgroundResource(R.drawable.daqi_select);
				} else {
					readcard_fragment_item_daqiImage.setBackgroundResource(R.drawable.daqi_select2);
				}
				// AlphaAnimation mAlphaAnimation = new AlphaAnimation(
				// 1.0f, 0f);
				// // 第一个参数fromAlpha 为动画开始时候透明度
				// // 第二个参数toAlpha 为动画结束时候透明度
				// // 注意：取值范围[0-1];[完全透明-完全不透明]
				// mAlphaAnimation.setDuration(500L);
				// mAlphaAnimation
				// .setAnimationListener(new AnimationListener() {
				//
				// @Override
				// public void onAnimationStart(Animation arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void onAnimationRepeat(Animation arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void onAnimationEnd(Animation arg0) {
				// // TODO Auto-generated method stub

				if (NowPage % 6 == 0) {
					isNetWork = true;
					isReflush = false;
					Conn(currentPage + 1, 0);
				}
				AlphaAnimation mAlphaAnimation1 = new AlphaAnimation(0f, 1.0f);
				// 第一个参数fromAlpha 为动画开始时候透明度
				// 第二个参数toAlpha 为动画结束时候透明度
				// 注意：取值范围[0-1];[完全透明-完全不透明]
				mAlphaAnimation1.setDuration(500);
				// }
				// });
				// //设置时间持续时间为3000 毫秒=3秒

			}
		});
		readcard_fragment_item_typeAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (type == 0) {
					return;
				}
				if (isConn) {
					Toast.makeText(context, "您的动作太快了，正在获取数据，请稍后", Toast.LENGTH_SHORT).show();
					return;
				}
				isConn = true;
				isReflush = true;
				type = 0;
				NowPage = 0;
				readcard_fragment_item_typeAll.setTextColor(getResources().getColor(R.color.meibao_color_1));
				readcard_fragment_item_typeHaveImage.setTextColor(getResources().getColor(R.color.meibao_color_14));

				Conn(1, 0);

			}
		});
		readcard_fragment_item_typeHaveImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stu

				if (type == 1) {
					return;
				}
				if (isConn) {
					Toast.makeText(context, "您的手速太快了，正在获取数据，请稍后", Toast.LENGTH_SHORT).show();
					return;
				}
				isConn = true;
				isReflush = true;
				type = 1;
				NowPage = 0;
				readcard_fragment_item_typeAll.setTextColor(getResources().getColor(R.color.meibao_color_14));
				readcard_fragment_item_typeHaveImage.setTextColor(getResources().getColor(R.color.meibao_color_1));
				Conn(1, 0);
			}
		});
	}
	/**
	 * @return 状态栏高度
	 */
	public int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(
				Resources.getSystem().getIdentifier("status_bar_height",
						"dimen", "android"));
	}
	public void findByID() {

		readcard_fragment_item_daqiTopImage = (ImageView) view.findViewById(R.id.readcard_fragment_item_daqiTopImage);
		// if (isShowDaqi) {
		// readcard_fragment_item_daqiTopImage.setVisibility(View.INVISIBLE);
		// } else {
		// readcard_fragment_item_daqiTopImage.setVisibility(View.VISIBLE);
		// }

		readcard_fragment_item = (RelativeLayout) view.findViewById(R.id.readcard_fragment_item);

		readcard_fragment_item_PageNum = (TextView) view.findViewById(R.id.readcard_fragment_item_PageNum);
		readcard_fragment_item_typeAll = (TextView) view.findViewById(R.id.readcard_fragment_item_typeAll);
		readcard_fragment_item_typeHaveImage = (TextView) view.findViewById(R.id.readcard_fragment_item_typeHaveImage);

		building_dialog_Layout = (RelativeLayout) view.findViewById(R.id.building_dialog_Layout);
		AnimImage = (ImageView) view.findViewById(R.id.building_dialog_image);
		AnimContent = (TextView) view.findViewById(R.id.building_dialog_Text1);
		AnimVoteText = (TextView) view.findViewById(R.id.building_dialog_voteContent);

	}

	public void Conn(int currentPage, long send_time) {
		StringBuffer sb = new StringBuffer();
		sb.append("?currentPage=");
		sb.append(currentPage);
		sb.append("&pageLimit=");
		sb.append(pageLimit);
		sb.append("&userId=");
		sb.append(userID);
		sb.append("&withPicFlag=");
		sb.append(type + "");
		// if(type == 0){
		// sb.append(type+"");
		// }else{
		// sb.append(type+"0");
		// }

		Service.getService(Contanst.HTTP_BUILDING_VIEWTODAYCARD, null, sb.toString(), ReadCardFragment.this).addList(null).request(UrlParams.GET);
	}

	public void connFollow(String thisJobUsedID) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", thisJobUsedID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_FOLLOW, null, null, ReadCardFragment.this).addList(params).request(UrlParams.POST);
	}

	public void connUnFollow(String thisJobUsedID) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("followedUserId", thisJobUsedID));
			params.add(new BasicNameValuePair("fansUserId", userID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_USER_UNFOLLOW, null, null, ReadCardFragment.this).addList(params).request(UrlParams.POST);
	}

	// 评论。。
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
		Service.getService(Contanst.HTTP_CREATECOMMENT, null, null, ReadCardFragment.this).addList(params).request(UrlParams.POST);
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
		Service.getService(Contanst.HTTP_CREATEVOTE, null, null, ReadCardFragment.this).addList(params).request(UrlParams.POST);
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
		Service.getService(Contanst.HTTP_CANCELVOTE, null, null, ReadCardFragment.this).addList(params).request(UrlParams.POST);
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

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.BUILDING_VIEWTODAYCARD:
					isConn = false;
					Tools.getLog(Tools.d, "ccc", "111111111111");
					// 刷新附近列表
					if (isReflush) {
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						currentPage = bean.getCurrentPage();
						totalCount = bean.getTotalCount();
						totalPage = bean.getTotalPage();
						newList = new ArrayList<TaskItemBean>(10);
						newList = bean.getListData();
						readcard_fragment_item_PageNum.setText("今日最新：" + (NowPage + 1) + "/" + totalCount);

						Tools.getLog(Tools.d, "ccc", "222222222222");
						if (newList.size() == 0) {
							// imageListNull.setVisibility(View.VISIBLE);
							return;
						}
						adapter = new CardAdapter(context, userID);
						adapter.setOnClickVoteOrComments(onClickVoteOrComments);
						adapter.setList(newList);
						contentLayout.setAdapter(adapter, 0);
					} else {
						currentPage++;
						Tools.getLog(Tools.d, "ccc", "333333333333");
						ByTaskIdBean bean = (ByTaskIdBean) msg.obj;
						newList.addAll(bean.getListData());
						Tools.getLog(Tools.d, "ccc", "newList.toString():" + newList.toString());
						// NowPage = currentPage*10-currentPage;
						// TaskItemBean itemBean = newList.get(NowPage);
						// initView(itemBean);
						adapter.setList(newList);
						adapter.notifyDataSetChanged();
					}

					Tools.getLog(Tools.d,"aaa", "newList ============= " + newList.size());

					if (loadingView.getVisibility() != View.GONE) {// 隐藏加载图
						loadingView.setVisibility(View.GONE);
					}

					isNewLoadAgain = false;// 刷新成功就不可以刷
					break;
				case Contanst.CREATEVOTE:// 点赞
					Tools.getLog(Tools.d,"aaa", "newList.get(NowPage).getVoteCount() ========= " + newList.get(NowPage).getVoteCount());
					newList.get(NowPage).setVoteCount(newList.get(NowPage).getVoteCount() + 1);
					newList.get(NowPage).setVoted("1");
					Tools.getLog(Tools.d,"aaa", "newList.get(NowPage).getVoteCount() ========= " + newList.get(NowPage).getVoteCount());
					adapter.setList(newList);
					contentLayout.setAdapter(adapter, NowPage);
					readcard_fragment_item_daqiImage.setBackgroundResource(R.drawable.daqi_select2);
					AnimImage.setImageResource(R.drawable.guangchang_bd_daqichenggong);
					AnimContent.setText("打气成功!");
					AnimVoteText.setText("你将得到气球 +1");
					AnimVoteText.setVisibility(View.VISIBLE);
					animAlertStart();
					break;
				case Contanst.CREATECOMMENT:
					newList.get(NowPage).setCommentCount(newList.get(NowPage).getCommentCount() + 1);
					adapter.setList(newList);
					contentLayout.setAdapter(adapter, NowPage);
					AnimImage.setImageResource(R.drawable.guangchang_bd_pinglun);
					AnimContent.setText("评论成功!");
					AnimVoteText.setText("你将得到气球 +2");
					AnimVoteText.setVisibility(View.VISIBLE);
					animAlertStart();
					break;
				case Contanst.USER_FOLLOW:
					Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
					newList.get(NowPage).setRelation(2);
					adapter.setList(newList);
					contentLayout.setAdapter(adapter, NowPage);
					break;
				case Contanst.CANCELVOTE:// 取消点赞
					newList.get(NowPage).setVoteCount(newList.get(NowPage).getVoteCount() - 1);
					readcard_fragment_item_daqiImage.setBackgroundResource(R.drawable.daqi_select);
					break;

				case Contanst.USER_UNFOLLOW:
					Toast.makeText(context, "取消关注," + thisJobName, Toast.LENGTH_SHORT).show();
					newList.get(NowPage).setRelation(0);
					
					break;

				}
				isNetWork = false;
				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;

				switch (msg.arg1) {
				case Contanst.BUILDING_VIEWTODAYCARD:
					isConn = false;
					break;
				}
				isNetWork = false;
				break;
			}
		}
	};

	OnClickVoteOrComments onClickVoteOrComments = new OnClickVoteOrComments() {

		@Override
		public void clickVote(int position) {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			if (isNetWork||position != NowPage) {
				return;
			}
			qiQiuUtils.startFly((int)(qiqiuX-50*BaseActivity.DENSITY), (int)(qiqiuY+getStatusBarHeight()+16*BaseActivity.DENSITY));
			isNetWork = true;
			Tools.getLog(Tools.d, "aaa", "paoition:====="+position);
			if (newList.get(position).getVoted().equals("0")) {
				addVoteConn(newList.get(position).getJobId());
			} else {
				AnimImage.setImageResource(R.drawable.guangchang_bd_daqichenggong);
				AnimContent.setText("你已经为Ta打气加油过了哦！");
				// AnimVoteText.setText("你将得到气球 +1");
				AnimVoteText.setVisibility(View.GONE);
				animAlertStart();
				isNetWork = false;
			}

		}

		@Override
		public void clickComments(final int position) {
			// TODO Auto-generated method stub
			if (isNetWork||position != NowPage) {
				return;
			}
			isNetWork = true;
			dialog = new AddCommentDialog(context, null);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setOnClickCommentSuccess(new onCLickCommentSuccess() {

				@Override
				public void onClickSuccess(String content) {
					// TODO Auto-generated method stub
					addComentConn(newList.get(position).getJobId(), content);
				}
			});
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					if(isNetWork){//可能会没有发送就给关闭了，状态还在联网的状态下。
						isNetWork = false;
					}
				}
			});
			dialog.show();

		}

		@Override
		public void clickFollow(String userId) {
			// TODO Auto-generated method stub
			//为什么不写 取消的    关注就把关注按钮隐藏了
			connFollow(userId);
		}
	};

	private void imageBrower(int position, String[] urls, String[] imageSource) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_SOURCE, imageSource);

		context.startActivity(intent);
	}

	/**
	 * 全角转换为半角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Tools.getLog(Tools.d, "ccc", "销毁阅卡fragment");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Tools.getLog(Tools.d, "aaa", "查看阅卡fragment");
		// }
	}

	private int getImageHeight(int width, int height) {
		if (width > height) {
			return (int) (204 * BaseActivity.DENSITY * height / width);
		} else {
			return (int) (204 * BaseActivity.DENSITY);
		}
	}

	boolean animAlertIsShow;

	public void animAlertStart() {
		if (building_dialog_Layout.getVisibility() == View.VISIBLE) {
			return;
		}
		Tools.getLog(Tools.i, "aaa", "开始动画：");
		int height = building_dialog_Layout.getHeight();
		Tools.getLog(Tools.i, "aaa", "animAlertStart height ============= " + height);
		TranslateAnimation animationStart = new TranslateAnimation(0, 0, height, 0);

		animationStart.setDuration(500);// 设置动画持续时间
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
						Animation mAnimation = new TranslateAnimation(0, 0, 0, height);
						mAnimation.setDuration(500L);
						// building_dialog_Layout.setAnimation(mAnimation);
						building_dialog_Layout.startAnimation(mAnimation);

						mAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.i, "aaa", "onAnimationStartonAnimationStartonAnimationStartonAnimationStart");
								building_dialog_Layout.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.i, "aaa", "onAnimationEndonAnimationEnd");

								building_dialog_Layout.setVisibility(View.GONE);

							}
						});
					}
				}, 5000);

			}
		});

	}
}
