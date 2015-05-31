package com.huntdreams.weibo.cache.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.cache.database.DataBaseHelper;
import com.huntdreams.weibo.cache.file.FileCacheManager;
import com.huntdreams.weibo.model.UserModel;

import java.lang.ref.WeakReference;
import java.util.HashMap;

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
    }
}
