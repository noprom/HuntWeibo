package com.huntdreams.weibo.cache.database.tables;

/**
 * 用户评论关系表结构
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class CommentMentionsTimeLineTable
{
	public static final String NAME = "comment_mentions";

	public static final String ID = "id";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
						+ "("
						+ ID + " integer primary key autoincrement,"
						+ JSON + " text"
						+ ");";
	
}
