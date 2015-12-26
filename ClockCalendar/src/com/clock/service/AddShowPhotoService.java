package com.clock.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.NotificationUtil;
import com.yktx.check.util.Tools;

public class AddShowPhotoService extends Service implements ServiceListener {
	/** 上传照片 */
	public static final int AddShowPhotoRun = 0;
	/** 上传联系人 */
	public static final int SendPhoneRun = 1;
	/** 注册信息上传 */
	public static final int RegisterInfo = 2;
	/** 发布商品图片上传 */
	public static final int ProductImageInfo = 3;
	/** 刷新地理位置 */
	public static final int RefreshLocation = 4;
	/** 环信登录 */
	public static final int HuanXinLogin = 5;
	public int ADDSTATE = 1;
	// DBHelper db;
	String msg;
	// String filename;
	String communityid;
	String uuid = null;
	int budingid;
	int floorid;
	String HXuserName;
	public static Bitmap bitmap_temp;
	ImageLoader imageLoader;

	public void setstate(int state) {
		ADDSTATE = state;
	}

	String access_token,userID;
	/**
	 *  1为job图片, 2为头像 
	 */
	int type;

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null) {
			ADDSTATE = intent.getIntExtra("state", 0);
			msg = intent.getStringExtra("msg");

			if (msg == null) {
				msg = "";
			}

		}
		SharedPreferences settings = getSharedPreferences("clock", MODE_PRIVATE);
		access_token = settings.getString("access_token", "");
		userID = settings.getString("userid", "");
		// filename = intent.getStringExtra("filename");
		if (intent == null) {
			// Toast.makeText(AddShowPhotoService.this, "intent is null!!!",
			// Toast.LENGTH_SHORT).show();\
			return 0;
		}

		Tools.getLog(Tools.i, "aaa", "curPage === " + curPage);
		curPage = 0;
		productid = intent.getStringExtra("productid");
		jobid = intent.getStringExtra("uuid");
		type = intent.getIntExtra("type", 0);
		Tools.getLog(Tools.d, "aaa", "jobid:" + jobid);
		ArrayList<ImageListBean> curlist = (ArrayList<ImageListBean>) intent
				.getSerializableExtra("list");

		list.clear();

		if (curlist != null) {
			for (int i = 0; i < curlist.size(); i++) {
				if (curlist.get(i).getIsCheck()) {
					list.add(curlist.get(i));
					Tools.getLog(Tools.i, "aaa",
							"list.getImageUrl ============ "
									+ curlist.get(i).getImageUrl());
				}
			}
			Tools.getLog(Tools.i, "kkk",
					"list.size ============ " + list.size());
			pageSum = list.size();
			if (pageSum > 0) {
				// ImageUpLoadConn(0);
				com.yktx.check.service.Service
						.getService(Contanst.HTTP_GET_QINIU_TOKEN, null, null,
								AddShowPhotoService.this).addList(null)
						.request(UrlParams.GET);
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	String number;
	String productid;
	String jobid;
	int curPage = 0;
	int pageSum;
	ArrayList<ImageListBean> list = new ArrayList<ImageListBean>(6);

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(null).showImageForEmptyUri(null)
			.cacheInMemory(false).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(false)
			.considerExifParams(true)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

	// public void ImageUpLoadConn(int listIndex) {
	// ImageLoader imageLoader = ImageLoader.getInstance();
	// final ImageListBean imageBean = list.get(listIndex);
	// Tools.getLog(Tools.d, "aaa",
	// "imageBean.getImageUrl():" + imageBean.getImageUrl());
	// imageLoader.loadImage(FileURl.LOAD_FILE + imageBean.getImageUrl(),
	// options, new ImageLoadingListener() {
	//
	// @Override
	// public void onLoadingStarted(String imageUri, View view) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onLoadingFailed(String imageUri, View view,
	// FailReason failReason) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onLoadingComplete(String imageUri, View view,
	// Bitmap loadedImage) {
	// // TODO Auto-generated method stub
	// String photoStr = PictureUtil.save(loadedImage);
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// try {
	// params.add(new BasicNameValuePair("userId",
	// productid));
	// params.add(new BasicNameValuePair("type", "1"));
	// params.add(new BasicNameValuePair("relateId", jobid));
	// params.add(new BasicNameValuePair("imageStr",
	// photoStr));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// Tools.getLog(Tools.i, "kkk", "params =============== "
	// + params.toString());
	// com.yktx.check.service.Service
	// .getService(Contanst.HTTP_IMAGEUPLOAD, null,
	// null, AddShowPhotoService.this)
	// .addList(params).request(UrlParams.POST);
	// }
	//
	// @Override
	// public void onLoadingCancelled(String imageUri, View view) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	//
	// }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yktx.snake.conn.ServiceListener#getJOSNdataSuccess(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
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

	String token;
	UploadManager uploadManager;
	UploadOptions opt;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.IMAGEUPLOAD:
					if (msg.arg1 == Contanst.IMAGEUPLOAD) {
						String url = (String) msg.obj;
						// sinaUploadPicConn(access_token, UrlParams.IP + url);
						curPage++;
						if (curPage < pageSum) {
							// ImageUpLoadConn(curPage);
						}

						// Toast.makeText(AddShowPhotoService.this, "发送成功!",
						// Toast.LENGTH_LONG).show();
					}
					break;
				case Contanst.GET_QINIU_TOKEN:
					token = (String) msg.obj;
					Configuration config = new Configuration.Builder().upPort(
							8888)
					// .chunkSize(256 * 1024) // 分片上传时，每片的大小。 默认 256K
					// .putThreshhold(512 * 1024) // 启用分片上传阀值。默认 512K
					// .connectTimeout(10) // 链接超时。默认 10秒
					// .responseTimeout(60) // 服务器响应超时。默认 60秒
					// // .recorder(recorder) // recorder 分片上传时，已上传片记录器。默认
					// // null
					// // .recorder(recorder, keyGen) // keyGen
					// // 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
					// .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认
					// // Zone.zone0
							.build();
					// 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
					uploadManager = new UploadManager(config);
					Map<String, String> params = new HashMap<String, String>();
					params.put("x:userId", userID);
					params.put("x:type", type + "");
					if (type == 1) {
						params.put("x:relateId", jobid);
					} else {
						params.put("x:relateId", userID);
					}
					opt = new UploadOptions(params, null, false, null, null);
					NotificationUtil.calltop(AddShowPhotoService.this,
							"照片发送中...."+(curPage+1)+"/"+pageSum);
					
					if (curPage < pageSum) {
						uploadManager.put(new File(list.get(curPage)
								.getImageUrl()), null, token,
								(UpCompletionHandler) handler, opt);
					}
					// for(int i = 0; i < list.size(); i++){
					// uploadManager.put(new File(list.get(i).getImageUrl()) ,
					// null, token, (UpCompletionHandler) handler, opt);
					// }

					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				if (msg.arg1 == Contanst.IMAGEUPLOAD) {
					// Toast.makeText(AddShowPhotoService.this, "断网了",
					// Toast.LENGTH_LONG).show();
				}
			}
		}

	};

	UpCompletionHandler handler = new UpCompletionHandler() {

		@Override
		public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
			// TODO Auto-generated method stub

			Log.i("aaa", "arg1.isOK() ========== " + arg1.isOK());
			if (arg1.isOK()) {

				Toast.makeText(AddShowPhotoService.this, "照片"+(curPage+1)+"/"+pageSum+"发送成功", Toast.LENGTH_LONG).show();
				curPage++;
				if (curPage < pageSum) {
					NotificationUtil.calltop(AddShowPhotoService.this,
							"照片发送中...."+(curPage+1)+"/"+pageSum);
					uploadManager.put(
							new File(list.get(curPage).getImageUrl()), null,
							token, (UpCompletionHandler) handler, opt);
				}
			} else {
				Toast.makeText(AddShowPhotoService.this, "照片发送失败...."+(curPage+1)+"/"+pageSum, Toast.LENGTH_LONG).show();

//				NotificationUtil.calltop(AddShowPhotoService.this,
//						"照片发送中...."+(curPage+1)+"/"+pageSum);
//				uploadManager.put(
//						new File(list.get(curPage).getImageUrl()), null,
//						token, (UpCompletionHandler) handler, opt);
			}
		}
	};

	public class PhoneCallInfo {
		public Integer _id;
		public String number;
		public Long date;
		public Integer duration;
		public Integer type;
		public String name;
	}

	private void sinaUploadPicConn(String access_token, String url) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("source", "3469838410"));
			params.add(new BasicNameValuePair("access_token", access_token));
			params.add(new BasicNameValuePair("pic", url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		com.yktx.check.service.Service
				.getService(Contanst.HTTP_SINA_UPLOAD_PIC, null, null,
						AddShowPhotoService.this).addList(params)
				.request(UrlParams.POST);
	}

}
