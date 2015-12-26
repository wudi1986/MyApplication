package com.yktx.check;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.util.Tools;
import com.yktx.sqlite.DBHelper;

public abstract class BaseActivity extends Activity{
	public Activity mContext;
	public static int ScreenHeight;
	public static int ScreenWidth;
	DBHelper dbHelper;
	ClockApplication clockApplication = ClockApplication.getInstance();
	
	private LocationClient locationClient = null;
	LocationListenner getLocation;
	/**
	 * 密度
	 */
	public static float DENSITY;
	public DisplayImageOptions options;
	public SharedPreferences settings;
	public Editor mEditor;
	public DisplayMetrics mDisplayMetrics;
	public String userID;
	public static boolean isLogin;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(200))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );//不显示程序的标题栏
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

		mContext = this;
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		mEditor = settings.edit();
		userID = settings.getString("userid", null);
		isLogin = settings.getBoolean("islogin", false);
		dbHelper = new DBHelper(mContext);
		mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		ScreenHeight = mDisplayMetrics.heightPixels;
		ScreenWidth = mDisplayMetrics.widthPixels;
		DENSITY = mDisplayMetrics.density;
		options = new DisplayImageOptions.Builder().showImageOnLoading(null)
				.showImageForEmptyUri(null).showImageOnFail(null)
				.cacheInMemory(false)
				// 启用内存缓存
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setPriority(LocationClientOption.GpsFirst); // 设置定位优先级
		option.setScanSpan(10 * 60 * 1000);// 设置发起定位请求的间隔时间为5000ms
		option.setAddrType("all");
		option.setProdName("group"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		locationClient = new LocationClient(this);
		locationClient.setLocOption(option);

		locationClient.registerLocationListener(new BDLocationListener() {

			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub

				if (location == null) {
					getLocation.fail();
					return;
				}
				String latitude = location.getLatitude() + "";
				String longitude = location.getLongitude() + "";

				StringBuffer sb = new StringBuffer(256);
				sb.append(location.getCity());
				sb.append(".");
				sb.append(location.getDistrict());
				Tools.getLog(Tools.i,  "aaa", "sb =====" + sb.toString());
				getLocation.getLocatione(longitude, latitude, sb.toString());
			}

			public void onReceivePoi(BDLocation location) {
			}
		});
		BaseActivity.this.getLocationClient(new LocationListenner() {
			@Override
			public void getLocatione(String longitude, String latitude,
					String address) {
				// TODO Auto-generated method stub
				Tools.getLog(Tools.i,  "aaa", "longitude " + longitude);
				Tools.getLog(Tools.i,  "aaa", "latitude " + latitude);
				Editor edit = settings.edit();
				if (longitude.indexOf("E") == -1) {
					edit.putString("longitude", longitude);
					edit.putString("latitude", latitude);
				} else {
					edit.putString("longitude", "-1");
					edit.putString("latitude", "-1");
				}
				edit.putString("address", address);
				edit.commit();

			}

			@Override
			public void fail() {
				// TODO Auto-generated method stub
				// Toast.makeText(BaseActivity.this, "获取位置失败",
				// Toast.LENGTH_SHORT).show();
			}

		});
		findViews();
		init();
		setListeners();
	}
	protected abstract void findViews();

	protected abstract void init();

	protected abstract void setListeners();
	
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.app.Activity#onPause()
//	 */
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//		MobclickAgent.onPause(this);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.app.Activity#onResume()
//	 */
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
//		MobclickAgent.onResume(this);
//	}
	
//	public ProgressDialog mDialog;
//
//	/**
//	 * 加载的dialog
//	 * 
//	 * @param message  dialog的文本
//	 */
//	public void showProgressDialog(String message) {
//
//		// mDialog = CustomProgressDialog.createDialog(this);
//		mDialog = new ProgressDialog(this);
//		mDialog.setMessage(message);
//		mDialog.setIndeterminate(true);
//		mDialog.setCancelable(true);
//		mDialog.show();
//	}
	public interface LocationListenner {
		public void getLocatione(String longitude, String latitude,
				String address);

		public void fail();
	}
	/** 获取定位信息先启动locationClient */
	public void getLocationClient(LocationListenner getLocation) {
		this.getLocation = getLocation;
		if (locationClient == null) {
			return;
		}
		if (locationClient.isStarted()) {
			locationClient.stop();
		} else {
			locationClient.start();
			/*
			 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。 调用requestLocation(
			 * )后，每隔设定的时间，定位SDK就会进行一次定位。 如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
			 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
			 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
			 */
			locationClient.requestLocation();
		}

	}
}
