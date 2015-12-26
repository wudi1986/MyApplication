package com.yktx.check.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yktx.check.R;
import com.yktx.check.bean.MedalBean;
import com.yktx.check.conn.UrlParams;

/**
 * Created by Administrator on 2014/4/8.
 */
public class MedalGridViewAdapter extends BaseAdapter {
	Activity context;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.zw_image)
			.showImageForEmptyUri(null).showImageOnFail(null)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false)
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	ArrayList<MedalBean> list = new ArrayList<MedalBean>(10);

	public MedalGridViewAdapter(Activity context) {
		this.context = context;
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}
	
	public void setList(ArrayList<MedalBean> list){
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		MedalBean groupBean = (MedalBean) list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.medal_image_adapter, null);
			holdView = new MedalGridViewAdapter.HoldView(convertView);

			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		show(holdView, groupBean);

		return convertView;
	}

	private void show(final HoldView holdView, MedalBean groupBean) {

		ImageLoader.getInstance().displayImage(
				UrlParams.QINIU_IP + groupBean.getPicPath(), holdView.medalImage,
				options);
	}

	public class HoldView {
		public ImageView medalImage;

		public HoldView(View convertView) {
			medalImage = (ImageView) convertView.findViewById(R.id.medalImage);
		}
	}
}
