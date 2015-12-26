package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskItemBean implements Serializable{
	/** taskId */
	private String taskId;
	/** JobId */
	private String jobId;
	/** 是否是放弃打卡, 1为是 0为不是 */
	private String give_up_flag;
	/** 放弃理由 */
	private String give_up_reason;
	/** 图片数量 */
	private String picCount;
	/** 数值 */
	private String quantity;
	/** 所有图片的路径, 以","分隔 */
	private String allPath;
	/** 打卡时间, HH-MM */
	private String time;
	/** 打卡日期, YYYY-MM-DD */
	private String date;
	/** 签名 */
	private String signature;
	/** 点赞列表(会返回最新5条)*/
	private ArrayList<VotesBean> votes;
	/** //评论数*/
	private int commentCount;
	/**当前用户是否赞过, 1为是, 0为否 */
	private String voted;
	/** 点赞数*/
	private int voteCount;
	/** 卡所属Task的标题*/
	private String title; 
	/**打卡时间 */
	private long check_time;
	/**打卡时间 */
	private long checkTime;
	/** //评论列表(会返回最新2条)*/
	private ArrayList<CommentsBean> comments;
	/** 所有图片的路径, 以","分隔   （building 用到的字段）*/
	private String allPicPath;
	/** 头像  */
	private String avartar_path;
	/** 名字*/
	private String name;
	/** 联网是否成功*/
	private int isConnSucces;

	/** 累计打卡天数 */
	private String totalDateCount;
	/** buildingId */
	private String buildingId;
	/** userId */
	private String userId;
	/** 防止多次点赞数据反映不过来*/
	private boolean ClickMore = true;
	/** */
	private String privateFlag;
	
	/** //勋章图片地址, 若无勋章则为null  */
	private String badgePath;
	/** 勋章来源, 1为123服务器, 2为七牛 */
	private int badgeSource;
	
	private int imageSource;

	private String allSource;
	
	/**此用户与当前用户的关系, -1为此用户是当前用户自己, 0为无关系,1为此用户是当前用户的粉丝, 2为当前用户是此用户的粉丝, 3为两人互相关注*/
	
	private int relation;
	
	private int avatar_source;
	/** 正在坚持人数 */
	private int taskCount;
	/** 当前等级进度, 0-6 */
	private int progress;
	/** 打卡单位, 若为"0"则没有单位 */
	private String unit;
	/**  stickFlag 1置顶   0不置顶*/
	private int stickFlag;
	/** 置顶理由, 只有置顶的项有此字段 */
	private String stickJobPraise;
	
	

	/** city如果没有就是0 */
	private String city;

	
	
	public String getStickJobPraise() {
		return stickJobPraise;
	}

	public void setStickJobPraise(String stickJobPraise) {
		this.stickJobPraise = stickJobPraise;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getStickFlag() {
		return stickFlag;
	}


	public void setStickFlag(int stickFlag) {
		this.stickFlag = stickFlag;
	}


	public int getProgress() {
		return progress;
	}


	public void setProgress(int progress) {
		this.progress = progress;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public int getTaskCount() {
		return taskCount;
	}


	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}


	public int getAvatar_source() {
		return avatar_source;
	}


	public void setAvatar_source(int avatar_source) {
		this.avatar_source = avatar_source;
	}


	public int getRelation() {
		return relation;
	}


	public void setRelation(int relation) {
		this.relation = relation;
	}


	public String getAllSource() {
		return allSource;
	}


	public void setAllSource(String allSource) {
		this.allSource = allSource;
	}


	public int getImageSource() {
		return imageSource;
	}


	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	
	


	
	@Override
	public String toString() {
		return "TaskItemBean [taskId=" + taskId + ", jobId=" + jobId
				+ ", give_up_flag=" + give_up_flag + ", give_up_reason="
				+ give_up_reason + ", picCount=" + picCount + ", quantity="
				+ quantity + ", allPath=" + allPath + ", time=" + time
				+ ", date=" + date + ", signature=" + signature + ", votes="
				+ votes + ", commentCount=" + commentCount + ", voted=" + voted
				+ ", voteCount=" + voteCount + ", title=" + title
				+ ", check_time=" + check_time + ", checkTime=" + checkTime
				+ ", comments=" + comments + ", allPicPath=" + allPicPath
				+ ", avartar_path=" + avartar_path + ", name=" + name
				+ ", isConnSucces=" + isConnSucces + ", totalDateCount="
				+ totalDateCount + ", buildingId=" + buildingId + ", userId="
				+ userId + ", ClickMore=" + ClickMore + ", privateFlag="
				+ privateFlag + ", badgePath=" + badgePath + ", badgeSource="
				+ badgeSource + "]";
	}



	public String getBadgePath() {
		return badgePath;
	}



	public void setBadgePath(String badgePath) {
		this.badgePath = badgePath;
	}



	public int getBadgeSource() {
		return badgeSource;
	}



	public void setBadgeSource(int badgeSource) {
		this.badgeSource = badgeSource;
	}



	public String getPrivateFlag() {
		return privateFlag;
	}


	public void setPrivateFlag(String privateFlag) {
		this.privateFlag = privateFlag;
	}


	public boolean getClickMore() {
		return ClickMore;
	}


	public void setClickMore(boolean clickMore) {
		ClickMore = clickMore;
	}


	public long getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getTotalDateCount() {
		return totalDateCount;
	}
	public void setTotalDateCount(String currentStreak) {
		this.totalDateCount = currentStreak;
	}
	public int getIsConnSucces() {
		return isConnSucces;
	}
	public void setIsConnSucces(int isConnSucces) {
		this.isConnSucces = isConnSucces;
	}
	public TaskItemBean() {
		super();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTitle() {
		return title.trim();
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getAllPicPath() {
		return allPicPath;
	}


	public void setAllPicPath(String allPicPath) {
		this.allPicPath = allPicPath;
	}


	public String getAvartar_path() {
		return avartar_path;
	}


	public void setAvartar_path(String avartar_path) {
		this.avartar_path = avartar_path;
	}


	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getGive_up_flag() {
		return give_up_flag;
	}
	public void setGive_up_flag(String give_up_flag) {
		this.give_up_flag = give_up_flag;
	}
	public String getGive_up_reason() {
		return give_up_reason;
	}
	public void setGive_up_reason(String give_up_reason) {
		this.give_up_reason = give_up_reason;
	}
	public String getPicCount() {
		return picCount;
	}
	public void setPicCount(String picCount) {
		this.picCount = picCount;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAllPath() {
		return allPath;
	}
	public void setAllPath(String allPath) {
		this.allPath = allPath;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public ArrayList<VotesBean> getVotes() {
		return votes;
	}
	public void setVotes(ArrayList<VotesBean> votes) {
		this.votes = votes;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getVoted() {
		return voted;
	}
	public void setVoted(String voted) {
		this.voted = voted;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public long getCheck_time() {
		return check_time;
	}
	public void setCheck_time(long check_time) {
		this.check_time = check_time;
	}
	public ArrayList<CommentsBean> getComments() {
		return comments;
	}
	public void setComments(ArrayList<CommentsBean> comments) {
		this.comments = comments;
	}
}
