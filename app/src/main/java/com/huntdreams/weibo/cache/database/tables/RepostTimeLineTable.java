package com.huntdreams.weibo.cache.database.tables;

/**
 * 用户回复表结构
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class RepostTimeLineTable
{
	public static final String NAME = "repost_timeline";

	public static final String MSGID = "msgId";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
						+ "("
						+ MSGID + " integer primary key autoincrement,"
						+ JSON + " text"
						+ ");";
}
