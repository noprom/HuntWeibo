package com.huntdreams.weibo.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * 接受来自新浪微博的通知
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/30.
 */
public class ReminderService extends IntentService{
    private static final String TAG = ReminderService.class.getSimpleName();

    private static final int ID = 100000;
    private static final int ID_CMT = ID + 1;
    private static final int ID_MENTION = ID + 2;
    private static final int ID_DM = ID + 3;


    public ReminderService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
