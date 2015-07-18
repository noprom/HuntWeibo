package com.huntdreams.weibo.api;

import android.util.Log;

import com.huntdreams.weibo.support.http.HttpUtility;
import com.huntdreams.weibo.support.http.WeiboParameters;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.huntdreams.weibo.BuildConfig.DEBUG;

/**
 * 接口基类
 *
 * @author noprom
 * @email tyee.noprom@qq.com
 * <p/>
 * Created by noprom on 15/7/1.
 */
public abstract class BaseApi
{
    private static final String TAG = BaseApi.class.getSimpleName();

    // Http Methods
    protected static final String HTTP_GET = HttpUtility.GET;
    protected static final String HTTP_POST = HttpUtility.POST;

    // Access Token
    private static String mAccessToken;

    protected static JSONObject request(String url, WeiboParameters params, String method) throws Exception {
        return request(mAccessToken, url, params, method, JSONObject.class);
    }

    protected static JSONArray requestArray(String url, WeiboParameters params, String method) throws Exception {
        return request(mAccessToken, url, params, method, JSONArray.class);
    }

    protected static <T> T request(String token, String url, WeiboParameters params, String method, Class<T> jsonClass) throws Exception {
        if (token == null) {
            return null;
        } else {
            params.put("access_token", token);
            String jsonData = HttpUtility.doRequest(url, params, method);

            if (DEBUG) {
                Log.d(TAG, "jsonData = " + jsonData);
            }

            if (jsonData != null && (jsonData.contains("{") || jsonData.contains("["))) {
                return jsonClass.getConstructor(String.class).newInstance(jsonData);
            } else {
                return null;
            }
        }
    }

    protected static JSONObject requestWithoutAccessToken(String url, WeiboParameters params, String method) throws Exception {
        String jsonData = HttpUtility.doRequest(url, params, method);

        if (DEBUG) {
            Log.d(TAG, "jsonData = " + jsonData);
        }

        if (jsonData != null && jsonData.contains("{")) {
            return new JSONObject(jsonData);
        } else {
            return null;
        }
    }

    public static String getAccessToken() {
        return mAccessToken;
    }

    public static void setAccessToken(String token) {
        mAccessToken = token;
    }
}
