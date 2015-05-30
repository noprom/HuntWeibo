package com.huntdreams.weibo.ui.entry;

import android.app.Activity;
import android.os.Bundle;

import com.huntdreams.weibo.cache.file.FileCacheManager;
import com.huntdreams.weibo.receiver.ConnectivityReceiver;
import com.huntdreams.weibo.support.common.CrashHandler;
import com.huntdreams.weibo.support.common.FilterUtility;

/**
 * 应用程序入口：授权新浪微博接口
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class EntryActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CrashHandler.init(this);
        CrashHandler.register();
        super.onCreate(savedInstanceState);

        // Clear cache
        FileCacheManager.instance(this).clearUnavailable();
        // Init
        ConnectivityReceiver.readNetworkState(this);
        // TODO init emotions
        FilterUtility.init(this);
        // TODO Crash log


    }
}
