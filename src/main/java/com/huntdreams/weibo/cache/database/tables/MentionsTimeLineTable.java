package com.huntdreams.weibo.cache.database.tables;

/**
 * 评论表结构
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class MentionsTimeLineTable
{
	public static final String NAME = "mentions_timeline";

	public static final String ID = "id";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
						+ "("
						+ ID + " integer primary key autoincrement,"
						+ JSON + " text"
						+ ");";
}
