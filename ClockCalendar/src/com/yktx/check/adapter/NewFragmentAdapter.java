package com.yktx.check.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yktx.check.ClockGroupInfoActivity;
import com.yktx.check.ClockGroupInfoFragmentActivity;
import com.yktx.check.ClockMyActivity;
import com.yktx.check.ClockOtherUserActivity;
import com.yktx.check.ClockTaskDynamicActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.PointExplainActivity;
import com.yktx.check.R;
import com.yktx.check.TaskInfoActivity;
import com.yktx.check.adapter.AttentionFragmentListViewAdapter.BuildingOnClick;
import com.yktx.check.adapter.AttentionFragmentListViewAdapter.HoldView;
import com.yktx.check.adapter.NewFragmentListViewAdapter.OnNewFragmentItemClick;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.conn.UrlParams;
import com.yktx.check.listener.IntoUserCenterListener;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;

import android.app.Activity;
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
/**
 * 阅卡的adapter
 * */
public class NewFragmentAdapter extends BaseAdapter{

	Activity context;
	String latitude = "0.0";
	String longitude = "0.0";
	BuildingOnClick buildingOnClick;
	//	OnNewFragmentItemClick fragmentItemClick;
	IntoUserCenterListener intoUserCenter;
	public DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.zw_touxiang)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	.cacheInMemory(false).displayer(new RoundedBitmapDisplayer(100))
	// 启用内存缓存
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.anim.loading_image_animation).showImageForEmptyUri(null)
	.showImageOnFail(null).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	public DisplayImageOptions options1 = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.toumingimg)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	// .displayer(new RoundedBitmapDisplayer(150))
	// .displayer(new RoundedBitmapDisplayer(6))
	// .cacheInMemory(false)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

	ArrayList<TaskItemBean> list = new ArrayList<TaskItemBean>(10);

	public HashMap<String, Object> chatFacialMap = new HashMap<String, Object>();

	String userID;
	int type;

	public NewFragmentAdapter(Activity context,String userID) {
		this.context = context;
		this.userID = userID;
	}

	public void setBuildingOnClick(BuildingOnClick buildingOnClick) {
		this.buildingOnClick = buildingOnClick;
	}
	BuildingIsHaveImage mBuildingIsHaveImage;
	public void setBuildingIsHaveImage(BuildingIsHaveImage buildingIsHaveImage){
		mBuildingIsHaveImage = buildingIsHaveImage;
	}

	//	public void setOnNewFragmentItemClick(
	//			OnNewFragmentItemClick fragmentItemClick) {
	//		this.fragmentItemClick = fragmentItemClick;
	//	}

	public void setList(ArrayList<TaskItemBean> list,int type) {
		this.list = list;
		this.type = type;
	}

	public void setIntoUserCenter(IntoUserCenterListener intoUserCenter) {
		this.intoUserCenter = intoUserCenter;
	}

	public void setDistance(String latitude, String longitude) {
		// TODO Auto-generated method stub
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private boolean isNullItem;

	@Override
	public int getCount() {
		// return list.size();

		if (list.size() == 0) {
			isNullItem = true;
			return 1;
		}
		isNullItem = false;
		return list.size()+1;
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
		// int type = 1;
		// type = getItemViewType(position);
		if (isNullItem) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.image_null_item, null);
			ImageView image = (ImageView) v.findViewById(R.id.imageListNull);
			image.setImageResource(R.drawable.zhanwei_zuixin);
			return v;
		}
		if(position == 0){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_fragment_firstitem, null);
			final TextView new_fragment_typeAll = (TextView) convertView.findViewById(R.id.new_fragment_typeAll);
			final TextView new_fragment_typeHaveImage = (TextView) convertView.findViewById(R.id.new_fragment_typeHaveImage);
			if(type == 0){
				new_fragment_typeAll.setTextColor(context.getResources().getColor(R.color.meibao_color_1));
				new_fragment_typeHaveImage.setTextColor(context.getResources().getColor(R.color.meibao_color_14));
			}else{
				new_fragment_typeAll.setTextColor(context.getResources().getColor(R.color.meibao_color_14));
				new_fragment_typeHaveImage.setTextColor(context.getResources().getColor(R.color.meibao_color_1));
			}
			new_fragment_typeAll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					type = 0;
					mBuildingIsHaveImage.clickType(type);
					new_fragment_typeAll.setTextColor(context.getResources().getColor(R.color.meibao_color_1));
					new_fragment_typeHaveImage.setTextColor(context.getResources().getColor(R.color.meibao_color_14));
				}
			});
			new_fragment_typeHaveImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					type = 1;
					mBuildingIsHaveImage.clickType(type);
					new_fragment_typeAll.setTextColor(context.getResources().getColor(R.color.meibao_color_14));
					new_fragment_typeHaveImage.setTextColor(context.getResources().getColor(R.color.meibao_color_1));
				}
			});
			return convertView;
		}
		
		TaskItemBean newMainBean = (TaskItemBean) list.get(position-1);
		// holder = (
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.attention_fragment_item, null);
			holdView = new HoldView(convertView);

			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		show(holdView, newMainBean, position);

		return convertView;
	}

	SpannableString msp = null;

	private void show(final HoldView holdView, final TaskItemBean itemBean,
			final int position) {
		final String[] imagePathArray1;
		final String[] imagePathArray2;
		final String[] imagePathArray3;
		final String[] getAllSource;
		boolean isHaveSingnature, isHaveImage, isHaveQuantity;
		boolean isblankVisity;
//		holdView.attention_fragment_item_imageStick.setVisibility(View.GONE);//图片
		if(itemBean.getStickFlag() == 0){
			holdView.attention_fragment_item_StickLayout.setVisibility(View.GONE);
			holdView.attention_fragment_item_textStick.setText("");
		}else{
			holdView.attention_fragment_item_StickLayout.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_textStick.setText(itemBean.getStickJobPraise());
		}
		holdView.attention_fragment_item_topLayout.setVisibility(View.VISIBLE);
		//		holdView.attention_fragment_item_dayLayout.setVisibility(View.GONE);
		holdView.attention_fragment_item_yisi.setVisibility(View.GONE);
		holdView.attention_fragment_item_blankTopLayout
		.setVisibility(View.GONE);
		holdView.attention_fragment_item_blankTopLayout1
		.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage(
				UrlParams.QINIU_IP + itemBean.getBadgePath(),//+"?imageMogr2/thumbnail/33x45"
				holdView.attention_fragment_item_medalImage, options1);
		ImageLoader.getInstance().displayImage(
				Tools.getImagePath(itemBean.getAvatar_source())
				+ itemBean.getAvartar_path()+(itemBean.getAvatar_source() == 2?"?imageMogr2/thumbnail/50x50":""),
				holdView.attention_fragment_item_headImage, headOptions);
		holdView.attention_fragment_item_headImage
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// intoUserCenter.getIntoUserCenter(itemBean.getTaskId());\
				// GroupMainFragmentActivity.isReflush =
				// true;//让主activity 不刷新fragment
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(context,
							ClockMyActivity.class);
					context.startActivity(in);
				} else {
					Intent in = new Intent(context,
							ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					context.startActivity(in);
				}
			}
		});
		holdView.attention_fragment_item_medalImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, PointExplainActivity.class);
				context.startActivity(in);	
			}
		});
		final String allImagePath = itemBean.getAllPicPath();
		if (itemBean.getPicCount().equals("0")) {
			holdView.attention_fragment_item_ImageLayout
			.setVisibility(View.GONE);
			isHaveImage = false;
		} else if (itemBean.getPicCount().equals("1")) {
			isHaveImage = true;
			imagePathArray1 = new String[1];
			imagePathArray1[0] = allImagePath;
			getAllSource = new String[1];
			getAllSource[0] = itemBean.getAllSource();
			holdView.newset_fragment_item_ImageLayout_only1
			.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ allImagePath+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/310x310":""),
					holdView.newset_fragment_item_ImageLayout_only1, options);
			holdView.attention_fragment_item_ImageLayout_Image1
			.setVisibility(View.GONE);
			holdView.attention_fragment_item_ImageLayout_Image2
			.setVisibility(View.GONE);
			holdView.attention_fragment_item_ImageLayout_Image3
			.setVisibility(View.GONE);
			holdView.attention_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
			holdView.newset_fragment_item_ImageLayout_only1
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray1, getAllSource);
					Tools.getLog(Tools.i, "aaa", "imagePathArray1:"
							+ imagePathArray1[0]);
				}
			});
		} else if (itemBean.getPicCount().equals("2")) {
			isHaveImage = true;
			imagePathArray2 = allImagePath.split(",");

			getAllSource = itemBean.getAllSource().split(",");
			holdView.newset_fragment_item_ImageLayout_only1
			.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray2[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.attention_fragment_item_ImageLayout_Image1,
					options);
			holdView.attention_fragment_item_ImageLayout_Image1
			.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray2[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.attention_fragment_item_ImageLayout_Image2,
					options);
			holdView.attention_fragment_item_ImageLayout_Image2
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_ImageLayout_Image3
			.setVisibility(View.GONE);
			holdView.attention_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2, getAllSource);
				}
			});
			holdView.attention_fragment_item_ImageLayout_Image2
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
			getAllSource = itemBean.getAllSource().split(",");
			holdView.newset_fragment_item_ImageLayout_only1
			.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[0]))
					+ imagePathArray3[0]+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.attention_fragment_item_ImageLayout_Image1,
					options);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+ imagePathArray3[1]+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.attention_fragment_item_ImageLayout_Image2,
					options);
			ImageLoader.getInstance().displayImage(
					Tools.getImagePath(Integer.parseInt(getAllSource[2]))
					+ imagePathArray3[2]+(Integer.parseInt(getAllSource[2]) == 2?"?imageMogr2/thumbnail/160x160":""),
					holdView.attention_fragment_item_ImageLayout_Image3,
					options);
			holdView.attention_fragment_item_ImageLayout_Image1
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_ImageLayout_Image2
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_ImageLayout_Image3
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_ImageLayout_Image1
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3, getAllSource);
				}
			});
			holdView.attention_fragment_item_ImageLayout_Image2
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(1, imagePathArray3, getAllSource);
				}
			});
			holdView.attention_fragment_item_ImageLayout_Image3
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(2, imagePathArray3, getAllSource);
				}
			});
			holdView.attention_fragment_item_ImageLayout
			.setVisibility(View.VISIBLE);
		}
		holdView.attention_fragment_item_name.setText(itemBean.getName());
		holdView.attention_fragment_item_currentStreak.setText(itemBean.getTaskCount()
				+ "");
		holdView.attention_fragment_item_clockName.setText(itemBean.getTitle());
		String singnature = itemBean.getSignature();
		if (singnature != null && singnature.length() != 0) {
			isHaveSingnature = true;
			holdView.attention_fragment_item_clockContent
			.setText(ToDBC(singnature));
			holdView.attention_fragment_item_clockContent
			.setVisibility(View.VISIBLE);
		} else {
			isHaveSingnature = false;
			holdView.attention_fragment_item_clockContent
			.setVisibility(View.GONE);
		}
		holdView.attention_fragment_item_clockContent.setText(itemBean
				.getSignature());

		String city = itemBean.getCity();
		String textStr = TimeUtil.getTimes(itemBean.getCheck_time()) + "  第"
				+ itemBean.getTotalDateCount() + "天";
		if (!city.equals("0")) {
			textStr += "  " + itemBean.getCity();
		}
		holdView.attention_fragment_item_TimeText.setText(textStr);
		
		holdView.attention_fragment_item_votesNumText.setText(itemBean
				.getVoteCount() + "");
		holdView.attention_fragment_item_commentsNumText.setText(itemBean
				.getCommentCount() + "");
		String quantity = itemBean.getQuantity();
		if (quantity != null && quantity.length() != 0) {
			isHaveQuantity = true;
			String unit = itemBean.getUnit();
			if(unit.equals("0")){
				unit = "";
			}
			holdView.attention_fragment_item_clockQuantity.setText(quantity+unit);
			holdView.attention_fragment_item_clockQuantity
			.setVisibility(View.VISIBLE);
			holdView.attention_fragment_item_clockQuantityLayout
			.setVisibility(View.VISIBLE);
		} else {
			isHaveQuantity = false;
			holdView.attention_fragment_item_clockQuantity
			.setVisibility(View.GONE);
			holdView.attention_fragment_item_clockQuantityLayout
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
		holdView.attention_fragment_item_votesUsersContent.setText(sb.toString());
		String voted = itemBean.getVoted();
		if (voted.equals("0")) {
			holdView.attention_fragment_item_votesNumImage
			.setImageResource(R.drawable.building_vote_image);
		} else {
			holdView.attention_fragment_item_votesNumImage
			.setImageResource(R.drawable.building_vote_selectimage);
		}
		//不要评论 点赞
//		holdView.attention_fragment_item_CVMLLayout.setVisibility(View.GONE);
//		holdView.attention_fragment_item_NumCVLayout.setVisibility(View.GONE);

				
				String CommentCount = itemBean.getCommentCount() + "";
				ArrayList<CommentsBean> commentsBeans = itemBean.getComments();
				String username, text, time, contentText, repliedUserName = "";
				int usernameEnd, timeStart, timeEnd;
				boolean isCommentVisity;
				if (commentsBeans.size() == 0) {
					holdView.attention_fragment_item_commentsContentLayout1
					.setVisibility(View.GONE);
					// holdView.attention_fragment_item_commentsContentUserbeforeimage2.setVisibility(View.GONE);
					// holdView.attention_fragment_item_commentsContentText2.setVisibility(View.GONE);
					holdView.attention_fragment_item_commentsContentLayout2
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
					msp.setSpan(new ForegroundColorSpan(context.getResources()
							.getColor(R.color.meibao_color_10)), 0, usernameEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
					if (commentsBeans.get(0).getCommentType() == 2) { // 回复消息
						msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd,
								usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 设置字体前景色
						msp.setSpan(new ForegroundColorSpan(context.getResources()
								.getColor(R.color.meibao_color_11)), usernameEnd,
								usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		
						msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd + 2,
								usernameEnd + 2 + repliedUserName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 设置字体前景色
						msp.setSpan(new ForegroundColorSpan(context.getResources()
								.getColor(R.color.meibao_color_10)), usernameEnd + 2,
								usernameEnd + 2 + repliedUserName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
					}
					msp.setSpan(new AbsoluteSizeSpan(12, true), timeStart, timeEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					// 设置字体前景色
					msp.setSpan(new ForegroundColorSpan(context.getResources()
							.getColor(R.color.meibao_color_11)), timeStart, timeEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		
					holdView.attention_fragment_item_commentsContentText1.setText(msp);
					holdView.attention_fragment_item_commentsContentText1
					.setMovementMethod(LinkMovementMethod.getInstance());
		
					holdView.attention_fragment_item_commentsContentLayout1
					.setVisibility(View.VISIBLE);
					holdView.attention_fragment_item_commentsContentLayout2
					.setVisibility(View.GONE);
				} else {
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
					msp.setSpan(new ForegroundColorSpan(context.getResources()
							.getColor(R.color.meibao_color_10)), 0, usernameEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		
					if (commentsBeans.get(0).getCommentType() == 2) { // 回复消息
						msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd,
								usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 设置字体前景色
						msp.setSpan(new ForegroundColorSpan(context.getResources()
								.getColor(R.color.meibao_color_11)), usernameEnd,
								usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		
						msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd + 2,
								usernameEnd + 2 + repliedUserName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 设置字体前景色
						msp.setSpan(new ForegroundColorSpan(context.getResources()
								.getColor(R.color.meibao_color_10)), usernameEnd + 2,
								usernameEnd + 2 + repliedUserName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
					}
					msp.setSpan(new AbsoluteSizeSpan(12, true), timeStart, timeEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					// 设置字体前景色
					msp.setSpan(new ForegroundColorSpan(context.getResources()
							.getColor(R.color.meibao_color_11)), timeStart, timeEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		
					holdView.attention_fragment_item_commentsContentText1.setText(msp);
					holdView.attention_fragment_item_commentsContentText1
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
					msp.setSpan(new ForegroundColorSpan(context.getResources()
							.getColor(R.color.meibao_color_10)), 0, usernameEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
					if (commentsBeans.get(1).getCommentType() == 2) { // 回复消息
						msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd,
								usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 设置字体前景色
						msp.setSpan(new ForegroundColorSpan(context.getResources()
								.getColor(R.color.meibao_color_11)), usernameEnd,
								usernameEnd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		
						msp.setSpan(new AbsoluteSizeSpan(12, true), usernameEnd + 2,
								usernameEnd + 2 + repliedUserName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						// 设置字体前景色
						msp.setSpan(new ForegroundColorSpan(context.getResources()
								.getColor(R.color.meibao_color_10)), usernameEnd + 2,
								usernameEnd + 2 + repliedUserName.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
					}
					msp.setSpan(new AbsoluteSizeSpan(12, true), timeStart, timeEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					// 设置字体前景色
					msp.setSpan(new ForegroundColorSpan(context.getResources()
							.getColor(R.color.meibao_color_11)), timeStart, timeEnd,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
					// holdView.newset_fragment_item_commentsContentUserText2.setText(commentsBeans.get(1).getName());
					// holdView.newset_fragment_item_commentsContentText2.setText(commentsBeans.get(1).getText()
					// +"    "+
					// TimeUtil.getTimes(Long.parseLong(commentsBeans.get(1).getSendTime())));
					holdView.attention_fragment_item_commentsContentText2.setText(msp);
					holdView.attention_fragment_item_commentsContentText2
					.setMovementMethod(LinkMovementMethod.getInstance());
					holdView.attention_fragment_item_commentsContentLayout2
					.setVisibility(View.VISIBLE);
					holdView.attention_fragment_item_commentsContentLayout1
					.setVisibility(View.VISIBLE);
				}
				if (CommentCount != null && Integer.parseInt(CommentCount) > 2) {
					holdView.attention_fragment_item_commentsContentMore
					.setVisibility(View.VISIBLE);
				} else {
					holdView.attention_fragment_item_commentsContentMore
					.setVisibility(View.GONE);
				}
		
				
				if (itemBean.getVoteCount() != 0) {
					holdView.attention_fragment_item_votesUsersLayout
					.setVisibility(View.VISIBLE);
					holdView.attention_fragment_item_lineImage
					.setVisibility(View.VISIBLE);
					holdView.attention_fragment_item_CVMLLayout
					.setVisibility(View.VISIBLE);
					holdView.attention_fragment_item_NumCVLayout
					.setVisibility(View.VISIBLE);
					if (isblankVisity) {
						holdView.attention_fragment_item_blank
						.setVisibility(View.VISIBLE);
					} else {
						holdView.attention_fragment_item_blank.setVisibility(View.GONE);
		
					}
				} else {
					// holdView.attention_fragment_item_votesNumText.setText("打气");
					holdView.attention_fragment_item_votesUsersLayout
					.setVisibility(View.GONE);
					holdView.attention_fragment_item_blank.setVisibility(View.GONE);
					if (isCommentVisity) {
						holdView.attention_fragment_item_lineImage
						.setVisibility(View.GONE);
						holdView.attention_fragment_item_CVMLLayout
						.setVisibility(View.GONE);
						holdView.attention_fragment_item_NumCVLayout
						.setVisibility(View.GONE);
					} else {
						holdView.attention_fragment_item_lineImage
						.setVisibility(View.VISIBLE);
						holdView.attention_fragment_item_CVMLLayout
						.setVisibility(View.VISIBLE);
						holdView.attention_fragment_item_NumCVLayout
						.setVisibility(View.VISIBLE);
					}
				}

		// if(isHaveImage){
		// holdView.attention_fragment_item_imagebottomblank.setVisibility(View.VISIBLE);
		// }else{
		// holdView.attention_fragment_item_imagebottomblank.setVisibility(View.GONE);
		// }图片下的Layout
		if (isHaveImage) {
			holdView.attention_fragment_item_clockContent_BottomBlackLayout
			.setVisibility(View.VISIBLE);
		} else {
			holdView.attention_fragment_item_clockContent_BottomBlackLayout
			.setVisibility(View.GONE);
		}
		if (!isHaveImage && !isHaveSingnature) {
			holdView.attention_fragment_item_clockContent.setText("打卡1次");
			holdView.attention_fragment_item_clockContent
			.setVisibility(View.VISIBLE);
		}

		// holdView.attention_fragment_item_CVMLLayout.setVisibility(View.GONE);//隐藏点赞和评论
		holdView.attention_fragment_item_clockLevelLayout
		.setBackgroundResource(R.drawable.newest_fragment_item_center_shape);
		if (isHaveImage || isHaveQuantity || isHaveSingnature) {
			holdView.attention_fragment_item_IQSLayout
			.setVisibility(View.VISIBLE);
			// if(level.equals("0")){
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_5));
			// }else if(level.equals("1")){
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_4));
			// }else{
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundColor(context.getResources().getColor(R.color.meibao_color_3));
			// }

		} else {
			// if(level.equals("0")){
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_level1_shape);
			// }else if(level.equals("1")){
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_level2_shape);
			// }else{
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_level3_shape);
			// }
			// holdView.attention_fragment_item_clockLevelLayout.setBackgroundResource(R.drawable.building_levelall_shape);
		}
		holdView.attention_fragment_item_name
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// GroupMainFragmentActivity.isReflush =
				// true;//让主activity 不刷新fragment
				if (userID.equals(itemBean.getUserId())) {
					Intent in = new Intent(context,
							ClockMyActivity.class);
					context.startActivity(in);
				} else {
					Intent in = new Intent(context,
							ClockOtherUserActivity.class);
					in.putExtra("userid", itemBean.getUserId());
					context.startActivity(in);
				}
			}
		});
		holdView.attention_fragment_item_clockLevelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent in = new Intent(context, ClockGroupInfoActivity.class);
//				in.putExtra("buildingId", itemBean.getBuildingId());
//				context.startActivity(in);
				Intent in = new Intent(context, TaskInfoActivity.class);
				in.putExtra("taskid", itemBean.getTaskId());
				in.putExtra("userid", itemBean.getUserId());
				context.startActivity(in);
			}
		});
		holdView.attention_fragment_item_IQSLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent in = new Intent(context, TaskInfoActivity.class);s
//				in.putExtra("taskid", itemBean.getTaskId());
//				in.putExtra("userid", itemBean.getUserId());
//				context.startActivity(in);
				Intent in = new Intent(context, ClockGroupInfoFragmentActivity.class);
				in.putExtra("buildingId", itemBean.getBuildingId());
				context.startActivity(in);
			}
		});
		holdView.attention_fragment_item_votesUsersContent
		.setOnClickListener(new OnClickListener() {
			// 点赞详情
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 0);//默认选中   0打气，1评论
				in.putExtra("jobid", itemBean.getJobId());
				in.putExtra("taskId", itemBean.getTaskId());
				 in.putExtra("createUserID", itemBean.getUserId());
				context.startActivity(in);
			}
		});
		holdView.attention_fragment_item_commentsContentText1
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent in = new Intent(context,
//						ClockCommentActivity.class);
//				in.putExtra("jobid", itemBean.getJobId());
//				in.putExtra("createUserID", itemBean.getUserId());
//				in.putExtra("taskId", itemBean.getTaskId());
//				context.startActivity(in);
				buildingOnClick.clickComment(itemBean, position-1, 0);
			}
		});
		holdView.attention_fragment_item_commentsContentText2
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent in = new Intent(context,
//						ClockCommentActivity.class);
//				in.putExtra("jobid", itemBean.getJobId());
//				in.putExtra("createUserID", itemBean.getUserId());
//				context.startActivity(in);
				buildingOnClick.clickComment(itemBean, position-1, 1);
			}
		});
		holdView.attention_fragment_item_commentsContentMore
		.setOnClickListener(new OnClickListener() {
			// 评论详情
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 1);//默认选中   0打气，1评论
				in.putExtra("jobid", itemBean.getJobId());
				in.putExtra("createUserID", itemBean.getUserId());
				in.putExtra("taskId", itemBean.getTaskId());
				context.startActivity(in);
			}
		});
		holdView.attention_fragment_item_votesNumLayout
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final int[] location = new int[2];   
				holdView.attention_fragment_item_votesNumImage.getLocationOnScreen(location);
				buildingOnClick.clickVote(itemBean.getJobId(),
						position-1, itemBean.getVoted(), location[0], location[1]);
			}
		});
		holdView.attention_fragment_item_commentsNumLayout
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buildingOnClick.clickComment(itemBean,position-1,-1);
			}
		});
	}

	public class HoldView {
		// public ImageView newUserHead,
		// new_chat_photo1,new_chat_photo2,new_chat_photo3;
		// public TextView newUserName, newUserAge, newUserLevel,
		// newUserDistance,
		// new_chat_text, newGroupName, newGroupPeopleNum;
		TextView attention_fragment_item_name,
		attention_fragment_item_currentStreak,
		attention_fragment_item_clockName,
		attention_fragment_item_clockContent,
		attention_fragment_item_textStick;// 上部分的id

		TextView attention_fragment_item_TimeText,
		attention_fragment_item_votesNumText,
		attention_fragment_item_commentsNumText;// 计数的id

		TextView attention_fragment_item_votesUsersContent;// 点赞的人的id

		TextView attention_fragment_item_commentsContentUserText1,
		attention_fragment_item_commentsContentText1;// 评论1
		TextView attention_fragment_item_commentsContentUserText2,
		attention_fragment_item_commentsContentText2;// 评论2
		TextView attention_fragment_item_commentsContentMore,
		attention_fragment_item_clockQuantity;

		ImageView attention_fragment_item_headImage,
		attention_fragment_item_ImageLayout_Image1,
		attention_fragment_item_ImageLayout_Image2,
		newset_fragment_item_ImageLayout_only1,
		attention_fragment_item_ImageLayout_Image3,
		attention_fragment_item_commentsContentUserbeforeimage2,
		attention_fragment_item_commentsContentUserbeforeimage1,
		attention_fragment_item_votesNumImage,
		attention_fragment_item_lineImage,
		attention_fragment_item_yisi,
		attention_fragment_item_medalImage,
		attention_fragment_item_imageStick;
		LinearLayout attention_fragment_item_ImageLayout,
		attention_fragment_item_commentsContentLayout1,
		attention_fragment_item_commentsContentLayout2,
		attention_fragment_item_votesNumLayout,
		attention_fragment_item_commentsNumLayout,
		attention_fragment_item_clockContentLayout,
		attention_fragment_item_blank,
		attention_fragment_item_blankTopLayout,
		attention_fragment_item_blankTopLayout1,
		attention_fragment_item_dayLayout;
		RelativeLayout attention_fragment_item_votesUsersLayout,
		attention_fragment_item_IQSLayout,
		attention_fragment_item_imagebottomblank,
		attention_fragment_item_CVMLLayout,
		attention_fragment_item_clockQuantityLayout,
		attention_fragment_item_clockContent_BottomBlackLayout,
		attention_fragment_item_topLayout,
		attention_fragment_item_clockLevelLayout,
		attention_fragment_item, attention_fragment_item_NumCVLayout,
		attention_fragment_item_StickLayout;

		public HoldView(View convertView) {
			attention_fragment_item_name = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_name);
			attention_fragment_item_currentStreak = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_currentStreak);
			attention_fragment_item_clockName = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_clockName);
			attention_fragment_item_clockContent = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_clockContent);

			attention_fragment_item_TimeText = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_TimeText);
			attention_fragment_item_votesNumText = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_votesNumText);
			attention_fragment_item_commentsNumText = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_commentsNumText);

			attention_fragment_item_votesUsersContent = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_votesUsersContent);

			attention_fragment_item_commentsContentUserText1 = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentUserText1);
			attention_fragment_item_commentsContentText1 = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentText1);
			attention_fragment_item_commentsContentUserText2 = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentUserText2);
			attention_fragment_item_commentsContentText2 = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentText2);

			attention_fragment_item_commentsContentMore = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentMore);
			attention_fragment_item_clockQuantity = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_clockQuantity);
			
			attention_fragment_item_textStick = (TextView) convertView
					.findViewById(R.id.attention_fragment_item_textStick);

			attention_fragment_item_headImage = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_headImage);

			newset_fragment_item_ImageLayout_only1 = (ImageView) convertView
					.findViewById(R.id.newset_fragment_item_ImageLayout_only1);
			attention_fragment_item_ImageLayout_Image1 = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_ImageLayout_Image1);
			attention_fragment_item_ImageLayout_Image2 = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_ImageLayout_Image2);
			attention_fragment_item_ImageLayout_Image3 = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_ImageLayout_Image3);
			attention_fragment_item_commentsContentUserbeforeimage1 = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentUserbeforeimage1);
			attention_fragment_item_commentsContentUserbeforeimage2 = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentUserbeforeimage2);
			attention_fragment_item_votesNumImage = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_votesNumImage);
			attention_fragment_item_lineImage = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_lineImage);
			attention_fragment_item_yisi = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_yisi);
			attention_fragment_item_medalImage = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_medalImage);
			attention_fragment_item_imageStick = (ImageView) convertView
					.findViewById(R.id.attention_fragment_item_imageStick);

			attention_fragment_item_ImageLayout = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_ImageLayout);
			attention_fragment_item_commentsContentLayout1 = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentLayout1);
			attention_fragment_item_commentsContentLayout2 = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_commentsContentLayout2);
			attention_fragment_item_votesNumLayout = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_votesNumLayout);
			attention_fragment_item_commentsNumLayout = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_commentsNumLayout);
			attention_fragment_item_clockContentLayout = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_clockContentLayout);
			attention_fragment_item_blank = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_blank);
			attention_fragment_item_blankTopLayout = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_blankTopLayout);
			attention_fragment_item_blankTopLayout1 = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_blankTopLayout1);
			attention_fragment_item_dayLayout = (LinearLayout) convertView
					.findViewById(R.id.attention_fragment_item_dayLayout);

			attention_fragment_item_votesUsersLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_votesUsersLayout);
			attention_fragment_item_IQSLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_IQSLayout);
			attention_fragment_item_imagebottomblank = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_imagebottomblank);
			attention_fragment_item_CVMLLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_CVMLLayout);
			attention_fragment_item_clockQuantityLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_clockQuantityLayout);
			attention_fragment_item_clockContent_BottomBlackLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_clockContent_BottomBlackLayout);
			attention_fragment_item_topLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_topLayout);
			attention_fragment_item_clockLevelLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_clockLevelLayout);
			attention_fragment_item = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item);
			attention_fragment_item_NumCVLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_NumCVLayout);
			
			attention_fragment_item_StickLayout = (RelativeLayout) convertView
					.findViewById(R.id.attention_fragment_item_StickLayout);
			// newUserHead = (ImageView) convertView
			// .findViewById(R.id.newUserHead);
			// new_chat_photo1 = (ImageView) convertView
			// .findViewById(R.id.new_chat_photo1);
			// new_chat_photo2 = (ImageView) convertView
			// .findViewById(R.id.new_chat_photo2);
			// new_chat_photo3 = (ImageView) convertView
			// .findViewById(R.id.new_chat_photo3);
			//
			// newUserName = (TextView)
			// convertView.findViewById(R.id.newUserName);
			// newUserAge = (TextView)
			// convertView.findViewById(R.id.newUserAge);
			// newUserLevel = (TextView) convertView
			// .findViewById(R.id.newUserLevel);
			// newUserDistance = (TextView) convertView
			// .findViewById(R.id.newUserDistance);
			// new_chat_text = (TextView) convertView
			// .findViewById(R.id.new_chat_text);
			// newGroupName = (TextView) convertView
			// .findViewById(R.id.newGroupName);
			// newGroupPeopleNum = (TextView) convertView
			// .findViewById(R.id.newGroupPeopleNum);
		}
	}

	public interface BuildingOnClick {
		/**
		 * 点击评论
		 * */
		public void clickComment(TaskItemBean taskItemBean, int position,
				int itemBeanIndex);

		/**
		 * 点赞
		 */
		public void clickVote(String jobid, int position, String type, int x, int y);
	}
	/**
	 * 是否只看图, 1是0不是
	 */
	public interface BuildingIsHaveImage{
		public void clickType(int type);
	}

	//	public interface OnNewFragmentItemClick {
	//		public void itemClick(int position);
	//	}

	private void imageBrower(int position, String[] urls, String[] imageSource) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_SOURCE, imageSource);

		context.startActivity(intent);
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

}
