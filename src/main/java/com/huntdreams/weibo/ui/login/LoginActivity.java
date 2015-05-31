package com.huntdreams.weibo.ui.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.cache.login.LoginApiCache;
import com.huntdreams.weibo.support.common.Utility;
import com.huntdreams.weibo.ui.common.AbsActivity;

/**
 * 微博登陆界面
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class LoginActivity extends AbsActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private WebView mWeb;
    private LoginApiCache mLogin;
    private boolean mIsMutli = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayout = R.layout.activity_login;
        super.onCreate(savedInstanceState);

        // Init
        mIsMutli = getIntent().getBooleanExtra("multi", false);
        mWeb = Utility.findViewById(this, R.id.login_web);
        mLogin = new LoginApiCache(this);

        // Setup webview
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWeb.setWebViewClient(new MyWebViewClient());
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
