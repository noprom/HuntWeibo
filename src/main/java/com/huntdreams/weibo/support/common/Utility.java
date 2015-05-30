package com.huntdreams.weibo.support.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.huntdreams.weibo.service.ReminderService;

import java.util.concurrent.TimeUnit;

/**
 * 帮助工具
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class Utility {

    private static final String TAG = Utility.class.getSimpleName();

    private static final int REQUEST_CODE = 100001;

    /**
     * 检查Token是否过期
     * @param time
     * @return
     */
    public static boolean isTokenExpired(long time){
        return time <= System.currentTimeMillis();
    }


    /**
     * 缓存是否有效
     * @param createTime
     * @param availableDays
     * @return
     */
    public static boolean isCacheAvailable(long createTime, int availableDays){
        return System.currentTimeMillis() <= createTime + TimeUnit.DAYS.toMillis(availableDays);
    }

    /**
     * 开启一个定时服务
     * @param context
     * @param service
     * @param intveral
     */
    public static void startServiceAlarm(Context context, Class<?> service, long intveral){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context,service);
        PendingIntent p = PendingIntent.getService(context, REQUEST_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, intveral, p);
    }

    /**
     * 关闭一个定时服务
     * @param context
     * @param service
     */
    public static void stopServiceAlarm(Context context, Class<?> service){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, service);
        PendingIntent p = PendingIntent.getService(context,  REQUEST_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(p);
    }

    /**
     * 开启一个service
     * @param context
     */
    public static void startServices(Context context){
        Settings settings = Settings.getInstance(context);
        int interval = getIntervalTime(settings.getInt(Settings.NOTIFICATION_INTERVAL, 1));
        if(interval > -1){
            startServiceAlarm(context, ReminderService.class, interval);
        }
    }

    /**
     * 关闭一个service
     * @param context
     */
    public static void stopServices(Context context){
        stopServiceAlarm(context, ReminderService.class);
    }

    /**
     * 重启一个service
     * @param context
     */
    public static void restartServices(Context context){
        stopServices(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()){
            startServices(context);
        }
    }

    /**
     * 根据ID获得间隔间隔触发时间
     * @param id
     * @return
     */
    public static int getIntervalTime(int id) {
        switch (id){
            case 0:
                return 1 * 60 * 1000;
            case 1:
                return 3 * 60 * 1000;
            case 2:
                return 5 * 60 * 1000;
            case 3:
                return 10 * 60 * 1000;
            case 4:
                return 30 * 60 * 1000;
            case 5:
                return -1;
        }
        return -1;
    }
}
