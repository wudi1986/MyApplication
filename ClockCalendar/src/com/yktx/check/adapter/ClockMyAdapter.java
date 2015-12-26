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
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.bean.MyTaskBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.TimeUtil;

public class ClockMyAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	private List<MyTaskBean> mListBeans = new ArrayList<MyTaskBean>(5);
	private boolean isOthers;
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.xz0).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	public ClockMyAdapter(Context mContext,boolean isOther) {
		super();
		this.mContext = mContext;
		isOthers  = isOther;
		inflater = LayoutInflater.from(mContext);

		//		isOncebl = isOnce;
		//		Tools.getLog(Tools.d, "aaa","isOnce:==="+isOnce);
	}
	PointExplainClickListener pointExplainClickListener;
	public void setPointExplainClickListener(PointExplainClickListener pointExplainClickListener){
		this.pointExplainClickListener = pointExplainClickListener;
	}
	/**
	 * 是否正在进行
	 */
	boolean isLeft;

	public void setList(List<MyTaskBean> list, boolean isLeft){
		mListBeans = list;
		this.isLeft = isLeft;
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
		convertView = inflater.inflate(R.layout.clock_my_item, null);
		holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder(convertView);
			convertView.setTag(convertView);
		}
		showView(position,holder);

		return convertView;
	}
	public void showView(int position,ViewHolder holder){
		MyTaskBean bean = mListBeans.get(position);

		holder.mName.setText(bean.getTitle());
		String content = "";
//		if (bean.getTaskCount() <= 1 && bean.getJobCountToday() <= 1){
//			content = "累计坚持"+bean.getTotalDateCount()+"天";
//		}else {
//			boolean isjianchi = false;
//			if(bean.getTaskCount() != 0){
//				content = bean.getTaskCount()+"人正坚持";
//				isjianchi = true;
//			}
//			if(bean.getJobCountToday() != 0){
//				if(isjianchi){
//					content = content+"  今天"+bean.getJobCountToday()+"次打卡";
//				}else{
//					content ="今天"+bean.getJobCountToday()+"次打卡";
//				}
//			}
//
//		}
//		holder.mTodayClock.setText(content);
		holder.mPointNum.setText(bean.getPoint());

		int progress = bean.getProgress();
		if(isLeft){
			holder.mName.setTextColor(mContext.getResources().getColor(R.color.meibao_color_9));
			holder.clock_my_item_pointImage.setImageResource(R.drawable.geren_task_jc_jifen);
			holder.mPointNum.setTextColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holder.clock_my_item_TopLayout.setVisibility(View.GONE);
			if(bean.getPrivateFlag() == 1){
				holder.clock_my_item_Yinsi.setVisibility(View.VISIBLE);
			}else{
				holder.clock_my_item_Yinsi.setVisibility(View.GONE);
			}

			if(isOthers){
				if(position == 0){
					holder.clock_my_item_TopLayout.setVisibility(View.VISIBLE);
				}else{
					holder.clock_my_item_TopLayout.setVisibility(View.GONE);
				}
			}else{
				holder.clock_my_item_TopLayout.setVisibility(View.GONE);
			}

			switch (progress) {
			case 0:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			case 1:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			case 2:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			case 3:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			case 4:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			case 5:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			case 6:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
				break;
			}

		} else {
			if(bean.getPrivateFlag() == 1){
				holder.clock_my_item_Yinsi.setVisibility(View.VISIBLE);
			}else{
				holder.clock_my_item_Yinsi.setVisibility(View.GONE);
			}
			holder.mName.setTextColor(mContext.getResources().getColor(R.color.meibao_color_14));
			holder.clock_my_item_pointImage.setImageResource(R.drawable.geren_task_zt_jifen);
			holder.mPointNum.setTextColor(mContext.getResources().getColor(R.color.meibao_color_14));

			switch (progress) {
			case 0:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			case 1:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			case 2:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			case 3:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			case 4:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			case 5:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			case 6:
				holder.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14));
				holder.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_14_light));
				break;
			}
		}
		if(bean.getLastCheckTime()!= null){
			holder.mJustNow.setText(TimeUtil.getTimes(Long.parseLong(bean.getLastCheckTime())));
		}else{
			holder.mJustNow.setText("你还没有打这个卡");
		}
		ImageLoader.getInstance().displayImage( UrlParams.QINIU_IP+bean.getBadgePath(),//+"?imageMogr2/thumbnail/24x29"
				holder.clock_my_item_Image,options);
		holder.clock_my_item_Image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pointExplainClickListener.setPointExplainClick();
				Intent in = new Intent(mContext, PointExplainActivity.class);
				mContext.startActivity(in);
			}
		});
		//		holder.clock_my_item_Image.setImageResource(R.drawable.xz1);
		//		if(isOncebl){
		//			holder.mName.setTextColor(mContext.getResources().getColor(R.color.meibao_color_11));
		//			//			holder.clock_my_item_leftFristImage.setBackgroundResource(R.drawable.clock_my_leftonce);
		//			Tools.getLog(Tools.d, "aaa","================isOnce:=================");
		//		}

	}
	
	/**
	 *  不让下面的item 点击发生刷新
	 * @author Administrator
	 *
	 */
	public interface PointExplainClickListener{
		public void setPointExplainClick();
	}
	class ViewHolder{
		TextView mPlayDayNum,mPointNum,mName,mJustNow,mTodayClock,clock_my_item_grade,clock_my_item_TotalCheckCount,clock_my_item_day;
		//		ImageView clock_my_item_leftFristImage;
		ImageView clock_my_item_Image,clock_my_item_pointImage;
		ImageView clock_my_item_progress1,clock_my_item_progress2,clock_my_item_progress3,clock_my_item_progress4,clock_my_item_progress5,
		clock_my_item_progress6,clock_my_item_progress7,clock_my_item_Yinsi;
		LinearLayout clock_my_item_TopLayout;
		
		public ViewHolder(View v){
			mName = (TextView) v.findViewById(R.id.clock_my_item_name);
			mTodayClock = (TextView) v.findViewById(R.id.clock_my_item_todayClock);
			mPointNum = (TextView) v.findViewById(R.id.clock_my_item_pointNum);
			mPlayDayNum  = (TextView) v.findViewById(R.id.clock_my_item_playDayNum);
			mJustNow = (TextView) v.findViewById(R.id.clock_my_item_justNow);
			clock_my_item_grade = (TextView) v.findViewById(R.id.clock_my_item_grade);
			//			clock_my_item_leftFristImage = (ImageView) v.findViewById(R.id.clock_my_item_leftFristImage);
			clock_my_item_TotalCheckCount = (TextView) v.findViewById(R.id.clock_my_item_TotalCheckCount);
			clock_my_item_day = (TextView) v.findViewById(R.id.clock_my_item_day);
			clock_my_item_Image = (ImageView) v.findViewById(R.id.clock_my_item_Image);
			clock_my_item_pointImage = (ImageView) v.findViewById(R.id.clock_my_item_pointImage);
			clock_my_item_progress1 = (ImageView) v.findViewById(R.id.clock_my_item_progress1);
			clock_my_item_progress2 = (ImageView) v.findViewById(R.id.clock_my_item_progress2);
			clock_my_item_progress3 = (ImageView) v.findViewById(R.id.clock_my_item_progress3);
			clock_my_item_progress4 = (ImageView) v.findViewById(R.id.clock_my_item_progress4);
			clock_my_item_progress5 = (ImageView) v.findViewById(R.id.clock_my_item_progress5);
			clock_my_item_progress6 = (ImageView) v.findViewById(R.id.clock_my_item_progress6);
			clock_my_item_progress7 = (ImageView) v.findViewById(R.id.clock_my_item_progress7);
			clock_my_item_Yinsi = (ImageView) v.findViewById(R.id.clock_my_item_Yinsi);

			clock_my_item_TopLayout = (LinearLayout) v.findViewById(R.id.clock_my_item_TopLayout);

		}

	}

}
