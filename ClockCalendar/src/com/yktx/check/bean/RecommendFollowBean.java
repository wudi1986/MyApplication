package com.yktx.check.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class RecommendFollowBean implements Serializable{
	/** 头像路径 */
	private String avartarPath;
	/** 头像来源 */
	private int imageSource;
	/** 正在坚持的卡数 */
	private int taskCount;
	/** 用户名 */
	private String name;
	/** 用户Id */
	private String userId;
	/** 此用户与当前用户的关系, -1为此用户是当前用户自己, 0为无关系,1为此用户是当前用户的粉丝, 2为当前用户是此用户的粉丝, 3为两人互相关注*/
	private int relation = 0;
	/** /最后创建的三张卡 */
	private ArrayList<RecommendFollowItemBean> tasks = new ArrayList<RecommendFollowItemBean>();
	
	public RecommendFollowBean() {
		super();
	}
	public RecommendFollowBean(String avartarPath, int imageSource,
			int taskCount, String name, String userId,
			ArrayList<RecommendFollowItemBean> tasks) {
		super();
		this.avartarPath = avartarPath;
		this.imageSource = imageSource;
		this.taskCount = taskCount;
		this.name = name;
		this.userId = userId;
		this.tasks = tasks;
	}
	@Override
	public String toString() {
		return "RecommendFollowBean [avartarPath=" + avartarPath
				+ ", imageSource=" + imageSource + ", taskCount=" + taskCount
				+ ", name=" + name + ", userId=" + userId + ", tasks=" + tasks
				+ "]";
	}
	
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}
	public String getAvartarPath() {
		return avartarPath;
	}
	public void setAvartarPath(String avartarPath) {
		this.avartarPath = avartarPath;
	}
	public int getImageSource() {
		return imageSource;
	}
	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ArrayList<RecommendFollowItemBean> getTasks() {
		return tasks;
	}
	public void setTasks(ArrayList<RecommendFollowItemBean> tasks) {
		this.tasks = tasks;
	}
	
	

}
