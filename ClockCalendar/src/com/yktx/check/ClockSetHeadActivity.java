package com.yktx.check;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.CommonUtils;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.PictureUtil;
import com.yktx.check.util.Tools;


public class ClockSetHeadActivity extends BaseActivity implements ServiceListener{
	private ImageView headImage;
	private ImageView LeftTitleImage,RightTitleImage,clock_set_head_switch;
	private TextView TitleText;
	private EditText InputName;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.shezhi_bg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(400))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	private boolean isWeiboOpen = true;
	private String OldName;
	private LinearLayout clock_set_head_isweibo;
	private RelativeLayout clock_set_head_exit;
	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_clock_set_head);
		ClockApplication.getInstance().addActivity(mContext);
		headImage = (ImageView) findViewById(R.id.clock_set_head_HeadImage);
		LeftTitleImage = (ImageView) findViewById(R.id.title_item_leftImage);
		RightTitleImage = (ImageView) findViewById(R.id.title_item_rightImage);
		TitleText = (TextView) findViewById(R.id.title_item_content);
		InputName = (EditText) findViewById(R.id.clock_set_head_InputName);
		clock_set_head_switch = (ImageView) findViewById(R.id.clock_set_head_switch);
		clock_set_head_isweibo = (LinearLayout) findViewById(R.id.clock_set_head_isweibo);
		clock_set_head_exit = (RelativeLayout) findViewById(R.id.clock_set_head_exit);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		RightTitleImage.setVisibility(View.GONE);
		TitleText.setText("用户设置");
		ImageLoader.getInstance().displayImage(UrlParams.IP+settings.getString("userheadimage", null), headImage, headOptions);
		OldName = settings.getString("username", null);
		InputName.setText(OldName);
		isWeiboOpen = settings.getBoolean("isWeiboOpen", true);
		if(isWeiboOpen){
			clock_set_head_switch.setImageResource(R.drawable.switch_on);
		}else{
			clock_set_head_switch.setImageResource(R.drawable.switch_off);
		}
		if(settings.getString("source", "0").equals("1")){
			clock_set_head_isweibo.setVisibility(View.VISIBLE);
		}else{
			clock_set_head_isweibo.setVisibility(View.GONE);
		}

	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		LeftTitleImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String thisName = InputName.getText().toString();
				if(thisName != null){
					if(!thisName.equals(OldName)){
						updateUserName(thisName);
						mEditor.putString("username", thisName);
						mEditor.commit();
					}
				}
				finish();
			}
		});
		headImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showsetheaddialog();
			}
		});
		clock_set_head_switch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isWeiboOpen){
					isWeiboOpen = false;
					clock_set_head_switch.setImageResource(R.drawable.switch_off);
				}else{
					isWeiboOpen = true;
					clock_set_head_switch.setImageResource(R.drawable.switch_on);
				}
				mEditor.putBoolean("isWeiboOpen", isWeiboOpen);
				mEditor.commit();
			}
		});
		clock_set_head_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showVisibleDialog();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 19) {
				if (data != null) {
					startPicCut(data.getData());
					// Uri selectedImage = data.getData();
					// if (selectedImage != null) {
					// // sendPicByUri(selectedImage);
					// }
				}
			} else if (requestCode == 18) {
				startPicCut(data.getData());

			} else if (requestCode == 26) {
				if (data != null) {
					Uri selectedImage = data.getData();
					ContentResolver resolver = getContentResolver();
					Bitmap photo = null;
					try {
						if (selectedImage != null) {
							// // sendPicByUri(selectedImage);
							photo = MediaStore.Images.Media.getBitmap(resolver,
									selectedImage);
							headImage.setImageBitmap(photo);
						} else {
							Bundle extras = data.getExtras();
							if (extras != null) {
								// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
								photo = extras.getParcelable("data");
								if (photo != null) {
									headImage
									.setImageBitmap(photo);
								}
							}

						}

						String filepath = FileURl.GoodsIamgeURL
								+ FileURl.IMAGE_NAME;
						File file = new File(filepath);
						if (file.exists()) {
							file.mkdirs();
						}
						BufferedOutputStream bos;
						bos = new BufferedOutputStream(new FileOutputStream(
								file));
						photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 将图片压缩的流里面
						bos.flush();// 刷新此缓冲区的输出流
						bos.close();// 关闭此输出流并释放与此流有关的所有系统资源

						String path = FileURl.LOAD_FILE + FileURl.GoodsIamgeURL
								+ FileURl.IMAGE_NAME;
						ImageLoader.getInstance().clearMemoryCache();
						ImageLoader.getInstance().displayImage(path,
								headImage, headOptions,new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.d, "aaa", "图片选择完成");
								upLoadImage(loadedImage);
							}

							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub

							}
						});
						//						

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		// if (resultCode == 444 || resultCode == 111) {
		// ImageLoader.getInstance().clearMemoryCache();
		// String sdStatus = Environment.getExternalStorageState();
		// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
		// return;
		// }
		//
		// String path = FileURl.LOAD_FILE + FileURl.GoodsIamgeURL
		// + FileURl.IMAGE_NAME;
		// ImageLoader.getInstance().displayImage(path,
		// registerinfo_headimage, options);
		// // registerinfo_headimageline
		// // .setBackgroundResource(R.drawable.takehead_finish);
		// // registerinfo_headimage.setImageResource(R.drawable.image_null);
		// //
		// registerinfo_headimage.setImageBitmap(BitmapFactory.decodeFile(path));
		// isUpdatePhoto = true;
		// iv_registerinfo_name
		// .setBackgroundResource(R.drawable.user_center_finish);
		// }

	}
	// *显示dialog*/
	private void showsetheaddialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(ClockSetHeadActivity.this,
						R.style.CustomDiaLog_by_SongHang));
		builder.setItems(new String[] { "拍照", "从相册选择", "取消" },
				new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					// opencamera();
					selectPicFromCamera();
					break;
				case 1:
					selectPicFromLocal();
					// Intent intent_gralley = new Intent(
					// UserCenterActivity.this,
					// PhotoActivity.class);
					// intent_gralley.putExtra(CameraActivity.IsRegister,
					// "1");
					// startActivityForResult(intent_gralley,
					// CameraActivity.GRALLEY);

					// Intent intent = new Intent();
					// intent.addCategory(Intent.CATEGORY_OPENABLE);
					// intent.setType("image/*");
					// // 根据版本号不同使用不同的Action
					// if (Build.VERSION.SDK_INT < 19) {
					// intent.setAction(Intent.ACTION_GET_CONTENT);
					// } else {
					// intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
					// }
					//
					// File out = new File(FileURl.ImageFilePath,
					// FileURl.IMAGE_NAME);
					// // out.mkdirs();
					// Uri uri = Uri.fromFile(out);
					// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					// startActivityForResult(intent,
					// GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
					break;
				default:
					break;
				}
			}
		});
		builder.show();
	}
	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(
					R.string.sd_card_does_not_exist);
			Toast.makeText(getApplicationContext(), st, 0).show();
			return;
		}
		//
		// cameraFile = new File(PathUtil.getInstance().getImagePath(),
		// GroupApplication.getInstance().getUserName()
		// + System.currentTimeMillis() + ".jpg");
		// cameraFile.getParentFile().mkdirs();
		// startActivityForResult(new
		// Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT,
		// Uri.fromFile(cameraFile)),
		// ChatEasemobActivity.REQUEST_CODE_CAMERA);

		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(camera, 18);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, 19);
	}

	/**
	 * 裁剪图片的方法
	 * 
	 * @param uri
	 */
	public void startPicCut(Uri uri) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		intentCarema.putExtra("crop", true);
		intentCarema.putExtra("scale", false);
		intentCarema.putExtra("noFaceDetection", true);// 不需要人脸识别功能
		// intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", 1);
		intentCarema.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片的宽高
		intentCarema.putExtra("outputX", 150);
		intentCarema.putExtra("outputY", 150);
		intentCarema.putExtra("return-data", true);
		startActivityForResult(intentCarema,
				26);
	}
	private void upLoadImage(Bitmap bitmap){
		String photoStr = PictureUtil.save(bitmap);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId",
					userID));
			params.add(new BasicNameValuePair("type", "2"));
			params.add(new BasicNameValuePair("relateId", userID));
			params.add(new BasicNameValuePair("imageStr",
					photoStr));

		} catch (Exception e) {
			e.printStackTrace();
		}

		Tools.getLog(Tools.i, "kkk", "params =============== "
				+ params.toString());
		com.yktx.check.service.Service
		.getService(Contanst.HTTP_IMAGEUPLOAD, null,
				null, ClockSetHeadActivity.this)
				.addList(params).request(UrlParams.POST);
	}
	private void updateUserName(String name){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			params.add(new BasicNameValuePair("userId",
					userID));
			params.add(new BasicNameValuePair("name",
					name));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		com.yktx.check.service.Service
		.getService(Contanst.HTTP_UPDATE_USERNAME, null,
				null, ClockSetHeadActivity.this)
				.addList(params).request(UrlParams.POST);
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
				case Contanst.IMAGEUPLOAD:
					Tools.getLog(Tools.d, "aaa", "图片===上传成功===完成");
					String imageUrl = (String) msg.obj;
					mEditor.putString("userheadimage", imageUrl);
					mEditor.commit();
					break;

				case Contanst.UPDATE_USERNAME:
					Tools.getLog(Tools.d, "aaa", "名改修改成功===完成");
					break;
				}
				break;
			case Contanst.BEST_INFO_FAIL:

				break;
			}
		}
	};
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		MobclickAgent.onPause(this);
	}
	public void showVisibleDialog(){
		new AlertDialog.Builder(mContext)
		.setTitle("提示")
		.setMessage("是否注销当前账号？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				JPushInterface.setAlias(mContext, "", new TagAliasCallback() {
					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
						// TODO Auto-generated method stub

					}
				});
				mEditor.putBoolean("islogin", false);
				mEditor.commit();
				ClockMainActivity.isLogin = false;
				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
				
				
			}
		})
		.setNegativeButton("返回", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		}).show();
	}
}
