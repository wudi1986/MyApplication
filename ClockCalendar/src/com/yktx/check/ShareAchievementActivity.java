package com.yktx.check;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.Tools;

public class ShareAchievementActivity extends BaseActivity implements
ServiceListener {
	private ImageView activity_share_weibo, activity_share_friendster,
	activity_share_qzeon;
	private EditText activity_share_input;
	private ImageView shareImage, share_delete;
	private MyUMSDK myShare;
	String textInfo;
	String date;
	boolean isShareImage;
	Bitmap shareBitmap;
	String shareUrl,thisActivityUserID;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub

		// textInfo = getIntent().getStringExtra("text");
		date = getIntent().getStringExtra("date");
		if(date == null||date.length() == 0){
			Date date1=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			date=format.format(date1);
		}

		thisActivityUserID = userID;
		if(thisActivityUserID == null){
			thisActivityUserID = getIntent().getStringExtra("userid");
		}
		shareUrl = "http://123.57.5.108:8087/architect/allAchievement?userId="
				+ thisActivityUserID + "&date=" + date;
		Tools.getLog(Tools.i, "aaa", "shareUrl ============= " + shareUrl);
		setContentView(R.layout.activity_achievement_share);
		activity_share_weibo = (ImageView) findViewById(R.id.activity_share_weibo);
		activity_share_friendster = (ImageView) findViewById(R.id.activity_share_friendster);
		activity_share_qzeon = (ImageView) findViewById(R.id.activity_share_qzeon);
		activity_share_input = (EditText) findViewById(R.id.activity_share_input);
		shareImage = (ImageView) findViewById(R.id.shareImage);
		share_delete = (ImageView) findViewById(R.id.share_delete);
		myShare = new MyUMSDK(mContext);

		isShareImage = true;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initTitle();
		getShareStr();
		ClockApplication.getInstance().openKeybord(activity_share_input, this);
		getLastImageConn();
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		activity_share_weibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// textInfo = textInfo.replace("null", "");
				String str = activity_share_input.getText().toString();
				if (str.length() > 140) {
					Toast.makeText(mContext, "内容大于微博限制的140字，请删减后再分享",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Tools.getLog(Tools.d, "aaa", "微博分享：" + isLogin);
				//				MobclickAgent.onEvent(mContext, "successshareClick");
				MobclickAgent.onEvent(mContext, "homepageshareWeiboclick");
				ClockApplication.getInstance().closeKeybord(
						activity_share_input, mContext);
				if (isShareImage)
					myShare.sinaUMShared(str, shareUrl, shareBitmap, false,2);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job

				else
					myShare.sinaUMShared(str, shareUrl, null, false,2);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job

				// FinishThis();
			}
		});
		activity_share_friendster.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str = activity_share_input.getText().toString();
				//				MobclickAgent.onEvent(mContext, "successshareClick");
				MobclickAgent.onEvent(mContext, "homepageshareWeChatclick");
				ClockApplication.getInstance().closeKeybord(
						activity_share_input, mContext);
				if (isShareImage)
					myShare.friendsterUMShared("打卡7", str, shareUrl,
							shareBitmap, false,2);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				else
					myShare.friendsterUMShared("打卡7", str, shareUrl, null,
							false,2);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job

				// FinishThis();
			}
		});
		activity_share_qzeon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str = activity_share_input.getText().toString();
				//				MobclickAgent.onEvent(mContext, "successshareClick");
				MobclickAgent.onEvent(mContext, "homepageshareQQclick");
				ClockApplication.getInstance().closeKeybord(
						activity_share_input, mContext);
				if (isShareImage)
					myShare.qzeroUMShared(mContext, str, "打卡7", shareUrl,
							shareBitmap,2);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
				else
					myShare.qzeroUMShared(mContext, str, "打卡7", shareUrl, null,2);//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job

				// FinishThis();
			}
		});

		share_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				shareImage.setVisibility(View.GONE);
				share_delete.setVisibility(View.GONE);
				isShareImage = false;

			}
		});

	}

	private void getLastImageConn() {

		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(thisActivityUserID);
		sb.append("&date=");
		sb.append(date);

		Service.getService(Contanst.HTTP_GET_LAST_IMAGE_TODAY, null,
				sb.toString(), ShareAchievementActivity.this).addList(null)
				.request(UrlParams.GET);

	}

	private void getShareStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(thisActivityUserID);
		sb.append("&date=");
		sb.append(date);
		Tools.getLog(Tools.d, "aaa", "date:" + date);
		Service.getService(Contanst.HTTP_TASK_SHARESTED, null, sb.toString(),
				ShareAchievementActivity.this).addList(null)
				.request(UrlParams.GET);
	}

	private void initTitle() {
		ImageView title_item_leftImage = (ImageView) findViewById(R.id.title_item_leftImage);
		ImageView title_item_rightImage = (ImageView) findViewById(R.id.title_item_rightImage);
		title_item_rightImage.setVisibility(View.GONE);
		TextView title_item_content = (TextView) findViewById(R.id.title_item_content);
		title_item_content.setText("分享成就");
		title_item_leftImage.setImageResource(R.drawable.home_share_close);
		title_item_leftImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FinishThis();
			}
		});
	}

	public void FinishThis() {
		ShareAchievementActivity.this.finish();
		ClockApplication.getInstance().closeKeybord(activity_share_input,
				mContext);
		overridePendingTransition(R.anim.slide_alpha_in_right,
				R.anim.slide_alpha_out_left);

		if (shareBitmap != null) {
			shareBitmap.recycle();
			shareBitmap = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ShareAchievementActivity.this.finish();
			overridePendingTransition(R.anim.slide_alpha_in_right,
					R.anim.slide_alpha_out_left);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa",
				"===========getJOSNdataSuccess=============");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "==========getJOSNdataFail===========");
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GET_LAST_IMAGE_TODAY:

					String[] array = ((String) msg.obj).split(",");

					if (array[0].equals("-1")) {
						isShareImage = false;
						shareImage.setVisibility(View.GONE);
						share_delete.setVisibility(View.GONE);
						// shareImage.setImageResource(R.drawable.icon);
						// shareBitmap = BitmapFactory.decodeResource(
						// getResources(), R.drawable.share_icon);//如果没有图片分享icon
					} else {

						String url = null;
						if (array[1].equals("1")) {
							url = UrlParams.IP + array[0];
						} else {
							url = UrlParams.QINIU_IP + array[0];
						}
						ImageLoader.getInstance().loadImage(url,
								new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(
									String imageUri, View view) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onLoadingFailed(
									String imageUri, View view,
									FailReason failReason) {
								// TODO Auto-generated method stub
								shareImage
								.setImageResource(R.drawable.share_icon);
							}

							@Override
							public void onLoadingComplete(
									String imageUri, View view,
									Bitmap loadedImage) {
								// TODO Auto-generated method stub
								shareBitmap = loadedImage;
								shareImage.setImageBitmap(shareBitmap);
							}

							@Override
							public void onLoadingCancelled(
									String imageUri, View view) {
								// TODO Auto-generated method stub
							}
						});
					}
					break;
				case Contanst.TASK_SHARESTED:
					String str = (String) msg.obj;
					activity_share_input.setText(str);
					break;
				}

				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.GET_LAST_IMAGE_TODAY:
					Tools.getLog(Tools.d, "aaa", "GETRECOMMEND:" + message);
					shareImage.setImageResource(R.drawable.share_icon);
					break;
				}

				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = MyUMSDK.mController.getConfig()
				.getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	};

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

}
