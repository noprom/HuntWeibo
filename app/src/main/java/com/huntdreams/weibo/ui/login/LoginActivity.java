package com.huntdreams.weibo.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.login.LoginApiCache;
import com.huntdreams.weibo.support.Utility;

import static com.huntdreams.weibo.BuildConfig.DEBUG;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private WebView mWeb;
    private LoginApiCache mLogin;
    private boolean mIsMulti = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIsMulti = getIntent().getBooleanExtra("multi", false);
        // Intialize views
        mWeb = (WebView) findViewById(R.id.login_web);
        // Intialize login cache
        mLogin = new LoginApiCache(this);
        // Setup login page
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWeb.setWebViewClient(new LoginWebViewClient());
        // TODO set PRIVATEKEY
    }

    private class LoginWebViewClient extends WebViewClient{

    }
    private class LoginTask extends AsyncTask<String, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.plz_wait));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            if(DEBUG){
                Log.d(TAG, "doInBackground...");
            }
            mLogin.login(params[0], params[1], params[2], params[3]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            // TODO Use user api to see if the login is successful
            if(mLogin.getAccessToken() != null){
                if(DEBUG){
                    Log.d(TAG, "Access Token:" + mLogin.getAccessToken());
                    Log.d(TAG, "Expires in:" + mLogin.getExpireDate());
                }
                mLogin.cache();
                BaseApi.setAccessToken(mLogin.getAccessToken());
                // Expire day
                String msg = String.format(getResources().getString(R.string.expires_in), Utility.expireTimeInDays(mLogin.getExpireDate()));
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                // TODO Enter main timeline
                                finish();
                            }
                        })
                        .create()
                        .show();
            }else{
                // Wrong username or password
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(R.string.login_fail)
                        .setCancelable(true)
                        .create()
                        .show();
            }
        }
    }
}
