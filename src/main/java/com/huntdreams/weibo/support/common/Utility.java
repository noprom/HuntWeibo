package com.huntdreams.weibo.support.common;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * 帮助工具
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class Utility {

    /**
     * 缓存是否有效
     * @param createTime
     * @param availableDays
     * @return
     */
    public static boolean isCacheAvailable(long createTime, int availableDays){
        return System.currentTimeMillis() <= createTime + TimeUnit.DAYS.toMillis(availableDays);
    }

    public static void startService(Context context){

    }
}
