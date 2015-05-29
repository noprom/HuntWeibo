package com.huntdreams.weibo.support.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 应用程序配置
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/29.
 */
public class Settings {
    public static final String XML_NAME = "settings";

    // Actions
    public static final String FAST_SCROLL = "fast_scroll";
    public static final String SHAKE_TO_RETURN = "shake_to_return";
    public static final String RIGHT_HANDED = "right_handed";
    public static final String KEYWORD = "keyword";

    // Notification
    public static final String NOTIFICATION_SOUND = "notification_sound",
            NOTIFICATION_VIBRATE = "notification_vibrate",
            NOTIFICATION_INTERVAL = "notification_interval",
            NOTIFICATION_ONGOING = "notification_ongoing";

    // Network
    public static final String AUTO_NOPIC = "auto_nopic";

    // Theme
    public static final String THEME_DARK = "theme_dark";

    // Debug
    public static final String LANGUAGE = "language";
    public static final String AUTO_SUBMIT_LOG = "debug_autosubmit";

    // Group
    public static final String CURRENT_GROUP = "current_group";

    // Position
    public static final String LAST_POSITION = "last_position";

    private static Settings sInstance;

    private SharedPreferences mPrefs;

    public static Settings getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Settings(context);
        }

        return sInstance;
    }

    private Settings(Context context) {
        mPrefs = context.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
    }

    public Settings putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).commit();
        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPrefs.getBoolean(key, def);
    }

    public Settings putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).commit();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public Settings putString(String key, String value) {
        mPrefs.edit().putString(key, value).commit();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }
}
