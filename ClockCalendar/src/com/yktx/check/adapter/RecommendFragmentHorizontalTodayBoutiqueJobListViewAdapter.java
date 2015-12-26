package com.yktx.check.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.bean.MostDatesTaskBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Tools;

public class RecommendFragmentHorizontalTodayBoutiqueJobListViewAdapter extends
		BaseAdapter {
	ArrayList<MostDatesTaskBean> todayBoutiqueJobList = new ArrayList<MostDatesTaskBean>(
			10);
	private Context mContext;
	private LayoutInflater inflater;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zw_touxiang)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zw_image).showImageForEmptyUri(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisk(true)
			// .displayer(new RoundedBitmapDisplayer(150))
			// .displayer(new RoundedBitmapDisplayer(6))
			// .cacheInMemory(false)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public DisplayImageOptions options1 = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.toumingimg)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			// .displayer(new RoundedBitmapDisplayer(150))
			// .displayer(new RoundedBitmapDisplayer(6))
			// .cacheInMemory(false)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	private String userID;

	public RecommendFragmentHorizontalTodayBoutiqueJobListViewAdapter(
			Context mContext, String userid) {
		super();
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		userID = userid;
	}

	public void setList(ArrayList<MostDatesTaskBean> list) {
		todayBoutiqueJobList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return todayBoutiqueJobList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView holderView;
		if (convertView == null) {
			convertView = inflater
					.inflate(
							R.layout.recommendfragment_horizontal_todayboutiquejob_item,
							null);
			holderView = new HoldView(convertView);
			convertView.setTag(holderView);

		} else {
			holderView = (HoldView) convertView.getTag();
		}
		MostDatesTaskBean itemBean = todayBoutiqueJobList.get(position);
		showView(itemBean, holderView, position);
		return convertView;
	}

	public void showView(final MostDatesTaskBean itemBean, HoldView holdView,
			int position) {
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(itemBean.getImageSource())
						+ itemBean.getAvartar_path()+(itemBean.getImageSource() == 2?"?imageMogr2/thumbnail/50x50":""),
				holdView.todayboutiquejob_item_headImage, headOptions);
		ImageLoader.getInstance().displayImage(
				UrlParams.QINIU_IP + itemBean.getBadgePath()+"?imageMogr2/thumbnail/21x26",
				holdView.todayboutiquejob_item_medalImage, options1);
		
		holdView.todayboutiquejob_item_clockName.setText(itemBean.getTitle());
		holdView.todayboutiquejob_item_currentStreak.setText(itemBean.getCheckDateCount()+"");
		
		holdView.todayboutiquejob_item_headImage
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// intoUserCenter.getIntoUserCenter(itemBean.getTaskId());\
						// GroupMainFragmentActivity.isReflush =
						// true;//让主activity 不刷新fragment
						if (userID.equals(itemBean.getUserId())) {
							Intent in = new Intent(mContext,
									ClockMyActivity.class);
							mContext.startActivity(in);
						} else {
							Intent in = new Intent(mContext,
									ClockOtherUserActivity.class);
							in.putExtra("userid", itemBean.getUserId());
							mContext.startActivity(in);
						}
					}
				});

		holdView.todayboutiquejob_item_name.setText(itemBean.getName());
		holdView.todayboutiquejob_item_name
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// GroupMainFragmentActivity.isReflush =
						// true;//让主activity 不刷新fragment
						if (userID.equals(itemBean.getUserId())) {
							Intent in = new Intent(mContext,
									ClockMyActivity.class);
							mContext.startActivity(in);
						} else {
							Intent in = new Intent(mContext,
									ClockOtherUserActivity.class);
							in.putExtra("userid", itemBean.getUserId());
							mContext.startActivity(in);
						}
					}
				});

		holdView.todayboutiquejob_item_clockContentLayout
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext,
								ClockGroupInfoFragmentActivity.class);
						in.putExtra("buildingId", itemBean.getBuildingId());
						mContext.startActivity(in);
					}
				});
	}

	public class HoldView {
		TextView todayboutiquejob_item_name,todayboutiquejob_item_clockName,
				todayboutiquejob_item_currentStreak;// 上部分的id

		ImageView todayboutiquejob_item_headImage,
				todayboutiquejob_item_medalImage;
		LinearLayout todayboutiquejob_item_clockContentLayout;
		RelativeLayout todayboutiquejob_item_topLayout,
				todayboutiquejob_item_clockLevelLayout;

		public HoldView(View convertView) {
			todayboutiquejob_item_name = (TextView) convertView
					.findViewById(R.id.todayboutiquejob_item_name);
			todayboutiquejob_item_currentStreak = (TextView) convertView
					.findViewById(R.id.todayboutiquejob_item_currentStreak);
			todayboutiquejob_item_clockName = (TextView) convertView
					.findViewById(R.id.todayboutiquejob_item_clockName);
			
			
			todayboutiquejob_item_headImage = (ImageView) convertView
					.findViewById(R.id.todayboutiquejob_item_headImage);
			todayboutiquejob_item_medalImage = (ImageView) convertView
					.findViewById(R.id.todayboutiquejob_item_medalImage);

			todayboutiquejob_item_clockContentLayout = (LinearLayout) convertView
					.findViewById(R.id.todayboutiquejob_item_clockContentLayout);

			todayboutiquejob_item_topLayout = (RelativeLayout) convertView
					.findViewById(R.id.todayboutiquejob_item_topLayout);
			todayboutiquejob_item_clockLevelLayout = (RelativeLayout) convertView
					.findViewById(R.id.todayboutiquejob_item_clockLevelLayout);
		}
	}


	/**
	 * 全角转换为半角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}
