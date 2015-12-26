package com.yktx.check.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yktx.check.BaseActivity;
import com.yktx.check.R;
import com.yktx.check.bean.ImageListBean;
import com.yktx.check.util.FileURl;
import com.yktx.check.util.ImageTool;
import com.yktx.check.util.Tools;

public class TakeClockAdapter extends BaseAdapter {
	/**
	 * 用来存储图片的选中情况
	 */

	private ArrayList<ImageListBean> curList = new ArrayList<ImageListBean>(3);
	protected LayoutInflater mInflater;
	Activity mContext;

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(null).showImageForEmptyUri(null)
			.cacheInMemory(false).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(false)
			.considerExifParams(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public TakeClockAdapter(Activity context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setList(ArrayList<ImageListBean> list) {
		curList.clear();
		curList.addAll(list);
		ImageListBean bean = new ImageListBean();
		ImageListBean bean1 = new ImageListBean();
		curList.add(0, bean);
		curList.add(bean1);
	};

	public ArrayList<ImageListBean> getList() {
		ArrayList<ImageListBean> list = curList;
		if(list.size() == 0){
			return list;
		}
		list.remove(0);
		if(list.size() == 0){
			return list;
		}
		list.remove(list.size() - 1);

		return list;
	}

	@Override
	public int getCount() {
		return curList.size();
	}

	@Override
	public Object getItem(int position) {
		return curList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final HoldView viewHolder;
		final ImageListBean bean = curList.get(position);
		String path = bean.getImageUrl();
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.takeclock_dialog_horizontallistview_item, null);
			viewHolder = new HoldView(convertView, position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (HoldView) convertView.getTag();
		}

		if (position == 0 || position == 4) {
			if (position == 0) {
				//去掉滑动前的空白
				RelativeLayout.LayoutParams hotLeftImageParams = new RelativeLayout.LayoutParams(
						(int) (16 * BaseActivity.DENSITY),
						(int) (68 * BaseActivity.DENSITY));
				viewHolder.chat_select_frist_image.setLayoutParams(hotLeftImageParams);
				viewHolder.chat_select_image
						.setImageResource(R.drawable.home_paizhao1);
				viewHolder.chat_select_frist_image.setVisibility(View.VISIBLE);
				viewHolder.chat_select_Right_image.setVisibility(View.GONE);
				
			} else {
				viewHolder.chat_select_frist_image.setVisibility(View.VISIBLE);
				viewHolder.chat_select_Right_image.setVisibility(View.VISIBLE);
				viewHolder.chat_select_image
						.setImageResource(R.drawable.home_checkphoto_unclick);
			}
			

		} else {
			viewHolder.chat_select_Right_image.setVisibility(View.GONE);
			viewHolder.chat_select_image
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Tools.getLog(Tools.d, "aaa", "点击里面的");
							if (bean.getIsCheck() == true) {
								curList.get(position).setCheck(false);
								viewHolder.chat_select_image
										.setImageResource(R.drawable.home_paizhao_weixuan);
								
							} else {
								curList.get(position).setCheck(true);
								viewHolder.chat_select_image
										.setImageResource(R.drawable.home_paizhao_xuanzhong);
							}

						}
					});
			if (bean.getIsCheck()) {
				viewHolder.chat_select_image
						.setImageResource(R.drawable.home_paizhao_xuanzhong);
			} else {
				viewHolder.chat_select_image
						.setImageResource(R.drawable.home_paizhao_weixuan);
			}

			if (path != null) {
			}
			ImageLoader.getInstance().displayImage(FileURl.LOAD_FILE + path,
					viewHolder.mImageView, options,new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							viewHolder.chat_select_image.setVisibility(View.GONE);
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							viewHolder.chat_select_image.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
		}
		return convertView;
	}

	public static class HoldView {
		public ImageView mImageView, chat_select_image,
				chat_select_frist_image,chat_select_Right_image;
		int position;

		public HoldView(View convertView, int position) {
			mImageView = (ImageView) convertView.findViewById(R.id.chat_image);
			chat_select_image = (ImageView) convertView
					.findViewById(R.id.chat_select_image);
			chat_select_frist_image = (ImageView) convertView
					.findViewById(R.id.chat_select_frist_image);
			chat_select_Right_image = (ImageView) convertView
					.findViewById(R.id.chat_select_Right_image);
			
			this.position = position;
		}

		public int getPosition() {
			return position;
		}
	}

}
