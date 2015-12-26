package com.yktx.check.adapter;

import java.util.ArrayList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duguang.baseanimation.ui.listivew.deletelistview.SlideView;
import com.yktx.check.ClockMainActivity;
import com.yktx.check.R;
import com.yktx.check.adapter.ClockCommentAdapter.HoldView;
import com.yktx.check.bean.ByDateBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.SlideBean;
//import com.yktx.check.listview.SlideTouchListener.OnCheckMove;

public class SlideAdapter extends BaseAdapter {
	private ArrayList<ByDateBean> mData = new ArrayList<ByDateBean>();
//	private ArrayList<SlideBean> slideList = new ArrayList<SlideBean>();
	private boolean isToday;
	 private LayoutInflater mInflater;
	 ClockMainActivity mContext;
	public SlideAdapter(ClockMainActivity mContext) {
		super();
		this.mContext = mContext;
		  mInflater =  LayoutInflater.from(mContext);
		  
	}

	public void setList(ArrayList<ByDateBean> data) {
//		mData = data;
//		slideList.clear();
		mData.clear();
		this.mData.addAll(data) ;
//		for(int i = 0; i < data.size(); i++){
//			SlideBean bean = new SlideBean();
//			bean.setItemBean(data.get(i));
//			slideList.add(bean);
//		}
		
	}

	public void setIsToday(boolean isToday) {
		this.isToday = isToday;
	}

	OnClickButton onClickBotton;
//	OnCheckMove onCheckMove;

//	public void setOnCheckMove(OnCheckMove onCheckMove) {
//		this.onCheckMove = onCheckMove;
//	}

	public void setOnClickBotton(OnClickButton onClickBotton) {
		this.onClickBotton = onClickBotton;
	}


	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
//		 SlideView slideView = (SlideView) convertView;
//		 
//		 
//		if (slideView == null) {
//			 View itemView = mInflater.inflate(R.layout.row_front_view, null);
//			 
//             slideView = new SlideView(mContext);
//             slideView.setContentView(itemView);
//
//             holder = new ViewHolder(slideView);
//             slideView.setOnSlideListener(mContext);
//             slideView.setTag(holder);
//		} else {
//			holder = (ViewHolder) slideView.getTag();
//		}
//
//		SlideBean sBean = slideList.get(position);
//		sBean.setSlideView(slideView);
//		sBean.getSlideView().shrink();
//		
//		if (sBean.getItemBean() != null) {
//			showView(sBean.getItemBean(), holder, position);
//		}
//		return slideView;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_front_view, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ByDateBean bean = mData.get(position);
		showView(bean, holder, position);
		return convertView;
	}

	private void showView(final ByDateBean bean, ViewHolder holder,
			final int position) {

		if (bean.getPrivateFlag() == 1) {
			holder.yinsiImage.setVisibility(View.VISIBLE);
		} else {
			holder.yinsiImage.setVisibility(View.GONE);
		}
		if (isToday) {
			
			holder.num.setText(bean.getCurrentStreak() + "");
		
			if (bean.getJobCount() > 0) {
				holder.taskSign.setImageResource(R.drawable.home_task_yida);
				holder.title.setTextColor(mContext.getResources().getColor(
						R.color.meibao_color_10));
				holder.num.setTextColor(mContext.getResources().getColor(
						R.color.white));
			} else {
				if(bean.getCurrentStreak() < 0){
					holder.taskSign.setImageResource(R.drawable.home_task_weida3);
					holder.num.setTextColor(mContext.getResources().getColor(
							R.color.white));
				} else {
					holder.taskSign.setImageResource(R.drawable.home_task_weida);
					holder.num.setTextColor(mContext.getResources().getColor(
							R.color.meibao_color_9));
				}
				holder.title.setTextColor(mContext.getResources().getColor(
						R.color.meibao_color_9));
				
			}
		} else {
			holder.num.setText("");
			if (bean.getJobCount() > 0) {
				holder.taskSign.setImageResource(R.drawable.home_task_yida2);
				holder.title.setTextColor(mContext.getResources().getColor(
						R.color.meibao_color_9));

			} else {
				holder.taskSign.setImageResource(R.drawable.home_task_weida2);
				holder.title.setTextColor(mContext.getResources().getColor(
						R.color.meibao_color_14));
			}
		}

		if (bean.getGiveUpFlag() != null && bean.getGiveUpFlag().equals("1")) {
			// 放弃打卡。变灰色
			holder.front.setBackgroundColor(mContext.getResources().getColor(
					R.color.meibao_color_13));

		} else {
			holder.front.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));

		}
		String text = bean.getTitle();
		holder.title.setText(text);
		final int type = getGiveUpType(bean);
//		switch (type) {
//		case 1:
//			holder.delete.setText("取消放弃");
//			break;
//		case 2:
//			holder.delete.setText("取消打卡");
//			break;
//		case 3:
//			holder.delete.setText("放弃");
//			break;
//		}
//		if (holder.delete != null) {
//			holder.delete.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (onClickBotton != null) {
//						onClickBotton.clickCancelCheck(bean.getTaskId(),
//								bean.getJobCount(), type, position);
//					}
//				}
//			});
//		}
		if (holder.taskInfo != null) {
			holder.taskInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onClickBotton != null) {
						onClickBotton.clickTaskInfoCheck(bean.getTaskId(),
								position);
					}
				}
			});
		}
		// if (bean.getStickFlag() == 1) {
		// holder.toUp.setText("取消置顶");
		// } else {
		// holder.toUp.setText("置顶");
		// }
//		holder.toUp.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (onClickBotton != null) {
//					onClickBotton.clickStickFlag(bean.getTaskId(), position);
//				}
//			}
//		});

//		if (holder.detail != null) {
//			holder.detail.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// Toast.makeText(mContext, "Click detail:" + position,
//					// Toast.LENGTH_SHORT).show();
////					if (onCheckMove != null) {
////						onCheckMove.getClick(position);
////					}
//
//				}
//			});
//		}

	}

	private int getGiveUpType(ByDateBean bean) {
		if (bean.getGiveUpFlag() != null && bean.getGiveUpFlag().equals("1")) {
			return 1;
		} else if (bean.getGiveUpFlag() != null
				&& bean.getGiveUpFlag().equals("0") && bean.getJobCount() > 0) {
			return 2;
		} else {
			return 3;
		}
	}

	class ViewHolder {
		TextView title;
		TextView delete;
		TextView toUp;
//		ImageView detail;
		TextView taskInfo;
		RelativeLayout front;
		ImageView taskSign;
		// TextView timeRight;
		// TextView timeLeft;
		ImageView taskZDsign;
		RelativeLayout midLayout;
		ImageView rightImage;
		TextView num;
		ImageView yinsiImage;

		View leftBG;
		public ViewHolder(View convertView) {
			// TODO Auto-generated constructor stub
			title = (TextView) convertView.findViewById(R.id.title);
//			delete = (TextView) convertView.findViewById(R.id.delete);
//			toUp = (TextView) convertView.findViewById(R.id.toUp);
//			detail = (ImageView) convertView.findViewById(R.id.detail);
			leftBG = (View) convertView.findViewById(R.id.leftBG);
			taskInfo = (TextView) convertView
					.findViewById(R.id.tastInfo);
			taskSign = (ImageView) convertView
					.findViewById(R.id.taskSign);
			num = (TextView) convertView.findViewById(R.id.num);
			// holder.timeRight = (TextView) convertView
			// .findViewById(R.id.timeRight);
			// holder.timeLeft = (TextView) convertView
			// .findViewById(R.id.timeLeft);
			midLayout = (RelativeLayout) convertView
					.findViewById(R.id.midLayout);
			rightImage = (ImageView) convertView
					.findViewById(R.id.rightImage);
			yinsiImage = (ImageView) convertView
					.findViewById(R.id.yinsiImage);
			

			taskZDsign = (ImageView) convertView
					.findViewById(R.id.taskZDsign);

			front = (RelativeLayout) convertView
					.findViewById(R.id.front);
		}
	}

	public interface OnClickButton {
		/**
		 * 置顶
		 * 
		 * @param taskID
		 * 
		 */
		public void clickStickFlag(String taskID, int position);

		/**
		 * 放弃
		 * 
		 * @param taskID
		 * @param jobCount
		 *            打卡次数
		 * 
		 */
		public void clickCancelCheck(String taskID, int jobCount, int type,
				int position);

		/**
		 * 
		 * 详情
		 * 
		 * @param taskID
		 * 
		 */
		public void clickTaskInfoCheck(String taskID, int position);
	}

}