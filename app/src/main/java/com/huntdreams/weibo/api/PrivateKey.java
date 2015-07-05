package com.huntdreams.weibo.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 网络接口地址常量
 * @author noprom
 * @email tyee.noprom@qq.com
 *
 * Created by noprom on 15/7/1.
 */
public class PrivateKey extends BaseApi
{
	private static final String TAG = PrivateKey.class.getSimpleName();
	
	private static final String PREF = "app_key",
								PREF_ID = "id",
								PREF_SECRET = "secret",
								PREF_PKG = "package",
								PREF_REDIRECT = "redirect",
								PREF_SCOPE = "scope";
	
	private static String sAppId, sAppSecret, sRedirectUri, sPackageName, sScope;
	
	public static void setPrivateKey(String appId, String appSecret, String redirectUri, String packageName, String scope) {
		sAppId = appId;
		sAppSecret = appSecret;
		sRedirectUri = redirectUri;
		sPackageName = packageName;
		sScope = scope;
	}
	
	public static boolean readFromPref(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_WORLD_READABLE);
		String id = prefs.getString(PREF_ID, null);
		String secret = prefs.getString(PREF_SECRET, null);
		String redirect = prefs.getString(PREF_REDIRECT, null);
		String pkg = prefs.getString(PREF_PKG, null);
		String scope = prefs.getString(PREF_SCOPE, null);
		
		if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(secret)
			&& !TextUtils.isEmpty(redirect) && !TextUtils.isEmpty(scope)) {
			
			setPrivateKey(id, secret, redirect, pkg, scope);
			return true;
		} else {
			return false;
		}
	}
	
	public static void writeToPref(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_WORLD_READABLE);
		prefs.edit()
			.putString(PREF_ID, sAppId)
			.putString(PREF_SECRET, sAppSecret)
			.putString(PREF_REDIRECT, sRedirectUri)
			.putString(PREF_PKG, sPackageName)
			.putString(PREF_SCOPE, sScope)
			.commit();
	}
	
	/*
	 * 0 - app id
	 * 1 - app secret
	 * 2 - redirect uri
	 * 3 - package name
	 * 4 - scope
	 *
	 */
	public static String[] getAll() {
		return new String[]{
			sAppId,
			sAppSecret,
			sRedirectUri,
			sPackageName,
			sScope
		};
	}
	
	public static String getOauthLoginPage() {
		return Constants.OAUTH2_ACCESS_AUTHORIZE + "?" + "client_id=" + sAppId
				+ "&response_type=token&redirect_uri=" + sRedirectUri
				+ "&key_hash=" + sAppSecret + (TextUtils.isEmpty(sPackageName) ? "" : "&packagename=" + sPackageName)
				+ "&display=mobile" + "&scope=" + sScope;
	}
	
	public static boolean isUrlRedirected(String url) {
		return url.startsWith(sRedirectUri);
	}
}
