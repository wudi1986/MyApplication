package com.yktx.check.bean;

import java.io.Serializable;

public class GetCategoryBean implements Serializable {
	/** 类别Id */
	private String id;
	/** 类别名称 */
	private String title;
	/** 类别顺序 */
	private int ordinal;
	/** 创建时间 */
	private long cTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	public long getcTime() {
		return cTime;
	}
	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

}
