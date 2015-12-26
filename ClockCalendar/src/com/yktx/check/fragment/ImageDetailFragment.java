package com.yktx.check.fragment;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yktx.check.R;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.Tools;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private int imageSource;
	private ImageView mImageView;
	private RelativeLayout LoadingLayout;
	private PhotoViewAttacher mAttacher;
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
//	.displayer(new RoundedBitmapDisplayer(6))
	.cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public static ImageDetailFragment newInstance(String imageUrl, int imageSource) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		args.putInt("sourch", imageSource);
		f.setArguments(args);

		return f;
	}	
	private Activity mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		imageSource = getArguments().getInt("sourch");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = ImageDetailFragment.this.getActivity();
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				mContext.finish();
			}
		});
		
		LoadingLayout = (RelativeLayout) v.findViewById(R.id.loadLayout);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ImageLoader.getInstance().displayImage(Tools.getImagePath(imageSource)+mImageUrl, mImageView,options, new ImageLoadingListener() {
			//加载开始 
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				LoadingLayout.setVisibility(View.VISIBLE);
			}
			// 加载失败  
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				Tools.getLog(Tools.d, "aaa", message);
//				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				LoadingLayout.setVisibility(View.VISIBLE);
			}
			// 加载完成  
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				LoadingLayout.setVisibility(View.GONE);
				mAttacher.update();
			}
			// 取消加载  
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				LoadingLayout.setVisibility(View.GONE);
				Tools.getLog(Tools.d, "aaa", "======清除图片缓存=====");
				ImageLoader.getInstance().clearMemoryCache();
			}
		});
		
		
	}

}
