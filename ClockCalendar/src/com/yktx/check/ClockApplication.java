package com.yktx.check;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.yktx.check.bean.AlarmBean;
import com.yktx.check.util.FileURl;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class ClockApplication extends Application {
	private static ClockApplication clockApplication;
	private ArrayList<AlarmBean> alarmList;

	public ArrayList<AlarmBean> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(ArrayList<AlarmBean> alarmList) {
		this.alarmList = alarmList;
	}

	public static ClockApplication getInstance() {
		return clockApplication;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 获取异常信息
//		 CrashHandler crashHandler = CrashHandler.getInstance();
//		 crashHandler.init(getApplicationContext());

		clockApplication = this;
		aboutimageloder();// 初始化ImageLoders
		MobclickAgent.setDebugMode(false);
		aboutjpush();// 激光推送初始化
	}

	public String getUUID() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		return tm.getDeviceId();// 得到设备唯一ID，（GSM手机的 IMEI 和 CDMA手机的 MEID）
	}

	/**
	 * 初始化ImageLoder
	 */
	private void aboutimageloder() {
		File goodsiamge = new File(FileURl.GoodsIamgeURL);
		if (!goodsiamge.exists()) {
			goodsiamge.mkdirs();
		}
		File cacheDir = StorageUtils.getOwnCacheDirectory(this, "mew/imgcache");

		// DisplayImageOptions options = new DisplayImageOptions.Builder()
		// .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// this)
		// // .memoryCacheExtraOptions(480, 800)
		// // // default = device screen dimensions
		// // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 50,
		// // null)
		// .threadPoolSize(3)
		// // default
		// .threadPriority(Thread.NORM_PRIORITY - 2)
		// // default
		// .tasksProcessingOrder(QueueProcessingType.LIFO)
		// // default
		// .denyCacheImageMultipleSizesInMemory()
		// .memoryCache(new WeakMemoryCache())
		// .memoryCacheSize(2 * 1024 * 1024)
		// .memoryCacheSizePercentage(13)
		// .diskCacheFileCount(400)
		// // default
		// // .discCache(
		// // new TotalSizeLimitedDiscCache(cacheDir,
		// // 50 * 1024 * 1024))//imageloader 1.9.3注掉的
		// // default
		// // .imageDownloader(new BaseImageDownloader(context)) // default
		// // .imageDecoder(new BaseImageDecoder()) // default
		// .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) //
		// default
		// // .writeDebugLogs()
		// .build();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// .showImageOnFail(R.drawable.img_fail)
				.resetViewBeforeLoading(false) // default
				.delayBeforeLoading(0).cacheInMemory(true) // default
				.cacheOnDisk(true) // default
				.considerExifParams(true) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.handler(new Handler()) // default
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				clockApplication)
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				.diskCache(new UnlimitedDiskCache(cacheDir))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(1000)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(clockApplication)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(options) // default
//				.writeDebugLogs()
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	private void aboutjpush() {
		JPushInterface.setDebugMode(true);
		JPushInterface.init(getApplicationContext());
	}

	/**
	 * Activity列表（一键退出）
	 */
	private List<Activity> activityList = new LinkedList<Activity>();

	/**
	 * 保存Activity到现有列表中（一键退出）
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 关闭保存的Activity（一键退出）
	 */
	public void exit() {
		if (activityList != null) {
			for (Activity activity : activityList) {
				activity.finish();
			}
			clearActivity();
			// System.exit(0);
		}
	}

	/** 清空列表，取消引用（一键退出） */
	public void clearActivity() {
		activityList.clear();
	}

	/** 软键盘的打开 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);

	}

	/** 软键盘的关闭 */
	public static void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
	 /** 
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
     * @param context 
     * @return true 表示开启 
     */  
    public static final boolean isOpenGPS(final Context context) {  
        LocationManager locationManager   
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
        if (gps || network) {  
            return true;  
        }  
  
        return false;  
    } 
}
