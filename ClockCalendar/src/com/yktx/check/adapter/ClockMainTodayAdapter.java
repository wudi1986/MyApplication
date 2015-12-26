package com.yktx.check.adapter;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yktx.check.ClockMainActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.MyTaskBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.TimeUtil;

public class ClockMainTodayAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private List<ByDateBean> mListBeans = new ArrayList<ByDateBean>(5);
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(null).showImageForEmptyUri(null)
			.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisk(true).cacheInMemory(true)
			// .displayer(new RoundedBitmapDisplayer(150))
			// .displayer(new RoundedBitmapDisplayer(6))
			// .cacheInMemory(false)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

	public ClockMainTodayAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	PointExplainClickListener pointExplainClickListener;

	public void setPointExplainClickListener(
			PointExplainClickListener pointExplainClickListener) {
		this.pointExplainClickListener = pointExplainClickListener;
	}

	public void setList(List<ByDateBean> list) {
		mListBeans = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListBeans.size();
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
		convertView = inflater.inflate(R.layout.clock_main_today_item, null);
		holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder(convertView);
			convertView.setTag(convertView);
		}
		showView(position, holder);

		return convertView;
	}

	public void showView(int position, ViewHolder holder) {
		final ByDateBean bean = mListBeans.get(position);

		holder.mName.setText(bean.getTitle());

		int progress = bean.getProgress();
		holder.mName.setTextColor(mContext.getResources().getColor(
				R.color.meibao_color_9));
		holder.clock_main_today_item_pointImage
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent(mContext, TaskInfoActivity.class);
						in.putExtra("taskid", bean.getTaskId());
						in.putExtra("totalCheckCount",
								bean.getTotalCheckCount() + "");// 打卡次数s
						in.putExtra("totalDateCount", bean.getTotalDateCount()
								+ "");
						in.putExtra("title", bean.getTitle());
						in.putExtra("description", bean.getDescription());
						mContext.startActivity(in);
					}
				});
		if (bean.getPrivateFlag() == 1) {
			holder.clock_main_today_item_Yinsi.setVisibility(View.VISIBLE);
		} else {
			holder.clock_main_today_item_Yinsi.setVisibility(View.GONE);
		}

		if (bean.getJobCount() > 0){
			holder.clock_main_today_checked.setVisibility(View.VISIBLE);
		} else {
			holder.clock_main_today_checked.setVisibility(View.GONE);
		}
		
		switch (progress) {
		case 0:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		case 1:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		case 2:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		case 3:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		case 4:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		case 5:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		case 6:
			holder.clock_main_today_item_progress1.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress2.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress3.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress4.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress5.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress6.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14));
			holder.clock_main_today_item_progress7.setBackgroundColor(mContext
					.getResources().getColor(R.color.meibao_color_14_light));
			break;
		}

		ImageLoader.getInstance().displayImage(
				UrlParams.QINIU_IP + bean.getBadgePath(),
				holder.clock_main_today_item_Image, options);
		holder.clock_main_today_item_Image
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						pointExplainClickListener.setPointExplainClick();
						Intent in = new Intent(mContext,
								PointExplainActivity.class);
						mContext.startActivity(in);
					}
				});

		if (bean.getCurrentStreak() >= 0) {
			holder.clock_main_today_item_grade.setVisibility(View.GONE);
		} else
			holder.clock_main_today_item_grade.setText(bean.getCurrentStreak()
					+ "天");
		// holder.clock_main_today_item_Image.setImageResource(R.drawable.xz1);
		// if(isOncebl){
		// holder.mName.setTextColor(mContext.getResources().getColor(R.color.meibao_color_141));
		// //
		// holder.clock_main_today_item_leftFristImage.setBackgroundResource(R.drawable.clock_main_today_leftonce);
		// Tools.getLog(Tools.d,
		// "aaa","================isOnce:=================");
		// }

	}

	/**
	 * 不让下面的item 点击发生刷新
	 * 
	 * @author Administrator
	 * 
	 */
	public interface PointExplainClickListener {
		public void setPointExplainClick();
	}

	class ViewHolder {
		TextView mPlayDayNum, mName, mTodayClock, clock_main_today_item_grade;
		// ImageView clock_main_today_item_leftFristImage;
		ImageView clock_main_today_item_Image,
				clock_main_today_item_pointImage,clock_main_today_checked;
		ImageView clock_main_today_item_progress1,
				clock_main_today_item_progress2,
				clock_main_today_item_progress3,
				clock_main_today_item_progress4,
				clock_main_today_item_progress5,
				clock_main_today_item_progress6,
				clock_main_today_item_progress7, clock_main_today_item_Yinsi;

		public ViewHolder(View v) {
			mName = (TextView) v.findViewById(R.id.clock_main_today_item_name);
			mTodayClock = (TextView) v
					.findViewById(R.id.clock_main_today_item_todayClock);
			mPlayDayNum = (TextView) v
					.findViewById(R.id.clock_main_today_item_playDayNum);
			clock_main_today_item_grade = (TextView) v
					.findViewById(R.id.clock_main_today_item_grade);
			// clock_main_today_item_leftFristImage = (ImageView)
			// v.findViewById(R.id.clock_main_today_item_leftFristImage);
			clock_main_today_item_Image = (ImageView) v
					.findViewById(R.id.clock_main_today_item_Image);
			clock_main_today_item_pointImage = (ImageView) v
					.findViewById(R.id.clock_main_today_item_pointImage);
			clock_main_today_item_progress1 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress1);
			clock_main_today_item_progress2 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress2);
			clock_main_today_item_progress3 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress3);
			clock_main_today_item_progress4 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress4);
			clock_main_today_item_progress5 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress5);
			clock_main_today_item_progress6 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress6);
			clock_main_today_item_progress7 = (ImageView) v
					.findViewById(R.id.clock_main_today_item_progress7);
			clock_main_today_item_Yinsi = (ImageView) v
					.findViewById(R.id.clock_main_today_item_Yinsi);
			clock_main_today_checked = (ImageView) v
					.findViewById(R.id.clock_main_today_checked);
			

		}

	}

}
