package com.huntdreams.weibo.cache.database.tables;

/**
 * DirectMessageUserTable
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class DirectMessageUserTable
{
	public static final String NAME = "direct_message_user";

	public static final String ID = "id";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
						+ "("
						+ ID + " integer primary key autoincrement,"
						+ JSON + " text"
						+ ");";
}
