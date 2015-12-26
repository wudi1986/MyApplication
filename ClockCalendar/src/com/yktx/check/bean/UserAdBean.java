package com.yktx.check.bean;

import java.io.Serializable;

public class UserAdBean implements Serializable{
	private String title;
	private String link;
	private String imagePath;
	private String text;
	
	
	public UserAdBean() {
		super();
	}
	public UserAdBean(String title, String link, String imagePath, String text) {
		super();
		this.title = title;
		this.link = link;
		this.imagePath = imagePath;
		this.text = text;
	}
	@Override
	public String toString() {
		return "UserAdBean [title=" + title + ", link=" + link + ", imagePath="
				+ imagePath + ", text=" + text + "]";
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
	

}
