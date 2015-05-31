package com.huntdreams.weibo.api.user;

import android.util.Log;

import com.google.gson.Gson;
import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.Constants;
import com.huntdreams.weibo.model.UserModel;
import com.huntdreams.weibo.support.http.WeiboParameters;

import org.json.JSONObject;
import static com.huntdreams.weibo.BuildConfig.DEBUG;
/**
 * 用户个人信息API
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class UserApi extends BaseApi {

    private static String TAG = UserApi.class.getSimpleName();

    public static UserModel getUser(String uid){
        WeiboParameters params = new WeiboParameters();
        params.put("uid", uid);

        try{
            JSONObject json = request(Constants.USER_SHOW, params, HTTP_GET);
            UserModel user = new Gson().fromJson(json.toString().replaceAll("-Weibo", ""), UserModel.class);
            return user;
        } catch (Exception e) {
            if(DEBUG){
                Log.e(TAG, "Failed to fetch user info from net: " + e.getClass().getSimpleName());
            }
            return null;
        }
    }
}
