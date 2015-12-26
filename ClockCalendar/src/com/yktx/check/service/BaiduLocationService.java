package com.yktx.check.service;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yktx.check.conn.ServiceListener;
import com.yktx.check.util.Contanst;
import com.yktx.check.util.Tools;


/**
 * @author Administrator 验证验证码
 * 
 */
public class BaiduLocationService extends Service {

	public static String pushalias = "";

	public BaiduLocationService(String requestType,
			Hashtable<String, String> params, String urlParams,
			ServiceListener serviceListener) {
		super(requestType, params, urlParams, serviceListener);
		this.url = Contanst.HTTP_BAIDU_LOCATION + urlParams;
		Tools.getLog(Tools.d,  "aaa", "url ===== " + url);
	}

	@Override
	void httpFail(String erro) {
		serviceListener.getJOSNdataFail("", "网络异常", Contanst.BAIDU_LOCATION);
		Tools.getLog(Tools.d,  "aaa", "httpFailhttpFail");
		// LodingActivity.isJion = false;
	}

	@Override
	void httpSuccess(String reponse) {
		// LodingActivity.isJion = false;
		try {
			// ======================根据协议本地测试数据================================
			// reponse =
			// ========================联调接口时注释就可以============================
			JSONObject result = new JSONObject(reponse);
			String retcode = (String) result.get("status");

			// reponse =
//			{
//		    "status":"OK",
//		    "result":{
//		        "location":{
//		            "lng":117.44034,
//		            "lat":45.945443
//		        },
//		        "formatted_address":"内蒙古自治区锡林郭勒盟东乌珠穆沁旗",
//		        "business":"",
//		        "addressComponent":{
//		            "city":"锡林郭勒盟",
//		            "direction":"",
//		            "distance":"",
//		            "district":"东乌珠穆沁旗",
//		            "province":"内蒙古自治区",
//		            "street":"",
//		            "street_number":""
//		        },
//		        "cityCode":63
//		    }
//		}
			Tools.getLog(Tools.d,  "aaa", "retcode = " + retcode);
			if ("OK".equals(retcode)) {// 成功获取数据

				JSONObject data = (JSONObject) result.get("result");
				JSONObject data2 = (JSONObject) data.get("addressComponent");
//				String province = data2.getString("province");
//				String district = data2.getString("district");
				String city = data2.getString("city");
				Tools.getLog(Tools.d,  "bbb", "city === " + city);

				if("市".equals(city.substring(city.length()-1))){
					city = city.substring(0, city.length()-1);
				}

				Tools.getLog(Tools.d,  "bbb", "city === " + city);
				serviceListener.getJOSNdataSuccess(city, retcode,
						Contanst.BAIDU_LOCATION);
			} else if ("111".equals(retcode)) {
				serviceListener.getJOSNdataSuccess("ok", retcode,
						Contanst.BAIDU_LOCATION);
				// JSONObject data = (JSONObject) result.get("data");
			} else {
				String errmsg = (String) result.get("mess");
				serviceListener.getJOSNdataFail(erroCodeParse(retcode), errmsg,
						Contanst.BAIDU_LOCATION);
			}
		} catch (JSONException e) {
			serviceListener.getJOSNdataFail("", "服务器异常", Contanst.BAIDU_LOCATION);
			e.printStackTrace();
		}
	}

	@Override
	void parse(String reponse) {
		// TODO Auto-generated method stub

	}

}
