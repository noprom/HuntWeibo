package com.huntdreams.weibo.api.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.user.AccountApi;
import com.huntdreams.weibo.cache.user.UserApiCache;
import com.huntdreams.weibo.model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 用户登录接口缓存
 *
 * @author noprom
 * @email tyee.noprom@qq.com
 * Created by noprom on 15/7/1.
 */
public class LoginApiCache {
    private static final String TAG = LoginApiCache.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mPrefs;
    private String mAccessToken;
    private String mUid;
    private long mExpireDate;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mTokens = new ArrayList<>();
    private ArrayList<Long> mExpireDates = new ArrayList<>();

    public LoginApiCache(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences("access_token", Context.BIND_WAIVE_PRIORITY);
        mAccessToken = mPrefs.getString("access_token", null);
        mUid = mPrefs.getString("uid", "");
        mExpireDate = mPrefs.getLong("expires_in", Long.MIN_VALUE);

        if(mAccessToken != null){
            BaseApi.setAccessToken(mAccessToken);
        }

        parseMultiUser();
    }

    /**
     * 登录
     * @param token
     * @param expire
     */
    public void login(String token, String expire){
        mAccessToken = token;
        BaseApi.setAccessToken(mAccessToken);
        mExpireDate = System.currentTimeMillis() + Long.valueOf(expire) * 1000;
        mUid = AccountApi.getUid();
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
                .putLong("expires_in", mExpireDate)
                .putString("uid", mUid)
                .commit();
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getUid(){
        return mUid;
    }

    public long getExpireDate() {
        return mExpireDate;
    }

    public String[] getUserNames(){
        return mNames.toArray(new String[mNames.size()]);
    }

    public void reloadMultiUser(){
        mNames.clear();
        mTokens.clear();
        mExpireDates.clear();
        parseMultiUser();
    }

    private void parseMultiUser() {
        String str = mPrefs.getString("names", "");
        if (str == null || str.trim().equals(""))
            return;

        mNames.addAll(Arrays.asList(str.split(",")));

        str = mPrefs.getString("tokens", "");
        if (str == null || str.trim().equals(""))
            return;

        mTokens.addAll(Arrays.asList(str.split(",")));

        str = mPrefs.getString("expires", "");
        if (str == null || str.trim().equals(""))
            return;

        String[] s = str.split(",");
        for (int i = 0; i < s.length; i++) {
            mExpireDates.add(Long.valueOf(s[i]));
        }

        if (mTokens.size() != mNames.size() ||
                mTokens.size() != mExpireDates.size() ||
                mExpireDates.size() != mNames.size()) {
            mNames.clear();
            mTokens.clear();
            mExpireDates.clear();
        }
    }

    private void writeMultiUser() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < mNames.size(); i++) {
            b.append(mNames.get(i));

            if (i < mNames.size() - 1) {
                b.append(",");
            }
        }
        mPrefs.edit().putString("names", b.toString()).commit();

        b = new StringBuilder();
        for (int i = 0; i < mTokens.size(); i++) {
            b.append(mTokens.get(i));

            if (i < mTokens.size() - 1) {
                b.append(",");
            }
        }
        mPrefs.edit().putString("tokens", b.toString()).commit();

        b = new StringBuilder();
        for (int i = 0; i < mExpireDates.size(); i++) {
            b.append(mExpireDates.get(i));

            if (i < mExpireDates.size() - 1) {
                b.append(",");
            }
        }
        mPrefs.edit().putString("expires", b.toString()).commit();
    }

    public long addUser(String token, String expire) {
        // Temporarily switch to the new user
        BaseApi.setAccessToken(token);

        // Fetch the new user info
        UserModel user = new UserApiCache(mContext).getUser(AccountApi.getUid());

        long exp = System.currentTimeMillis() + Long.valueOf(expire) * 1000;
        if (user != null && !mNames.contains(user.getNameNoRemark())) {
            // Add it to list
            mNames.add(user.getNameNoRemark());
            mTokens.add(token);
            mExpireDates.add(exp);
        }

        // Set Token back
        BaseApi.setAccessToken(mAccessToken);

        writeMultiUser();

        return exp;
    }

    // Should restart the app after doing this
    public void switchToUser(int position) {
        UserApiCache c = new UserApiCache(mContext);
        UserModel current = c.getUser(mUid);
        if (current == null)
            return;

        String newToken = mTokens.get(position);

        // Get new user
        BaseApi.setAccessToken(newToken);
        UserModel next = c.getUser(AccountApi.getUid());
        if (next == null)
            return;

        long newExpires = mExpireDates.get(position);
        mNames.remove(position);
        mTokens.remove(position);
        mExpireDates.remove(position);

        mNames.add(current.getNameNoRemark());
        mTokens.add(mAccessToken);
        mExpireDates.add(mExpireDate);
        writeMultiUser();

        mExpireDate = newExpires;
        mAccessToken = newToken;
        mUid = next.id;

        cache();
    }

}
