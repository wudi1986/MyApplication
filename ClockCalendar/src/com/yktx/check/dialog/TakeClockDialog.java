package com.yktx.check.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.gallety.PhotoActivity;
import com.clock.service.AddShowPhotoService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.ClockApplication;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockMainActivity.OnSetDialogImage;
import com.yktx.check.R;
import com.yktx.check.adapter.TakeClockAdapter;
import com.yktx.check.bean.BasicInfoBean;
import com.yktx.check.bean.ByTaskIdBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.CreateJobBean;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.UnitBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listview.HorizontalListView;
import com.yktx.check.service.GetRecommendService;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.Tools;

public class TakeClockDialog extends Dialog implements ServiceListener {
	private Activity mActivity;
	private ImageView mTakeClockSuccess;
	private EditText mInputContent, mInputNum;
	private TaskClockDialogOnCLickClockSuccess mCLickClockSuccess;
	public String cameraPhotoName;
	// 相机跳转的标示
	public static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	// 相册的跳转提示
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;
	public File cameraFile;
	private TakeClockAdapter adapter;
	private HorizontalListView mHorizontalListView;
	private LinearLayout mOutSide;
	private TextView taskName,unitText;
	String unitStr = "0";
	private String lastNum, taskNameStr;// 上次数量
	public ArrayList<ImageListBean> filenames = new ArrayList<ImageListBean>(3);// 相册选取的集合的名字
	private LinearLayout titleLayout, unitLayout;
	private ImageView mTakeWeiboSyImage, taskImage;

	public boolean isWeiboSY = true;

	private boolean isCamera;

	public ArrayList<ImageListBean> getFilenames() {
		return filenames;
	}

	public void setFilenames(ArrayList<ImageListBean> filenames) {
		this.filenames = filenames;
	}

	public TakeClockDialog(Activity activity) {
		super(activity, R.style.dialog);
		// TODO Auto-generated constructor stub
		mActivity = activity;
		conn();
	}

	private void conn() {
		Service.getService(Contanst.HTTP_JOB_GETALLUNIT, null, null,
				TakeClockDialog.this).addList(null).request(UrlParams.GET);
	}

	public void setLastNumAndUnit(String num, String unit) {
		lastNum = num;
		unitStr = unit;

	}

	public void setTaskNameStr(String taskNameStr) {
		this.taskNameStr = taskNameStr;

	}

	public void setOnClickClockSuccess(
			TaskClockDialogOnCLickClockSuccess cLickClockSuccess) {
		mCLickClockSuccess = cLickClockSuccess;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.takeclock_dialog);
		initView();
	}

	public void reflashList(ArrayList<ImageListBean> list) {
		filenames = list;
		adapter.setList(list);
		adapter.notifyDataSetChanged();
	}

	public void initView() {
		setOnShowListener(tener);
		setOnDismissListener(dismissListener);
		windowDeploy(0, 100);
		titleLayout = (LinearLayout) findViewById(R.id.titleLayout);

		unitLayout = (LinearLayout) findViewById(R.id.unitLayout);
		mHorizontalListView = (HorizontalListView) findViewById(R.id.takeclock_dialog_HorizontalListView);
		mInputContent = (EditText) findViewById(R.id.takeclock_dialog_input);
		mInputNum = (EditText) findViewById(R.id.takephoto_dialog_inputNum);
		mTakeClockSuccess = (ImageView) findViewById(R.id.takephoto_dialog_success);
		taskImage = (ImageView) findViewById(R.id.taskImage);
		mOutSide = (LinearLayout) findViewById(R.id.takephoto_dialog_outSide);
		mTakeWeiboSyImage = (ImageView) findViewById(R.id.takephoto_dialog_weiboSY);
		taskName = (TextView) findViewById(R.id.taskName);
		unitText = (TextView) findViewById(R.id.unitText);

		// ClockMainActivity.setDialogImage(setDialogImage);
		Tools.getLog(Tools.i, "aaa", "lastNum:" + "asdas" + lastNum);
		taskName.setText(taskNameStr);
		if (lastNum != null && lastNum.length() != 0) {
			String s = "数值（你上一卡：" + lastNum + "）";
			mInputNum.setHint(s);
		}
		mInputNum.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			int editStart;
			int editEnd;

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				editStart = mInputNum.getSelectionStart();
				editEnd = mInputNum.getSelectionEnd();
				int infoNum = temp.length();
				String str = s.toString();
				if (str.length() > 1 && !isFloathString(str)) {
					Toast.makeText(mActivity, "请输入数字。", Toast.LENGTH_SHORT)
					.show();
					s.delete(editStart - 1, editEnd);

					temp = s;
					infoNum = temp.length();
					mInputNum.setText(temp);
					mInputNum.setSelection(infoNum);
				} else if (str.length() > 1 && Float.parseFloat(str) > 10000) {
					Toast.makeText(mActivity, "数字应小于10000。", Toast.LENGTH_SHORT)
					.show();
					s.delete(editStart - 1, editEnd);
					temp = s;
					infoNum = temp.length();
					mInputNum.setText(temp);
					mInputNum.setSelection(infoNum);
				} else if (str.length() > 1) {

					String[] floatStringPartArray = str.split("\\.");
					if (floatStringPartArray.length > 1) {
						if (floatStringPartArray[1].length() > 2) {
							Toast.makeText(mActivity, "小数点最多保留两位小数",
									Toast.LENGTH_SHORT).show();
							s.delete(editStart - 1, editEnd);
							temp = s;
							infoNum = temp.length();
							mInputNum.setText(temp);
							mInputNum.setSelection(infoNum);
						}
					}
				}
				// 判断首位 的特殊字符
				String oldstr = mInputNum.getText().toString();
				Pattern pattern = Pattern
						.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&;*（）——+|{}【】‘；：”“’。，、？]");
				Matcher matcher = pattern.matcher(oldstr);
				StringBuffer sbr = new StringBuffer();
				while (matcher.find()) {
					matcher.appendReplacement(sbr, "");
				}
				matcher.appendTail(sbr);
				String newstr = sbr.toString();
				if (!newstr.equals(oldstr) && oldstr.length() == 1) {
					Toast.makeText(mActivity, "输入字符不合法", Toast.LENGTH_SHORT)
					.show();
					mInputNum.setSelection(newstr.length());
					mInputNum.setText(newstr);
				}

			}
		});

		if(unitStr == null || unitStr.equals("0") ){
			unitText.setText("单位");
		} else {
			unitText.setText(unitStr);
		}
		unitLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (unitArray == null || unitArray.length == 0) {
					Toast.makeText(mActivity, "您的手速如此之快。", Toast.LENGTH_SHORT)
					.show();
				} else {
					showsetheaddialog();
				}

			}
		});

		mInputContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mInputContent.setCursorVisible(true);
			}
		});
		mTakeWeiboSyImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isWeiboSY) {
					isWeiboSY = false;
					mTakeWeiboSyImage
					.setImageResource(R.drawable.home_paizhao_weibo1);
				} else {
					isWeiboSY = true;
					mTakeWeiboSyImage
					.setImageResource(R.drawable.home_paizhao_weibo2);
				}
			}
		});
		mHorizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				getCanShowNum();
				Tools.getLog(Tools.i, "aaa", "arg2 ============= " + arg2);
				if (arg2 == 0) {
					if (canshownum == 3) {
						return;
					}
					isCamera = true;
					cameraPhotoName = System.currentTimeMillis() + ".jpg";
					Tools.getLog(Tools.d, "aaa", 123456789 + "");
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					cameraFile = new File(FileURl.ImageFilePath,
							cameraPhotoName);
					cameraFile.getParentFile().mkdirs();
					Tools.getLog(Tools.d, "aaa", "cameraFile.getParentFile():"
							+ cameraFile.getPath());
					Uri uri = Uri.fromFile(cameraFile);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					mActivity
					.startActivityForResult(
							intent,
							TakeClockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
					if (!Tools.isExitsSdcard()) {
						String st = "SD卡不存在，不能拍照";
						Toast.makeText(mActivity, st, 0).show();
						return;
					}
					// Intent camera = new
					// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// mActivity.startActivityForResult(camera,
					// TakeClockDialog.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);

				} else if (arg2 == 4) {
					// TODO Auto-generated method stub

					if (canshownum == 3) {
						return;
					}
					isCamera = false;
					Intent intent_gralley = new Intent(mActivity,
							PhotoActivity.class);

					getCanShowNum();
					intent_gralley.putExtra("canshownum", canshownum);
					mActivity
					.startActivityForResult(
							intent_gralley,
							TakeClockDialog.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
				// else {
				// if (filenames.get(arg2 - 1).getIsCheck() == true) {
				// filenames.get(arg2 - 1).setCheck(false);
				// } else {
				// filenames.get(arg2 - 1).setCheck(true);
				// }
				// adapter.setList(filenames);
				// // adapter.notifyDataSetChanged();
				// }

				// if (bean.getIsCheck()) {
				// // holder.mcheck.setVisibility(View.GONE);
				// filenames.get(arg2).setCheckNum(0);
				// mPhotoList.get(arg2).setCheck(false);
				// filenames.get(arg2).setCheck(false);
				// Tools.getLog(Tools.d, "aaa", "filenames============="
				// + filenames.toString());
				// } else {
				// mPhotoList.get(arg2).setCheck(true);
				// filenames.get(arg2).setCheck(true);
				// // holder.mcheck.setVisibility(View.VISIBLE);
				// int check = 0;
				// for (int i = 0; i < filenames.size(); i++) {
				// if (filenames.get(i).getIsCheck()) {
				// filenames.get(i)
				// .setCheckNum(
				// filenames.get(i)
				// .getCheckNum() + 1);
				// }
				// }
				// filenames.get(arg2).setCheckNum(check + 1);
				// }
			}
		});

		getPhoto();
		mOutSide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mActivity, "clickblank");
				dismiss();

			}
		});
		mTakeClockSuccess.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// titleLayout.setBackgroundResource(R.drawable.giveup_dialog_result_shape);
				// taskImage.setImageResource(R.drawable.home_paizhao_duihao2);
				// taskName.setTextColor(mActivity.getResources().getColor(R.color.white));
				String content = mInputContent.getText().toString();
				String num = mInputNum.getText().toString();
				if (content != null && content.length() != 0) {
					MobclickAgent.onEvent(mActivity, "remarknotnull");
				}
				if (num != null && num.length() != 0) {
					MobclickAgent.onEvent(mActivity, "numbernotnull");
					if (num.substring(num.length() - 1).equals(".")) {// 如果他最后为
						// . 不合法
						// 不上传
						Toast.makeText(mActivity, "输入字符不合法", Toast.LENGTH_SHORT)
						.show();

						return;
					}
				}
				if(unitStr == null ||unitStr.equals("无") ||unitStr.equals("单位")){
					unitStr = "0";
				}
				if(adapter == null){
					mCLickClockSuccess.onClickSuccess(content, num, unitStr,
							new ArrayList<ImageListBean>());
				}else{
					mCLickClockSuccess.onClickSuccess(content, num, unitStr,
							adapter.getList());
				}
				
				// new Handler().postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// dismiss();
				// }
				// }, 10);
				dismiss();
			}
		});
	}

	// *显示dialog*/
	private void showsetheaddialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(mActivity,
						R.style.CustomDiaLog_by_SongHang));

		builder.setItems(unitArray,
				new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//						if(which == 0){
				//							unitText.setTextColor(mActivity.getResources().getColor(R.color.meibao_color_10));
				//						}else{
				unitText.setTextColor(mActivity.getResources().getColor(R.color.meibao_color_9));
				//						}
				unitStr = unitArray[which];
				unitText.setText(unitStr);
			}
		});
		builder.show();
	}

	// 判断某个字符串是否是整数字符串，若是数字字符串返回0，若不是则返回-1
	private boolean isNumberString(String testString) {
		String numAllString = "0123456789";
		if (testString.length() <= 0)
			return false;
		for (int i = 0; i < testString.length(); i++) {
			String charInString = testString.substring(i, i + 1);
			if (!numAllString.contains(charInString))
				return false;
		}
		return true;
	}

	// 判断某个字符串是否是float字符串，若是返回0，若不是则返回-1
	public boolean isFloathString(String testString) {
		if (!testString.contains(".")) {
			return isNumberString(testString);
		} else {
			if (testString.substring(testString.length() - 1).equals(".")) {
				testString += "00";
			}
			String[] floatStringPartArray = testString.split("\\.");
			if (floatStringPartArray.length == 2) {
				if (isNumberString(floatStringPartArray[0])
						&& isNumberString(floatStringPartArray[1]))
					return true;
				else
					return false;

			}

			else
				return false;
		}

	}

	// private boolean isWithinMath(String testString){
	// Float math = Float.parseFloat(testString);
	// if(math<9999){
	// }
	//
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Tools.getLog(Tools.d, "aaa", "dialog的back");
			MobclickAgent.onEvent(mActivity, "clickback");
			dismiss();
		}
		return super.onKeyDown(keyCode, event);
	}

	public int canshownum;

	private void getCanShowNum() {
		int index = 0;
		for (int i = 0; i < filenames.size(); i++) {
			if (filenames.get(i).getIsCheck()) {
				index++;
			}
		}
		canshownum = index;
	}

	OnSetDialogImage setDialogImage = new OnSetDialogImage() {

		@Override
		public void setImage(ArrayList<ImageListBean> beans,
				boolean isTakePicture) {
			// TODO Auto-generated method stub
			Tools.getLog(Tools.d, "aaa", beans.toString());
			adapter.setList(beans);
			Tools.getLog(Tools.d, "aaa", "beans" + beans.toString());
		}
	};

	OnDismissListener dismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			ClockApplication.closeKeybord(mInputContent, mActivity);
			Tools.getLog(Tools.d, "aaa", "==========关闭dialog============");
			// if (filenames != null) {
			// for (int i = 0; i < filenames.size(); i++) {
			// filenames.get(i).setCheck(false);
			// }
			// }
		}
	};
	OnShowListener tener = new OnShowListener() {
		@Override
		public void onShow(DialogInterface arg0) {
			// TODO Auto-generated method stub
			WindowManager windowManager = mActivity.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.width = (int) (display.getWidth()); // 设置宽度
			getWindow().setAttributes(lp);
		}

	};
	// 设置窗口显示
	private Window window = null;

	public void windowDeploy(int x, int y) {
		window = getWindow(); // 得到对话框
		window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画
		WindowManager.LayoutParams wl = window.getAttributes();
		// 根据x，y坐标设置窗口需要显示的位置w
		wl.x = x; // x小于0左移，大于0右移
		wl.y = y; // y小于0上移，大于0下移
		// wl.alpha = 0.6f; //设置透明度
		// wl.gravity = Gravity.BOTTOM; //设置重力
		window.setAttributes(wl);
	}

	public void getPhoto() {
		filenames.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 获取SDcard卡的路径
				String sdcardPath = Environment.getExternalStorageDirectory()
						.toString();

				ContentResolver mContentResolver = mActivity
						.getContentResolver();
				Cursor mCursor = mContentResolver.query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.Media._ID,
								MediaStore.Images.Media.DATA },
								MediaStore.Images.Media.MIME_TYPE + "=? OR "
										+ MediaStore.Images.Media.MIME_TYPE + "=?",
										new String[] { "image/jpeg", "image/png" },
										MediaStore.Images.Media._ID + " DESC"); // 按图片ID降序排列
				while (mCursor.moveToNext()) {
					// 打印LOG查看照片ID的值
					long id = mCursor.getLong(mCursor
							.getColumnIndex(MediaStore.Images.Media._ID));
					Tools.getLog(Tools.i, "MediaStore.Images.Media_ID=", id
							+ "");
					// 获取照片路径
					ImageListBean bean = new ImageListBean();
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					bean.setImageUrl(path);
					Tools.getLog(Tools.i, "aaa", path);
					filenames.add(bean);
					// list.add("file://" + path);
					if (filenames.size() == 3) {
						break;
					}
				}
				mCursor.close();
				// 超过三张的删除，不加会出现三张全选之后不变灰的情况
				for (int i = filenames.size() - 1; i >= 3; i--) {
					filenames.remove(i);
				}
				Message msg = new Message();
				msg.obj = filenames;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	public void getLastPhoto() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String lastPath = "";
				// TODO Auto-generated method stub
				// 获取SDcard卡的路径
				String sdcardPath = Environment.getExternalStorageDirectory()
						.toString();

				ContentResolver mContentResolver = mActivity
						.getContentResolver();
				Cursor mCursor = mContentResolver.query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.Media._ID,
								MediaStore.Images.Media.DATA },
								MediaStore.Images.Media.MIME_TYPE + "=? OR "
										+ MediaStore.Images.Media.MIME_TYPE + "=?",
										new String[] { "image/jpeg", "image/png" },
										MediaStore.Images.Media._ID + " DESC"); // 按图片ID降序排列
				while (mCursor.moveToNext()) {
					// 打印LOG查看照片ID的值
					long id = mCursor.getLong(mCursor
							.getColumnIndex(MediaStore.Images.Media._ID));
					Tools.getLog(Tools.i, "MediaStore.Images.Media_ID=", id
							+ "");
					// 获取照片路径
					lastPath = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					// list.add("file://" + path);
					break;
				}
				mCursor.close();
				// 超过三张的删除，不加会出现三张全选之后不变灰的情况
				setChatPhotoListCamera(lastPath);
				Message msg = new Message();
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	public void reflashCanmera() {
		getLastPhoto();
		adapter.setList(filenames);
		adapter.notifyDataSetChanged();

	}

	private void setChatPhotoListCamera(String filePath) {
		ImageListBean bean = new ImageListBean();
		bean.setImageUrl(filePath);
		bean.setCheck(true);
		if (filenames.size() < 3) {
			filenames.add(0, bean);
		} else {
			for (int i = filenames.size() - 1; i >= 0; i--) {
				if (!filenames.get(i).getIsCheck()) {
					filenames.remove(i);
					filenames.add(0, bean);
					return;
				}
			}
		}
	}

	/** 获取聊天发送图片最后一张图 */
	private String getChatImageLastName() {
		// 获取SDcard卡的路径
		String sdcardPath = Environment.getExternalStorageDirectory()
				.toString();

		ContentResolver mContentResolver = mActivity.getContentResolver();
		Cursor mCursor = mContentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
						MediaStore.Images.Media._ID,
						MediaStore.Images.Media.DATA },
						MediaStore.Images.Media.MIME_TYPE + "=? OR "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
								new String[] { "image/jpeg", "image/png" },
								MediaStore.Images.Media._ID + " DESC"); // 按图片ID降序排列
		// ArrayList<ChatPhotoBean> list = new ArrayList<ChatPhotoBean>();
		int index = 0;
		while (mCursor.moveToNext() && index < 1) {
			// ImageListBean bean = new ImageListBean();
			// 打印LOG查看照片ID的值
			long id = mCursor.getLong(mCursor
					.getColumnIndex(MediaStore.Images.Media._ID));
			Tools.getLog(Tools.i, "MediaStore.Images.Media_ID=", id + "");
			// 获取照片路径
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			// bean.setImagePath(FileURl.LOAD_FILE + path);
			// list.add(bean);
			// index++;
			return FileURl.LOAD_FILE + path;
		}
		mCursor.close();
		return null;
		// Message msg = new Message();
		// msg.obj = list;
		// msg.what = GET_PHOTO_LIST_DESC;
		// mHandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.getLog(Tools.d, "aaa", "查看相册！");
			adapter = new TakeClockAdapter(mActivity);
			if (isCamera) {
				filenames.get(0).setCheck(true);// 拍照打卡返回会默认吧第一张为选中的状态
			}
			adapter.setList(filenames);
			mHorizontalListView.setAdapter(adapter);
		}
	};

	public interface TaskClockDialogOnCLickClockSuccess {
		public void onClickSuccess(String content, String num,String unit,
				ArrayList<ImageListBean> filenames);
	}

	@Override
	public void getJOSNdataSuccess(Object bean, String sccmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_OK;
		msg.obj = bean;
		msg.arg1 = connType;
		mConnHandler.sendMessage(msg);
	}

	@Override
	public void getJOSNdataFail(String errcode, String errmsg, int connType) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Contanst.BEST_INFO_FAIL;
		msg.obj = errmsg;
		msg.arg1 = connType;
		mConnHandler.sendMessage(msg);
	}

	String[] unitArray;

	private Handler mConnHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:
				switch (msg.arg1) {
				case Contanst.JOB_GETALLUNIT:

					ArrayList<UnitBean> list = (ArrayList<UnitBean>) msg.obj;
					unitArray = new String[list.size()+1];
					unitArray[0] = "无";
					for (int i = 0; i < list.size(); i++) {
						unitArray[i+1] = list.get(i).getText();
					}

					break;
				}
				break;

			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				switch (msg.arg1) {
				case Contanst.JOB_GETALLUNIT:
					Tools.getLog(Tools.d, "aaa", "GETBASICINFO:" + message);
					break;

				}
				break;
			}
		}
	};
}
