package com.huntdreams.weibo.ui.entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.login.LoginApiCache;
import com.huntdreams.weibo.support.Utility;
import com.huntdreams.weibo.ui.login.LoginActivity;
import com.huntdreams.weibo.ui.main.MainActivity;

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
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, MainActivity.class);
            i.putExtra(Intent.EXTRA_INTENT, getIntent().getIntExtra(Intent.EXTRA_INTENT, 0));
            startActivity(i);
            finish();
        }
    }

    /**
     * 是否需要登陆
     * @param login
     * @return
     */
    private boolean needsLogin(LoginApiCache login){
        return login.getAccessToken() == null || Utility.isTokenExpired(login.getExpireDate());
    }


}
