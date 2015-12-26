package com.yktx.check.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.gallety.MyImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yktx.check.R;
import com.yktx.check.bean.ImageBean;
import com.yktx.check.util.FileURl;

public class GroupAdapter extends BaseAdapter {
	private List<ImageBean> list;
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(null).showImageForEmptyUri(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
			.cacheInMemory(false).cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

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
	Context context;
	public GroupAdapter(Context context, List<ImageBean> list,
			GridView mGridView) {
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ImageBean mImageBean = list.get(position);
		String path = mImageBean.getTopImagePath();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.grid_group_item, null);
			viewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView
					.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView
					.findViewById(R.id.group_count);

			// 用来监听ImageView的宽和高
			viewHolder.mImageView
					.setOnMeasureListener(new MyImageView.OnMeasureListener() {

						@Override
						public void onMeasureSize(int width, int height) {
							mPoint.set(width, height);
						}
					});

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.toumingimg);
		}

		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean
				.getImageCounts()));
		// 给ImageView设置路径Tag,这是异步加载图片的小技巧
		viewHolder.mImageView.setTag(path);

		// //利用NativeImageLoader类加载本地图片
		// Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,
		// mPoint, new NativeImageLoader.NativeImageCallBack() {
		//
		// @Override
		// public void onImageLoader(Bitmap bitmap, String path) {
		// ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
		// if(bitmap != null && mImageView != null){
		// mImageView.setImageBitmap(bitmap);
		// }
		// }
		// });
		//
		// if(bitmap != null){
		// viewHolder.mImageView.setImageBitmap(bitmap);
		// }else{
		// viewHolder.mImageView.setImageResource(R.drawable.bg_home_item_none);
		// }
		// imageLoader.displayImage(FileURl.LOAD_FILE+path,
		// viewHolder.mImageView, options);
		imageLoader.loadImage(FileURl.LOAD_FILE + path,
				new ImageSize(200, 200), options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						viewHolder.mImageView.setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
		return convertView;
	}

	public static class ViewHolder {
		public MyImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}

}
