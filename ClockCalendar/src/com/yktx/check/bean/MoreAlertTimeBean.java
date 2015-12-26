package com.yktx.check.bean;

import java.io.Serializable;

public class MoreAlertTimeBean implements Serializable{
	private String id;
	private String taskId;
	private String platform = "2";
	private String days = "1,2,3,4,5,6,7";
	private String time;
	private String ringtone = "1";
	private String vibrationFlag = "0";
	private String remark;
	private String status = "2";
	private String pickervisity = "1" ;//自己加的Timerpicker是否显示   1 为不显示 2 为显示
	private int u_id;//提醒的int id设置为闹钟会用
	private String title;
	
	
	

	@Override
	public String toString() {
		return "MoreAlertTimeBean [id=" + id + ", taskId=" + taskId
				+ ", platform=" + platform + ", days=" + days + ", time="
				+ time + ", ringtone=" + ringtone + ", vibrationFlag="
				+ vibrationFlag + ", remark=" + remark + ", status=" + status
				+ ", pickervisity=" + pickervisity + ", u_id=" + u_id
				+ ", title=" + title + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	public String getPickervisity() {
		return pickervisity;
	}
	public void setPickervisity(String pickervisity) {
		this.pickervisity = pickervisity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRingtone() {
		return ringtone;
	}
	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}
	public String getVibrationFlag() {
		return vibrationFlag;
	}
	public void setVibrationFlag(String vibrationFlag) {
		this.vibrationFlag = vibrationFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
