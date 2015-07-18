package com.huntdreams.weibo.api.user;

import org.json.JSONObject;

import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.Constants;
import com.huntdreams.weibo.support.http.WeiboParameters;

/* Current Account Api of Sina Weibo */
public class AccountApi extends BaseApi
{
	public static String getUid() {
		try {
			JSONObject json = request(Constants.GET_UID, new WeiboParameters(), HTTP_GET);
			return json.optString("uid");
		} catch (Exception e) {
			return null;
		}
	}
}
