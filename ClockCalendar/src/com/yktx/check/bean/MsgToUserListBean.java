package com.yktx.check.bean;

import java.io.Serializable;

public class MsgToUserListBean implements Serializable{
	private String taskId;
	private String title;
	private String type;
	private String text;
	private String userId;
	private String msgId;
	private String userName;
	private String avatarPath;
	private String cTime;
	private String buildingId;
	private String jobId;
	private int imageSource;
	private String point;
	private String jobOwnerName;
	private String jobOwnerImageSource;
	private String jobOwnerAvartarPath;
	private String jobOwnerId;
	
	
	
	
	
	@Override
	public String toString() {
		return "MsgToUserListBean [taskId=" + taskId + ", title=" + title
				+ ", type=" + type + ", text=" + text + ", userId=" + userId
				+ ", msgId=" + msgId + ", userName=" + userName
				+ ", avatarPath=" + avatarPath + ", cTime=" + cTime
				+ ", buildingId=" + buildingId + ", jobId=" + jobId
				+ ", imageSource=" + imageSource + ", point=" + point
				+ ", jobOwnerName=" + jobOwnerName + ", jobOwnerImageSource="
				+ jobOwnerImageSource + ", jobOwnerAvartarPath="
				+ jobOwnerAvartarPath + ", jobOwnerId=" + jobOwnerId + "]";
	}


	public String getJobOwnerName() {
		return jobOwnerName;
	}


	public void setJobOwnerName(String jobOwnerName) {
		this.jobOwnerName = jobOwnerName;
	}


	public String getJobOwnerImageSource() {
		return jobOwnerImageSource;
	}


	public void setJobOwnerImageSource(String jobOwnerImageSource) {
		this.jobOwnerImageSource = jobOwnerImageSource;
	}


	public String getJobOwnerAvartarPath() {
		return jobOwnerAvartarPath;
	}


	public void setJobOwnerAvartarPath(String jobOwnerAvartarPath) {
		this.jobOwnerAvartarPath = jobOwnerAvartarPath;
	}


	public String getJobOwnerId() {
		return jobOwnerId;
	}


	public void setJobOwnerId(String jobOwnerId) {
		this.jobOwnerId = jobOwnerId;
	}


	public int getImageSource() {
		return imageSource;
	}


	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}


	public String getPoint() {
		return point;
	}


	public void setPoint(String point) {
		this.point = point;
	}


	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	public String getcTime() {
		return cTime;
	}
	public void setcTime(String cTime) {
		this.cTime = cTime;
	}

	
}
