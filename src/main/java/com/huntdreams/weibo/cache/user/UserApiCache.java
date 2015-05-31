package com.huntdreams.weibo.cache.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
}
