package com.yktx.check.bean;

import java.io.Serializable;

public class CommentsBean implements Serializable{
	private String commentId;
	/** 回复者Id */
	private String userId;
	/** 回复者用户名 */
	private String name;
	/** 回复者头像 */
	private String 	avartar_path;
	/** 被回复者Id */
	private String repliedUserId;
	/** 被回复者用户名 */
	private String repliedUserName;
	/** 被回复者头像 */
	private String repliedUserAvartar;
	
	private String text;
	/**被回复的回复Id, 若是对job的直接回复则为-1*/
	private String pCommentId;
	/** 回复类型, 1为对job的直接回复(不带@字符), 2为对回复的回复(带@字符) */
	private int commentType;
	
	/** 回复时间 */
	private long sendTime;

	private int imageSource;
	
	private int repliedUserImageSource;

	public int getImageSource() {
		return imageSource;
	}

	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}

	public int getRepliedUserImageSource() {
		return repliedUserImageSource;
	}

	public void setRepliedUserImageSource(int repliedUserImageSource) {
		this.repliedUserImageSource = repliedUserImageSource;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvartar_path() {
		return avartar_path;
	}

	public void setAvartar_path(String avartar_path) {
		this.avartar_path = avartar_path;
	}

	public String getRepliedUserId() {
		return repliedUserId;
	}

	public void setRepliedUserId(String repliedUserId) {
		this.repliedUserId = repliedUserId;
	}

	public String getRepliedUserName() {
		return repliedUserName;
	}

	public void setRepliedUserName(String repliedUserName) {
		this.repliedUserName = repliedUserName;
	}

	public String getRepliedUserAvartar() {
		return repliedUserAvartar;
	}

	public void setRepliedUserAvartar(String repliedUserAvartar) {
		this.repliedUserAvartar = repliedUserAvartar;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getpCommentId() {
		return pCommentId;
	}

	public void setpCommentId(String pCommentId) {
		this.pCommentId = pCommentId;
	}

	public int getCommentType() {
		return commentType;
	}

	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	
	
	
	
	
	
}
