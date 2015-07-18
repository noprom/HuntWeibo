package com.huntdreams.weibo.cache.database.tables;

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
