package com.huntdreams.weibo.support;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.huntdreams.weibo.R;

import java.util.concurrent.TimeUnit;

/**
 * 辅助工具类
 *
 * @author noprom
 * @email tyee.noprom@qq.com
 * Created by noprom on 15/7/1.
 */
public class Utility {

    /**
     * 即将在几天之内过期
     * @param time
     * @return
     */
    public static int expireTimeInDays(long time){
        return (int) TimeUnit.MILLISECONDS.toDays(time - System.currentTimeMillis());
    }

    /**
     * Token是否过期
     * @param time
     * @return
     */
    public static boolean isTokenExpired(long time){
        return time <= System.currentTimeMillis();
    }

    /**
     * 复制到剪切板
     * @param context
     * @param data
     */
    public static void copyToClipboard(Context context, String data) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("msg", data);
        cm.setPrimaryClip(cd);

        // Inform the user
        Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
    }

    public static <T> T findViewById(View v, int id) {
        return (T) v.findViewById(id);
    }

    public static <T> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }
}
