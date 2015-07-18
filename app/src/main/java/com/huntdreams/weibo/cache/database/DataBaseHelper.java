package com.huntdreams.weibo.cache.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.huntdreams.weibo.cache.database.tables.CommentMentionsTimeLineTable;
import com.huntdreams.weibo.cache.database.tables.CommentTimeLineTable;
import com.huntdreams.weibo.cache.database.tables.DirectMessageUserTable;
import com.huntdreams.weibo.cache.database.tables.FavListTable;
import com.huntdreams.weibo.cache.database.tables.HomeTimeLineTable;
import com.huntdreams.weibo.cache.database.tables.MentionsTimeLineTable;
import com.huntdreams.weibo.cache.database.tables.RepostTimeLineTable;
import com.huntdreams.weibo.cache.database.tables.SearchHistoryTable;
import com.huntdreams.weibo.cache.database.tables.StatusCommentTable;
import com.huntdreams.weibo.cache.database.tables.UserTimeLineTable;
import com.huntdreams.weibo.cache.database.tables.UsersTable;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "weibo_data";
	private static int DB_VER = 15;
	
	private static DataBaseHelper instance;
	
	private DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VER);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(UsersTable.CREATE);
		db.execSQL(HomeTimeLineTable.CREATE);
		db.execSQL(UserTimeLineTable.CREATE);
		db.execSQL(MentionsTimeLineTable.CREATE);
		db.execSQL(CommentTimeLineTable.CREATE);
		db.execSQL(CommentMentionsTimeLineTable.CREATE);
		db.execSQL(StatusCommentTable.CREATE);
		db.execSQL(RepostTimeLineTable.CREATE);
		db.execSQL(FavListTable.CREATE);
		db.execSQL(DirectMessageUserTable.CREATE);
		db.execSQL(SearchHistoryTable.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int from, int to) {
		if (from == 13) {
			db.execSQL(DirectMessageUserTable.CREATE);
		} else if (from == 14) {
			db.execSQL(SearchHistoryTable.CREATE);
		}
	}
	
	public static synchronized DataBaseHelper instance(Context context) {
		if (instance == null) {
			instance = new DataBaseHelper(context);
		}
		
		return instance;
	}

}
