package com.huntdreams.weibo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import static com.huntdreams.weibo.BuildConfig.DEBUG;
/**
 * 网络连接类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/29.
 */
public class ConnectivityReceiver extends BroadcastReceiver{

    private static final String TAG = ConnectivityReceiver.class.getSimpleName();
    public static boolean isWIFI = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(readNetworkState(context)){

        }
    }

    /**
     * 读取网络状态
     * @param context
     * @return
     */
    public static boolean readNetworkState(Context context){
        if(context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null && cm.getActiveNetworkInfo() != null &&cm.getActiveNetworkInfo().isConnected()){
            if(DEBUG){
                Log.d(TAG,"Network connected.");
            }

            isWIFI = (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
            return true;
        }else{
            if(DEBUG){
                Log.d(TAG,"Network disconnected.");
            }
            return false;
        }
    }
}
