package com.huntdreams.weibo.cache;

/**
 * 缓存常量类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class Constants {

    public static final int DB_CACHE_DAYS = 10;
    public static final int FILE_CACHE_DAYS = 3;

    // File Cache Types
    public static final String FILE_CACHE_AVATAR_SMALL = "avatar_small";
    public static final String FILE_CACHE_AVATAR_LARGE = "avatar_large";
    public static final String FILE_CACHE_COVER = "cover";
    public static final String FILE_CACHE_PICS_SMALL = "pics_small";
    public static final String FILE_CACHE_PICS_LARGE = "pics_large";

    // Statuses
    public static final int HOME_TIMELINE_PAGE_SIZE = 20;

    // SQL
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";

}
