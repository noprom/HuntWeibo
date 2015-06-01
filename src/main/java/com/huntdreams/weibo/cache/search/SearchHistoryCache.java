package com.huntdreams.weibo.cache.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huntdreams.weibo.cache.database.DataBaseHelper;
import com.huntdreams.weibo.cache.database.tables.SearchHistoryTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索历史缓存
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/6/1.
 */
public class SearchHistoryCache {
    private static final int MAX_HISTORY = 5;
    private DataBaseHelper mHelper;

    public SearchHistoryCache(Context context){
        mHelper = DataBaseHelper.instance(context);
    }

    public List<String> getHistory(){
        Cursor cursor = mHelper.getReadableDatabase().query(SearchHistoryTable.NAME,
                null,null,null,null,null,SearchHistoryTable.ID + " DESC");
        if(cursor != null){
            cursor.moveToFirst();
            List<String> list = new ArrayList<String>();
            if(cursor.getCount() > 0){
                do{
                    list.add(cursor.getString(cursor.getColumnIndex(SearchHistoryTable.KEYWORD)));
                }while(cursor.moveToNext());
            }
            cursor.close();
            return list;
        }
        return null;
    }

    public void addHistory(String keyword){
        // Get count first
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(SearchHistoryTable.NAME,null, null, null, null, null, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        int firstId = -1;
        if(count > 0){
            firstId = cursor.getInt(cursor.getColumnIndex(SearchHistoryTable.ID));
        }
        cursor.close();
        db.close();

        // Add a record
        db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SearchHistoryTable.KEYWORD, keyword);

        // Insert
        db.beginTransaction();
        db.insert(SearchHistoryTable.NAME, null, values);
        if(count == MAX_HISTORY){
            db.delete(SearchHistoryTable.NAME, SearchHistoryTable.ID + "=" + firstId, null);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}
