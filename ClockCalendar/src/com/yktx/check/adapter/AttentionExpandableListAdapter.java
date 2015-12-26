package com.yktx.check.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockSearchActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.UserFollowingJobBean;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

@SuppressLint("NewApi")
public class AttentionExpandableListAdapter extends BaseExpandableListAdapter {
	private Activity mContext;
	private LayoutInflater inflater;
	ArrayList<UserFollowingJobBean> itemBeans = new ArrayList<UserFollowingJobBean>(
			10);

	giveUpFlagClick upFlagClick;

	int tixingPosition = -1;
	public void setTixingPosition(int tixingPosition) {
		this.tixingPosition = tixingPosition;
	}

	SharedThisJob sharedThisJob;
	private boolean isOther;

	public void setGiveUpFlagClick(giveUpFlagClick flagClick) {
		upFlagClick = flagClick;
	}

	public void setSharedThisJob(SharedThisJob thisJob) {
		sharedThisJob = thisJob;
	}

	public void isOtherTaksInfo(boolean other) {
		isOther = other;
	}

	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageOnFail(R.anim.loading_image_animation)
	.showImageOnLoading(null).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(200))
	.cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	.build();


	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageForEmptyUri(R.drawable.zw_image)
	.showImageOnFail(R.anim.loading_image_animation)
	.showImageOnLoading(R.anim.loading_image_animation).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	.cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	.build();
	public DisplayImageOptions XZoptions = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.xz0)
	.showImageForEmptyUri(R.drawable.xz0)
	.showImageOnFail(R.anim.loading_image_animation)
	.showImageOnLoading(null).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	.cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	.build();
	String userID;
	public AttentionExpandableListAdapter(Activity mContext,String userid) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		userID = userid;
	}

	TaskInfoOnClick taskInfoOnClick;

	public void setTaskInfoOnClick(TaskInfoOnClick taskInfoOnClick) {
		this.taskInfoOnClick = taskInfoOnClick;
	}

	public void setList(ArrayList<UserFollowingJobBean> bean) {
		itemBeans = bean;
	}

	// 返回父列表个数
	@Override
	public int getGroupCount() {
		return itemBeans.size();
	}

	// 返回子列表个数
	@Override
	public int getChildrenCount(int groupPosition) {

		if (itemBeans.size() != 0 && groupPosition >= itemBeans.size()) {
			return itemBeans.size() - 1;
		}
		return itemBeans.get(groupPosition).getJobs().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groupPosition >= itemBeans.size()) {
			groupPosition = itemBeans.size() - 1;
		}
		if(groupPosition == -1)
			return null;
		return itemBeans.get(groupPosition);
	}
	AttentionOtherTakeClock attentionOtherTakeClock;
	public void setAttentionOtherTakeClock(AttentionOtherTakeClock parent){
		attentionOtherTakeClock  = parent;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return itemBeans.get(groupPosition).getJobs().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.group_attention, null);
			groupHolder.imageView = (ImageView) convertView
					.findViewById(R.id.userHeadImage);
			groupHolder.textView = (TextView) convertView
					.findViewById(R.id.userName);
			groupHolder.group_right = (TextView) convertView
					.findViewById(R.id.day);
			groupHolder.groupTopLayout = (RelativeLayout) convertView.findViewById(R.id.groupTopLayout);
			groupHolder.userLayout = (LinearLayout) convertView.findViewById(R.id.userLayout);
			groupHolder.groupTopLayout_blank = (LinearLayout) 
					convertView.findViewById(R.id.groupTopLayout_blank);



			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}


		groupHolder.textView.setVisibility(View.VISIBLE);
		groupHolder.group_right.setVisibility(View.VISIBLE);
		if (groupPosition >= itemBeans.size()) {
			groupPosition = itemBeans.size() - 1;
		}
		final UserFollowingJobBean bean = itemBeans.get(groupPosition);
		groupHolder.textView.setText(bean.getName());
		final String time = bean.getDateDisplay();
		if(time.equals("提醒Ta打卡")){

			groupHolder.group_right.setBackgroundResource(R.drawable.guangchang_gz_tixingbg);
			if (tixingPosition == groupPosition || tixingPosition == -1){
				groupHolder.groupTopLayout.setVisibility(View.VISIBLE);
				groupHolder.groupTopLayout_blank.setVisibility(View.GONE);//6dp 的高度 提醒存在 不显示
				tixingPosition = groupPosition;
			} else {
				groupHolder.groupTopLayout.setVisibility(View.GONE);
				groupHolder.groupTopLayout_blank.setVisibility(View.VISIBLE);//6dp 的高度 提醒存在 不显示
			}

			groupHolder.group_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					attentionOtherTakeClock.setAttention(bean.getUserId(),bean.getName());
				}
			});
		}else{
			if(groupPosition == 0){
				groupHolder.groupTopLayout_blank.setVisibility(View.GONE);//6dp 的高度 第一条 不显示
			}else{
				groupHolder.groupTopLayout_blank.setVisibility(View.VISIBLE);//6dp 的高度 其他时候 ，没有第一条，并且不是提醒
			}
			groupHolder.group_right.setBackgroundResource(R.drawable.guangchang_gz_timebg);
			groupHolder.groupTopLayout.setVisibility(View.GONE);
		}

//		if(groupPosition == 0){
//			groupHolder.groupSearchLayout.setVisibility(View.VISIBLE);
//			groupHolder.groupSearchLayout.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
////					Toast.makeText(mContext, "搜索！", Toast.LENGTH_SHORT).show();
//					
//				}
//			});
//		}else{
//			groupHolder.groupSearchLayout.setVisibility(View.GONE);
//		}
		
		groupHolder.group_right.setText(time);
		// if (isExpanded)// ture is Expanded or false is not isExpanded
		ImageLoader.getInstance().displayImage(Tools.getImagePath(bean.getImageSource()) + bean.getAvartar_path()+(bean.getImageSource() == 2?"?imageMogr2/thumbnail/70x70":""),
				groupHolder.imageView, headOptions);
		groupHolder.userLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(bean.getUserId())) {
					Intent in = new Intent(mContext,
							ClockMyActivity.class);
					mContext.startActivity(in);
				} else {
					Intent in = new Intent(mContext,
							ClockOtherUserActivity.class);
					in.putExtra("userid", bean.getUserId());
					mContext.startActivity(in);
				}
			}
		});
		// else
		// groupHolder.imageView.setImageResource(R.drawable.collapse);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// Tools.getLog(Tools.i, "kkk", "psotion:" + childPosition);
		//
		ChildHolder holder;
		convertView = inflater
				.inflate(R.layout.attention_expandable_item, null);
		holder = (ChildHolder) convertView.getTag();
		if (holder == null) {
			holder = new ChildHolder(convertView);
			convertView.setTag(holder);
		}
		if (groupPosition >= itemBeans.size()) {
			groupPosition = itemBeans.size() - 1;
		}
		ShowView(holder, groupPosition, childPosition);
		return convertView;
	}

	public void ShowView(final ChildHolder holdView, final int groupPosition,
			final int childPosition) {


		if(childPosition == itemBeans.get(groupPosition)
				.getJobs().size()-1){
			holdView.bgLayout.setBackgroundResource(R.drawable.guangchang_guanzhu_bg2);
		} else {
			holdView.bgLayout.setBackgroundResource(R.drawable.guangchang_guanzhu_bg3);
		}
		final TaskItemBean taskItemBean = itemBeans.get(groupPosition)
				.getJobs().get(childPosition);
		final UserFollowingJobBean bean = itemBeans.get(groupPosition);
		// 为了区分是不是这一天最后一条
		if (childPosition == (itemBeans.get(groupPosition).getJobs().size() - 1)) {
			if(itemBeans.get(groupPosition).getTotalJobCount() <= 3){
				holdView.taskItem_bottomLine.setVisibility(View.GONE);
				holdView.taskItem_bottomLine2.setVisibility(View.GONE);
				holdView.taskItem_bottomLayout.setVisibility(View.GONE);
			}else{
				holdView.taskItem_bottomText.setText(itemBeans.get(groupPosition)
						.getTotalJobCount() + "卡");
				holdView.taskItem_bottomLine.setVisibility(View.VISIBLE);
				holdView.taskItem_bottomLayout.setVisibility(View.VISIBLE);
				holdView.taskItem_bottomLine2.setVisibility(View.VISIBLE);
			}
		} else {
			holdView.taskItem_bottomLine.setVisibility(View.VISIBLE);
			holdView.taskItem_bottomLine2.setVisibility(View.GONE);
			holdView.taskItem_bottomLayout.setVisibility(View.GONE);
		}
		holdView.attention_fragment_item_commentsNumLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {//评论的点击事件
				// TODO Auto-generated method stub

				taskInfoOnClick.clickComment(taskItemBean, groupPosition, childPosition, 0);
			}
		});
		holdView.cardName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", taskItemBean.getBuildingId());
				mContext.startActivity(in);
			}
		});
		holdView.taskItem_lineLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext,
						TaskInfoActivity.class);//这个跳转不存在是不是自己，判断会在TaskInfoActivity有
				in.putExtra("taskid",taskItemBean.getTaskId());
				in.putExtra("userid", bean.getUserId());
				mContext.startActivity(in);
			}
		});
		holdView.taskItem_bottomLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//				if (userID.equals(bean.getUserId())) {
				//					Intent in = new Intent(mContext,
				//							ClockMyActivity.class);
				//					mContext.startActivity(in);
				//				} else {
				//					Intent in = new Intent(mContext,
				//							ClockOtherUserActivity.class);
				//					in.putExtra("userid", bean.getUserId());
				//					mContext.startActivity(in);
				//				}
//				Intent in = new Intent(mContext, ClockDetailActivity.class);
//				in.putExtra("userid", bean.getUserId());
//				in.putExtra("name", bean.getName());
//				in.putExtra("isother", true);
//				mContext.startActivity(in);
			}
		});

		final String[] imagePathArray1;
		final String[] imagePathArray2;
		final String[] imagePathArray3;
		final String[] getAllSource;

		boolean isHaveImage = false;
		String ClockContentNum = taskItemBean.getQuantity();
		final String signature = taskItemBean.getSignature();
		holdView.taskItem_Content2.setVisibility(View.GONE);

		if (signature != null && signature.length() != 0) {
			if (ClockContentNum != null && ClockContentNum.length() != 0) {
				//				holdView.taskItem_Content.getViewTreeObserver()
				//						.addOnPreDrawListener(new OnPreDrawListener() {
				//							public boolean onPreDraw() {
				// 这里textView已经初始化完毕，你可以得到所有属性

				//								if (isFirst) {
				//									isFirst = false;
				int textWidth = holdView.taskItem_Content
						.getWidth();
				Tools.getLog(Tools.i,"aaa", "textview2 222====== "
						+ textWidth);
				Tools.getLog(Tools.i,"aaa", "signature====== " + signature);
				int index = Tools.getWidthIndex(
						holdView.taskItem_Content,
						signature, 576);
				Tools.getLog(Tools.i,"aaa", "index ====== " + index);
				Tools.getLog(Tools.i,"aaa",
						"content.substring(index,content.length()) ====== "
								+ signature
								.substring(index));
				if (index == signature.length()) {
					holdView.taskItem_Content
					.setText(signature);
				} else {//index-1  是因为第一行会有一个字会越行显示
					holdView.taskItem_Content
					.setText(signature.substring(0,
							index-1));
					holdView.taskItem_Content2
					.setVisibility(View.VISIBLE);
					holdView.taskItem_Content2
					.setText(signature.substring(
							index-1,
							signature.length()));
				}
				//								}
				//								return true;
				//							}
				//						});
			} else {
				holdView.taskItem_Content.setText(signature);
			}

		} else {
			holdView.taskItem_Content.setText("打卡一次");
		}

		holdView.clock_my_item_name.setText(taskItemBean.getTitle());
		
		String city = taskItemBean.getCity();
		String textStr = TimeUtil.getMMDDHHmm(taskItemBean.getCheckTime())
				+ "  第" + taskItemBean.getTotalDateCount() + "天";
		if(!city.equals("0")){
			textStr += "  "+taskItemBean.getCity(); 
		}
		holdView.attention_fragment_item_TimeText.setText(textStr);//时间
		holdView.clock_my_item_pointNum.setText(taskItemBean.getVoteCount()+"");//点赞数量
		holdView.attention_fragment_item_commentsNumText.setText(taskItemBean
				.getCommentCount() + "");//评论数

		if(taskItemBean.getVoted().equals("0")){//没有点过赞
			holdView.clock_my_item_pointImage.setImageResource(R.drawable.guangchang_gz_daqi1);
			holdView.clock_my_item_pointNum.setTextColor(mContext.getResources().getColor(R.color.meibao_color_14));
		}else{
			holdView.clock_my_item_pointImage.setImageResource(R.drawable.guangchang_gz_daqi2);
			holdView.clock_my_item_pointNum.setTextColor(mContext.getResources().getColor(R.color.meibao_color_1));
		}


		int progress = taskItemBean.getProgress();
		switch (progress) {
		case 0:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 1:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 2:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 3:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 4:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 5:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 6:
			holdView.clock_my_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.clock_my_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		}
		if (ClockContentNum != null && ClockContentNum.length() != 0) {
			String unit = taskItemBean.getUnit();
			if(unit.equals("0")){
				unit = "";
			}
			holdView.taskItem_ContentNum.setText(ClockContentNum+unit);
			holdView.taskItem_ContentNum.setVisibility(View.VISIBLE);
		} else {
			holdView.taskItem_ContentNum.setVisibility(View.GONE);
		}
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(taskItemBean.getBadgeSource())
				+ taskItemBean.getBadgePath(),//+(taskItemBean.getBadgeSource() == 2?"?imageMogr2/thumbnail/25x30":"")
				holdView.clock_my_item_Image, XZoptions);
		holdView.clock_my_item_Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, PointExplainActivity.class);
				mContext.startActivity(in);	
			}
		});
		String allImagePath = taskItemBean.getAllPicPath();
		if (taskItemBean.getPicCount().equals("0")) {
			holdView.taskItem_ImageLayout.setVisibility(View.GONE);
			isHaveImage = false;
		} else if (taskItemBean.getPicCount().equals("1")) {
			isHaveImage = true;
			imagePathArray1 = new String[1];
			imagePathArray1[0] = allImagePath;
			getAllSource = new String[1];
			getAllSource[0] = taskItemBean.getAllSource();
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_only1.setVisibility(View.VISIBLE);


			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ allImagePath+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/240x240":""),
					holdView.taskItem_ImageLayout_only1, options);

			holdView.taskItem_ImageLayout_Image1.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout_Image2.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout_Image3.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout_only1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray1, getAllSource);
				}
			});
		} else if (taskItemBean.getPicCount().equals("2")) {

			holdView.taskItem_ImageLayout_only1.setVisibility(View.GONE);
			isHaveImage = true;
			imagePathArray2 = allImagePath.split(",");
			getAllSource = taskItemBean.getAllSource().split(",");
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray2[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image1, options);
			holdView.taskItem_ImageLayout_Image1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray2[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image2, options);
			holdView.taskItem_ImageLayout_Image2.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image3.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2, getAllSource);
				}
			});
			holdView.taskItem_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2, getAllSource);
				}
			});
		} else {
			holdView.taskItem_ImageLayout_only1.setVisibility(View.GONE);
			isHaveImage = true;
			imagePathArray3 = allImagePath.split(",");
			getAllSource = taskItemBean.getAllSource().split(",");
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray3[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image1, options);
			holdView.taskItem_ImageLayout_Image1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray3[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image2, options);
			holdView.taskItem_ImageLayout_Image2.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[2]))
					+ imagePathArray3[2]+(Integer.parseInt(getAllSource[2]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image3, options);
			holdView.taskItem_ImageLayout_Image3.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
			holdView.taskItem_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
			holdView.taskItem_ImageLayout_Image3
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
		}
		holdView.clock_my_item_RightLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final int[] location = new int[2];   
				holdView.clock_my_item_pointImage.getLocationOnScreen(location);
				taskInfoOnClick.clickVote(taskItemBean.getJobId(), groupPosition, 
						childPosition, taskItemBean.getVoted(),
						location[0],location[1]);
			}
		});



	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupHolder {
		TextView textView, group_right;
		ImageView imageView;
		RelativeLayout groupTopLayout;
		LinearLayout userLayout,groupTopLayout_blank;

		// RelativeLayout groupLayout;
	}

	class ChildHolder {
		public TextView clock_my_item_name,// 卡名
		taskItem_Content,	//备注
		taskItem_Content2,
		taskItem_ContentNum,
		attention_fragment_item_TimeText,
		attention_fragment_item_commentsNumText,
		clock_my_item_pointNum,
		taskItem_bottomText;
		private ImageView taskItem_ImageLayout_Image1,
		taskItem_ImageLayout_only1, taskItem_ImageLayout_Image2,
		taskItem_ImageLayout_Image3, 
		clock_my_item_pointImage,// 点赞按钮
		clock_my_item_Image;// 勋章图片
		private ImageView clock_my_item_progress1, clock_my_item_progress2,
		clock_my_item_progress3, clock_my_item_progress4,
		clock_my_item_progress5, clock_my_item_progress6,
		clock_my_item_progress7;

		private View taskItem_bottomLine,taskItem_bottomLine2;

		private RelativeLayout bgLayout,taskItem_bottomLayout,taskItem_lineLayout;
		private LinearLayout taskItem_ImageLayout,clock_my_item_RightLayout,
		attention_fragment_item_commentsNumLayout,cardName;


		public ChildHolder(View v) {
			clock_my_item_name = (TextView) v
					.findViewById(R.id.clock_my_item_name);
			taskItem_ContentNum = (TextView) v
					.findViewById(R.id.taskItem_ContentNum);
			taskItem_Content = (TextView) v.findViewById(R.id.taskItem_Content);
			taskItem_Content2 = (TextView) v
					.findViewById(R.id.taskItem_Content2);
			attention_fragment_item_commentsNumText = (TextView) v
					.findViewById(R.id.attention_fragment_item_commentsNumText);
			attention_fragment_item_TimeText = (TextView) v
					.findViewById(R.id.attention_fragment_item_TimeText);
			clock_my_item_pointNum = (TextView) v
					.findViewById(R.id.clock_my_item_pointNum);
			taskItem_bottomText = (TextView) v
					.findViewById(R.id.taskItem_bottomText);

			taskItem_ImageLayout = (LinearLayout) v
					.findViewById(R.id.taskItem_ImageLayout);
			clock_my_item_RightLayout = (LinearLayout) v
					.findViewById(R.id.clock_my_item_RightLayout);
			attention_fragment_item_commentsNumLayout = (LinearLayout) v
					.findViewById(R.id.attention_fragment_item_commentsNumLayout);
			cardName = (LinearLayout) v
					.findViewById(R.id.cardName);

			clock_my_item_pointImage = (ImageView) v
					.findViewById(R.id.clock_my_item_pointImage);
			clock_my_item_progress1 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress1);
			clock_my_item_progress2 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress2);
			clock_my_item_progress3 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress3);
			clock_my_item_progress4 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress4);
			clock_my_item_progress5 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress5);
			clock_my_item_progress6 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress6);
			clock_my_item_progress7 = (ImageView) v
					.findViewById(R.id.clock_my_item_progress7);
			clock_my_item_Image = (ImageView) v
					.findViewById(R.id.clock_my_item_Image);

			taskItem_ImageLayout_only1 = (ImageView) v
					.findViewById(R.id.taskItem_ImageLayout_only1);
			taskItem_ImageLayout_Image1 = (ImageView) v
					.findViewById(R.id.taskItem_ImageLayout_Image1);
			taskItem_ImageLayout_Image2 = (ImageView) v
					.findViewById(R.id.taskItem_ImageLayout_Image2);
			taskItem_ImageLayout_Image3 = (ImageView) v
					.findViewById(R.id.taskItem_ImageLayout_Image3);


			//			taskItem_votesUsersLayout = (RelativeLayout) v
			//					.findViewById(R.id.taskItem_votesUsersLayout);
			bgLayout = (RelativeLayout) v
					.findViewById(R.id.bgLayout);
			taskItem_bottomLayout = (RelativeLayout) v
					.findViewById(R.id.taskItem_bottomLayout);
			taskItem_lineLayout = (RelativeLayout) v
					.findViewById(R.id.taskItem_lineLayout);



			taskItem_bottomLine2 = (View) v
					.findViewById(R.id.taskItem_bottomLine2);
			taskItem_bottomLine = (View) v
					.findViewById(R.id.taskItem_bottomLine);

		}
	}

	private void imageBrower(int position, String[] urls, String[] imageSource) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_SOURCE, imageSource);

		mContext.startActivity(intent);
	}

	/**
	 * 全角转换为半角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public interface TaskInfoOnClick {
		/**
		 * 点击评论
		 * */
		public void clickComment(TaskItemBean taskItemBean, int groupPosition,
				int childPosition, int itemBeanIndex);

		/**
		 * 点赞
		 */
		public void clickVote(String jobid, int groupPosition,
				int childPosition, String type,int x, int y);
	}

	public interface giveUpFlagClick {
		public void setGiveUp(int groupPosition, int childPosition);
	}

	public interface SharedThisJob {
		public void thisJob(TaskItemBean item);
	}
	public interface AttentionOtherTakeClock{
		public void setAttention(String userid,String name);
	}

}
