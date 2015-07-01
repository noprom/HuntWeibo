package com.huntdreams.weibo.api.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.huntdreams.weibo.BuildConfig.DEBUG;

/**
 * 用户登录接口缓存
 *
 * @author noprom
 * @email tyee.noprom@qq.com
 * Created by noprom on 15/7/1.
 */
public class LoginApiCache {
    private static final String TAG = LoginApiCache.class.getSimpleName();

    private SharedPreferences mPrefs;
    private String mAccessToken;
    private String mAppId;
    private String mAppSecret;
    private long mExpireDate;

    public LoginApiCache(Context context) {
        mPrefs = context.getSharedPreferences("access_token", Context.BIND_WAIVE_PRIORITY);
        mAccessToken = mPrefs.getString("access_token", null);
        mAppId = mPrefs.getString("app_id", null);
        mAppSecret = mPrefs.getString("app_secret", null);
        mExpireDate = mPrefs.getLong("expires_in", Long.MIN_VALUE);
    }

    /**
     * 登陆微博
     *
     * @param appId
     * @param appSecret
     * @param username
     * @param password
     */
    public void login(String appId, String appSecret, String username, String password) {
        if (mAccessToken == null || mExpireDate == Long.MIN_VALUE) {
            if (DEBUG) {
                Log.d(TAG, "access token not initialized, running login function");
            }
            String[] result = LoginApi.login(appId, appSecret, username, password);
            if (result != null) {
                if (DEBUG) {
                    Log.d(TAG, "result got, loading to cache");
                    Log.d(TAG, "access_token = " + result[0] + " and expires_in = " + result[1]);
                }

                mAccessToken = result[0];
                mExpireDate = System.currentTimeMillis() + Long.valueOf(result[1]) * 1000;
                mAppId = appId;
                mAppSecret = appSecret;
            }
        }
    }

    /**
     * 退出登陆
     */
    public void logout() {
        mAccessToken = null;
        mExpireDate = Long.MIN_VALUE;
        mPrefs.edit().remove("access_token").remove("expires_in").commit();
    }

    /**
     * 将登陆信息写入缓存
     */
    public void cache() {
        mPrefs.edit().putString("access_token", mAccessToken)
                .putString("app_id", mAppId)
                .putString("app_secret", mAppSecret)
                .putLong("expires_in", mExpireDate)
                .commit();
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public long getExpireDate() {
        return mExpireDate;
    }

    public String getAppId() {
        return mAppId;
    }

    public String getAppSecret() {
        return mAppSecret;
    }

}
