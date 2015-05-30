package com.huntdreams.weibo.cache.login;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

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

        }
    }
}
