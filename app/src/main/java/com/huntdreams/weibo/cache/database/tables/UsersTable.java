package com.huntdreams.weibo.cache.database.tables;

/**
 * 用户个人信息表结构
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class UsersTable {

    public static final String NAME = "users";

    public static final String UID = "uid";

    public static final String USERNAME = "username";

    public static final String TIMESTAMP = "timestamp";

    public static final String JSON = "json";

    public static final String CREATE = "create table " + NAME
            + "("
            + UID + " integer primary key autoincrement,"
            + TIMESTAMP + " integer,"
            + USERNAME + " text,"
            + JSON + " text"
            + ");";

}
