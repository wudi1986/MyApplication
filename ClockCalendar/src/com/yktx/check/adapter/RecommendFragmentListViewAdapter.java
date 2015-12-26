package com.yktx.check.adapter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.bean.HotTestBean;
import com.yktx.check.bean.LatestBean;
import com.yktx.check.bean.MostDatesTaskBean;
import com.yktx.check.bean.TopPointUserBean;
import com.yktx.check.listview.TwoWayView;
import com.yktx.check.listview.TwoWayView.OnScrollListener;
import com.yktx.check.util.Tools;

/**
 * Created by Administrator on 2014/4/8.
 */
public class RecommendFragmentListViewAdapter extends BaseAdapter {//实现的是横滑的listview的东西
	//	implements OnScrollListener
	Activity context;

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private boolean isContinue = true;
	private boolean isRun = false;
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(null).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(20))
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public DisplayImageOptions options2 = new DisplayImageOptions.Builder()
	.showImageOnLoading(null).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheInMemory(false) // 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	private LayoutInflater inflater;


	public final int TOPPOINTUSER = 1;// 第一条达人排行
	public final int TODAYHOTJOB = 2;// 今日精品
	public final int HOTTASK = 3;// 今日热卡的item
	public final int NEWITEM = 5;// 最新创建的item
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	String userID;

	ArrayList<HotTestBean> hotList = new ArrayList<HotTestBean>(10);
	ArrayList<MostDatesTaskBean> todayBoutiqueJobList = new ArrayList<MostDatesTaskBean>(10);
	ArrayList<TopPointUserBean> topPointUserBeans = new ArrayList<TopPointUserBean>(10);
	ArrayList<LatestBean> latestBeans = new ArrayList<LatestBean>(10);
	private int thisAdapterType;
	int hotListSize = 0;


	public RecommendFragmentListViewAdapter(Activity context, String userID,int type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.userID = userID;
		thisAdapterType = type;
		Tools.getLog(Tools.d, "ccc", "thisAdapterType:===="+thisAdapterType);
	}


	public void setHotList(ArrayList<HotTestBean> hotList) {
		this.hotList = hotList;
		if (hotList != null)
			hotListSize = hotList.size();
	}

	public void setTodayBoutiqueJobList(ArrayList<MostDatesTaskBean> list) {
		todayBoutiqueJobList = list;
	}

	public void setTopPointUserBeans(
			ArrayList<TopPointUserBean> topPointUserBeans) {
		this.topPointUserBeans = topPointUserBeans;
	}


	public void setLatestBeans(ArrayList<LatestBean> latestBeans) {
		this.latestBeans = latestBeans;
	}



	@Override
	public int getCount() {
		// 头部的横滑 + 今日精品 + 今日6张热卡 + 最新创建的内容 = getContent
		if(thisAdapterType == 0){
			return topPointUserBeans.size();
		}else if(thisAdapterType == 1){
			return hotListSize-3;
		}else if(thisAdapterType == 2){
			return todayBoutiqueJobList.size()-3;
		}else{
			return latestBeans.size()-3;
		}


	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		if (position == 0) {
			return TOPPOINTUSER;
		} else if (position == hotListSize + 1) {
			return TODAYHOTJOB;
		} else if (position > 0 && position < hotListSize + 1) {
			return HOTTASK;
		} else {
			return NEWITEM;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecommendFragmentListViewAdapter.HoldView hotHoldView;
		NewHolderView newHolderView;

		if (thisAdapterType == 0) {
			TopPointUserViewHolder holder;
			if(convertView == null){
				convertView = inflater.inflate(
						R.layout.recommendfragment_listviewadapter_item1, null);
				holder = new TopPointUserViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (TopPointUserViewHolder) convertView.getTag();
			}
			TopPointUserBean topPointUserBean = topPointUserBeans.get(position);
			TopPointUserShowView(holder, position, topPointUserBean);

			return convertView;
		} else if (thisAdapterType == 2) {

			TopPointUserViewHolder holder;
			if(convertView == null){
				convertView = inflater.inflate(
						R.layout.recommendfragment_listviewadapter_item1, null);
				holder = new TopPointUserViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (TopPointUserViewHolder) convertView.getTag();
			}
			MostDatesTaskBean mostDatesTaskBean = todayBoutiqueJobList.get(position);
			MostDatesTaskShowView(holder, position, mostDatesTaskBean);

			return convertView;

		} else if (thisAdapterType == 1) {
			if (convertView == null) {

				HotTestBean HotTestBean = hotList.get(position);
				convertView = inflater
						.inflate(R.layout.hot_fragment_item, null);
				hotHoldView = new RecommendFragmentListViewAdapter.HoldView(
						convertView);
				convertView.setTag(hotHoldView);
				show(hotHoldView, HotTestBean, position);
			} else {

				hotHoldView = (HoldView) convertView.getTag();
				HotTestBean HotTestBean = hotList.get(position );
				show(hotHoldView, HotTestBean, position);
				hotHoldView = (HoldView) convertView.getTag();
			}
			return convertView;
		} else {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.recommendfragment_listview_newitem, null);
				newHolderView = new NewHolderView(convertView);
				convertView.setTag(newHolderView);
			} else {
				newHolderView = (NewHolderView) convertView.getTag();
			}
			LatestBean latestBean = latestBeans.get(position);
			showNewView(newHolderView, latestBean);
			return convertView;
		}
	}

	public void showNewView(NewHolderView newHolderView,
			final LatestBean latestBean) {
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(latestBean.getImageSource())
				+ latestBean.getAvartarPath()+(latestBean.getImageSource() == 2?"?imageMogr2/thumbnail/45x45":""),
				newHolderView.recommendfragment_listview_newitem_headImage,
				headOptions);
		newHolderView.recommendfragment_listview_newitem_name
		.setText(latestBean.getName());
		newHolderView.recommendfragment_listview_newitem_title
		.setText(latestBean.getTitle());
		newHolderView.recommendfragment_listview_newitem_userLayout
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(latestBean.getUserId())) {
					Intent in = new Intent(context,
							ClockMyActivity.class);
					context.startActivity(in);
				} else {
					Intent in = new Intent(context,
							ClockOtherUserActivity.class);
					in.putExtra("userid", latestBean.getUserId());
					context.startActivity(in);
				}
			}
		});
	}
	public void TopPointUserShowView(TopPointUserViewHolder holder,int position,TopPointUserBean topPointUserBean){//第一个type 
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(topPointUserBean.getImageSource())
				+ topPointUserBean.getAvartarPath()+(topPointUserBean.getImageSource() == 2?"?imageMogr2/thumbnail/85x85":""),
				holder.recommendfragment_listviewadapter_item1_headImage,
				headOptions);
		if(position == 0){
			holder.recommendfragment_listviewadapter_item1_headImageBackground.setImageResource(R.drawable.guangchang_gc_first);
		}else if(position == 1){
			holder.recommendfragment_listviewadapter_item1_headImageBackground.setImageResource(R.drawable.guangchang_gc_second);
		}else if(position == 2){
			holder.recommendfragment_listviewadapter_item1_headImageBackground.setImageResource(R.drawable.guangchang_gc_third);
		}else{
			holder.recommendfragment_listviewadapter_item1_headImageBackground.setImageResource(R.drawable.guangchang_gc_other);
		}
		holder.recommendfragment_listviewadapter_item1_Name.setText(topPointUserBean.getName());
		holder.recommendfragment_listviewadapter_item1_pointNum.setText(topPointUserBean.getPoint());
	}
	public void MostDatesTaskShowView(TopPointUserViewHolder holder,int position,final MostDatesTaskBean mostDatesTaskBean){// 第三个type
		holder.recommendfragment_listviewadapter_item1_RightLayout1.setVisibility(View.GONE);
		holder.recommendfragment_listviewadapter_item1_RightLayout2.setVisibility(View.VISIBLE);
		holder.recommendfragment_listviewadapter_item1_JobName.setVisibility(View.VISIBLE);
		holder.recommendfragment_listviewadapter_item1_headImage.setVisibility(View.GONE);
		holder.recommendfragment_listviewadapter_item1_headImage1.setVisibility(View.VISIBLE);
		holder.recommendfragment_listviewadapter_item1_headImageBackground.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(mostDatesTaskBean.getImageSource())
				+ mostDatesTaskBean.getAvartar_path()+(mostDatesTaskBean.getImageSource() == 2?"?imageMogr2/thumbnail/75x75":""),
				holder.recommendfragment_listviewadapter_item1_headImage1, headOptions);
		holder.recommendfragment_listviewadapter_item1_Name.setText(mostDatesTaskBean.getName());
		holder.recommendfragment_listviewadapter_item1_JobName.setText("# "+mostDatesTaskBean.getTitle()+" #");
		holder.recommendfragment_listviewadapter_item1_totalDateCount.setText(mostDatesTaskBean.getCheckDateCount()+"");
		holder.recommendfragment_listviewadapter_item1_headImage1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(mostDatesTaskBean.getUserId())) {
					Intent in = new Intent(context,
							ClockMyActivity.class);
					context.startActivity(in);
				} else {
					Intent in = new Intent(context,
							ClockOtherUserActivity.class);
					in.putExtra("userid", mostDatesTaskBean.getUserId());
					context.startActivity(in);
				}
			}
		});
	}


	private void show(final HoldView holdView, HotTestBean HotTestBean,
			int position) {
		AbsListView.LayoutParams leftParams;
		RelativeLayout.LayoutParams hotLeftImageParams;
		RelativeLayout.LayoutParams hotLeftTextParams;
		if (position < 3) {
			holdView.hotMsgnum.setTextColor(context.getResources().getColor(
					R.color.white));
			holdView.hotMsgnum
			.setBackgroundResource(R.drawable.recommend_item_1);
			holdView.hotMsgnum.setTextSize(16);// 像素为sp
		} else {
			holdView.hotMsgnum.setTextColor(context.getResources().getColor(
					R.color.meibao_color_11));
			holdView.hotMsgnum.setBackgroundResource(R.drawable.toumingimg);
			holdView.hotMsgnum.setTextSize(16);// 像素为sp
		}
		holdView.hotMsgnum.setText((position + 1) + "");
		holdView.hotGroupName.setText(HotTestBean.getTitle());
		holdView.hotGroupPeopleNum.setText("今日"
				+ HotTestBean.getJobCountToday());
	}

	public class HoldView {
		public TextView hotMsgnum, hotGroupName, hotGroupPeopleNum,
		hotGroupMsgCount;
		private LinearLayout hotLayout;
		private ImageView hotLeftImage;

		public HoldView(View convertView) {

			hotLayout = (LinearLayout) convertView
					.findViewById(R.id.hotLayout);
			hotGroupMsgCount = (TextView) convertView
					.findViewById(R.id.hotGroupMsgCount);
			hotMsgnum = (TextView) convertView.findViewById(R.id.hotMsgnum);
			hotGroupName = (TextView) convertView
					.findViewById(R.id.hotGroupName);
			hotGroupPeopleNum = (TextView) convertView
					.findViewById(R.id.hotGroupPeopleNum);
			hotLeftImage = (ImageView) convertView
					.findViewById(R.id.hotLeftImage);

		}
	}

	class NewHolderView {
		private ImageView recommendfragment_listview_newitem_headImage;
		private TextView recommendfragment_listview_newitem_title,
		recommendfragment_listview_newitem_name;
		private RelativeLayout recommendfragment_listview_newitem_userLayout;

		public NewHolderView(View convertView) {
			recommendfragment_listview_newitem_headImage = (ImageView) convertView
					.findViewById(R.id.recommendfragment_listview_newitem_headImage);
			recommendfragment_listview_newitem_title = (TextView) convertView
					.findViewById(R.id.recommendfragment_listview_newitem_title);
			recommendfragment_listview_newitem_name = (TextView) convertView
					.findViewById(R.id.recommendfragment_listview_newitem_name);
			recommendfragment_listview_newitem_userLayout = (RelativeLayout) convertView
					.findViewById(R.id.recommendfragment_listview_newitem_userLayout);
		}

	}
	class TopPointUserViewHolder{
		LinearLayout recommendfragment_listviewadapter_item1_RightLayout1,
		recommendfragment_listviewadapter_item1_RightLayout2;
		TextView recommendfragment_listviewadapter_item1_Name,recommendfragment_listviewadapter_item1_JobName,
		recommendfragment_listviewadapter_item1_totalDateCount,recommendfragment_listviewadapter_item1_pointNum;
		ImageView recommendfragment_listviewadapter_item1_headImage,recommendfragment_listviewadapter_item1_headImageBackground,
		recommendfragment_listviewadapter_item1_headImage1;
		public TopPointUserViewHolder(View convertView){
			recommendfragment_listviewadapter_item1_RightLayout1 = (LinearLayout) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_RightLayout1);
			recommendfragment_listviewadapter_item1_RightLayout2 = (LinearLayout) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_RightLayout2);

			recommendfragment_listviewadapter_item1_Name = (TextView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_Name);
			recommendfragment_listviewadapter_item1_JobName = (TextView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_JobName);
			recommendfragment_listviewadapter_item1_totalDateCount = (TextView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_totalDateCount);
			recommendfragment_listviewadapter_item1_pointNum = (TextView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_pointNum);

			recommendfragment_listviewadapter_item1_headImage = (ImageView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_headImage);
			recommendfragment_listviewadapter_item1_headImageBackground = (ImageView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_headImageBackground);
			recommendfragment_listviewadapter_item1_headImage1 = (ImageView) convertView.findViewById(R.id.recommendfragment_listviewadapter_item1_headImage1);
		}
	}

		

}
