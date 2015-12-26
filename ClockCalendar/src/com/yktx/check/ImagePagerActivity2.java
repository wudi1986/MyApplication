package com.yktx.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.bean.TaskGetImageBean;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.fragment.ImageDetailFragment;
import com.yktx.check.service.Service;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.HackyViewPager;

public class ImagePagerActivity2 extends FragmentActivity implements ServiceListener{
	private static final String STATE_POSITION = "STATE_POSITION";
	
	public static final String EXTRA_IMAGE_SOURCE = "getAllSource";
	/** 图片日期 */
	public static final String EXTRA_IMAGE_DATE = "image_date";
	public static final String EXTRA_IMAGE_USERID = "userID";
	public static final String EXTRA_IMAGE_TASKID = "taskID";
	
	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator
//	,page_imageInfo
	;
	String imageDate, getUserID,taskID,userId;
	ImagePagerAdapter mAdapter ;
	ArrayList<TaskGetImageBean> allCurList = new ArrayList<TaskGetImageBean>(10);
//	ImageView page_imageDelete;
	public SharedPreferences settings;
	/**
	 * 是否查看前一天的图片
	 */
	boolean isBefore;
	
	int curNum = 0;
	
	private String clockCalendar7UserID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		settings = getSharedPreferences("clock", MODE_PRIVATE);
		userId = settings.getString("userid", null);
		imageDate = getIntent().getStringExtra(EXTRA_IMAGE_DATE);
		getUserID = getIntent().getStringExtra(EXTRA_IMAGE_USERID);
		taskID = getIntent().getStringExtra(EXTRA_IMAGE_TASKID);
		String[] allSource = getIntent().getStringArrayExtra(EXTRA_IMAGE_SOURCE);
		Conn(imageDate);
//		page_imageDelete = (ImageView) findViewById(R.id.page_imageDelete);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
//		page_imageInfo = (TextView) findViewById(R.id.page_imageInfo);

		
//		if(!userId.equals(getUserID)){
//			page_imageDelete.setVisibility(View.GONE);
//		}
//		page_imageDelete.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				deleteImageDialog();
//			}
//		});
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageSelected(int arg0) {
				curNum = arg0;
				StringBuffer sb = new StringBuffer();
				String checkDate = allCurList.get(curNum).getCheckDate();
				int checkDayNum = allCurList.get(curNum).getCheckDayNum();
				String city = allCurList.get(curNum).getCity();
				sb.append("Day"+checkDayNum+" | "+checkDate.replace("-", "."));
				if(city != null &&!city.equals("0")){
					sb.append(" | "+city);
				}
//				page_imageInfo.setText(sb.toString());
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}
			
		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		private ArrayList<TaskGetImageBean> curList;

		public void setList(ArrayList<TaskGetImageBean> curList) {
			// TODO Auto-generated method stub
			this.curList = curList;

		}
		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return curList == null ? 0 : curList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = curList.get(position).getPath();
			int sourchArr = curList.get(position).getImageSource();
			return ImageDetailFragment.newInstance(url,sourchArr);
		}

	}
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
	
//	public void deleteImageConn(String imageid){
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		try {
//			params.add(new BasicNameValuePair("userId", getUserID));
//			params.add(new BasicNameValuePair("imgId", imageid));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		Service.getService(Contanst.HTTP_IMAGE_DELETE, null, null,
//				ImagePagerActivity2.this).addList(params).request(UrlParams.POST);
//	}

	public void Conn(String imageDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("?userId=");
		sb.append(getUserID);
		sb.append("&taskId=");
		sb.append(taskID);
		sb.append("&date=");
		sb.append(imageDate);
		Service.getService(Contanst.HTTP_TASK_GETIMAGE, null,
				sb.toString(), ImagePagerActivity2.this).addList(null)
				.request(UrlParams.GET);
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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contanst.BEST_INFO_OK:

				switch (msg.arg1) {

				case Contanst.TASK_GETIMAGE:
					// createDate = new CustomDate(2014,);

					allCurList = (ArrayList<TaskGetImageBean>) msg.obj;
					if(allCurList.size() == 0){
						finish();
						Toast.makeText(ImagePagerActivity2.this, "木有图片了！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					StringBuffer sb = new StringBuffer();
					String checkDate = allCurList.get(0).getCheckDate();
					int checkDayNum = allCurList.get(0).getCheckDayNum();
					String city = allCurList.get(0).getCity();
					sb.append("Day"+checkDayNum+" | "+checkDate.replace("-", "."));
					if(city != null &&!city.equals("0")){
						sb.append(" | "+city);
					}
//					page_imageInfo.setText(sb.toString());
					
//					if(isBefore){
//						curList.addAll(0,list);
//					} else {
//						curList.addAll(list);
//					}
					
					mAdapter.setList(allCurList);
					mPager.setAdapter(mAdapter);
					
					mPager.setCurrentItem(allCurList.size()-1);
					
					CharSequence text = getString(R.string.viewpager_indicator, mPager.getAdapter().getCount(), mPager
							.getAdapter().getCount());
					indicator.setText(text);
//					mAdapter.notifyDataSetChanged();
					
					break;
//				case Contanst.IMAGE_DELETE:
//					Conn(imageDate);
//					break;
				}

				break;
			case Contanst.BEST_INFO_FAIL:
				String message = (String) msg.obj;
				Tools.getLog(Tools.d, "aaa", message);
				switch (msg.arg1) {

				case Contanst.TASK_GETIMAGE:
					break;
				}
				break;
			}
		}
	};
//	public void deleteImageDialog() {
//		new AlertDialog.Builder(ImagePagerActivity.this)
//		.setTitle("提示")
//		.setMessage("如果删除将同步删除打卡7上的数据。")
//		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				deleteImageConn(allCurList.get(curNum).getImgId());
//			}
//		})
//		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//
//			}
//		}).show();
//	}
}