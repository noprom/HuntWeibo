package com.huntdreams.weibo.cache.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.gson.Gson;
import com.huntdreams.weibo.R;
import com.huntdreams.weibo.api.user.UserApi;
import com.huntdreams.weibo.cache.Constants;
import com.huntdreams.weibo.cache.database.DataBaseHelper;
import com.huntdreams.weibo.cache.database.tables.UsersTable;
import com.huntdreams.weibo.cache.file.FileCacheManager;
import com.huntdreams.weibo.model.UserModel;
import com.huntdreams.weibo.support.common.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.huntdreams.weibo.BuildConfig.DEBUG;
/**
 * 用户个人信息缓存
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class UserApiCache {

    private static final String TAG = UserApiCache.class.getSimpleName();
    private static BitmapDrawable[] mVipDrawable;
    private static HashMap<String, WeakReference<Bitmap>> mSmallAvatarCache = new HashMap<String, WeakReference<Bitmap>>();
    private DataBaseHelper mHelper;
    private FileCacheManager mManager;

    public UserApiCache(Context context){
        mHelper = DataBaseHelper.instance(context);
        mManager = FileCacheManager.instance(context);

        if(mVipDrawable == null){
            mVipDrawable = new BitmapDrawable[]{
                    (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_personal_vip),
                    (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_enterprise_vip)
            };
        }
    }

    /**
     * 根据uid获取用户
     * @param uid
     * @return
     */
    public UserModel getUser(String uid){
        UserModel model = UserApi.getUser(uid);
        if(model == null){
            Cursor cursor = mHelper.getReadableDatabase().query(UsersTable.NAME, new String[]{
                    UsersTable.UID,
                    UsersTable.TIMESTAMP,
                    UsersTable.USERNAME,
                    UsersTable.JSON
            },UsersTable.UID + "?=", new String[]{uid}, null, null, null);

            if(cursor.getCount() >= 1){
                cursor.moveToFirst();
                long time = cursor.getLong(cursor.getColumnIndex(UsersTable.TIMESTAMP));
                if (DEBUG) {
                    Log.d(TAG, "time = " + time);
                    Log.d(TAG, "available = " + Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS));
                }

                if (Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS)) {
                    model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(UsersTable.JSON)), UserModel.class);
                    model.timestamp = cursor.getInt(cursor.getColumnIndex(UsersTable.TIMESTAMP));
                }
            }
        }else {
            // Insert into database
            ContentValues values = new ContentValues();
            SQLiteDatabase db = mHelper.getWritableDatabase();

            db.beginTransaction();
            db.delete(UsersTable.NAME, UsersTable.UID + "=?", new String[]{uid});
            db.insert(UsersTable.NAME, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        return model;
    }

    /**
     * 根据username来获取用户
     * @param name
     * @return
     */
    public UserModel getUserByName(String name) {
        UserModel model;

        model = UserApi.getUserByName(name);

        if (model == null) {
            Cursor cursor = mHelper.getReadableDatabase().query(UsersTable.NAME, new String[] {
                    UsersTable.UID,
                    UsersTable.TIMESTAMP,
                    UsersTable.USERNAME,
                    UsersTable.JSON
            }, UsersTable.USERNAME + "=?", new String[]{name}, null, null, null);

            if (cursor.getCount() >= 1) {
                cursor.moveToFirst();

                long time = cursor.getLong(cursor.getColumnIndex(UsersTable.TIMESTAMP));

                if (DEBUG) {
                    Log.d(TAG, "time = " + time);
                    Log.d(TAG, "available = " + Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS));
                }

                if (Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS)) {
                    model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(UsersTable.JSON)), UserModel.class);
                    model.timestamp = cursor.getInt(cursor.getColumnIndex(UsersTable.TIMESTAMP));
                }
            }
        } else {

            // Insert into database
            ContentValues values = new ContentValues();
            values.put(UsersTable.UID, model.id);
            values.put(UsersTable.TIMESTAMP, model.timestamp);
            values.put(UsersTable.USERNAME, name);
            values.put(UsersTable.JSON, new Gson().toJson(model));

            SQLiteDatabase db = mHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(UsersTable.NAME, UsersTable.USERNAME + "=?", new String[]{name});
            db.insert(UsersTable.NAME, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();

        }

        return model;
    }

    public Bitmap getSmallAvatar(UserModel model) {
        String cacheName = model.id + model.profile_image_url.replaceAll("/", ".").replaceAll(":", "");
        InputStream cache;
        try {
            cache = mManager.getCache(Constants.FILE_CACHE_AVATAR_SMALL, cacheName);
        } catch (Exception e) {
            cache = null;
        }

        if (cache == null) {
            try {
                cache = mManager.createCacheFromNetwork(Constants.FILE_CACHE_AVATAR_SMALL, cacheName, model.profile_image_url);
            } catch (Exception e) {
                cache = null;
            }
        }

        if (cache == null) {
            return null;
        } else {
            Bitmap bmp = BitmapFactory.decodeStream(cache);
            mSmallAvatarCache.put(model.id, new WeakReference<Bitmap>(bmp));

            try {
                cache.close();
            } catch (IOException e) {

            }

            return bmp;
        }
    }

    public Bitmap getCachedSmallAvatar(UserModel model) {
        WeakReference<Bitmap> ref = mSmallAvatarCache.get(model.id);
        return ref == null ? null : ref.get();
    }

    public Bitmap getLargeAvatar(UserModel model) {
        String cacheName = model.id + model.avatar_large.replaceAll("/", ".").replaceAll(":", "");
        InputStream cache;
        try {
            cache = mManager.getCache(Constants.FILE_CACHE_AVATAR_LARGE, cacheName);
        } catch (Exception e) {
            cache = null;
        }

        if (cache == null) {
            try {
                cache = mManager.createCacheFromNetwork(Constants.FILE_CACHE_AVATAR_LARGE, cacheName, model.avatar_large);
            } catch (Exception e) {
                cache = null;
            }
        }

        if (cache != null) {
            Bitmap ret = BitmapFactory.decodeStream(cache);

            try {
                cache.close();
            } catch (IOException e) {

            }

            return ret;
        } else {
            return null;
        }
    }

    public Bitmap getCover(UserModel model) {
        String url = model.getCover();
        if (url.trim().equals("")) {
            return null;
        }

        if (DEBUG) {
            Log.d(TAG, "url = " + url);
        }

        String cacheName = model.id + url.substring(url.lastIndexOf("/") + 1, url.length());

        InputStream cache;
        try {
            cache = mManager.getCache(Constants.FILE_CACHE_COVER, cacheName);
        } catch (Exception e) {
            cache = null;
        }

        if (cache == null) {
            try {
                cache = mManager.createCacheFromNetwork(Constants.FILE_CACHE_COVER, cacheName, url);
            } catch (Exception e) {
                cache = null;
            }
        }

        if (cache != null) {
            Bitmap ret = BitmapFactory.decodeStream(cache);

            try {
                cache.close();
            } catch (IOException e) {

            }

            return ret;
        } else {
            return null;
        }
    }

}
