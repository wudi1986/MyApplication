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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.R;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class ClockVoteAdapter extends BaseAdapter{
	private Context mContext;
	ArrayList<CommentsBean> itemBeans = new ArrayList<CommentsBean>(10);
	private LayoutInflater inflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zw_touxiang)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.toumingimg)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false)
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public ClockVoteAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}
	public void setList(ArrayList<CommentsBean> list){
		itemBeans = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemBeans.size();
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
		HoldView holder = null;
		CommentsBean bean = itemBeans.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.vote_info_item, null);
			holder = new HoldView(convertView);
			convertView.setTag(holder);
		} else {
			holder = (HoldView) convertView.getTag();
		}
		ShowView(bean, holder);
		return convertView;
	}

	public void ShowView(final CommentsBean bean, HoldView holdView) {
		holdView.vote_info_item_headImage
				.setImageResource(R.drawable.zw_touxiang);
		imageLoader.displayImage(Tools.getImagePath(bean.getImageSource())
				+ bean.getAvartar_path()+(bean.getImageSource() == 2?"?imageMogr2/thumbnail/70x70":""), holdView.vote_info_item_headImage,
				headOptions);
		holdView.vote_info_item_Content.setText(TimeUtil.getTimes(bean
				.getSendTime()));
		holdView.vote_info_item_headImage
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext,
								ClockOtherUserActivity.class);
						in.putExtra("userid", bean.getUserId());
						mContext.startActivity(in);
					}
				});
		holdView.vote_info_item_username.setText(bean.getName());
	}

	class HoldView {
		ImageView vote_info_item_headImage;
		TextView vote_info_item_username, vote_info_item_Content;

		public HoldView(View convertView) {
			vote_info_item_headImage = (ImageView) convertView
					.findViewById(R.id.vote_info_item_headImage);
			vote_info_item_username = (TextView) convertView
					.findViewById(R.id.vote_info_item_username);
			vote_info_item_Content = (TextView) convertView
					.findViewById(R.id.vote_info_item_Content);
		}
	}

}
