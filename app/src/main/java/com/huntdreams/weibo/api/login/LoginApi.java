package com.huntdreams.weibo.api.login;

import android.util.Log;

import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.Constants;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONObject;

import static com.huntdreams.weibo.BuildConfig.DEBUG;

/**
 * 用户登录接口
 *
 * @author noprom
 * @email tyee.noprom@qq.com
 * <p/>
 * Created by noprom on 15/7/1.
 */
public class LoginApi extends BaseApi {

    private static final String TAG = LoginApi.class.getSimpleName();

    /**
     * 获得登陆token和过期日期
     *
     * @param appId
     * @param appSecret
     * @param username
     * @param password
     * @return
     */
    public static String[] login(String appId, String appSecret, String username, String password) {
        WeiboParameters params = new WeiboParameters();
        params.put("username", username);
        params.put("password", password);
        params.put("client_id", appId);
        params.put("client_secret", appSecret);
        params.put("grant_type", "password");

        try {
            JSONObject json = requestWithoutAccessToken(Constants.OAUTH2_ACCESS_TOKEN, params, HTTP_POST);
            if(DEBUG){
                Log.d(TAG, "access_token = " + json.optString("access_token") + "expires_in = " + json.optString("expires_in"));
            }
            return new String[]{json.optString("access_token"), json.optString("expires_in")};
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
                Log.d(TAG, "login error:" + e.getClass().getSimpleName());
            }
            return null;
        }
    }
}
