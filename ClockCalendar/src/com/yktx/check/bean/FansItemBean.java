package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FansItemBean implements Serializable{
	/**  //最后一次打卡的taskId*/
	private String taskId;
	/**此用户积分 */
	private String point;
	
	/** 此用户头像路径*/
	private String avartarPath;
	/** 最后一次打卡的卡标题*/
	private String title;
	/** 头像来源, 1为123服务器, 2为七牛*/
	private int imageSource;
	/**用户Id */
	private String userId;
	/** 此用户与当前用户的关系, -1为此用户是当前用户自己, 0为无关系,1为此用户是当前用户的粉丝, 2为当前用户是此用户的粉丝, 3为两人互相关注*/
	private int relation;
	/** 用户名*/
	private String userName;
	/** 最后一次打卡时间*/
	private long checkTime;
	/** 最后一次打卡的buildingId*/
	private String buildingId;
	/** 累计打卡次数*/
	private String jobCount;
	/** //勋章图片地址, 若无勋章则为null  */
	private String badgePath;
	/** 勋章来源, 1为123服务器, 2为七牛 */
	private String badgeSource;
	/** 勋章的宽度 动态获取  */
	private int medalwidth = 0;
	/** //此用户所有勋章  */ 
	private ArrayList<MedalBean> badges = new ArrayList<MedalBean>();
	/** 当前等级进度, 0-6  */ 
	private int progress;
	
	
	
	@Override
	public String toString() {
		return "FansItemBean [taskId=" + taskId + ", point=" + point
				+ ", avartarPath=" + avartarPath + ", title=" + title
				+ ", imageSource=" + imageSource + ", userId=" + userId
				+ ", relation=" + relation + ", userName=" + userName
				+ ", checkTime=" + checkTime + ", buildingId=" + buildingId
				+ ", jobCount=" + jobCount + ", badgePath=" + badgePath
				+ ", badgeSource=" + badgeSource + "]";
	}
	public FansItemBean() {
		super();
	}
	public FansItemBean(String taskId, String point, String avartarPath,
			String title, int imageSource, String userId, int relation,
			String userName, long checkTime, String buildingId) {
		super();
		this.taskId = taskId;
		this.point = point;
		this.avartarPath = avartarPath;
		this.title = title;
		this.imageSource = imageSource;
		this.userId = userId;
		this.relation = relation;
		this.userName = userName;
		this.checkTime = checkTime;
		this.buildingId = buildingId;
	}
	
	
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public ArrayList<MedalBean> getBadges() {
		return badges;
	}
	public void setBadges(ArrayList<MedalBean> badges) {
		this.badges = badges;
	}
	public int getMedalwidth() {
		return medalwidth;
	}
	public void setMedalwidth(int medalwidth) {
		this.medalwidth = medalwidth;
	}
	public String getBadgePath() {
		return badgePath;
	}
	public void setBadgePath(String badgePath) {
		this.badgePath = badgePath;
	}
	public String getBadgeSource() {
		return badgeSource;
	}
	public void setBadgeSource(String badgeSource) {
		this.badgeSource = badgeSource;
	}
	public String getJobCount() {
		return jobCount;
	}
	public void setJobCount(String jobCount) {
		this.jobCount = jobCount;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getAvartarPath() {
		return avartarPath;
	}
	public void setAvartarPath(String avartarPath) {
		this.avartarPath = avartarPath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getImageSource() {
		return imageSource;
	}
	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	
}
