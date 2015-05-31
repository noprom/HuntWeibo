package com.huntdreams.weibo.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.PrivateKey;
import com.huntdreams.weibo.cache.login.LoginApiCache;
import com.huntdreams.weibo.support.common.Utility;
import com.huntdreams.weibo.ui.common.AbsActivity;

import static com.huntdreams.weibo.BuildConfig.DEBUG;
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
    private boolean mIsMulti = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayout = R.layout.activity_login;
        super.onCreate(savedInstanceState);

        // Init
        mIsMulti = getIntent().getBooleanExtra("multi", false);
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
            if(PrivateKey.isUrlRedirected(url)){
                view.stopLoading();
                handleRedirectedUrl(url);
            }else{
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && PrivateKey.isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }

    private void handleRedirectedUrl(String url){
        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token=");
            int expiresIndex = url.indexOf("expires_in=");
            String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));

            if (DEBUG) {
                Log.d(TAG, "url = " + url);
                Log.d(TAG, "token = " + token);
                Log.d(TAG, "expires_in = " + expiresIn);
            }

            new LoginTask().execute(token, expiresIn);
        } else {
            showLoginFail();
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Long>{

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage(getResources().getString(R.string.plz_wait));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Long doInBackground(String... params) {
            if(DEBUG){
                Log.d(TAG, "doInBackground...");
            }
            if(!mIsMulti){
                mLogin.login(params[0], params[1]);
                return mLogin.getExpireDate();
            }else{
                return mLogin.addUser(params[0], params[1]);
            }
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            if (!mIsMulti && mLogin.getAccessToken() != null) {
                if (DEBUG) {
                    Log.d(TAG, "Access Token:" + mLogin.getAccessToken());
                    Log.d(TAG, "Expires in:" + mLogin.getExpireDate());
                }
                mLogin.cache();
                BaseApi.setAccessToken(mLogin.getAccessToken());
            } else if (!mIsMulti && mLogin.getAccessToken() == null) {
                showLoginFail();
                return;
            }

            // Expire date
            String msg = String.format(getResources().getString(R.string.expires_in), Utility.expireTimeInDays(result));
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if (!mIsMulti) {
                                Intent i = new Intent();
                                i.setAction(Intent.ACTION_MAIN);
                                i.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    })
                    .create()
                    .show();

        }
    }
}
