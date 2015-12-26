package com.yktx.check.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.umeng.analytics.MobclickAgent;
import com.yktx.check.R;
import com.yktx.check.bean.MoreAlertTimeBean;
import com.yktx.check.util.Tools;
import com.yktx.check.widget.SlideSwitch;
import com.yktx.check.widget.SlideSwitch.SlideListener;

public class ClockMoreAlertAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<MoreAlertTimeBean> alertTimeBeans = new ArrayList<MoreAlertTimeBean>(10);
	//	private setListListener listListener;

	public ClockMoreAlertAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(context);
	}
	
	public void setList(ArrayList<MoreAlertTimeBean> list){
		alertTimeBeans = list;
		for(int i=0;i<alertTimeBeans.size();i++){
			alertTimeBeans.get(i).setPickervisity("1");
		}
		Tools.getLog(Tools.d, "aaa", ""+alertTimeBeans.toString());

	}
	
	public ArrayList<MoreAlertTimeBean> getAlertTimeBeans() {
		return alertTimeBeans;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alertTimeBeans.size()+1;
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
	public View getView(int position, View 	convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(alertTimeBeans.size()-1 >= position){
		
		final ViewHolder holder; 
			convertView = inflater.inflate(R.layout.more_alert_item_layout, null);
			holder = new ViewHolder(convertView);
		
		MoreAlertTimeBean bean = alertTimeBeans.get(position); 

		initView(holder, position,bean);
		return convertView;
		
		}else{
			return inflater.inflate(R.layout.more_alert_item_bottom, null);
		}
	}
		private void initView(final ViewHolder holder,final int position,final MoreAlertTimeBean bean){
			Tools.getLog(Tools.d, "aaa", "adapter加载："+position);
			final String time = bean.getTime();
			if(bean.getStatus().equals("1")){
				holder.mSlideSwitch.setImageResource(R.drawable.switch_on);
				holder.more_alert_Layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			}else{
				holder.mSlideSwitch.setImageResource(R.drawable.switch_off);
				holder.more_alert_Layout.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_13));
			}
			if(bean.getStatus().equals("1")){
				if(bean.getPickervisity().equals("1")){
					holder.mTimePicker.setVisibility(View.GONE);
				}else{
					holder.mTimePicker.setVisibility(View.VISIBLE);
				}
			}else{
				holder.mTimePicker.setVisibility(View.GONE);
			}
			holder.mSlideSwitch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(!alertTimeBeans.get(position).getStatus().equals("1")){
						MobclickAgent.onEvent(mContext, "clicksetalarm");
						holder.mSlideSwitch.setImageResource(R.drawable.switch_on);
						Tools.getLog(Tools.d, "aaa", "打开提醒===的==="+position);
						holder.more_alert_Layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
						alertTimeBeans.get(position).setStatus("1");
						Tools.getLog(Tools.d, "aaa", "打开提醒"+position);
						if(time.equals("00:00")){
							holder.mTimePicker.setVisibility(View.VISIBLE);
							alertTimeBeans.get(position).setPickervisity("2");
						}
					}else{
						holder.mSlideSwitch.setImageResource(R.drawable.switch_off);
						holder.more_alert_Layout.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_13));
						Tools.getLog(Tools.d, "aaa", "关闭开提醒===的==="+position);
						holder.mTimePicker.setVisibility(View.GONE);
						if(!bean.getStatus().equals("2")||!bean.getPickervisity().equals("1")){
							alertTimeBeans.get(position).setStatus("2");
							alertTimeBeans.get(position).setPickervisity("1");
							Tools.getLog(Tools.d, "aaa", "关闭提醒"+position);
						}
					}
				}
			});
//			holder.mSlideSwitch.setSlideListener(new SlideListener() {
//	
//				@Override
//				public void open() {
//					// TODO Auto-generated method stub
//					Tools.getLog(Tools.d, "aaa", "打开提醒===的==="+position);
//					holder.more_alert_Layout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//					if(!alertTimeBeans.get(position).getStatus().equals("1")){
//						alertTimeBeans.get(position).setStatus("1");
//						Tools.getLog(Tools.d, "aaa", "打开提醒"+position);
//					}
//					if(time.equals("00:00")){
//						holder.mTimePicker.setVisibility(View.VISIBLE);
//						alertTimeBeans.get(position).setPickervisity("2");
//					}
//	
//				}
//	
//				@Override
//				public void close() {
//					// TODO Auto-generated method stub
//					holder.more_alert_Layout.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_13));
//					Tools.getLog(Tools.d, "aaa", "关闭开提醒===的==="+position);
//					if(holder.mTimePicker.getVisibility() == View.VISIBLE){
//						holder.mTimePicker.setVisibility(View.GONE);
//					}
//					if(!bean.getStatus().equals("2")||!bean.getPickervisity().equals("1")){
//						alertTimeBeans.get(position).setStatus("2");
//						alertTimeBeans.get(position).setPickervisity("1");
//						Tools.getLog(Tools.d, "aaa", "关闭提醒"+position);
//					}
//	
//				}
//			});
			holder.TopLayout.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if(bean.getStatus().equals("1")){
						if(bean.getPickervisity().equals("1")){
							holder.mTimePicker.setVisibility(View.VISIBLE);
							alertTimeBeans.get(position).setPickervisity("2");
							Tools.getLog(Tools.d, "aaa", "显示Timepicker"+position);
						}else{
							holder.mTimePicker.setVisibility(View.GONE);
							alertTimeBeans.get(position).setPickervisity("1");
							Tools.getLog(Tools.d, "aaa", "隐藏Timepicker"+position);
						}
					}else{
						Tools.getLog(Tools.d, "aaa", "打开开关！！！！！"+position);
					}
				}
			});
			holder.mTime.setText(time);
			String timeArray[] = time.split(":");
			int hour = Integer.parseInt(timeArray[0]);
			int min = Integer.parseInt(timeArray[1]);
			Tools.getLog(Tools.d, "aaa", hour+"====="+min);
			
			holder.mTimePicker.setCurrentHour(hour);
			holder.mTimePicker.setCurrentMinute(min);
			holder.mTimePicker.setIs24HourView(true);
			holder.mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
	
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					StringBuffer sb = new StringBuffer();
					if(hourOfDay< 10){
						sb.append("0"+hourOfDay);
					}else{
						sb.append(hourOfDay);
					}
					sb.append(":");
					if(minute< 10){
						sb.append("0"+minute);
					}else{
						sb.append(minute);
					}
					if(!sb.toString().equals(bean.getTime())){
						holder.mTime.setText(sb.toString());
						alertTimeBeans.get(position).setTime(sb.toString());
					}
				}
			});
			
			holder.TopLayout.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					deleteDialog(position);
					return false;
				}
			});
			holder.mTimePicker.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					deleteDialog(position);
					return false;
				}
			});
		}
		public void deleteDialog(final int position){
			AlertDialog.Builder builder = new AlertDialog.Builder(
					new ContextThemeWrapper(mContext,
							R.style.CustomDiaLog_by_SongHang));
			builder.setItems(new String[] { "删除" },new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:
						alertTimeBeans.remove(position);
						notifyDataSetChanged();
						break;

					default:
						break;
					}
				}
			});
			builder.show();
		}


	class ViewHolder{
		private RelativeLayout TopLayout;
		private TextView mTime;
		private TimePicker mTimePicker;
		private ImageView mSlideSwitch;
		private LinearLayout more_alert_Layout;
		public ViewHolder(View 	convertView){
			TopLayout = (RelativeLayout) convertView.findViewById(R.id.clock_more_alert_item_topLayout);
			mTime = (TextView) convertView.findViewById(R.id.clock_more_alert_item_time);
			mTimePicker = (TimePicker) convertView.findViewById(R.id.clock_more_alert_item_TimePicker);
			mSlideSwitch = (ImageView) convertView.findViewById(R.id.clock_more_alert_item_SlideSwitch);
			more_alert_Layout = (LinearLayout) convertView.findViewById(R.id.more_alert_Layout);
		}
	}

}
