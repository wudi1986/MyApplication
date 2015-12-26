package com.yktx.check.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yktx.check.R;

/**
 * 
 * @author Administrator 分享
 */
public class MyUMSDK {
	private Context mContext;

	public final String appID = "wx23ab83263830f459";
	public final String weixinappSecret = "5ee7ec98fde8e6f1fcfea15ddb13d865";
	final String QQID = "1104618178";
	final String QQappSecret = "M6EzFwC4gw5fjsRd ";
	private int state;
	public static UMSocialService mController;

	public MyUMSDK(Context context) {
		mContext = context;
		mController = UMServiceFactory.getUMSocialService(
				"com.yingtao.testFra", RequestType.SOCIAL);
		// // 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().closeToast();
	}

	public int qqUMShared(Activity activity, String content, String title,
			String contentUrl, Bitmap imageBitmap, boolean isCopper) {
		MobclickAgent.onEvent(mContext, "qqhaoyou");
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQID,
				QQappSecret);
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();

		// //设置分享文字
		if (!isCopper) {
			if (content != null && content.length() > 0) {
				qqShareContent.setShareContent(content);
			} else {
				state = 2;
				return state;
			}
		}
		// 设置分享title
		if (title != null && title.length() > 0) {
			qqShareContent.setTitle(title);
		} else {
			state = 3;
			return state;
		}
		// 设置点击分享内容的跳转链接
		if (contentUrl != null && contentUrl.length() > 0) {
			qqShareContent.setTargetUrl(contentUrl);
		} else {
			state = 4;
			return state;
		}
		// 设置分享图片
		if (imageBitmap != null) {
			qqShareContent.setShareImage(new UMImage(mContext, imageBitmap));
		} else {
			// qqShareContent.setShareImage(new UMImage(mContext,
			// R.drawable.share_icon));
			// state = 5;
			// return state;
		}
		mController.setShareMedia(qqShareContent);
		mController.postShare(mContext, SHARE_MEDIA.QQ, new SnsPostListener() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "分享开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int eCode,
					SocializeEntity arg2) {
				// TODO Auto-generated method stub
				if (eCode == 200) {
					Toast.makeText(mContext, "分享成功.", Toast.LENGTH_SHORT)
					.show();
					state = 0;
					
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(mContext, "分享失败[" + eCode + "] " + eMsg,
							Toast.LENGTH_SHORT).show();
					state = 1;
				}
			}
		});
		return state;
	}
	//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
	public int qzeroUMShared(Activity activity, String content, String title,
			String contentUrl, Bitmap imageBitmap,final int type) {
		MobclickAgent.onEvent(mContext, "qqkongjian");
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, QQID,
				QQappSecret);
		qZoneSsoHandler.addToSocialSDK();
		QZoneShareContent qzone = new QZoneShareContent();
		if (content != null && content.length() > 0) {
			qzone.setShareContent(content);
		} else {
			state = 2;
			return state;
		}
		// 设置分享title
		if (title != null && title.length() > 0) {
			qzone.setTitle(title);
		} else {
			state = 3;
			return state;
		}
		// 设置点击分享内容的跳转链接
		if (contentUrl != null && contentUrl.length() > 0) {
			qzone.setTargetUrl(contentUrl);
		} else {
			state = 4;
			return state;
		}
		// 设置分享图片
		if (imageBitmap != null) {
			qzone.setShareImage(new UMImage(mContext, imageBitmap));
		} else {
			qzone.setShareImage(new UMImage(mContext, R.drawable.share_icon));
			// state = 5;
			// return state;
		}
		mController.setShareMedia(qzone);
		mController.postShare(mContext, SHARE_MEDIA.QZONE,
				new SnsPostListener() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "分享开始", Toast.LENGTH_SHORT)
				.show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int eCode,
					SocializeEntity arg2) {
				// TODO Auto-generated method stub
				if (eCode == 200) {
					Toast.makeText(mContext, "分享成功.",
							Toast.LENGTH_SHORT).show();
					state = 0;
					if(type == 1){
						MobclickAgent.onEvent(mContext, "jobshareQQ");
					}else if(type == 2){
						MobclickAgent.onEvent(mContext, "successshareQQ");
					}else if(type == 3){
						MobclickAgent.onEvent(mContext, "homepageshareQQ");
					}else if(type == 4){
						MobclickAgent.onEvent(mContext, "detailshareQQ");
					}
				} else {
					String eMsg = "";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					Toast.makeText(mContext,
							"分享失败[" + eCode + "] " + eMsg,
							Toast.LENGTH_SHORT).show();
					state = 1;
				}

			}
		});
		return state;
	}

	public int weixinUMShared(String content, String title, String contentUrl,
			Bitmap imageBitmap, boolean isCopper) {
		MobclickAgent.onEvent(mContext, "weixinhaoyou");
		// TODO Auto-generated method stub

		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
		UMWXHandler wxHandler = new UMWXHandler(mContext, appID,
				weixinappSecret);
		wxHandler.addToSocialSDK();
		// wxHandler
		// .setWXTitle("我在使用“婴淘市场”买卖闲置的母婴东东，下载一个吧，亲  http://14.17.120.252");
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		if (!isCopper) {
			if (content != null && content.length() > 0) {
				weixinContent.setShareContent(content);
			} else {
				state = 2;
				return state;
			}
		}
		// 设置分享title
		if (title != null && title.length() > 0) {
			weixinContent.setTitle(title);
		} else {
			state = 3;
			return state;
		}
		// 设置点击分享内容的跳转链接
		if (contentUrl != null && contentUrl.length() > 0) {
			weixinContent.setTargetUrl(contentUrl);
		} else {
			state = 4;
			return state;
		}
		// 设置分享图片
		if (imageBitmap != null) {
			weixinContent.setShareImage(new UMImage(mContext, imageBitmap));
		} else {
			// weixinContent.setShareImage(new UMImage(mContext,
			// R.drawable.share_icon));
			state = 5;
		}
		mController.setShareMedia(weixinContent);
		// wxHandler.setWXTitle("亲爱的宝妈宝爸，我发现一个不错的闲置物品正在出售，快进来看看吧~");
		// 分享
		mController.directShare(mContext, SHARE_MEDIA.WEIXIN,
				new SnsPostListener() {

			@Override
			public void onStart() {
				Toast.makeText(mContext, "分享开始", Toast.LENGTH_SHORT)
				.show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				// TODO Auto-generated method stub
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT)
					.show();
					state = 0;
				} else {
					Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT)
					.show();
					state = 1;
				}
			}
		});
		return state;
	}

	/**
	 * 
	 * @param content
	 * @param title
	 * @param contentUrl
	 * @param imageBitmap
	 * @param isCopper
	 *            是否为纯图
	 * @return
	 */
	//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
	public int friendsterUMShared(String content, String title,
			String contentUrl, Bitmap imageBitmap, boolean isCopper,final int type) {
		MobclickAgent.onEvent(mContext, "pengyouquan");
		// TODO Auto-generated method stub

		// 支持微信朋友圈

		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appID,
				weixinappSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		CircleShareContent circleMedia = new CircleShareContent();
		// 设置朋友圈分享内容
		if (!isCopper) {
			if (content != null && content.length() > 0) {
				Tools.getLog(Tools.d, "aaa", "content:" + content);
				circleMedia.setShareContent(content);
			} else {
				state = 2;
				//				return state;
			}
		}
		// 设置分享title
		if (title != null && title.length() > 0) {
			circleMedia.setTitle(title);
		} else {
			state = 3;
			//			return state;
		}
		// 设置点击分享内容的跳转链接
		if (contentUrl != null && contentUrl.length() > 0) {
			circleMedia.setTargetUrl(contentUrl);
		} else {
			state = 4;
			// return state;
		}
		// 设置分享图片
		if (imageBitmap != null) {
			circleMedia.setShareImage(new UMImage(mContext, imageBitmap));
		} else {
			circleMedia.setShareImage(new UMImage(mContext,R.drawable.share_icon));
			// state = 5;
			// return state;
		}
		mController.setShareMedia(circleMedia);
		// 分享
		mController.directShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {

			@Override
			public void onStart() {
				Toast.makeText(mContext, "分享开始", Toast.LENGTH_SHORT)
				.show();
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				// TODO Auto-generated method stub
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT)
					.show();
					state = 0;
					if(type == 1){
						MobclickAgent.onEvent(mContext, "jobshareWeiXin");
					}else if(type == 2){
						MobclickAgent.onEvent(mContext, "successshareWeiXin");
					}else if(type == 3){
						MobclickAgent.onEvent(mContext, "homepageshareWeiXin");
					}else if(type == 4){
						MobclickAgent.onEvent(mContext, "detailshareWeiXin");
					}
				} else {
					Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT)
					.show();
					state = 1;
				}
			}
		});
		return state;
	}
	//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
		public int friendsterUMShared1(String content, String title,
				String contentUrl, String imageUrl, boolean isCopper,final int type) {
			MobclickAgent.onEvent(mContext, "pengyouquan");
			// TODO Auto-generated method stub

			// 支持微信朋友圈

			// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
			UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appID,
					weixinappSecret);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.addToSocialSDK();

			CircleShareContent circleMedia = new CircleShareContent();
			// 设置朋友圈分享内容
			if (!isCopper) {
				if (content != null && content.length() > 0) {
					Tools.getLog(Tools.d, "aaa", "content:" + content);
					circleMedia.setShareContent(content);
				} else {
					state = 2;
					//				return state;
				}
			}
			// 设置分享title
			if (title != null && title.length() > 0) {
				circleMedia.setTitle(title);
			} else {
				state = 3;
				//			return state;
			}
			// 设置点击分享内容的跳转链接
			if (contentUrl != null && contentUrl.length() > 0) {
				circleMedia.setTargetUrl(contentUrl);
			} else {
				state = 4;
				// return state;
			}
			// 设置分享图片
			if (imageUrl != null) {
				circleMedia.setShareImage(new UMImage(mContext, imageUrl));
			} else {
				circleMedia.setShareImage(new UMImage(mContext,
						R.drawable.share_icon));
				// state = 5;
				// return state;
			}
			mController.setShareMedia(circleMedia);
			// 分享
			mController.directShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE,
					new SnsPostListener() {

				@Override
				public void onStart() {
					Toast.makeText(mContext, "分享开始", Toast.LENGTH_SHORT)
					.show();
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode,
						SocializeEntity entity) {
					// TODO Auto-generated method stub
					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
						Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT)
						.show();
						state = 0;
						if(type == 1){
							MobclickAgent.onEvent(mContext, "jobshareWeiXin");
						}else if(type == 2){
							MobclickAgent.onEvent(mContext, "successshareWeiXin");
						}else if(type == 3){
							MobclickAgent.onEvent(mContext, "homepageshareWeiXin");
						}else if(type == 4){
							MobclickAgent.onEvent(mContext, "detailshareWeiXin");
						}
					} else {
						Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT)
						.show();
						state = 1;
					}
				}
			});
			return state;
		}

	/**
	 * 
	 * @param content
	 * @param contentUrl
	 * @param imageBitmap
	 * @param isInput
	 *            加不加编辑页
	 * @return
	 */
	//最后一个参数是那一页 1 为主页打卡成功 2为打卡成就 3详情页Task打卡成就 4详情页Job
	public int sinaUMShared(String content, String contentUrl,
			Bitmap imageBitmap, boolean isInput,final int type) {
		// 设置分享的内容
		mController.setShareContent(content + contentUrl);
		// //设置分享的图片
		// UMImage localImage = new UMImage(mContext, R.drawable.share_icon);
		// mController.setShareImage(localImage);
		// 新浪的分享
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTargetUrl(contentUrl);
		sinaContent.setShareContent(content + contentUrl);
		// 设置分享的图片
		UMImage localImage = null;
		if (imageBitmap != null) {
			localImage = new UMImage(mContext, imageBitmap);
		} else {
			// localImage = new UMImage(mContext, R.drawable.share_icon);
		}
		sinaContent.setShareImage(localImage);
		mController.setShareMedia(sinaContent);
		if (!isInput) {
			mController.directShare(mContext, SHARE_MEDIA.SINA,
					new SnsPostListener() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, "分享开始", Toast.LENGTH_SHORT)
					.show();
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode,
						SocializeEntity entity) {
					// TODO Auto-generated method stub
					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
						Toast.makeText(mContext, "分享成功",
								Toast.LENGTH_SHORT).show();
						if(type == 1){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}else if(type == 2){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}else if(type == 3){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}else if(type == 4){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}
					} else {
						Toast.makeText(mContext, "分享失败",
								Toast.LENGTH_SHORT).show();
					}

				}
			});
		} else {
			mController.postShare(mContext, SHARE_MEDIA.SINA,
					new SnsPostListener() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode,
						SocializeEntity entity) {
					// TODO Auto-generated method stub
					String showText = platform.toString();
					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
						showText += "平台分享成功";
						state = 0;
						if(type == 1){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}else if(type == 2){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}else if(type == 3){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}else if(type == 4){
							MobclickAgent.onEvent(mContext, "jobshareWeiBo");
						}
					} else {
						showText += "平台分享失败";
						state = 1;
					}
					Toast.makeText(mContext, showText,
							Toast.LENGTH_SHORT).show();
				}
			});
		}

		return state;
	}
	// OnActivityResultListener l = new OnActivityResultListener() {
	//
	// @Override
	// public boolean onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// UMSsoHandler ssoHandler =
	// mController.getConfig().getSsoHandler(requestCode) ;
	// if(ssoHandler != null){
	// ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	// }
	// return false;
	// }
	//
	// };
}
