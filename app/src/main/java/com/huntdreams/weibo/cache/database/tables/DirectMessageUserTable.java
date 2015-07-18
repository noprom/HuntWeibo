package com.huntdreams.weibo.cache.database.tables;

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
