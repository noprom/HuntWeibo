package com.huntdreams.weibo.cache.user;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

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


}
