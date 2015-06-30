package com.huntdreams.weibo.api.user;

import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.Constants;
import com.huntdreams.weibo.support.http.WeiboParameters;

import org.json.JSONObject;

/**
 * 账户缓存类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/30.
 */
public class AccountApi extends BaseApi{

    public static String getUid() {
        try {
            JSONObject json = request(Constants.GET_UID, new WeiboParameters(), HTTP_GET);
            return json.optString("uid");
        } catch (Exception e) {
            return null;
        }
    }
}
