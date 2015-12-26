package com.yktx.check.adapter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.yktx.check.ImagePagerActivity2;
import com.yktx.check.R;
import com.yktx.check.bean.CustomDate;
import com.yktx.check.bean.GetByTaskIdCameraBean;
import com.yktx.check.util.DateUtil;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class TaskGridViewAdapter extends BaseAdapter {
	private LinkedHashMap<String, GetByTaskIdCameraBean> curMap = new LinkedHashMap<String, GetByTaskIdCameraBean>();
	protected LayoutInflater mInflater;

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.anim.loading_image_animation)
			.showImageForEmptyUri(R.drawable.xq_rl_wutu)
			.showImageOnFail(R.drawable.xq_rl_wutu)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
			.cacheInMemory(false)
			// 启用内存缓存
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	private Activity mContext;
	private CustomDate createDate;
	String today;

	InfoTakePhoto infoTakePhoto;

	public void setInfoTakePhoto(InfoTakePhoto takePhoto) {
		infoTakePhoto = takePhoto;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	public void setCreateDate(CustomDate createDate) {
		this.createDate = createDate;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	String getUserID, taskID, userID;

	public TaskGridViewAdapter(Activity context, String getUserID,
			String taskID, String userID) {
		mContext = context;
		this.getUserID = getUserID;
		this.userID = userID;
		this.taskID = taskID;
		mInflater = LayoutInflater.from(context);
		today = TimeUtil.getYYMMDD(System.currentTimeMillis());
	}

	String imageSource[];

	public void setList(LinkedHashMap<String, GetByTaskIdCameraBean> curMap) {
		this.curMap = curMap;
		if (curMap.size() != 0) {
			imageSource = new String[curMap.size()];

			Iterator iter = curMap.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				imageSource[i] = key + "";
				GetByTaskIdCameraBean bean = curMap.get(key);
				bean.setPosition(i);
				curMap.put(key, bean);
				i++;
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		// GetByTaskIdCameraBean bean = list.get(position);
		// String path = mNewCameraBean.getTopImagePath();

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.task_gridview_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			// viewHolder.mImageView.setImageResource(R.drawable.bg_home_item_none);
		}

		showView(viewHolder, position);
		return convertView;
	}

	@SuppressLint("ResourceAsColor")
	private void showView(ViewHolder viewHolder, final int position) {
		if (createDate != null) {
			long curDate = DateUtil.getDate(createDate.toString(), -position);
			String curDateStr = TimeUtil.getYYMMDD(curDate);
			boolean isToday = false;
			viewHolder.taskImageKuang.setVisibility(View.GONE);
			if (curDateStr.equals(today)) {
				// 今天
				isToday = true;
				viewHolder.taskBg.setTextColor(0xffff9500);
				viewHolder.taskBg.setBackgroundResource(R.color.white);
			} else if (curDate < System.currentTimeMillis()) {
				// 过去的
				viewHolder.taskBg
						.setBackgroundResource(R.color.meibao_color_13);
				viewHolder.taskBg.setTextColor(R.color.meibao_color_11);
			} else {
				// 以后的
				viewHolder.taskBg
						.setBackgroundResource(R.color.meibao_color_15);
				viewHolder.taskBg.setTextColor(R.color.meibao_color_12);
			}

			String array[] = curDateStr.split("-");
			int month = Integer.parseInt(array[1]);
			int day = Integer.parseInt(array[2]);
			if (isToday) {
				if (userID.equals(getUserID)) {
					viewHolder.taskBg.setText("");// 原来是空""
				} else {
					viewHolder.taskBg.setText(day + "");
				}
			} else if (day == 1) {
				// 月初
				// viewHolder.taskBg.setText(month + "月" + day);
				viewHolder.taskBg.setText(month + "月");
			} else {
				viewHolder.taskBg.setText(day + "");
			}

			if (curMap.get(curDateStr) != null) {
				viewHolder.taskLastPhoto.setVisibility(View.VISIBLE);
				// viewHolder.taskDate.setVisibility(View.VISIBLE);
				final GetByTaskIdCameraBean bean = curMap.get(curDateStr);
				if (bean.getLastImagePath() == null
						|| bean.getLastImagePath().equals("null")) {
					viewHolder.taskLastPhoto
							.setImageResource(R.drawable.xq_rl_wutu);
					viewHolder.taskBg.setText("");
					viewHolder.taskImageKuang.setVisibility(View.GONE);
				} else {
					ImageLoader.getInstance().displayImage(
							Tools.getImagePath(bean.getLastImageSource())
									+ bean.getLastImagePath()
									+ "?imageMogr2/thumbnail/100x100",
							viewHolder.taskLastPhoto, options);
					viewHolder.taskImageKuang.setVisibility(View.VISIBLE);
				}
				if (bean.getImageCount() > 1) {
					viewHolder.taskMorePhoto.setVisibility(View.VISIBLE);
				} else {
					viewHolder.taskMorePhoto.setVisibility(View.GONE);
				}

				// viewHolder.taskDate.setText(bean.getCheckDate().substring(5));
				viewHolder.taskLastPhoto
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (bean.getImageCount() == 0) {
									// AlertDialog.Builder builder = new
									// AlertDialog.Builder(
									// mContext);
									// builder.setTitle("提示");
									// builder.setMessage("浏览此日内容，需通过打卡7app");
									// builder.setPositiveButton("下载",
									// new DialogInterface.OnClickListener() {
									// public void onClick(DialogInterface
									// dialog,
									// int whichButton) {
									// MobclickAgent.onEvent(mContext,
									// "infoShareWeixinClick");
									//
									// // 这里添加点击确定后的逻辑
									// Uri uri = Uri
									// .parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.yktx.check");
									// Intent intent = new
									// Intent(Intent.ACTION_VIEW, uri);
									// mContext.startActivity(intent);
									//
									// }
									// });
									// builder.setNegativeButton("返回", new
									// DialogInterface.OnClickListener() {
									//
									// @Override
									// public void onClick(DialogInterface arg0,
									// int arg1) {
									// // TODO Auto-generated method stub
									//
									// }
									// });
									// builder.show();
									// Intent in = new Intent(mContext,
									// DailycamLoadClockActivity.class);
									// in.putExtra("isMainPage", false);
									// mContext.startActivity(in);
									// mContext.overridePendingTransition(R.anim.my_scale_action,
									// R.anim.my_scale_action1);
								} else {
									imageBrower(bean.getPosition());
								}
							}
						});
			} else if (isToday) {
				if (userID.equals(getUserID)) {
					viewHolder.taskLastPhoto.setVisibility(View.VISIBLE);
				} else {
					viewHolder.taskLastPhoto.setVisibility(View.INVISIBLE);
				}
				viewHolder.taskLastPhoto
						.setImageResource(R.drawable.xq_rl_jintiandaka);
				viewHolder.taskMorePhoto.setVisibility(View.GONE);
				// viewHolder.taskDate.setVisibility(View.GONE);
				viewHolder.taskLastPhoto
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Tools.getLog(Tools.d, "aaa", "今天没有照片的点击！");
								if (infoTakePhoto != null)
									infoTakePhoto.TakePhoto();
							}
						});
			} else {
				viewHolder.taskLastPhoto.setVisibility(View.INVISIBLE);
				viewHolder.taskMorePhoto.setVisibility(View.GONE);
			}

		} else {
			viewHolder.taskLastPhoto.setVisibility(View.INVISIBLE);
		}

	}

	public static class ViewHolder {
		private ImageView taskLastPhoto, taskMorePhoto, taskImageKuang;
		private TextView taskBg;

		public ViewHolder(View convertView) {
			// TODO Auto-generated constructor stub
			taskLastPhoto = (ImageView) convertView
					.findViewById(R.id.taskLastPhoto);
			taskBg = (TextView) convertView.findViewById(R.id.taskBg);
			taskMorePhoto = (ImageView) convertView
					.findViewById(R.id.taskMorePhoto);
			taskImageKuang = (ImageView) convertView
					.findViewById(R.id.taskImageKuang);

		}
	}

	private void imageBrower(int position) {
		Intent intent = new Intent(mContext, ImagePagerActivity2.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity2.EXTRA_IMAGE_SOURCE, imageSource);
		 intent.putExtra(ImagePagerActivity2.EXTRA_IMAGE_DATE,
		 imageSource[position]);
		 intent.putExtra(ImagePagerActivity2.EXTRA_IMAGE_USERID, getUserID);
		 intent.putExtra(ImagePagerActivity2.EXTRA_IMAGE_TASKID, taskID);

		mContext.startActivity(intent);
	}

	public interface InfoTakePhoto {
		public void TakePhoto();
	}

}
