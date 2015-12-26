package com.yktx.check.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.BaseActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockTaskDynamicActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.bean.BasicInfoBean;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

public class ClockGroupInfoAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<TaskItemBean> itemBeans = new ArrayList<TaskItemBean>(10);
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
	.showImageOnLoading(R.anim.loading_image_animation).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// 启用内存缓存
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	public DisplayImageOptions options1 = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	// 启用内存缓存
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	private String mTitle, added, userID,description;
	BasicInfoBean basicInfoBean;
	public ClockGroupInfoAdapter(Context context,BasicInfoBean bean , String userId) {// String TaskCount,String
		mContext = context;
		basicInfoBean = bean;
		mTitle = basicInfoBean.getTitle();
		this.added = basicInfoBean.getAdded();
		inflater = LayoutInflater.from(mContext);
		this.userID = userId;
		description = basicInfoBean.getTaskCount()+"人   打卡"+basicInfoBean.getTotalJobCount()+"次";
	}

	public void setList(ArrayList<TaskItemBean> list) {
		itemBeans = list;
	}

	BuildingInfoOnClick buildingInfoOnClick;

	public void setBuildingOnClick(BuildingInfoOnClick buildingOnClick) {
		buildingInfoOnClick = buildingOnClick;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return itemBeans.size()+1;
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
		// if(position == 0){
		// convertView = inflater.inflate(R.layout.group_info_topitem, null);
		// TextView group_info_taskCount = (TextView)
		// convertView.findViewById(R.id.group_info_taskCount);
		// TextView group_info_jobCountToday = (TextView)
		// convertView.findViewById(R.id.group_info_jobCountToday);
		// group_info_taskCount.setText("有"+TaskCount+"人在坚持");
		// group_info_jobCountToday.setText("今日已打卡"+JobCountToday+"次");
		// return convertView;
		// }
//		if (position == 0) {
//			View view = inflater.inflate(R.layout.group_info_first_item, null);
//			TextView title = (TextView) view
//					.findViewById(R.id.group_info_frist_item_title);
//			TextView content = (TextView) view
//					.findViewById(R.id.group_info_frist_item_content);
//			ImageView addTask = (ImageView) view.findViewById(R.id.addTask);
//			if (added.equals("1")) {
//				addTask.setVisibility(View.GONE);
//				// title_item_username.setTextColor(getResources().getColor(R.color.meibao_color_12));
//			} else {
//				addTask.setVisibility(View.VISIBLE);
//				// title_item_username.setTextColor(getResources().getColor(R.color.meibao_color_1));
//			}
//			addTask.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					if (onClickAdd != null) {
//						onClickAdd.clickAdd();
//					}
//				}
//			});
//			title.setText(mTitle);
//			content.setText(description);
//			//			if (mContent != null && mContent.length() != 0) {
//			//				content.setText(mContent);
//			//			} else {
//			//				content.setVisibility(View.GONE);
//			//			}
//
//			return view;
//		}
		HoldView holder;
		convertView = inflater.inflate(R.layout.group_info_item, null);
		holder = (HoldView) convertView.getTag();
		if (holder == null) {
			holder = new HoldView(convertView);
			convertView.setTag(holder);
		}
		// position = position -1;
		TaskItemBean bean = itemBeans.get(position);
		if (bean != null) {

			ShowView(bean, holder, position);
		}
		return convertView;
	}

	SpannableString msp = null;

	public void ShowView(final TaskItemBean itemBean, final HoldView holdView,
			final int position) {
		final String[] imagePathArray1;
		final String[] imagePathArray2;
		final String[] imagePathArray3;
		final String[] getAllSource;
		boolean isHaveSingnature, isHaveImage, isHaveQuantity;
		boolean isblankVisity;
		imageLoader.displayImage(UrlParams.QINIU_IP + itemBean.getBadgePath(),//+"?imageMogr2/thumbnail/33x41"
				holdView.group_info_item_medalImage, options1);
		imageLoader.displayImage(Tools.getImagePath(itemBean.getAvatar_source())
				+ itemBean.getAvartar_path()+(itemBean.getAvatar_source() == 2?"?imageMogr2/thumbnail/50x50":""),
				holdView.group_info_item_headImage, headOptions);
		holdView.group_info_item_headImage
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(mContext,
							ClockMyActivity.class);
					mContext.startActivity(in);
				} else {
					Intent in = new Intent(mContext,
							ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					mContext.startActivity(in);
				}
			}
		});
		holdView.group_info_item_medalImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, PointExplainActivity.class);
				mContext.startActivity(in);	
			}
		});

		String allImagePath = itemBean.getAllPicPath();

		if (itemBean.getPicCount().equals("0")) {
			holdView.group_info_item_ImageLayout.setVisibility(View.GONE);
			isHaveImage = false;
		} else if (itemBean.getPicCount().equals("1")) {
			isHaveImage = true;
			imagePathArray1 = new String[1];
			imagePathArray1[0] = allImagePath;
			getAllSource = new String[1];
			getAllSource[0] = itemBean.getAllSource();
			holdView.group_info_item_ImageLayout_only1
			.setVisibility(View.VISIBLE);
			imageLoader.displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ allImagePath+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/310x310":""),
					holdView.group_info_item_ImageLayout_only1, options);
			holdView.group_info_item_ImageLayout_Image1
			.setVisibility(View.GONE);
			holdView.group_info_item_ImageLayout_Image2
			.setVisibility(View.GONE);
			holdView.group_info_item_ImageLayout_Image3
			.setVisibility(View.GONE);
			holdView.group_info_item_ImageLayout.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout_only1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Tools.getLog(Tools.i, "aaa", "imagePathArray1:"
							+ imagePathArray1[0]);
					imageBrower(0, imagePathArray1, getAllSource);
				}
			});
		} else if (itemBean.getPicCount().equals("2")) {
			holdView.group_info_item_ImageLayout_only1.setVisibility(View.GONE);
			isHaveImage = true;
			imagePathArray2 = allImagePath.split(",");
			getAllSource = itemBean.getAllSource().split(",");
			imageLoader.displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray2[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.group_info_item_ImageLayout_Image1, options);
			holdView.group_info_item_ImageLayout_Image1
			.setVisibility(View.VISIBLE);
			imageLoader.displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray2[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.group_info_item_ImageLayout_Image2, options);
			holdView.group_info_item_ImageLayout_Image2
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout_Image3
			.setVisibility(View.GONE);
			holdView.group_info_item_ImageLayout.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Tools.getLog(Tools.i, "aaa", "imagePathArray2:"
							+ imagePathArray2[0]);
					imageBrower(0, imagePathArray2, getAllSource);
				}
			});
			holdView.group_info_item_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray2, getAllSource);
				}
			});
		} else {
			isHaveImage = true;
			imagePathArray3 = allImagePath.split(",");
			holdView.group_info_item_ImageLayout_only1.setVisibility(View.GONE);

			getAllSource = itemBean.getAllSource().split(",");
			imageLoader.displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray3[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.group_info_item_ImageLayout_Image1, options);
			imageLoader.displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray3[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.group_info_item_ImageLayout_Image2, options);
			imageLoader.displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[2]))
					+ imagePathArray3[2]+(Integer.parseInt(getAllSource[2]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.group_info_item_ImageLayout_Image3, options);
			holdView.group_info_item_ImageLayout_Image1
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout_Image2
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout_Image3
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout.setVisibility(View.VISIBLE);
			holdView.group_info_item_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Tools.getLog(Tools.i, "aaa", "imagePathArray3:"
							+ imagePathArray3[0]);
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
			holdView.group_info_item_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray3, getAllSource);
				}
			});
			holdView.group_info_item_ImageLayout_Image3
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(2, imagePathArray3, getAllSource);
				}
			});
		}
		holdView.group_info_item_name.setText(itemBean.getName());
		holdView.group_info_item_currentStreak.setText(itemBean
				.getTotalDateCount());

		
		
		String city = itemBean.getCity(); // 是否有城市信息

		if (city.equals("0")) {
			// readcard_fragment_item_daqiImage
			// .setBackgroundResource(R.drawable.daqi_select);
			holdView.group_info_item_TimeText.setText(TimeUtil
					.getBuildTimes(itemBean.getCheckTime())
					+ "  第"
					+ itemBean.getTotalDateCount() + "天");
		} else {
			holdView.group_info_item_TimeText.setText(TimeUtil
					.getBuildTimes(itemBean.getCheckTime())
					+ "  第"
					+ itemBean.getTotalDateCount() + "天   " + city);
		}
		holdView.group_info_item_votesNumText.setText(itemBean.getVoteCount()
				+ "");
		holdView.group_info_item_commentsNumText.setText(itemBean
				.getCommentCount() + "");
		String singnature = itemBean.getSignature();
		if (singnature != null && singnature.length() != 0) {
			isHaveSingnature = true;
			holdView.group_info_item_clockContent.setText(ToDBC(singnature));
			holdView.group_info_item_clockContent.setVisibility(View.VISIBLE);
		} else {
			isHaveSingnature = false;
			holdView.group_info_item_clockContent.setVisibility(View.GONE);
		}
		String quantity = itemBean.getQuantity();
		if (quantity != null && quantity.length() != 0) {
			isHaveQuantity = true;
			String unit = itemBean.getUnit();
			if(unit.equals("0")){
				unit = "";
			}
			holdView.group_info_item_clockQuantity.setText(quantity+unit);
			holdView.group_info_item_clockQuantity.setVisibility(View.VISIBLE);
			holdView.group_info_item_clockQuantityLayout
			.setVisibility(View.VISIBLE);
		} else {
			isHaveQuantity = false;
			holdView.group_info_item_clockQuantity.setVisibility(View.GONE);
			holdView.group_info_item_clockQuantityLayout
			.setVisibility(View.GONE);
		}

		ArrayList<VotesBean> votesBeans = itemBean.getVotes();
		int votesBeansSize = votesBeans.size();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < votesBeansSize; i++) {
			sb.append(votesBeans.get(i).getName());
			if (i != (votesBeansSize - 1)) {
				sb.append(",");
			}
		}
		if(itemBean.getVoteCount() > 5){
			sb.append("...");
		}

		holdView.group_info_item_votesUsersContent.setText(sb.toString());
		String CommentCount = itemBean.getCommentCount() + "";
		ArrayList<CommentsBean> commentsBeans = itemBean.getComments();
		String username, text, time, contentText, repliedUserName = "";
		int usernameEnd, timeStart, timeEnd;
		boolean isCommentVisity;
		if (commentsBeans.size() == 0) {
			holdView.group_info_item_commentsContentLayout1
			.setVisibility(View.GONE);
			// holdView.group_info_item_commentsContentUserbeforeimage2.setVisibility(View.GONE);
			// holdView.group_info_item_commentsContentText2.setVisibility(View.GONE);
			holdView.group_info_item_commentsContentLayout2
			.setVisibility(View.GONE);
			isCommentVisity = true;
			isblankVisity = false;
		} else if (commentsBeans.size() == 1) {
			isCommentVisity = false;
			isblankVisity = true;
			username = commentsBeans.get(0).getName();
			text = commentsBeans.get(0).getText();
			time = "   "
					+ TimeUtil.getTimes(commentsBeans.get(0).getSendTime());

			if (commentsBeans.get(0).getCommentType() == 2) { // 回复消息
				repliedUserName = commentsBeans.get(0).getRepliedUserName()
						+ "：";
				contentText = username + "回复" + repliedUserName + text + time;
			} else {
				contentText = username + "：" + text + time;
			}

			usernameEnd = username.length();
			timeEnd = contentText.length();
			timeStart = timeEnd - time.length();
			msp = new SpannableString(ToDBC(contentText));
			msp.setSpan(new AbsoluteSizeSpan(12, true), 0, usernameEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 设置字体前景色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.meibao_color_10)), 0, usernameEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
			if (commentsBeans.get(0).getCommentType() == 2) { // 回复消息
				msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd,
						usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 设置字体前景色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.meibao_color_11)), usernameEnd,
						usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

				msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd + 2,
						usernameEnd + 2 + repliedUserName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 设置字体前景色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.meibao_color_10)), usernameEnd + 2,
						usernameEnd + 2 + repliedUserName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
			}
			msp.setSpan(new AbsoluteSizeSpan(12, true), timeStart, timeEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 设置字体前景色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.meibao_color_11)), timeStart, timeEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

			holdView.group_info_item_commentsContentText1.setText(msp);
			holdView.group_info_item_commentsContentText1
			.setMovementMethod(LinkMovementMethod.getInstance());

			// holdView.group_info_item_commentsContentUserbeforeimage2.setVisibility(View.GONE);
			// holdView.group_info_item_commentsContentText2.setVisibility(View.GONE);
			holdView.group_info_item_commentsContentLayout1
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_commentsContentLayout2
			.setVisibility(View.GONE);
		} else {
			// holdView.group_info_item_commentsContentUserText1.setText(commentsBeans.get(0).getName());
			// holdView.group_info_item_commentsContentUserText2.setText(commentsBeans.get(1).getName());
			// holdView.group_info_item_commentsContentText1.setText(commentsBeans.get(0).getText()
			// +"    "+
			// TimeUtil.getTimes(Long.parseLong(commentsBeans.get(0).getSendTime())));
			// holdView.group_info_item_commentsContentText2.setText(commentsBeans.get(1).getText()
			// +"    "+
			// TimeUtil.getTimes(Long.parseLong(commentsBeans.get(1).getSendTime())));
			isCommentVisity = false;
			isblankVisity = true;
			// 第一条
			username = commentsBeans.get(0).getName();
			text = commentsBeans.get(0).getText();
			time = "   "
					+ TimeUtil.getTimes(commentsBeans.get(0).getSendTime());
			if (commentsBeans.get(0).getCommentType() == 2) { // 回复消息
				repliedUserName = commentsBeans.get(0).getRepliedUserName()
						+ "：";
				contentText = username + "回复" + repliedUserName + text + time;
			} else {
				contentText = username + "：" + text + time;
			}

			usernameEnd = username.length();
			timeEnd = contentText.length();
			timeStart = timeEnd - time.length();
			msp = new SpannableString(ToDBC(contentText));
			msp.setSpan(new AbsoluteSizeSpan(12, true), 0, usernameEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 设置字体前景色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.meibao_color_10)), 0, usernameEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

			if (commentsBeans.get(0).getCommentType() == 2) { // 回复消息
				msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd,
						usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 设置字体前景色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.meibao_color_11)), usernameEnd,
						usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

				msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd + 2,
						usernameEnd + 2 + repliedUserName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 设置字体前景色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.meibao_color_10)), usernameEnd + 2,
						usernameEnd + 2 + repliedUserName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
			}
			msp.setSpan(new AbsoluteSizeSpan(12, true), timeStart, timeEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 设置字体前景色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.meibao_color_11)), timeStart, timeEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

			holdView.group_info_item_commentsContentText1.setText(msp);
			holdView.group_info_item_commentsContentText1
			.setMovementMethod(LinkMovementMethod.getInstance());

			// 第二条
			username = commentsBeans.get(1).getName();
			text = commentsBeans.get(1).getText();
			time = "   "
					+ TimeUtil.getTimes(commentsBeans.get(1).getSendTime());

			if (commentsBeans.get(1).getCommentType() == 2) { // 回复消息
				repliedUserName = commentsBeans.get(1).getRepliedUserName()
						+ "：";
				contentText = username + "回复" + repliedUserName + text + time;
			} else {
				contentText = username + "：" + text + time;
			}

			usernameEnd = username.length();
			timeEnd = contentText.length();
			timeStart = timeEnd - time.length();
			msp = new SpannableString(ToDBC(contentText));
			msp.setSpan(new AbsoluteSizeSpan(12, true), 0, usernameEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 设置字体前景色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.meibao_color_10)), 0, usernameEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
			if (commentsBeans.get(1).getCommentType() == 2) { // 回复消息
				msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd,
						usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 设置字体前景色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.meibao_color_11)), usernameEnd,
						usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

				msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd + 2,
						usernameEnd + 2 + repliedUserName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 设置字体前景色
				msp.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.meibao_color_10)), usernameEnd + 2,
						usernameEnd + 2 + repliedUserName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
			}
			msp.setSpan(new AbsoluteSizeSpan(12, true), timeStart, timeEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 设置字体前景色
			msp.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.meibao_color_11)), timeStart, timeEnd,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
			// holdView.newset_fragment_item_commentsContentUserText2.setText(commentsBeans.get(1).getName());
			// holdView.newset_fragment_item_commentsContentText2.setText(commentsBeans.get(1).getText()
			// +"    "+
			// TimeUtil.getTimes(Long.parseLong(commentsBeans.get(1).getSendTime())));
			holdView.group_info_item_commentsContentText2.setText(msp);
			holdView.group_info_item_commentsContentText2
			.setMovementMethod(LinkMovementMethod.getInstance());
			holdView.group_info_item_commentsContentLayout2
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_commentsContentLayout1
			.setVisibility(View.VISIBLE);
		}
		if (CommentCount != null && Integer.parseInt(CommentCount) > 2) {
			holdView.group_info_item_commentsContentMore
			.setVisibility(View.VISIBLE);
		} else {
			holdView.group_info_item_commentsContentMore
			.setVisibility(View.GONE);
		}

		String voted = itemBean.getVoted();
		if (voted.equals("0")) {
			holdView.group_info_item_votesNumImage
			.setImageResource(R.drawable.building_vote_image);
		} else {
			holdView.group_info_item_votesNumImage
			.setImageResource(R.drawable.building_vote_selectimage);
		}
		if (itemBean.getVoteCount() != 0) {
			holdView.group_info_item_votesUsersLayout
			.setVisibility(View.VISIBLE);
			holdView.group_info_item_lineImage.setVisibility(View.VISIBLE);
			holdView.group_info_item_CVMLLayout.setVisibility(View.VISIBLE);
			holdView.group_info_item_NumCVLayout.setVisibility(View.VISIBLE);
			if (isblankVisity) {
				holdView.group_info_item_blank.setVisibility(View.VISIBLE);
			} else {
				holdView.group_info_item_blank.setVisibility(View.GONE);
			}
		} else {
			holdView.group_info_item_votesUsersLayout.setVisibility(View.GONE);
			holdView.group_info_item_blank.setVisibility(View.GONE);
			if (isCommentVisity) {
				holdView.group_info_item_lineImage.setVisibility(View.GONE);
				holdView.group_info_item_CVMLLayout.setVisibility(View.GONE);
				holdView.group_info_item_NumCVLayout.setVisibility(View.GONE);
			} else {
				holdView.group_info_item_lineImage.setVisibility(View.VISIBLE);
				holdView.group_info_item_CVMLLayout.setVisibility(View.VISIBLE);
				holdView.group_info_item_NumCVLayout
				.setVisibility(View.VISIBLE);
			}
		}
		// String level = itemBean.getLevel();
		if (isHaveImage) {
			holdView.group_info_item_imagebottomblank
			.setVisibility(View.VISIBLE);
			// holdView.group_info_item_clockContent_BottomBlackLayout.setVisibility(View.VISIBLE);
		} else {
			holdView.group_info_item_imagebottomblank.setVisibility(View.GONE);
			// holdView.group_info_item_clockContent_BottomBlackLayout.setVisibility(View.GONE);
		}
		if (!isHaveImage && !isHaveSingnature) {
			holdView.group_info_item_clockContent.setVisibility(View.VISIBLE);
			holdView.group_info_item_clockContent.setText("打卡1次");
		}
		/** 这个是是否保持有颜色的圆角 */
		// if(isHaveImage||isHaveQuantity||isHaveSingnature){
		// holdView.group_info_item_IQSLayout.setVisibility(View.VISIBLE);
		// // if(level.equals("0")){
		// //
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_level1);
		// // }else if(level.equals("1")){
		// //
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_level2);
		// // }else{
		// //
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_level3);
		// // }
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_levelall);
		// }else{
		// holdView.group_info_item_IQSLayout.setVisibility(View.GONE);
		// // if(level.equals("0")){
		// //
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_level1_no);
		// // }else if(level.equals("1")){
		// //
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_level2_no);
		// // }else{
		// //
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_level3_no);
		// // }
		// holdView.group_info_item_clockLevelLayout.setBackgroundResource(R.drawable.building_groupinfo_levelall_no);
		// }
		int progress = itemBean.getProgress();
		switch (progress) {
		case 0:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 1:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 2:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 3:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 4:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 5:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		case 6:
			holdView.group_info_item_progress1.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress2.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress3.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress4.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress5.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress6.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1));
			holdView.group_info_item_progress7.setBackgroundColor(mContext.getResources().getColor(R.color.meibao_color_1_light));
			break;
		}
		
		
		
		holdView.group_info_item_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(mContext, ClockMyActivity.class);
					mContext.startActivity(in);
				} else {
					Intent in = new Intent(mContext,
							ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					mContext.startActivity(in);
				}
			}
		});
		
		holdView.group_info_item_IQSLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, TaskInfoActivity.class);
				in.putExtra("taskid", itemBean.getTaskId());
				in.putExtra("userid", itemBean.getUserId());
				mContext.startActivity(in);
			}
		});
		holdView.group_info_item_commentsContentText1
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent in = new Intent(mContext,
				// ClockCommentActivity.class);
				// in.putExtra("jobid", itemBean.getJobId());
				// mContext.startActivity(in);
				buildingInfoOnClick.clickComment(itemBean, position, 0);
			}
		});
		holdView.group_info_item_commentsContentText2
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent in = new Intent(mContext,
				// ClockCommentActivity.class);
				// in.putExtra("jobid", itemBean.getJobId());
				// mContext.startActivity(in);
				buildingInfoOnClick.clickComment(itemBean, position, 1);
			}
		});
		holdView.group_info_item_votesUsersContent
		.setOnClickListener(new OnClickListener() {
			// 点赞详情
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 0);//默认选中   0打气，1评论
				in.putExtra("jobid", itemBean.getJobId());
				in.putExtra("taskId", itemBean.getTaskId());
				 in.putExtra("createUserID", itemBean.getUserId());
				mContext.startActivity(in);
			}
		});
		holdView.group_info_item_commentsContentMore
		.setOnClickListener(new OnClickListener() {
			// 评论详情
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 1);//默认选中   0打气，1评论
				in.putExtra("jobid", itemBean.getJobId());
				in.putExtra("createUserID", itemBean.getUserId());
				in.putExtra("taskId", itemBean.getTaskId());
				mContext.startActivity(in);
			}
		});
		holdView.group_info_item_votesNumLayout
		.setOnClickListener(new OnClickListener() {
			// 点赞或者取消点赞
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (itemBean.getClickMore()) {
					itemBean.setClickMore(false);
					
					final int[] location = new int[2];   
					holdView.group_info_item_votesNumImage.getLocationOnScreen(location);
					buildingInfoOnClick.clickVote(itemBean.getJobId(),
							position, itemBean.getVoted(),location[0],location[1]);
				}

			}
		});
		holdView.group_info_item_commentsNumLayout
		.setOnClickListener(new OnClickListener() {
			// 评论
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buildingInfoOnClick.clickComment(itemBean, position, -1);
			}
		});

	}

	public class HoldView {
		TextView group_info_item_name, group_info_item_currentStreak,
		group_info_item_clockContent;// 上部分的id

		TextView group_info_item_TimeText, group_info_item_votesNumText,
		group_info_item_commentsNumText;// 计数的id

		TextView group_info_item_votesUsersContent;// 点赞的人的id

		TextView group_info_item_commentsContentUserText1,
		group_info_item_commentsContentText1;// 评论1
		TextView group_info_item_commentsContentUserText2,
		group_info_item_commentsContentText2;// 评论2
		TextView group_info_item_commentsContentMore,
		group_info_item_clockQuantity;

		ImageView group_info_item_headImage, group_info_item_ImageLayout_only1,
		group_info_item_ImageLayout_Image1,
		group_info_item_ImageLayout_Image2,
		group_info_item_ImageLayout_Image3,
		group_info_item_commentsContentUserbeforeimage2,
		group_info_item_votesNumImage, group_info_item_lineImage,
		group_info_item_clockContentLayout_buttomLine,
		group_info_item_medalImage;
		LinearLayout group_info_item_ImageLayout,
		group_info_item_commentsContentLayout1,
		group_info_item_commentsContentLayout2,
		group_info_item_votesNumLayout,
		group_info_item_commentsNumLayout, group_info_item_blank;
		RelativeLayout group_info_item_votesUsersLayout,
		group_info_item_clockLevelLayout, group_info_item_IQSLayout,
		group_info_item_imagebottomblank, group_info_item_CVMLLayout,
		group_info_item_NumCVLayout,
		group_info_item_clockQuantityLayout,
		group_info_item_clockContent_BottomBlackLayout;
		ImageView group_info_item_progress1,group_info_item_progress2,group_info_item_progress3,
		group_info_item_progress4,group_info_item_progress5,group_info_item_progress6,group_info_item_progress7;

		public HoldView(View convertView) {
			group_info_item_name = (TextView) convertView
					.findViewById(R.id.group_info_item_name);
			group_info_item_currentStreak = (TextView) convertView
					.findViewById(R.id.group_info_item_currentStreak);
			group_info_item_clockContent = (TextView) convertView
					.findViewById(R.id.group_info_item_clockContent);

			group_info_item_TimeText = (TextView) convertView
					.findViewById(R.id.group_info_item_TimeText);
			group_info_item_votesNumText = (TextView) convertView
					.findViewById(R.id.group_info_item_votesNumText);
			group_info_item_commentsNumText = (TextView) convertView
					.findViewById(R.id.group_info_item_commentsNumText);

			group_info_item_votesUsersContent = (TextView) convertView
					.findViewById(R.id.group_info_item_votesUsersContent);

			group_info_item_commentsContentUserText1 = (TextView) convertView
					.findViewById(R.id.group_info_item_commentsContentUserText1);
			group_info_item_commentsContentText1 = (TextView) convertView
					.findViewById(R.id.group_info_item_commentsContentText1);
			group_info_item_commentsContentUserText2 = (TextView) convertView
					.findViewById(R.id.group_info_item_commentsContentUserText2);
			group_info_item_commentsContentText2 = (TextView) convertView
					.findViewById(R.id.group_info_item_commentsContentText2);

			group_info_item_commentsContentMore = (TextView) convertView
					.findViewById(R.id.group_info_item_commentsContentMore);
			group_info_item_clockQuantity = (TextView) convertView
					.findViewById(R.id.group_info_item_clockQuantity);

			group_info_item_headImage = (ImageView) convertView
					.findViewById(R.id.group_info_item_headImage);
			group_info_item_ImageLayout_Image1 = (ImageView) convertView
					.findViewById(R.id.group_info_item_ImageLayout_Image1);
			group_info_item_ImageLayout_only1 = (ImageView) convertView
					.findViewById(R.id.group_info_item_ImageLayout_only1);

			group_info_item_ImageLayout_Image2 = (ImageView) convertView
					.findViewById(R.id.group_info_item_ImageLayout_Image2);
			group_info_item_ImageLayout_Image3 = (ImageView) convertView
					.findViewById(R.id.group_info_item_ImageLayout_Image3);
			group_info_item_commentsContentUserbeforeimage2 = (ImageView) convertView
					.findViewById(R.id.group_info_item_commentsContentUserbeforeimage2);
			group_info_item_votesNumImage = (ImageView) convertView
					.findViewById(R.id.group_info_item_votesNumImage);
			group_info_item_lineImage = (ImageView) convertView
					.findViewById(R.id.group_info_item_lineImage);
			group_info_item_clockContentLayout_buttomLine = (ImageView) convertView
					.findViewById(R.id.group_info_item_clockContentLayout_buttomLine);
			group_info_item_medalImage = (ImageView) convertView
					.findViewById(R.id.group_info_item_medalImage);

			group_info_item_progress1 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress1);
			group_info_item_progress2 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress2);
			group_info_item_progress3 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress3);
			group_info_item_progress4 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress4);
			group_info_item_progress5 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress5);
			group_info_item_progress6 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress6);
			group_info_item_progress7 = (ImageView) convertView
					.findViewById(R.id.group_info_item_progress7);

			group_info_item_blank = (LinearLayout) convertView
					.findViewById(R.id.group_info_item_blank);
			group_info_item_ImageLayout = (LinearLayout) convertView
					.findViewById(R.id.group_info_item_ImageLayout);
			group_info_item_commentsContentLayout1 = (LinearLayout) convertView
					.findViewById(R.id.group_info_item_commentsContentLayout1);
			group_info_item_commentsContentLayout2 = (LinearLayout) convertView
					.findViewById(R.id.group_info_item_commentsContentLayout2);
			group_info_item_votesNumLayout = (LinearLayout) convertView
					.findViewById(R.id.group_info_item_votesNumLayout);
			group_info_item_commentsNumLayout = (LinearLayout) convertView
					.findViewById(R.id.group_info_item_commentsNumLayout);

			group_info_item_votesUsersLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_votesUsersLayout);
			group_info_item_clockLevelLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_clockLevelLayout);
			group_info_item_IQSLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_IQSLayout);
			group_info_item_imagebottomblank = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_imagebottomblank);
			group_info_item_CVMLLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_CVMLLayout);
			group_info_item_NumCVLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_NumCVLayout);
			group_info_item_clockQuantityLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_clockQuantityLayout);
			group_info_item_clockContent_BottomBlackLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_info_item_clockContent_BottomBlackLayout);
		}
	}

	public interface BuildingInfoOnClick {
		/**
		 * 点击评论
		 * */
		public void clickComment(TaskItemBean taskItemBean, int position,
				int itemBeanIndex);

		/**
		 * 点赞
		 */
		public void clickVote(String jobid, int position, String type,int x, int y);
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

	OnClickAdd onClickAdd;

	public void setOnClickAdd(OnClickAdd onClickAdd) {
		this.onClickAdd = onClickAdd;
	}

	public interface OnClickAdd {
		public void clickAdd();
	}

}
