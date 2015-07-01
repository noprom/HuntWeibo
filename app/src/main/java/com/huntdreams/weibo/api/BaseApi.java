package com.huntdreams.weibo.api;

import android.util.Log;

import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONException;
import org.json.JSONObject;
import static com.huntdreams.weibo.BuildConfig.DEBUG;

/**
 * 接口基类
 * @author noprom
 * @email tyee.noprom@qq.com
 *
 * Created by noprom on 15/7/1.
 */
public class BaseApi {

    private static final String TAG = BaseApi.class.getSimpleName();

    // Http methods
    protected static final String HTTP_GET = "GET";
    protected static final String HTTP_POST = "POST";

    // Access Token
    private static String mAccessToken;

    /**
     * 用于向新浪微博请求服务器数据
     * @param url
     * @param params
     * @param method
     * @return
     * @throws JSONException
     */
    protected static JSONObject request(String url, WeiboParameters params,String method) throws JSONException{
        if(mAccessToken == null){
            return null;
        }else{
            params.put("access_token", mAccessToken);
            String jsonData = AsyncWeiboRunner.request(url, params, method);
            if(DEBUG){
                Log.d(TAG, "jsonData = " + jsonData);
            }

            if(jsonData != null && jsonData.contains("{")){
                return new JSONObject(jsonData);
            }else{
                return null;
            }
        }

    }

}
