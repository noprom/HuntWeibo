package com.huntdreams.weibo.cache.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.huntdreams.weibo.api.BaseApi;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 用户登录缓存
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/30.
 */
public class LoginApiCache {

    private static final String TAG = LoginApiCache.class.getSimpleName();
    private Context mContext;
    private SharedPreferences mPrefs;
    private String mAccessToken;
    private String mUid;
    private long mExpireDate;

    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mTokens = new ArrayList<String>();
    private ArrayList<Long> mExpireDates = new ArrayList<Long>();

    public LoginApiCache(Context context){
        mContext = context;
        mPrefs = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        mAccessToken = mPrefs.getString("access_token", null);
        mUid = mPrefs.getString("uid", "");
        mExpireDate = mPrefs.getLong("expires_in",Long.MIN_VALUE);

        if(mAccessToken != null){
            BaseApi.setAccessToken(mAccessToken);
        }

    }

    private void parseMultiUser(){
        // names
        String str = mPrefs.getString("names","");
        if(str == null || str.trim().equals("")){
            return;
        }
        mNames.addAll(Arrays.asList(str.split(",")));

        // tokens
        str = mPrefs.getString("tokens","");
        if(str == null || str.trim().equals("")){
            return;
        }
        mTokens.addAll(Arrays.asList(str.split(",")));

        // expires
        str = mPrefs.getString("expires","");
        if(str == null || str.trim().equals("")){
            return;
        }
        String[] s = str.split(",");
        for(String ss : s){
            mExpireDates.add(Long.valueOf(ss));
        }

        if(mTokens.size() != mNames.size() ||
           mTokens.size() != mExpireDates.size() ||
           mExpireDates.size() != mNames.size()) {
           mNames.clear();
           mTokens.clear();
           mExpireDates.clear();
        }

    }
}
