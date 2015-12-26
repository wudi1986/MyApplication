package com.yktx.check.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockApplication;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockSearchActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.RecommendFragmentListViewAdapter.HoldView;
import com.yktx.check.bean.RecommendFollowBean;
import com.yktx.check.util.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AttentionRecommendFragmentListViewAdapter extends BaseAdapter{
	String userID;
	private Activity mContext;
	private LayoutInflater inflater;
	private ArrayList<RecommendFollowBean> recommendFollowBeans = new ArrayList<RecommendFollowBean>(10);
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public DisplayImageOptions options1 = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	boolean isSearch;
	boolean isNull;
	private EditText SearchInput;
	
	
	public AttentionRecommendFragmentListViewAdapter(Activity mContext,String userID,boolean issearch) {
		super();
		this.mContext = mContext;
		this.userID = userID;
		isSearch = issearch;
		inflater = LayoutInflater.from(mContext);
	}
	setFansTypeLisitener fansTypeLisitener;
	public void thisetFansTypeLisitener(setFansTypeLisitener fansTypeLisitener){
		this.fansTypeLisitener = fansTypeLisitener;
	}
	public void setList(ArrayList<RecommendFollowBean> list){
		recommendFollowBeans = list;
	}
	public void setEditText(EditText editText){
		SearchInput = editText;
	}
	public void setIsSearch(boolean issearch){
		isSearch = issearch;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int sice = recommendFollowBeans.size();
		if(sice == 0){
			isNull = true;
			return 1;
		}
		isNull = false;
		return sice;
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
		ViewHolder holder = null;
		if(isNull){
			View v = inflater.inflate(
					R.layout.image_null_item, null);
			LinearLayout imageListNullLayout = (LinearLayout) v.findViewById(R.id.imageListNullLayout);
			imageListNullLayout.setBackgroundResource(R.drawable.toumingimg);
			ImageView image = (ImageView) v.findViewById(R.id.imageListNull);
			image.setImageResource(R.drawable.zhanwei_search);
			return v;
		}
		if(convertView == null|| holder == null){
			convertView = inflater.inflate(R.layout.attentionrecommendfragmentlistview_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		RecommendFollowBean recommendFollowBean = recommendFollowBeans.get(position); 
		showView(holder, recommendFollowBean, position);
		return convertView;
	}
	public void showView(ViewHolder holder,final RecommendFollowBean bean,final int position){
		if(isSearch){
			holder.attentionrecommendfragmentlistview_item_TopTitle.setVisibility(View.GONE);
//			holder.groupSearchLayout.setVisibility(View.GONE);
		}else{
			if(position == 0){
				holder.attentionrecommendfragmentlistview_item_TopTitle.setVisibility(View.VISIBLE);
				
//				holder.groupSearchLayout.setVisibility(View.VISIBLE);
//				holder.groupSearchLayout.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
////						Toast.makeText(mContext, "搜索！", Toast.LENGTH_SHORT).show();
//						Intent in = new Intent(mContext, ClockSearchActivity.class);
//						mContext.startActivity(in);
//						mContext.overridePendingTransition(R.anim.zoomin,
//								R.anim.zoom_anim1);
//					}
//				});
				
			}else{
				holder.attentionrecommendfragmentlistview_item_TopTitle.setVisibility(View.GONE);
//				holder.groupSearchLayout.setVisibility(View.GONE);
			}
		}
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(bean.getImageSource()) + bean.getAvartarPath()+(bean.getImageSource() == 2?"?imageMogr2/thumbnail/70x70":""),
				holder.attentionrecommendfragmentlistview_item_headImage, headOptions);
		
		
		holder.attentionrecommendfragmentlistview_item_name.setText(bean.getName());
		holder.attentionrecommendfragmentlistview_item_botttomText.setText("正在坚持"+bean.getTaskCount()+"张卡");
		int relation = bean.getRelation();
		boolean isFans = false ;
		if(relation == -1){
			holder.attentionrecommendfragmentlistview_item_fanstypeLayout.setVisibility(View.GONE);

		}else if(relation == 0 ||relation == 1){
			holder.attentionrecommendfragmentlistview_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.attentionrecommendfragmentlistview_item_fanstypeImage.setImageResource(R.drawable.geren_guanzhu_weiguan);
			holder.attentionrecommendfragmentlistview_item_fanstypeText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.attentionrecommendfragmentlistview_item_fanstypeText.setText("关注");
			isFans = false;

		}else if(relation == 2 || relation == 3){
			holder.attentionrecommendfragmentlistview_item_fanstypeLayout.setVisibility(View.VISIBLE);
			holder.attentionrecommendfragmentlistview_item_fanstypeImage.setImageResource(R.drawable.geren_guanzhu_yiguan);
			holder.attentionrecommendfragmentlistview_item_fanstypeText.setTextColor(mContext.getResources().getColor(R.color.meibao_color_11));
			holder.attentionrecommendfragmentlistview_item_fanstypeText.setText("已关注");
			isFans = true;
		}

		if(bean.getTasks().size() >= 1){
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(bean.getTasks().get(0).getBadgeSource()) + bean.getTasks().get(0).getBadgePath()
					,//+(bean.getTasks().get(0).getBadgeSource() == 2?"?imageMogr2/thumbnail/30x36":"")
					holder.attentionrecommendfragmentlistview_item_taskXZImage1,options1);
			holder.attentionrecommendfragmentlistview_item_taskName1.setText(bean.getTasks().get(0).getTitle());
			holder.attentionrecommendfragmentlistview_item_taskTotalDateCount1.setText("坚持"+bean.getTasks().get(0).getTotalDateCount()+"天");
		}else{
			holder.attentionrecommendfragmentlistview_item_taskLayout1.setVisibility(View.GONE);
		}


		if(bean.getTasks().size() >= 2){
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(bean.getTasks().get(1).getBadgeSource()) + bean.getTasks().get(1).getBadgePath()
					,//+(bean.getTasks().get(1).getBadgeSource() == 2?"?imageMogr2/thumbnail/30x36":"")
					holder.attentionrecommendfragmentlistview_item_taskXZImage2, options1);

			holder.attentionrecommendfragmentlistview_item_taskName2.setText(bean.getTasks().get(1).getTitle());
			holder.attentionrecommendfragmentlistview_item_taskTotalDateCount2.setText("坚持"+bean.getTasks().get(1).getTotalDateCount()+"天");
		}else{
			holder.attentionrecommendfragmentlistview_item_taskLayout2.setVisibility(View.GONE);
		}

		if(bean.getTasks().size() >= 3){
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(bean.getTasks().get(2).getBadgeSource()) + bean.getTasks().get(2).getBadgePath()
					,//+(bean.getTasks().get(2).getBadgeSource() == 2?"?imageMogr2/thumbnail/30x36":"")
					holder.attentionrecommendfragmentlistview_item_taskXZImage3, options1);
			holder.attentionrecommendfragmentlistview_item_taskName3.setText(bean.getTasks().get(2).getTitle());
			holder.attentionrecommendfragmentlistview_item_taskTotalDateCount3.setText("坚持"+bean.getTasks().get(2).getTotalDateCount()+"天");
		}else{
			holder.attentionrecommendfragmentlistview_item_taskLayout3.setVisibility(View.GONE);
		}

		holder.attentionrecommendfragmentlistview_item_headLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(bean.getUserId())) {
					Intent in = new Intent(mContext,
							ClockMyActivity.class);
					mContext.startActivity(in);
				} else {
					Intent in = new Intent(mContext,
							ClockOtherUserActivity.class);
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
				if(isSearch){
					ClockApplication.closeKeybord(SearchInput, mContext);
				}
			}
		});
		holder.attentionrecommendfragmentlistview_item_taskLayout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", bean.getTasks().get(0).getBuildingId());
				mContext.startActivity(in);
				if(isSearch){
					ClockApplication.closeKeybord(SearchInput, mContext);
				}
			}
		});
		holder.attentionrecommendfragmentlistview_item_taskLayout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", bean.getTasks().get(1).getBuildingId());
				mContext.startActivity(in);
				if(isSearch){
					ClockApplication.closeKeybord(SearchInput, mContext);
				}
			}
		});
		holder.attentionrecommendfragmentlistview_item_taskLayout3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", bean.getTasks().get(2).getBuildingId());
				mContext.startActivity(in);
				if(isSearch){
					ClockApplication.closeKeybord(SearchInput, mContext);
				}
			}
		});
		final boolean isfensi = isFans;
		holder.attentionrecommendfragmentlistview_item_fanstypeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fansTypeLisitener.setFansType(position, isfensi);
			}
		});
		holder.attentionrecommendfragmentlistview_item_botttomText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(bean.getUserId())) {
					Intent in = new Intent(mContext,
							ClockMyActivity.class);
					mContext.startActivity(in);
				} else {
					Intent in = new Intent(mContext,
							ClockOtherUserActivity.class);
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
				if(isSearch){
					ClockApplication.closeKeybord(SearchInput, mContext);
				}
			}
		});
	}
	class ViewHolder{
		ImageView attentionrecommendfragmentlistview_item_headImage,
		attentionrecommendfragmentlistview_item_fanstypeImage,
		attentionrecommendfragmentlistview_item_taskXZImage1,
		attentionrecommendfragmentlistview_item_taskXZImage2,
		attentionrecommendfragmentlistview_item_taskXZImage3;

		TextView attentionrecommendfragmentlistview_item_TopTitle,
		attentionrecommendfragmentlistview_item_name,
		attentionrecommendfragmentlistview_item_fanstypeText,
		attentionrecommendfragmentlistview_item_taskName1,
		attentionrecommendfragmentlistview_item_taskTotalDateCount1,
		attentionrecommendfragmentlistview_item_taskName2,
		attentionrecommendfragmentlistview_item_taskTotalDateCount2,
		attentionrecommendfragmentlistview_item_taskName3,
		attentionrecommendfragmentlistview_item_taskTotalDateCount3,
		attentionrecommendfragmentlistview_item_botttomText;

		LinearLayout attentionrecommendfragmentlistview_item_fanstypeLayout;

		RelativeLayout attentionrecommendfragmentlistview_item_taskLayout1,
		attentionrecommendfragmentlistview_item_taskLayout2,
		attentionrecommendfragmentlistview_item_taskLayout3,
		attentionrecommendfragmentlistview_item_headLayout,
		groupSearchLayout;

		public ViewHolder(View convertView){
			attentionrecommendfragmentlistview_item_headImage = (ImageView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_headImage);
			attentionrecommendfragmentlistview_item_fanstypeImage = (ImageView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_fanstypeImage);
			attentionrecommendfragmentlistview_item_taskXZImage1 = (ImageView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskXZImage1);
			attentionrecommendfragmentlistview_item_taskXZImage2 = (ImageView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskXZImage2);
			attentionrecommendfragmentlistview_item_taskXZImage3 = (ImageView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskXZImage3);

			attentionrecommendfragmentlistview_item_TopTitle = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_TopTitle);
			attentionrecommendfragmentlistview_item_name = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_name);
			attentionrecommendfragmentlistview_item_taskName1 = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskName1);
			attentionrecommendfragmentlistview_item_taskTotalDateCount1 = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskTotalDateCount1);
			attentionrecommendfragmentlistview_item_taskName2 = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskName2);
			attentionrecommendfragmentlistview_item_taskTotalDateCount2 = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskTotalDateCount2);
			attentionrecommendfragmentlistview_item_taskName3 = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskName3);
			attentionrecommendfragmentlistview_item_taskTotalDateCount3 = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskTotalDateCount3);
			attentionrecommendfragmentlistview_item_botttomText = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_botttomText);
			attentionrecommendfragmentlistview_item_fanstypeText = (TextView) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_fanstypeText);

			attentionrecommendfragmentlistview_item_fanstypeLayout = (LinearLayout) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_fanstypeLayout);

			attentionrecommendfragmentlistview_item_headLayout = (RelativeLayout) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_headLayout);
			attentionrecommendfragmentlistview_item_taskLayout1 = (RelativeLayout) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskLayout1);
			attentionrecommendfragmentlistview_item_taskLayout2 = (RelativeLayout) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskLayout2);
			attentionrecommendfragmentlistview_item_taskLayout3 = (RelativeLayout) 
					convertView.findViewById(R.id.attentionrecommendfragmentlistview_item_taskLayout3);
			groupSearchLayout = (RelativeLayout) convertView.findViewById(R.id.groupSearchLayout);

		}
	}
	public interface setFansTypeLisitener{
		public void setFansType(int position,boolean isfan);
	}

}
