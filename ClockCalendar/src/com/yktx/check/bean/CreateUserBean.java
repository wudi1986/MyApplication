package com.yktx.check.bean;

import java.io.Serializable;

public class CreateUserBean implements Serializable {
	private int id;
	private String name;
	private int gender;
	private String pubId;
	/** //头像路径 */
	private String avartarPath;
	/** 积分 */
	private String point;
	private String fistTaskCdate;
	/** 粉丝数 */
	private String fansCount;
	/** 关注人数 */
	private String followingCount;
	/** 此用户与当前用户的关系, -1为此用户是当前用户自己, 0为无关系,1为此用户是当前用户的粉丝, 2为当前用户是此用户的粉丝, 3为两人互相关注 */
	private int relation;
	private int imageSource;
	/** 此用户共打卡次数 */
	private int totalJobCount;
	

	
	public int getTotalJobCount() {
		return totalJobCount;
	}

	public void setTotalJobCount(int totalJobCount) {
		this.totalJobCount = totalJobCount;
	}

	public int getImageSource() {
		return imageSource;
	}

	public void setImageSource(int imageSource) {
		this.imageSource = imageSource;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public String getFistTaskCdate() {
		return fistTaskCdate;
	}

	public void setFistTaskCdate(String fistTaskCdate) {
		this.fistTaskCdate = fistTaskCdate;
	}


	
	@Override
	public String toString() {
		return "CreateUserBean [id=" + id + ", name=" + name + ", gender="
				+ gender + ", pubId=" + pubId + ", avartarPath=" + avartarPath
				+ ", point=" + point + ", fistTaskCdate=" + fistTaskCdate
				+ ", fansCount=" + fansCount + ", followingCount="
				+ followingCount + "]";
	}

	public String getFansCount() {
		return fansCount;
	}

	public void setFansCount(String fansCount) {
		this.fansCount = fansCount;
	}

	public String getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(String followingCount) {
		this.followingCount = followingCount;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public CreateUserBean() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public String getAvartarPath() {
		return avartarPath;
	}

	public void setAvartarPath(String avartarPath) {
		this.avartarPath = avartarPath;
	}

}
