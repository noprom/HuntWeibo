package com.huntdreams.weibo.support.common;

import java.util.concurrent.TimeUnit;

/**
 * 帮助工具
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class Utility {

    public static boolean isCacheAvailable(long createTime, int availableDays){
        return System.currentTimeMillis() <= createTime + TimeUnit.DAYS.toMillis(availableDays);
    }
}
