package com.yktx.check.util;

import com.yktx.check.R;

public class Contanst {
	/** 网络判断值*/
	public static final int BEST_INFO_OK = 0;
	public static final int BEST_INFO_FAIL = 1;
	public static final int MINA_CONN_FAIL = 2;
	public static final int MINA_MESSAGE_RECEIVED = 3;
	public static final int CreateJobFail = 4;

	//	public static final int[] TASK_COLOR = {R.color.meibao_color_8, R.color.meibao_color_4, R.color.meibao_color_6};
	//	public static final int[] TASK_LIGHT_COLOR = {R.color.meibao_color_14, R.color.meibao_color_5, R.color.meibao_color_7};
	public static final int[] TASK_COLOR = {R.color.meibao_color_1, R.color.meibao_color_1, R.color.meibao_color_1};
	public static final int[] TASK_LIGHT_COLOR = {R.color.meibao_color_2, R.color.meibao_color_2, R.color.meibao_color_2};
	//置顶图片
	/** 环信聊天*/
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final String ACCOUNT_REMOVED = "account_removed";
	/** 网络请求的成功值 */
	public static final String HTTP_SUCCESS = "200";
	/** 显示调试信息 */
	public static final boolean isDebug = false;
	/** 当前网络是否断开 */
	public static boolean isConnStop = false;
	/** 勋章图片的宽度 */
	public static final int  MEDAL_IMAGE_WIDTH = 14;
	/** 勋章图片的高度*/
	public static final int  MEDAL_IMAGE_HEIGHT = 17;
	/** 获取用户的所有Task */
	public static final String HTTP_GETALL = "/task/getAll";
	/** 创建一个词条 */
	public static final String HTTP_CREATETASK = "/task/create";
	/** 更新一个词条 */
	public static final String HTTP_UPDATETASK = "/task/update";
	/** 查询用户在一个时间段内的任务执行状况 */
	public static final String HTTP_GETPERFORMANCE = "/task/getPerformance";
	//	/** 获取用户当前所有未暂停的Task, 并给出在一段时间内用户打过卡的所有日期 */
	//	public static final String HTTP_GETONGOING = "/task/getOngoing";
	/**  获取用户某一日的Task记录*/
	public static final String HTTP_GETBYDATE = "/task/getByDate";
	/** 上传无网环境下用户的每日表现 */
	public static final String HTTP_UPLOADPERFORMANCE = "/task/uploadPerformance";
	/** 根据类别获取推荐Building */
	public static final String HTTP_GETRECOMMEND = "/building/getRecommend";
	/** 获取Building里最后的三张打卡记录 */
	public static final String HTTP_GETLASTTHREE = "/building/getLastTwoJobs";
	/** 注册用户 */
	public static final String HTTP_CREATEUSER = "/user/create";
	/** 打一次卡 */
	public static final String HTTP_CREATEJOB = "/job/create";
	/** 取消打卡或取消放弃 */
	public static final String HTTP_UPDATEJOB = "/job/update";
	/** 获取一段时间内的统计数据 */
	public static final String HTTP_GETSTATISTIC = "/task/getStatistic";
	/** 分页查询用户在某一项任务下的打卡记录 */
	public static final String HTTP_GETBYTASKID = "/job/getByTaskId";
	/** 根据微博Id获取用户信息 */
	public static final String HTTP_GETBYPUBID = "/user/getByPubId";
	/** 上传图片 */
	public static final String HTTP_IMAGEUPLOAD = "/image/upload";
	/** 根据用户Id获取用户基本信息  */
	public static final String HTTP_GETBYIDUSER = "/user/getById";
	/** 根据Id获取详细信息 */
	public static final String HTTP_GETBYIDTASK = "/task/getById";
	/** 查询building的基本信息 */
	public static final String HTTP_GETBASICINFO = "/building/getBasicInfo";
	/** 分页获取某一building下的楼层及楼层内的部分评论及点赞信息 */
	public static final String HTTP_GETFLOOR = "/building/getFloor";
	/** 分页获取最新打卡 */
	public static final String HTTP_GETNEWEST = "/building/getNewest";
	/** 分页获取最热打卡 */
	public static final String HTTP_GETHOTTEST = "/building/getHottest";
	/** 根据关键字模糊查询building */
	public static final String HTTP_SEARCH = "/building/search";
	/** 创建一条评论, 返回评论后当前Job最新的评论(2条) */
	public static final String HTTP_CREATECOMMENT = "/message/createComment";
	/** 点赞一次, 返回点赞后当前Job最新的点赞(5条) */
	public static final String HTTP_CREATEVOTE = "/message/createVote";
	/** 分页获取所有评论 */
	public static final String HTTP_GETCOMMENT = "/message/getComment";
	/** 分页获取所有点赞 */
	public static final String HTTP_GETVOTE = "/message/getVote";
	/** 取消点赞 */
	public static final String HTTP_CANCELVOTE = "/message/cancelVote";
	/** 取消点赞 */
	public static final String HTTP_UPDATE_ORDER = "/task/updateOrder";
	/** 获取所有类别 */
	public static final String HTTP_GET_CATEGORY = "/building/getCategory";
	/** 分页获取个人动态 */
	public static final String HTTP_GET_MSGTOUSER = "/message/getTrends";

	/** 批量创建闹表 */
	public static final String HTTP_ALERT_CREATE = "/alarm/create";
	/** 批量更新闹表 */
	public static final String HTTP_ALERT_UPDATE = "/alarm/update";
	/** 批量更新闹表 */
	public static final String HTTP_GET_ALL_CHECKDATE = "/task/getAllCheckDate";

	/** 删除一条打卡记录 */
	public static final String HTTP_DELETEJOB = "/job/delete";
	/** 设置极光推送的RegistrationID */
	public static final String HTTP_UPDATEJPUSHID = "/user/updateJpushId";

	/** 设置极光推送的RegistrationID */
	public static final String HTTP_SINA_UPLOAD_PIC = "upload_pic.json";


	/** 设置极光推送的RegistrationID */
	public static final String HTTP_ADDERRORLOG = "/errorLog/create";
	/** 获取今日所有打卡的最后一张图片地址 */
	public static final String HTTP_GET_LAST_IMAGE_TODAY = "/job/getLastImageToday";
	/** 更新用户名 */
	public static final String HTTP_UPDATE_USERNAME = "/user/updateName";
	/** 获取某一Task下的所有闹表设置, 排序规则为开启的在前,关闭的在后, 再按创建时间排序 */
	public static final String HTTP_ALARM_GETBYRASKID = "/alarm/getByTaskId";
	/** 拼接共享字符串 */
	public static final String HTTP_TASK_SHARESTED = "/task/getShareStr";
	/** 删除task */
	public static final String HTTP_TASK_DELETE = "/task/delete";
	/** 获取某一用户的所有闹表设置, 排序规则为开启的在前,关闭的在后, 再按创建时间排序  */
	public static final String HTTP_ALARM_GETBYUSERID = "/alarm/getByUserId";
	/**每日系统自动打一次卡  */
	public static final String HTTP_JOB_DEFAULTCHECK = "/job/defaultCheck";
	/**  获取今日得分最高的五个用户  */
	public static final String HTTP_BUILDING_GETTOPPOINTUSER = "/building/getTopPointUser";
	/**   获取最新创建的10个building  */
	public static final String HTTP_BUILDING_GETLATESTBUILDING = "/building/getLatestBuilding";

	/**每日系统自动打一次卡  */
	public static final String HTTP_GET_QINIU_TOKEN = "/image/getQiniuToken";
	/**分页查询某用户的所有打卡记录, 并给出当前用户是否赞过  */
	public static final String HTTP_JOB_GETBYUSERID = "/job/getByUserId";
	/**获取指定卡所在Building里最新的两个打卡用户  */
	public static final String HTTP_BUILDING_GETLASTTWOUSERS = "/building/getLastTwoUsers";
	/**更新用户手机平台  */
	public static final String HTTP_USER_UPDATEPLATFORM = "/user/updatePlatform";
	/**关注  */
	public static final String HTTP_USER_FOLLOW = "/user/follow";
	/**取消关注  */
	public static final String HTTP_USER_UNFOLLOW = "/user/unfollow";
	/**获取粉丝列表  */
	public static final String HTTP_USER_GETFANS = "/user/getFans";
	/**获取关注列表  */
	public static final String HTTP_USER_GETFOLLOWING = "/user/getFollowing";
	/**获取广场关注列表  */
	public static final String HTTP_USER_GETBUILDINGFOLLOWING = "/user/getBuildingFollowing";
	/**获取Building用户列表  */
	public static final String HTTP_USER_GETUSERINBUILDING = "/user/getUserInBuilding";
	/**获取精品job, 10条 */
	public static final String HTTP_BUILDING_GETRECOMMENDJOB = "/building/getRecommendJob";
	/**获得用户勋章 */
	public static final String HTTP_USER_GETBADGE = "/user/getBadge";

	/**上传离线打卡记录 */
	public static final String HTTP_UPLOAD_OFFLINE = "/job/uploadOffline";
	/** 获取坚持天数最多的task, 10条 */
	public static final String HTTP_GET_MOST_DATES_TASK = "/building/getMostDatesTask";
	/** 浏览今日的卡 */
	public static final String HTTP_BUILDING_VIEWTODAYCARD = "/building/viewTodayCard";
	/** 获取广场关注列表 */
	public static final String HTTP_USER_GETFOLLOWINGJOB = "/user/getFollowingJob";
	/** 获取推荐关注 */
	public static final String HTTP_USER_GETRECOMMENDFOLLOW = "/user/getRecommendFollow";
	/** 获取所有单位 */
	public static final String HTTP_JOB_GETALLUNIT = "/job/getAllUnit";
	/** 提醒别人打卡 */
	public static final String HTTP_MESSAGE_REMIND = "/message/remind";
	/** 暂停打卡 */
	public static final String HTTP_TASK_SUSPEND = "/task/suspend";
	/** 获取广场关注列表 */
	public static final String HTTP_USER_SEARCH = "/user/search";
	/** 获取广告 */
	public static final String HTTP_USER_AD = "/user/getAd";

	public static final String HTTP_BAIDU_LOCATION = "http://api.map.baidu.com/geocoder?output=json&location=";

	/**  查询打卡相机中某一task下某一月份的卡情况 */
	public static final String HTTP_TASK_INMONTH= "/task/getInMonth";
	
	public static final String HTTP_TASK_GETIMAGE = "/task/getImage";


	public static final int GETALL = 1;
	public static final int CREATETASK = 2;
	public static final int UPDATETASK = 3;
	public static final int GETPERFORMANCE = 4;
	//	public static final int GETONGOING = 5;
	public static final int GETBYDATE = 6;
	public static final int UPLOADPERFORMANCE = 7;
	public static final int GETRECOMMEND = 8;
	public static final int GETLASTTHREE = 9;
	public static final int CREATEUSER = 10;
	public static final int CREATEJOB = 11;
	public static final int UPDATEJOB = 12;
	public static final int GETSTATISTIC = 13;
	public static final int GETBYTASKID = 14;
	public static final int GETBYPUBID = 15;
	public static final int IMAGEUPLOAD = 16;
	public static final int GETBYIDUSER = 17;
	public static final int GETBYIDTASK = 18;
	public static final int GETBASICINFO = 19;
	public static final int GETFLOOR = 20;
	public static final int GETNEWEST = 21;
	public static final int GETHOTTEST = 22;
	public static final int SEARCH = 23;
	public static final int CREATECOMMENT = 24;
	public static final int GETCOMMENT = 25;
	public static final int CREATEVOTE = 26;
	public static final int GETVOTE = 27;
	public static final int CANCELVOTE = 28;
	public static final int UPDATE_ORDER = 29;
	public static final int GET_CATEGORY = 30;
	public static final int GET_MSGTOUSER = 31;
	public static final int ALERT_CREATE = 32;
	public static final int ALERT_UPDATE = 33;
	public static final int GET_ALL_CHECKDATE = 34;
	public static final int DELETEJOB = 35;
	public static final int UPDATEJPUSHID = 36;
	public static final int SINA_UPLOAD_PIC = 37;
	public static final int ADDERRORLOG = 38;
	public static final int GET_LAST_IMAGE_TODAY = 39;
	public static final int UPDATE_USERNAME = 40;
	public static final int ALARM_GETBYRASKID = 41;
	public static final int TASK_DELETE = 42;

	public static final int TASK_SHARESTED = 43;
	public static final int ALARM_GETBYUSERID = 44;
	public static final int JOB_DEFAULTCHECK = 45;
	public static final int BUILDING_GETTOPPOINTUSER = 46;
	public static final int BUILDING_GETLATESTBUILDING = 47;
	public static final int GET_QINIU_TOKEN = 48;
	public static final int JOB_GETBYUSERID = 49;
	public static final int BUILDING_GETLASTTWOUSERS = 50;
	public static final int USER_UPDATEPLATFORM = 51;
	public static final int USER_FOLLOW = 52;
	public static final int USER_UNFOLLOW = 53;
	public static final int USER_GETFANS = 54;
	public static final int USER_GETFOLLOWING = 55;
	public static final int USER_GETBUILDINGFOLLOWING = 56;
	public static final int USER_GETUSERINBUILDING = 57;
	public static final int BUILDING_GETRECOMMENDJOB = 58;
	public static final int USER_GETBADGE = 59;
	public static final int UPLOAD_OFFLINE = 60;
	public static final int GET_MOST_DATES_TASK = 61;
	public static final int BUILDING_VIEWTODAYCARD = 62;
	public static final int USER_GETFOLLOWINGJOB = 63;
	public static final int USER_GETRECOMMENDFOLLOW = 64;
	public static final int JOB_GETALLUNIT = 65;
	public static final int MESSAGE_REMIND = 66;
	public static final int TASK_SUSPEND = 67;
	public static final int USER_SEARCH = 68;
	public static final int BAIDU_LOCATION = 69;
	public static final int USER_AD = 70;
	public static final int TASK_INMONTH = 71;
	public static final int TASK_GETIMAGE = 72;
	




}
