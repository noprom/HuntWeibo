package com.huntdreams.weibo.support.common;

import android.content.Context;

/**
 * 关键词过滤工具
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/30.
 */
public class FilterUtility {

    private static String[] mKeyworks = new String[0];

    public static void init(Context context){
        Settings settings = Settings.getInstance(context);
        String keywords = settings.getString(Settings.KEYWORD, "");
        if(!keywords.equals("")){
            mKeyworks = keywords.split(", ");
        }
    }

    /**
     * 是否需要过滤
     * @param text
     * @return
     */
    public static boolean shouldFilter(String text){
        if(mKeyworks.length == 0){
            return false;
        }
        for(String key : mKeyworks){
            if(key.startsWith("`") && key.endsWith("`")){
                if(text.matches(key.substring(1, key.length() - 1))){
                    return true;
                }
            }else if(text.contains(key)){
                return true;
            }
        }
        return false;
    }
}
