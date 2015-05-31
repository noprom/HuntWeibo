package com.huntdreams.weibo.ui.entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huntdreams.weibo.cache.file.FileCacheManager;
import com.huntdreams.weibo.cache.login.LoginApiCache;
import com.huntdreams.weibo.receiver.ConnectivityReceiver;
import com.huntdreams.weibo.support.common.CrashHandler;
import com.huntdreams.weibo.support.common.FilterUtility;
import com.huntdreams.weibo.support.common.Utility;
import com.huntdreams.weibo.ui.login.LoginActivity;
import com.huntdreams.weibo.ui.main.MainActivity;
import static com.huntdreams.weibo.BuildConfig.DEBUG;
/**
 * 应用程序入口：授权新浪微博接口
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class EntryActivity extends Activity{
    private static final String TAG = EntryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CrashHandler.init(this);
        CrashHandler.register();
        super.onCreate(savedInstanceState);
        if(DEBUG){
            Log.d(TAG, "onCreate()");
        }

        // Clear cache
        FileCacheManager.instance(this).clearUnavailable();
        // Init
        ConnectivityReceiver.readNetworkState(this);
        // TODO init emotions
        FilterUtility.init(this);
        // TODO Crash log

        LoginApiCache login = new LoginApiCache(this);
        if(needsLogin(login)){
            login.logout();
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, LoginActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, MainActivity.class);
            i.putExtra(Intent.EXTRA_INTENT, getIntent().getIntExtra(Intent.EXTRA_INTENT,0));
            startActivity(i);
            finish();
        }
    }

    private boolean needsLogin(LoginApiCache login){
        return login.getAccessToken() == null || Utility.isTokenExpired(login.getExpireDate());
    }
}
