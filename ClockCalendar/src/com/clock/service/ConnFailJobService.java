package com.clock.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import u.aly.db;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;
import com.yktx.sqlite.DBHelper;

public class ConnFailJobService extends Service implements ServiceListener {

	// String filename;
	DBHelper dbHelper;
	ArrayList<TaskItemBean> connFailJobList = new ArrayList<TaskItemBean>();

	String userID;
	boolean isHaveImage;

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences("clock", MODE_PRIVATE);
		// filename = intent.getStringExtra("filename");
		if (intent == null) {
			// Toast.makeText(AddShowPhotoService.this, "intent is null!!!",
			// Toast.LENGTH_SHORT).show();\
			return 0;
		}

		dbHelper = new DBHelper(ConnFailJobService.this);

		connFailJobList = dbHelper.getFialJobList();

		if (connFailJobList.size() > 0) {

			ArrayList<Map<String, String>> gsonList = new ArrayList<Map<String, String>>();

			for (int i = 0; i < connFailJobList.size(); i++) {
				TaskItemBean bean = connFailJobList.get(i);
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("id", bean.getJobId());
				map.put("taskId", bean.getTaskId());
				String signature = bean.getSignature();
				if (signature != null && signature.length() != 0) {
					map.put("signature", signature);
				}
				String quantity = bean.getQuantity();
				if (quantity != null && quantity.length() != 0) {
					map.put("quantity", quantity);
				}
				map.put("checkTime", bean.getCheck_time() + "");
				map.put("picNum", bean.getPicCount());
				if (Integer.parseInt(bean.getPicCount()) > 0) {
					isHaveImage = true;
				}
				gsonList.add(map);
			}

			Gson gson = new Gson();
			String json = gson.toJson(gsonList);
			settings = getSharedPreferences("clock", MODE_PRIVATE);
			userID = settings.getString("userid", null);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("userId", userID));
				params.add(new BasicNameValuePair("clientLocalTime", System
						.currentTimeMillis() + ""));
				params.add(new BasicNameValuePair("jobListJson", json));

			} catch (Exception e) {
				e.printStackTrace();
			}
			com.yktx.check.service.Service
					.getService(Contanst.HTTP_UPLOAD_OFFLINE, null, null,
							ConnFailJobService.this).addList(params)
					.request(UrlParams.POST);

			Tools.getLog(Tools.i, "kkk",
					"params =============== " + params.toString());

		}

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// Tools.getLog(Tools.i, "aaa", "curPage === " + curPage);
		// curPage = 0;
		// productid = intent.getStringExtra("productid");
		// jobid = intent.getStringExtra("uuid");
		// type = intent.getIntExtra("type", 1);
		// Tools.getLog(Tools.d, "aaa", "jobid:" + jobid);
		// ArrayList<ImageListBean> curlist = (ArrayList<ImageListBean>) intent
		// .getSerializableExtra("list");
		// list.clear();
		// if (curlist != null) {
		// for (int i = 0; i < curlist.size(); i++) {
		// if (curlist.get(i).getIsCheck()) {
		// list.add(curlist.get(i));
		// }
		// }
		// Tools.getLog(Tools.i, "kkk", "list.size ============ "
		// + list.size());
		// pageSum = list.size();
		// if (pageSum > 0) {
		// ImageUpLoadConn(0);
		// }
		// }
		//
		// }
		// }).start();

		return super.onStartCommand(intent, flags, startId);
	}

	int jobSum;
	int jobIndex;
	int curPage = 0;
	int pageSum;
	ArrayList<ImageListBean> list = new ArrayList<ImageListBean>(6);

	// public DisplayImageOptions options = new DisplayImageOptions.Builder()
	// .showImageOnLoading(null).showImageForEmptyUri(null)
	// .cacheInMemory(false).showImageOnFail(null)
	// .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(false)
	// .considerExifParams(true)
	// .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	// public void ImageUpLoadConn(int listIndex, final String jobid) {
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
	// userID));
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
	// null, ConnFailJobService.this)
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

	ArrayList<String> successId = new ArrayList<String>();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.UPLOAD_OFFLINE:
					successId = (ArrayList<String>) (msg.obj);
					jobSum = successId.size();
					if (isHaveImage) {
						com.yktx.check.service.Service
								.getService(Contanst.HTTP_GET_QINIU_TOKEN,
										null, null, ConnFailJobService.this)
								.addList(null).request(UrlParams.GET);
					} else {
						dbHelper.deleteFialJobBean();
					}

					break;
				case Contanst.GET_QINIU_TOKEN:
					final String token = (String) msg.obj;
					for (int j = 0; j < successId.size(); j++) {

						TaskItemBean bean = dbHelper
								.getFialJobBean(successId.get(j));
						String path = bean.getAllPath();

						if (path != null && path.length() > 0) {
							final String[] imagePath = path.split(",");
							Configuration config = new Configuration.Builder()
									.upPort(8888).build();
							UploadManager uploadManager = new UploadManager(
									config);
							Map<String, String> params = new HashMap<String, String>();
							params.put("x:userId", userID);
							params.put("x:type", "1");
							params.put("x:relateId", successId.get(j));
							UploadOptions opt = new UploadOptions(params,
									null, false, null, null);
							for (int i = 0; i < imagePath.length; i++) {
								upLoad(uploadManager, imagePath[i], token, opt);
							}
						}
					}
					
					dbHelper.deleteFialJobBean();

					break;
				case Contanst.IMAGEUPLOAD:

					String url = (String) msg.obj;
					// sinaUploadPicConn(access_token, UrlParams.IP + url);

					curPage++;
					if (curPage < pageSum) {
						// ImageUpLoadConn(curPage, successId[jobIndex]);
					}

					// Toast.makeText(AddShowPhotoService.this, "发送成功!",
					// Toast.LENGTH_LONG).show();

					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:
				switch (msg.arg1) {
				case Contanst.UPLOAD_OFFLINE:
					String str = (String) msg.obj;
					if("10000".equals(str)){
						dbHelper.deleteFialJobBean();
					}
					break;
				case Contanst.IMAGEUPLOAD:

					break;
				}
			}
		}

	};
	
	private void upLoad(final UploadManager uploadManager ,final String imagePath, final String token, final UploadOptions opt){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uploadManager.put(new File(imagePath), null, token,
						(UpCompletionHandler) handler, opt);
			}
		}).start();
	}
	
	UpCompletionHandler handler = new UpCompletionHandler() {

		@Override
		public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
			// TODO Auto-generated method stub
		}
	};

}
