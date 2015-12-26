package com.yktx.check.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yktx.check.conn.HttpConnectinWrapper;
import com.yktx.check.conn.HttpPostListener;
import com.yktx.check.conn.ServiceListener;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;

public abstract class Service {
	protected String url = "";
	static Service service = null;
	List<NameValuePair> listRequest = new ArrayList<NameValuePair>();
	ServiceListener serviceListener = null;
	Map<String, String> params = new HashMap<String, String>();
	Map<String, File> files = new HashMap<String, File>();

	public Service(String requestType, Hashtable<String, String> params,
			String urlParams, ServiceListener serviceListener) {
		// ����post�������
		this.addHashtable(params);
		// ����������������
		this.serviceListener = serviceListener;
	}

	// 调用接口
	public static Service getService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		if (Contanst.HTTP_GETALL.equals(requestType)) {
			service = new GetAllService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_CREATETASK.equals(requestType)) {
			service = new CreateTaskService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_UPDATETASK.equals(requestType)) {
			service = new UpdateTaskService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETPERFORMANCE.equals(requestType)) {
			service = new GetPerformance(requestType, params, urlParams,
					serviceListener);
			// }else if(Contanst.HTTP_GETONGOING.equals(requestType)){
			// service = new GetOnGoingService(requestType, params, urlParams,
			// serviceListener);
		} else if (Contanst.HTTP_GETBYDATE.equals(requestType)) {
			service = new GetByDateService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_UPLOADPERFORMANCE.equals(requestType)) {
			service = new UpLoadPerformanceService(requestType, params,
					urlParams, serviceListener);
		} else if (Contanst.HTTP_GETRECOMMEND.equals(requestType)) {
			service = new GetRecommendService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETLASTTHREE.equals(requestType)) {
			service = new GetLastThreeService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_CREATEUSER.equals(requestType)) {
			service = new CreateUserService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_CREATEJOB.equals(requestType)) {
			service = new CreateJobService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_UPDATEJOB.equals(requestType)) {
			service = new UpdateJobService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETSTATISTIC.equals(requestType)) {
			service = new GetStatisticService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETBYTASKID.equals(requestType)) {
			service = new GetByTaskIdService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETBYPUBID.equals(requestType)) {
			service = new GetByWeiboIdService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_IMAGEUPLOAD.equals(requestType)) {
			service = new ImageUpLoadService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETBYIDUSER.equals(requestType)) {
			service = new GetByIdUserService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETBYIDTASK.equals(requestType)) {
			service = new GetByIdTaskService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETBASICINFO.equals(requestType)) {
			service = new GetBasicInfoService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETFLOOR.equals(requestType)) {
			service = new GetFloorService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETNEWEST.equals(requestType)) {
			service = new GetNewestService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETHOTTEST.equals(requestType)) {
			service = new GetHotTestService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_SEARCH.equals(requestType)) {
			service = new SearchService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_CREATECOMMENT.equals(requestType)) {
			service = new CreateCommentService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_CREATEVOTE.equals(requestType)) {
			service = new CreateVoteService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETCOMMENT.equals(requestType)) {
			service = new GetCommentService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GETVOTE.equals(requestType)) {
			service = new GetVoteService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_UPDATE_ORDER.equals(requestType)) {
			service = new UpdateOrderService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GET_CATEGORY.equals(requestType)) {
			service = new GetCategoryService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_CANCELVOTE.equals(requestType)) {
			service = new CancelVoteService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_GET_MSGTOUSER.equals(requestType)) {
			service = new GetMsgToUserService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_ALERT_CREATE.equals(requestType)) {
			service = new AlertCreateService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_GET_ALL_CHECKDATE.equals(requestType)) {
			service = new GetAllCheckDateService(requestType, params,
					urlParams, serviceListener);
		} else if (Contanst.HTTP_ALERT_UPDATE.equals(requestType)) {
			service = new AlertUpdateService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_DELETEJOB.equals(requestType)) {
			service = new DeleteJobService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_UPDATEJPUSHID.equals(requestType)) {
			service = new UpdateJpushIdService(requestType, params, urlParams,
					serviceListener);
		} else if (Contanst.HTTP_ADDERRORLOG.equals(requestType)){
			service = new AddErrorLogService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_SINA_UPLOAD_PIC.equals(requestType)) {
			service = new SinaUploadPicService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_GET_LAST_IMAGE_TODAY.equals(requestType)) {
			service = new GetLastImageTodayService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_UPDATE_USERNAME.equals(requestType)) {
			service = new UpdateUserNameService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_ALARM_GETBYRASKID.equals(requestType)){
			service = new AlarmGetbyTaskIdService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_TASK_SHARESTED.equals(requestType)){
			service = new ShareStrTaskService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_TASK_DELETE.equals(requestType)) {
			service = new TaskDeleteService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_ALARM_GETBYUSERID.equals(requestType)) {
			service = new AlarmGetByUserIdService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_JOB_DEFAULTCHECK.equals(requestType)) {
			service = new JobDefaultcheckService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_BUILDING_GETTOPPOINTUSER.equals(requestType)) {
			service = new BuildingGetTopPointUserService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_BUILDING_GETLATESTBUILDING.equals(requestType)) {
			service = new BuildingGetLatestService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_GET_QINIU_TOKEN.equals(requestType)) {
			service = new GetQiNiuTokenService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_JOB_GETBYUSERID.equals(requestType)) {
			service = new JobGetByUserIdService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_BUILDING_GETLASTTWOUSERS.equals(requestType)) {
			service = new BuildingGetLastTwoUsersService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_UPDATEPLATFORM.equals(requestType)) {
			service = new UserUpdatePlatformService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_FOLLOW.equals(requestType)) {
			service = new UserFollowService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_UNFOLLOW.equals(requestType)) {
			service = new UserUnFollowService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_GETFANS.equals(requestType)) {
			service = new UserGetFansService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_GETFOLLOWING.equals(requestType)) {
			service = new UserGetFollowingService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_GETBUILDINGFOLLOWING.equals(requestType)) {
			service = new UserGetBuildingFollowingService(requestType, params, urlParams,
					serviceListener);
		}else if (Contanst.HTTP_USER_GETUSERINBUILDING.equals(requestType)) {
			service = new UserGetInBuildingService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_BUILDING_GETRECOMMENDJOB.equals(requestType)){
			service = new BuildingGetRecommendJobService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_USER_GETBADGE.equals(requestType)){
			service = new UserGetBadgeService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_UPLOAD_OFFLINE.equals(requestType)){
			service = new UploadOfflineService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_GET_MOST_DATES_TASK.equals(requestType)){
			service = new GetMostDatesTaskService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_BUILDING_VIEWTODAYCARD.equals(requestType)){
			service = new BuildingViewTodayCardService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_USER_GETFOLLOWINGJOB.equals(requestType)){
			service = new GetFollowingJobService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_USER_GETRECOMMENDFOLLOW.equals(requestType)){
			service = new UserGetRecommendFollowService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_JOB_GETALLUNIT.equals(requestType)){
			service = new GetAllUnitService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_MESSAGE_REMIND.equals(requestType)){
			service = new MessageRemindService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_TASK_SUSPEND.equals(requestType)){
			service = new TaskSuspendService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_USER_SEARCH.equals(requestType)){
			service = new UserSearchService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_BAIDU_LOCATION.equals(requestType)){
			service = new BaiduLocationService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_USER_AD.equals(requestType)){
			service = new UserAdService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_TASK_INMONTH.equals(requestType)){
			service = new TaskGetInMonthService(requestType, params, urlParams,
					serviceListener);
		}else if(Contanst.HTTP_TASK_GETIMAGE.equals(requestType)){
			service = new TaskGetImageService(requestType, params, urlParams,
					serviceListener);
		}
		
		return service;
	}

	public Service addList(List<NameValuePair> params) {
		this.listRequest = params;
		return service;
	}

	public Service addPart(Map<String, String> params, Map<String, File> files) {
		this.params = params;
		this.files = files;
		return service;
	}

	List<NameValuePair> addHashtable(Hashtable<String, String> params) {
		if (params == null)
			return listRequest;
		Enumeration<String> perpertyKeys = params.keys();
		while (perpertyKeys.hasMoreElements()) {
			String key = (String) perpertyKeys.nextElement();
			try {
				listRequest.add(new BasicNameValuePair(key, (String) params
						.get(key)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listRequest;
	}

	List<NameValuePair> addParam(String key, String value) {
		try {
			listRequest.add(new BasicNameValuePair(key, value));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listRequest;
	}

	protected HttpPostListener httpPostListener = new HttpPostListener() {

		public void connFail(String erro) {
			Tools.getLog(Tools.i, "aaa", "connFail");
			Tools.getLog(Tools.i, "aaa", "erro = " + erro);
			httpFail(erro);
		}

		public void connSuccess(String reponse) {
			Tools.getLog(Tools.i, "aaa", "connSuccess");
			Tools.getLog(Tools.i, "aaa", "reponse = " + reponse);
			httpSuccess(reponse);
		}
	};

	public void request(String httpMethod) {
		Tools.getLog(Tools.i, "aaa", "httpMethod = " + httpMethod);
		new HttpConnectinWrapper().request(httpMethod, url, listRequest,
				params, files, httpPostListener);
	}

	abstract void httpSuccess(String reponse);

	abstract void httpFail(String erro);

	abstract void parse(String reponse);

	protected String erroCodeParse(String erroCode) {
		return erroCode;
	}
}
