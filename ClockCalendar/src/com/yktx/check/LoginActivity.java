package com.yktx.check;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.yktx.check.bean.CreateUserBean;
import com.yktx.check.bean.GetUserBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.MyUMSDK;
import com.yktx.check.util.Tools;
import com.yktx.sqlite.DBInstance;

public class LoginActivity extends BaseActivity implements ServiceListener {
	private ImageView mImageView;
	private ImageView sinaLogin, weixinLogin, QQLogin;

	MyUMSDK mShareSDK;
	private String ssoUserID;
	boolean isConn;
	private TextView login_userTerms, login_privacyPolicy, login_allBottomText;
	private boolean isNewUser;
	private String JGPushID ;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		mShareSDK = new MyUMSDK(this);
		mImageView = (ImageView) findViewById(R.id.login_Image);
		sinaLogin = (ImageView) findViewById(R.id.sinaLogin);
		weixinLogin = (ImageView) findViewById(R.id.weixinLogin);
		QQLogin = (ImageView) findViewById(R.id.QQLogin);
		login_userTerms = (TextView) findViewById(R.id.login_userTerms);
		login_privacyPolicy = (TextView) findViewById(R.id.login_privacyPolicy);
		login_allBottomText = (TextView) findViewById(R.id.login_allBottomText);
		JGPushID = JPushInterface.getRegistrationID(LoginActivity.this);
		Tools.getLog(Tools.d,"aaa","JPushInterface.getRegistrationID(mContext):"+JGPushID);
		
	}

	private void addQZoneQQPlatform() {
		String appId = "1104618178";
		String appKey = "M6EzFwC4gw5fjsRd";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		login_allBottomText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		login_allBottomText.getPaint().setAntiAlias(true);// 抗锯齿
		login_userTerms.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		login_userTerms.getPaint().setAntiAlias(true);// 抗锯齿
		login_privacyPolicy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		login_privacyPolicy.getPaint().setAntiAlias(true);// 抗锯齿
		addQZoneQQPlatform();
//		Animation anim=AnimationUtils.loadAnimation(this,R.anim.login_small_big); 
//		anim.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				// TODO Auto-generated method stub
//				TranslateAnimation animationStart = new TranslateAnimation(0, 0,
//						0, -60*BaseActivity.DENSITY);
//				animationStart.setDuration(500L);
//				animationStart.setFillAfter(true);
//				animationStart.setAnimationListener(new AnimationListener() {
//					
//					@Override
//					public void onAnimationStart(Animation arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onAnimationRepeat(Animation arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onAnimationEnd(Animation arg0) {
//						// TODO Auto-generated method stub
						visiteSNS();
//					}
//				});
//				mImageView.setAnimation(animationStart);
//			}
//		});
//		mImageView.setAnimation(anim);
	}

	
	private void visiteSNS(){
		final Animation anim=AnimationUtils.loadAnimation(this,R.anim.login_sns_small_big); 
		final Animation anim1=AnimationUtils.loadAnimation(this,R.anim.login_sns_small_big); 
		final Animation anim2=AnimationUtils.loadAnimation(this,R.anim.login_sns_small_big); 
		weixinLogin.setVisibility(View.VISIBLE);
		weixinLogin .setAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				sinaLogin .setVisibility(View.VISIBLE);
				sinaLogin.setAnimation(anim1);
				anim1.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						
						QQLogin.setVisibility(View.VISIBLE);
						QQLogin.setAnimation(anim2);
						
					}
				});
			}
		});
	
		
	}
	
	
	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		sinaLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isConn) {
					loginSina(SHARE_MEDIA.SINA);
					isConn = true;
				}
			}
		});
		weixinLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isConn) {
					UMWXHandler wxHandler = new UMWXHandler(mContext,
							mShareSDK.appID, mShareSDK.weixinappSecret);
					wxHandler.addToSocialSDK();
					loginWeiXin(SHARE_MEDIA.WEIXIN);
					isConn = true;
				}
			}
		});
		QQLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isConn) {
					loginQQ(SHARE_MEDIA.QQ);
					isConn = true;
				}
			}
		});
		login_allBottomText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(LoginActivity.this, XieYiActivity.class);
				LoginActivity.this.startActivity(in);
			}
		});
		login_userTerms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(LoginActivity.this, XieYiActivity.class);
				LoginActivity.this.startActivity(in);
			}
		});
		login_privacyPolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(LoginActivity.this, XieYiActivity.class);
				LoginActivity.this.startActivity(in);
			}
		});
	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	private void loginSina(final SHARE_MEDIA platform) {
		mShareSDK.mController.doOauthVerify(this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						Tools.getLog(Tools.d, "ccc", "onStart");
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Tools.getLog(Tools.d, "ccc", "onError");
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						ssoUserID = value.getString("uid");
						Tools.getLog(Tools.d, "ccc", "onStart");
						if (!TextUtils.isEmpty(ssoUserID)) {
							getUserInfo(1);
							mEditor.putBoolean("isWeiboOpen", true);
							mEditor.commit();
						} else {
							Toast.makeText(LoginActivity.this, "授权失败...",
									Toast.LENGTH_SHORT).show();
							Tools.getLog(Tools.d, "ccc", "授权失败..");
							isConn = false;
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Tools.getLog(Tools.d, "ccc", "onCancel");
						isConn = false;
					}
				});
	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	private void loginWeiXin(final SHARE_MEDIA platform) {

		mShareSDK.mController.doOauthVerify(this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT)
								.show();
						// 获取相关授权信息
						mShareSDK.mController.getPlatformInfo(
								LoginActivity.this, SHARE_MEDIA.WEIXIN,
								new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(LoginActivity.this,
												"获取平台数据开始...",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											// Set<String> keys = info.keySet();
											// for (String key : keys) {
											// sb.append(key
											// + "="
											// + info.get(key)
											// .toString()
											// + "\r\n");
											// }
											mEditor.putBoolean("isWeiboOpen",
													false);
											mEditor.commit();
											source = "2";
											Tools.getLog(Tools.d, "TestData",
													sb.toString());
											userName = info.get("nickname")
													.toString();

											userUrl = (String) info
													.get("headimgurl");
											sex = (Integer) info.get("sex")
													+ "";
											weiboId = info.get("unionid") + "";
											access_token = info.get("unionid")
													+ "";
											isRegisterConn(weiboId, source);
										} else {
											Tools.getLog(Tools.d, "aaa",
													"发生错误：" + status);
											isConn = false;
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT)
								.show();
						isConn = false;
					}

				});
	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	private void loginQQ(final SHARE_MEDIA platform) {
		mShareSDK.mController.doOauthVerify(this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						ssoUserID = value.getString("uid");
						if (!TextUtils.isEmpty(ssoUserID)) {
							getUserInfo(2);
						} else {
							Toast.makeText(LoginActivity.this, "授权失败...",
									Toast.LENGTH_SHORT).show();
							isConn = false;
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						isConn = false;
					}
				});
	}

	/**
	 * 获取授权平台的用户信息</br>
	 */
	private String userName, userUrl, sex, weiboId, access_token, source;

	private void getUserInfo(final int platformIndex) {
		SHARE_MEDIA platform = null;
		if (platformIndex == 1) {
			platform = SHARE_MEDIA.SINA;
		} else {
			platform = SHARE_MEDIA.QQ;
		}

		mShareSDK.mController.getPlatformInfo(LoginActivity.this, platform,
				new UMDataListener() {
					@Override
					public void onStart() {
						
					}
					@Override
					public void onComplete(int status, Map<String, Object> info) {
						// showProgressDialog("授权成功，正在加载数据。请稍后...");
						if (info != null) {
							
							if (platformIndex == 1) {
								//weibo
								source = "1";
								Tools.getLog(Tools.d, "aaa", info.toString());
								userName = (String) info.get("screen_name");
								userUrl = (String) info
										.get("profile_image_url");
								sex = info.get("gender") + "";
								weiboId = info.get("uid") + "";
								access_token = info.get("access_token") + "";
								isRegisterConn(weiboId, source);

							} else {
								
								//qq
								
								source = "3";
								Tools.getLog(Tools.d, "aaa", info.toString());
								userName = (String) info.get("screen_name");
								userUrl = (String) info
										.get("profile_image_url");
								String gender = info.get("gender") + "";
								if(gender.equals("男")){
									sex = "1";
								} else{
									sex = "2";
								}
								weiboId = ssoUserID;
								isRegisterConn(weiboId, source);
							}
						}
					}
				});

	}

	public ProgressDialog mDialog;

	public void showProgressDialog(String message) {

		// mDialog = CustomProgressDialog.createDialog(this);
		mDialog = new ProgressDialog(mContext);
		mDialog.setMessage(message);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		// mDialog.setCanceledOnTouchOutside(false);//点击空白处消失
		mDialog.show();
	}

	private void isRegisterConn(String pubId, String source) {
		showProgressDialog("数据加载中...  请稍等。");
		StringBuffer sb = new StringBuffer();
		sb.append("?pubId=");
		sb.append(pubId);
		sb.append("&source=");
		sb.append(source);
		Service.getService(Contanst.HTTP_GETBYPUBID, null, sb.toString(),
				LoginActivity.this).addList(null).request(UrlParams.GET);
	}

	private void LoginConn(String name, String sex, String weiboId,
			String avatarUrl) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("gender", sex));
			params.add(new BasicNameValuePair("pubId", weiboId));
			params.add(new BasicNameValuePair("avatarUrl", avatarUrl));
			params.add(new BasicNameValuePair("platform", "2"));
			params.add(new BasicNameValuePair("source", source));

		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_CREATEUSER, null, null,
				LoginActivity.this).addList(params).request(UrlParams.POST);
	}

	public void updateJpushId(String userid, String jPushId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId", userid));
			params.add(new BasicNameValuePair("jPushId", jPushId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Service.getService(Contanst.HTTP_UPDATEJPUSHID, null, null,
				LoginActivity.this).addList(params).request(UrlParams.POST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mShareSDK.mController.getConfig()
				.getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		mShareSDK.mController.getConfig().setSsoHandler(new SinaSsoHandler());
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

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mHandler.sendMessage(msg);
	}

	CreateUserBean bean;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// if (mDialog != null && mDialog.isShowing())
			// mDialog.dismiss();
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.GETBYPUBID:
					GetUserBean getUserBean = (GetUserBean) msg.obj;
					if (getUserBean.getExsit().equals("1")) {
						bean = getUserBean.getCreateUserBean();
						if(JGPushID != null){
							Tools.getLog(
									Tools.d,
									"aaa",
									"JPushInterface.getRegistrationID(mContext):"
											+JPushInterface.getRegistrationID(LoginActivity.this));
							updateJpushId(bean.getId() + "",
									JPushInterface.getRegistrationID(LoginActivity.this));
						}
						
						isNewUser = false;
					} else {
						LoginConn(userName, sex + "", weiboId + "", userUrl);
						isNewUser = true;
					}

					break;
				case Contanst.CREATEUSER:
					bean = (CreateUserBean) msg.obj;
					Tools.getLog(Tools.d, "aaa",
							"CreateUserBean:" + bean.toString());
					updateJpushId(bean.getId() + "",
							JPushInterface.getRegistrationID(mContext));
					break;
				case Contanst.UPDATEJPUSHID:
					finshLogin(bean);
					break;
				}

				break;

			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Tools.getLog(Tools.i, "aaa", message);
				break;
			}

		}
	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			clockApplication.exit();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	};

	private void finshLogin(CreateUserBean bean) {

		mEditor.putBoolean("islogin", true);
		mEditor.putString("userid", bean.getId() + "");
		mEditor.putString("username", bean.getName());
		mEditor.putString("userheadimage", bean.getAvartarPath());
		mEditor.putString("weiboid", bean.getPubId());
		mEditor.putString("usersex", bean.getGender() + "");
		mEditor.putString("access_token", access_token);
		mEditor.putString("getFistTaskCdate", bean.getFistTaskCdate());
		mEditor.putString("source", source);
		mEditor.putInt("imageSource", bean.getImageSource());
		

		mEditor.commit();
		HashSet<String> set = new HashSet<String>();
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date(currentTime);
		Tools.getLog(Tools.d, "aaa", "时间：" + formatter.format(date));
		set.add(formatter.format(date));
		JPushInterface.setAliasAndTags(mContext, bean.getId() + "", set,
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
						// TODO Auto-generated method stub
						Tools.getLog(Tools.d, "aaa", "arg0:" + arg0);
						Tools.getLog(Tools.d, "aaa", "arg1:" + arg1);
						Tools.getLog(Tools.d, "aaa", "arg2:" + arg2.toString());
					}
				});
		if(mDialog != null){
			mDialog.dismiss();
		}
		
		Intent in = new Intent(mContext, ClockMainActivity.class);
		in.putExtra("isnewuser", isNewUser);
		startActivity(in);
		clockApplication.exit();
		// if(mDialog != null){
		// mDialog.dismiss();
		// }
		
		Intent intent = new Intent("com.android.REFLASH");
		this.sendBroadcast(intent);

		finish();
		overridePendingTransition(R.anim.slide_alpha_in_right,
				R.anim.slide_alpha_out_left);

	}

	public void onResume() {
		super.onResume();
//		isConn = false; 
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
