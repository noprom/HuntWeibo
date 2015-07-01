package com.huntdreams.weibo.support;

import java.util.concurrent.TimeUnit;

/**
 * 辅助工具类
 *
 * @author noprom
 * @email tyee.noprom@qq.com
 * Created by noprom on 15/7/1.
 */
public class Utility {

    /**
     * 即将在几天之内过期
     * @param time
     * @return
     */
    public static int expireTimeInDays(long time){
        return (int) TimeUnit.MILLISECONDS.toDays(time - System.currentTimeMillis());
    }

    /**
     * Token是否过期
     * @param time
     * @return
     */
    public static boolean isTokenExpired(long time){
        return time <= System.currentTimeMillis();
    }
}
