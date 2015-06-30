package com.huntdreams.weibo.cache.database.tables;

/**
 * UserTimeLineTable
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class UserTimeLineTable
{
	public static final String NAME = "user_timeline";

	public static final String UID = "uid";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
            + "("
            + UID + " integer primary key autoincrement,"
            + JSON + " text"
            + ");";
}
