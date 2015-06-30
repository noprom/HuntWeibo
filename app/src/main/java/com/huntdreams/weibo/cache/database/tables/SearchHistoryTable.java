package com.huntdreams.weibo.cache.database.tables;

/**
 * 搜索记录表结构
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class SearchHistoryTable
{
	public static final String NAME = "search_history";

	public static final String ID = "id";

	public static final String KEYWORD = "keyword";

	public static final String CREATE = "create table " + NAME
										+ "("
										+ ID + " integer primary key autoincrement,"
										+ KEYWORD + " text"
										+ ");";
}
