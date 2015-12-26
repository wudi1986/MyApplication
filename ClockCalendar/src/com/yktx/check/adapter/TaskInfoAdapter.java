package com.yktx.check.adapter;

import java.util.ArrayList;

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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yktx.check.ClockTaskDynamicActivity;
import com.yktx.check.ImagePagerActivity;
import com.yktx.check.R;
import com.yktx.check.bean.CommentsBean;
import com.yktx.check.bean.TaskItemBean;
import com.yktx.check.bean.VotesBean;
import com.yktx.check.util.TimeUtil;
import com.yktx.check.util.Tools;
import com.yktx.check.view.ChartMeteringView;

public class TaskInfoAdapter extends BaseAdapter{
	private Context mContext;
	private Activity mActivity;
	private LayoutInflater inflater;
	ArrayList<TaskItemBean> itemBeans = new ArrayList<TaskItemBean>(10);
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.zw_touxiang)
	.showImageForEmptyUri(R.drawable.zw_touxiang)
	.showImageOnFail(R.drawable.zw_touxiang)
	.showImageOnLoading(null)
	.showImageForEmptyUri(null).showImageOnFail(null)
	.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
	//	.displayer(new RoundedBitmapDisplayer(400))
	.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	//	.displayer(new RoundedBitmapDisplayer(4))
	.build();	
	ChartMeteringView chartMeteringView ;
	giveUpFlagClick upFlagClick;
	public void setGiveUpFlagClick(giveUpFlagClick flagClick){
		upFlagClick = flagClick;
	} 
	public TaskInfoAdapter(Activity mContext) {
		super();
		this.mContext = mContext;
		mActivity = mContext;
		inflater = LayoutInflater.from(mContext);
		chartMeteringView = new ChartMeteringView(mActivity);
	}
	TaskInfoOnClick taskInfoOnClick;
	public void setTaskInfoOnClick(TaskInfoOnClick taskInfoOnClick){
		this.taskInfoOnClick = taskInfoOnClick;
	}
	public void setList(ArrayList<TaskItemBean> bean){
		itemBeans = bean;
		Tools.getLog(Tools.i, "aaa", "=============size============="+bean.size());
		chartMeteringView.isHaveBottomList(bean.size());
	} 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemBeans.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		// TODO Auto-generated method stub
		Tools.getLog(Tools.i, "aaa", "psotion:"+position);
//		if(position == 0){
//			return chartMeteringView.getBestView(is);
//		}

		ViewHolder holder;
		convertView = inflater.inflate(R.layout.tast_info_listview_item, null);
		holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		ShowView(holder,position);
		return convertView;
	}


	public void ShowView(ViewHolder holdView,final int position){
		boolean isblankVisity;
		SpannableString msp = null;
		final String [] imagePathArray1;
		final String [] imagePathArray2;
		final String [] imagePathArray3;
		final String [] getAllSource;
		boolean isHaveSingnature,isHaveImage,isHaveQuantity;
		final TaskItemBean taskItemBean = itemBeans.get(position-1);
		String ClockContent = taskItemBean.getSignature();
		String ClockContentNum = taskItemBean.getQuantity();
		String Give_up_flag = taskItemBean.getGive_up_flag();
		String Give_up_reason = taskItemBean.getGive_up_reason();
		//		if(ClockContent !=null && ClockContent.length() != 0){
		//			holder.taskItem_Content.setText(ToDBC(ClockContent));
		//		}else{
		//			holder.taskItem_Content.setText("他还没有签名！");
		//		}
		if(Give_up_flag.equals("0")){
			if(ClockContent!=null && ClockContent.length() != 0){
				isHaveSingnature = true;
				holdView.taskItem_Content.setText(ToDBC(ClockContent));
				holdView.taskItem_Content.setVisibility(View.VISIBLE);
			}else{
				isHaveSingnature = false;
				holdView.taskItem_Content.setVisibility(View.GONE);
			}
		}else{
			if(Give_up_reason!=null){
				holdView.taskItem_Content.setText(ToDBC(Give_up_reason));
				holdView.taskItem_Content.setVisibility(View.VISIBLE);
			}else{
				holdView.taskItem_Content.setVisibility(View.GONE);
			}
		}


		if(ClockContentNum !=null && ClockContentNum.length() != 0){
			isHaveQuantity = true;
			String unit = taskItemBean.getUnit();
			if(unit.equals("0")){
				unit = "";
			}
			holdView.taskItem_ContentNum.setText(ClockContentNum+unit);
			holdView.taskItem_ContentNum.setVisibility(View.VISIBLE);
		}else{
			isHaveQuantity = false;
			holdView.taskItem_ContentNum.setVisibility(View.GONE);
		}

		Tools.getLog(Tools.d, "aaa","taskItemBean.getSignature()=========="+taskItemBean.getSignature());
		Tools.getLog(Tools.d, "aaa","taskItemBean.getQuantity()=========="+taskItemBean.getQuantity());
		String allImagePath = taskItemBean.getAllPath();
		if(taskItemBean.getPicCount().equals("0")){
			holdView.taskItem_ImageLayout.setVisibility(View.GONE);
			isHaveImage = false;
		}else if(taskItemBean.getPicCount().equals("1")){
			isHaveImage = true;
			imagePathArray1 = new String[1];
			imagePathArray1[0] = allImagePath;
			getAllSource = new String[1];
			getAllSource[0] = taskItemBean.getAllSource();
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			imageLoader.displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[0]))+allImagePath+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/120x120":""), 
					holdView.taskItem_ImageLayout_Image1, options);
			holdView.taskItem_ImageLayout_Image1.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image2.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout_Image3.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray1,getAllSource);
				}
			});
		}else if(taskItemBean.getPicCount().equals("2")){
			isHaveImage = true;
			imagePathArray2 = allImagePath.split(",");
			getAllSource = taskItemBean.getAllSource().split(",");
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			imageLoader.displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[0]))+imagePathArray2[0]
					+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/120x120":""), 
					holdView.taskItem_ImageLayout_Image1, options);
			holdView.taskItem_ImageLayout_Image1.setVisibility(View.VISIBLE);
			imageLoader.displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[1]))+imagePathArray2[1]
					+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/120x120":""), 
					holdView.taskItem_ImageLayout_Image2, options);
			holdView.taskItem_ImageLayout_Image2.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image3.setVisibility(View.GONE);
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2,getAllSource);
				}
			});
			holdView.taskItem_ImageLayout_Image2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray2,getAllSource);
				}
			});
		}else{
			isHaveImage = true;
			imagePathArray3 = allImagePath.split(",");
			getAllSource = taskItemBean.getAllSource().split(",");
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			imageLoader.displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[0]))+imagePathArray3[0]
					+(Integer.parseInt(getAllSource[0]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image1, options);
			holdView.taskItem_ImageLayout_Image1.setVisibility(View.VISIBLE);
			imageLoader.displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[1]))
					+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/120x120":""),
					holdView.taskItem_ImageLayout_Image2, options);
			holdView.taskItem_ImageLayout_Image2.setVisibility(View.VISIBLE);
			imageLoader.displayImage(Tools.getImagePath(Integer.parseInt(getAllSource[2]))+imagePathArray3[2]
					+(Integer.parseInt(getAllSource[1]) == 2?"?imageMogr2/thumbnail/120x120":""), 
					holdView.taskItem_ImageLayout_Image3, options);
			holdView.taskItem_ImageLayout_Image3.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_ImageLayout_Image1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3,getAllSource);
				}
			});
			holdView.taskItem_ImageLayout_Image2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3,getAllSource);
				}
			});
			holdView.taskItem_ImageLayout_Image3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(0, imagePathArray3,getAllSource);
				}
			});
		}
		holdView.taskItem_TimeText.setText(TimeUtil.getTimes(taskItemBean.getCheck_time()));
		holdView.taskItem_votesNumText.setText(taskItemBean.getVoteCount()+"");
		holdView.taskItem_commentsNumText.setText(taskItemBean.getCommentCount()+"");


		ArrayList<VotesBean> votesBeans = taskItemBean.getVotes();
		int votesBeansSize = votesBeans.size();
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<votesBeansSize;i++){
			sb.append(votesBeans.get(i).getName());
			if(i != (votesBeansSize-1)){
				sb.append(",");
			}
		}
		if(taskItemBean.getVoteCount() > 5){
			sb.append("...");
		}
		holdView.taskItem_votesUsersContent.setText(sb.toString());
		ArrayList<CommentsBean> commentsBeans = taskItemBean.getComments();
		String username,text,time,contentText,repliedUserName;
		int usernameEnd,timeStart,timeEnd;
		String CommentCount = taskItemBean.getCommentCount()+"";
		boolean isCommentVisity;
		if(CommentCount.equals("0")){
			holdView.taskItem_commentsContentLayout1.setVisibility(View.GONE);
			//			holder.taskItem_commentsContentUserbeforeimage2.setVisibility(View.GONE);
			//			holder.taskItem_commentsContentText2.setVisibility(View.GONE);
			holdView.taskItem_commentsContentLayout2.setVisibility(View.GONE);
			isCommentVisity = true;
			isblankVisity = false;
		}else if(CommentCount.equals("1")){
			isCommentVisity = false;
			isblankVisity = true;
			username = commentsBeans.get(0).getName();
			repliedUserName = commentsBeans.get(0).getRepliedUserName()+"：";
			text = commentsBeans.get(0).getText();
			time = "   "+TimeUtil.getTimes(commentsBeans.get(0).getSendTime());
			contentText =username+"回复"+repliedUserName+text+time;
			usernameEnd = username.length();
			timeEnd = contentText.length();
			timeStart = timeEnd - time.length();
			msp = new SpannableString(ToDBC(contentText));
			msp.setSpan(new AbsoluteSizeSpan(14,true), 0, usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_10)),0, usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  

			msp.setSpan(new AbsoluteSizeSpan(14,true), usernameEnd, usernameEnd+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11)),usernameEnd, usernameEnd+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  
		
			msp.setSpan(new AbsoluteSizeSpan(14,true), usernameEnd+2, usernameEnd+2+repliedUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_10)),usernameEnd+2, usernameEnd+2+repliedUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  

			msp.setSpan(new AbsoluteSizeSpan(12,true), timeStart, timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11)),timeStart, timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  

			holdView.taskItem_commentsContentText1.setText(msp);
			holdView.taskItem_commentsContentText1.setMovementMethod(LinkMovementMethod.getInstance());  

			//			holdView.group_info_item_commentsContentUserbeforeimage2.setVisibility(View.GONE);
			//			holdView.group_info_item_commentsContentText2.setVisibility(View.GONE);
			holdView.taskItem_commentsContentLayout2.setVisibility(View.GONE);
		}else {
			//			holdView.group_info_item_commentsContentUserText1.setText(commentsBeans.get(0).getName());
			//			holdView.group_info_item_commentsContentUserText2.setText(commentsBeans.get(1).getName());
			//			holdView.group_info_item_commentsContentText1.setText(commentsBeans.get(0).getText() +"    "+
			//					TimeUtil.getTimes(Long.parseLong(commentsBeans.get(0).getSendTime())));
			//			holdView.group_info_item_commentsContentText2.setText(commentsBeans.get(1).getText() +"    "+
			//					TimeUtil.getTimes(Long.parseLong(commentsBeans.get(1).getSendTime())));
			isCommentVisity = false;
			isblankVisity = true;
			//第一条
			username = commentsBeans.get(0).getName();
			repliedUserName = commentsBeans.get(0).getRepliedUserName()+"：";
			text = commentsBeans.get(0).getText();
			time = "   "+TimeUtil.getTimes(commentsBeans.get(0).getSendTime());
			contentText =username+"回复"+repliedUserName+text+time;
			usernameEnd = username.length();
			timeEnd = contentText.length();
			timeStart = timeEnd - time.length();
			msp = new SpannableString(ToDBC(contentText));
			msp.setSpan(new AbsoluteSizeSpan(14,true), 0, usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_10)),0, usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  
			msp.setSpan(new AbsoluteSizeSpan(14,true), usernameEnd, usernameEnd+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11)),usernameEnd, usernameEnd+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  
		
			msp.setSpan(new AbsoluteSizeSpan(14,true), usernameEnd+2, usernameEnd+2+repliedUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_10)),usernameEnd+2, usernameEnd+2+repliedUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  

			msp.setSpan(new AbsoluteSizeSpan(12,true), timeStart, timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11)),timeStart, timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  

			holdView.taskItem_commentsContentText1.setText(msp);
			holdView.taskItem_commentsContentText1.setMovementMethod(LinkMovementMethod.getInstance());

			//第二条
			username = commentsBeans.get(1).getName();
			text = commentsBeans.get(1).getText();
			repliedUserName = commentsBeans.get(1).getRepliedUserName()+"：";
			time = "   "+TimeUtil.getTimes(commentsBeans.get(1).getSendTime());
			
			contentText =username+"回复"+repliedUserName+text+time;
			usernameEnd = username.length();
			timeEnd = contentText.length();
			timeStart = timeEnd - time.length();
			msp = new SpannableString(ToDBC(contentText));
			msp.setSpan(new AbsoluteSizeSpan(14,true), 0, usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_10)),0, usernameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  
			msp.setSpan(new AbsoluteSizeSpan(14,true), usernameEnd, usernameEnd+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11)),usernameEnd, usernameEnd+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  
		
			msp.setSpan(new AbsoluteSizeSpan(14,true), usernameEnd+2, usernameEnd+2+repliedUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_10)),usernameEnd+2, usernameEnd+2+repliedUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  

			msp.setSpan(new AbsoluteSizeSpan(12,true), timeStart, timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置字体前景色  
			msp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.meibao_color_11)),timeStart, timeEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色  
			//			holdView.newset_fragment_item_commentsContentUserText2.setText(commentsBeans.get(1).getName());
			//			holdView.newset_fragment_item_commentsContentText2.setText(commentsBeans.get(1).getText() +"    "+
			//					TimeUtil.getTimes(Long.parseLong(commentsBeans.get(1).getSendTime())));
			holdView.taskItem_commentsContentText2.setText(msp);
			holdView.taskItem_commentsContentText2.setMovementMethod(LinkMovementMethod.getInstance());
			holdView.taskItem_commentsContentLayout2.setVisibility(View.VISIBLE);
			holdView.taskItem_commentsContentLayout1.setVisibility(View.VISIBLE);
		}
		if(CommentCount!= null&&Integer.parseInt(CommentCount)>2){
			holdView.taskItem_commentsContentMore.setVisibility(View.VISIBLE);
		}else{
			holdView.taskItem_commentsContentMore.setVisibility(View.GONE);
		}
		String voted = taskItemBean.getVoted();
		if(voted.equals("0")){
			holdView.taskItem_votesNumImage.setImageResource(R.drawable.building_vote_image);
		}else{
			holdView.taskItem_votesNumImage.setImageResource(R.drawable.building_vote_selectimage);
		}
		if(isHaveImage){
			holdView.taskItem_ImageBlank.setVisibility(View.VISIBLE);
		}else{
			holdView.taskItem_ImageBlank.setVisibility(View.GONE);
		}
		if(!isHaveImage){
			holdView.taskItem_Content.setText("打卡1次");
			holdView.taskItem_Content.setVisibility(View.VISIBLE);
		}
		if(taskItemBean.getVoteCount() != 0){
			holdView.taskItem_votesUsersLayout.setVisibility(View.VISIBLE);
			holdView.taskItem_lineImage.setVisibility(View.VISIBLE);
			holdView.taskinfo_NumLayoutBottomBlank.setVisibility(View.VISIBLE);
			if(isblankVisity){
				holdView.taskItem_blank.setVisibility(View.VISIBLE);
			}else{
				holdView.taskItem_blank.setVisibility(View.GONE);
			}
		}else{
			if(isCommentVisity){
				holdView.taskinfo_NumLayoutBottomBlank.setVisibility(View.GONE);
				holdView.taskItem_lineImage.setVisibility(View.GONE);
			}else{
				holdView.taskinfo_NumLayoutBottomBlank.setVisibility(View.VISIBLE);
				holdView.taskItem_lineImage.setVisibility(View.VISIBLE);
			}
			holdView.taskItem_votesUsersLayout.setVisibility(View.GONE);
			holdView.taskItem_blank.setVisibility(View.GONE);
		}


		holdView.taskItem_votesUsersContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 0);//默认选中   0打气，1评论
				in.putExtra("taskId", taskItemBean.getTaskId());
				in.putExtra("jobid", taskItemBean.getJobId());
				 in.putExtra("createUserID", taskItemBean.getUserId());
				mContext.startActivity(in);
			}
		});
		holdView.taskItem_commentsContentMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(mContext, ClockTaskDynamicActivity.class);
				in.putExtra("CurrentItem", 1);//默认选中   0打气，1评论
				in.putExtra("jobid", taskItemBean.getJobId());
				in.putExtra("createUserID", taskItemBean.getUserId());
				in.putExtra("taskId", taskItemBean.getTaskId());
				mContext.startActivity(in);
			}
		});
		holdView.taskItem_votesNumLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(taskItemBean.getClickMore()){
					taskItemBean.setClickMore(false);
					taskInfoOnClick.clickVote(taskItemBean.getJobId(), position-1, taskItemBean.getVoted());
				}
			}
		});
		holdView.taskItem_commentsNumLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				taskInfoOnClick.clickComment(taskItemBean.getJobId(), position-1);
			}
		});
		holdView.taskItem_moreImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				upFlagClick.setGiveUp(position);
			}
		});

	}
	class ViewHolder{
		private TextView taskItem_Content,taskItem_ContentNum;
		private ImageView taskItem_ImageLayout_Image1,taskItem_ImageLayout_Image2,taskItem_ImageLayout_Image3,
		taskItem_votesNumImage,taskItem_commentsContentUserbeforeimage2,taskItem_lineImage,taskItem_moreImage;
		private TextView taskItem_TimeText,taskItem_votesNumText,taskItem_commentsNumText;
		private TextView taskItem_votesUsersContent,taskItem_commentsContentMore;
		private TextView taskItem_commentsContentText1,taskItem_commentsContentText2;
		private LinearLayout taskItem_commentsContentLayout1,taskItem_commentsContentLayout2,taskItem_ImageLayout,
		taskItem_votesNumLayout,taskItem_commentsNumLayout,taskItem_blank;

		private RelativeLayout taskItem_votesUsersLayout,taskinfo_NumLayoutBottomBlank,taskItem_ImageBlank;

		public ViewHolder(View v){
			taskItem_Content = (TextView) v.findViewById(R.id.taskItem_Content);
			taskItem_ContentNum = (TextView) v.findViewById(R.id.taskItem_ContentNum);
			taskItem_TimeText = (TextView) v.findViewById(R.id.taskItem_TimeText);
			taskItem_votesNumText = (TextView) v.findViewById(R.id.taskItem_votesNumText);
			taskItem_commentsNumText = (TextView) v.findViewById(R.id.taskItem_commentsNumText);
			taskItem_votesUsersContent = (TextView) v.findViewById(R.id.taskItem_votesUsersContent);
			taskItem_commentsContentText1 = (TextView) v.findViewById(R.id.taskItem_commentsContentText1);
			taskItem_commentsContentText2 = (TextView) v.findViewById(R.id.taskItem_commentsContentText2);

			taskItem_ImageLayout_Image1 = (ImageView) v.findViewById(R.id.taskItem_ImageLayout_Image1);
			taskItem_ImageLayout_Image2 = (ImageView) v.findViewById(R.id.taskItem_ImageLayout_Image2);
			taskItem_ImageLayout_Image3 = (ImageView) v.findViewById(R.id.taskItem_ImageLayout_Image3);
			taskItem_commentsContentUserbeforeimage2 = (ImageView) v.findViewById(R.id.taskItem_commentsContentUserbeforeimage2);
			taskItem_votesNumImage = (ImageView) v.findViewById(R.id.taskItem_votesNumImage);
			taskItem_lineImage = (ImageView) v.findViewById(R.id.taskItem_lineImage);
			taskItem_moreImage = (ImageView) v.findViewById(R.id.taskItem_moreImage);

			taskItem_commentsContentLayout1 = (LinearLayout) v.findViewById(R.id.taskItem_commentsContentLayout1);
			taskItem_commentsContentLayout2 = (LinearLayout) v.findViewById(R.id.taskItem_commentsContentLayout2);
			taskItem_ImageLayout = (LinearLayout) v.findViewById(R.id.taskItem_ImageLayout);
			taskItem_commentsContentMore = (TextView) v.findViewById(R.id.taskItem_commentsContentMore);
			taskItem_votesNumLayout = (LinearLayout) v.findViewById(R.id.taskItem_votesNumLayout);
			taskItem_commentsNumLayout = (LinearLayout) v.findViewById(R.id.taskItem_commentsNumLayout);
			taskItem_blank = (LinearLayout) v.findViewById(R.id.taskItem_blank);

			taskItem_votesUsersLayout = (RelativeLayout) v.findViewById(R.id.taskItem_votesUsersLayout);
			taskinfo_NumLayoutBottomBlank = (RelativeLayout) v.findViewById(R.id.taskinfo_NumLayoutBottomBlank);
			taskItem_ImageBlank = (RelativeLayout) v.findViewById(R.id.taskItem_ImageBlank);
			
		}
	}
	public interface TaskInfoOnClick{
		/**
		 * 点击评论
		 * */
		public void clickComment(String jobid,int position);
		/**
		 * 点赞
		 */
		public void clickVote(String jobid,int position,String type);
	}

	private void imageBrower(int position, String[] urls, String [] imageSource) {
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
	public interface giveUpFlagClick{
		public void setGiveUp(int position);
	}
}
