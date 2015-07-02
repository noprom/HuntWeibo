package com.huntdreams.weibo.ui.entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.login.LoginApiCache;
import com.huntdreams.weibo.support.Utility;
import com.huntdreams.weibo.ui.login.LoginActivity;

public class EntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginApiCache login = new LoginApiCache(this);
        if(needsLogin(login)){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, LoginActivity.class);
            startActivity(i);
            finish();
        }else{
            BaseApi.setAccessToken(login.getAccessToken());
            // TODO Enter the main time line
        }
    }

    /**
     * 是否需要登陆
     * @param login
     * @return
     */
    private boolean needsLogin(LoginApiCache login){
        // TODO consider expire date
        return login.getAccessToken() == null || Utility.isTokenExpired(login.getExpireDate());
    }


}
