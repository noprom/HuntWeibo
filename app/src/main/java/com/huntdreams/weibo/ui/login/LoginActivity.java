package com.huntdreams.weibo.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.api.BaseApi;
import com.huntdreams.weibo.api.PrivateKey;
import com.huntdreams.weibo.api.login.LoginApiCache;
import com.huntdreams.weibo.support.Utility;
import com.huntdreams.weibo.ui.main.MainActivity;

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
        if(PrivateKey.readFromPref(this)){
            mWeb.loadUrl(PrivateKey.getOauthLoginPage());
        }else{
            mWeb.loadUrl("about:blank");
            Log.d(TAG, "mWeb.loadUrl(\"about:blank\")");
            showAppKeyDialog(); // 显示密钥输入框
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }else if(item.getItemId() == R.id.home){
            showAppKeyDialog();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }

    private void handleRedirectedUrl(String url) {
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

    private void showLoginFail() {
        // Wrong username or password
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(R.string.login_fail)
                .setCancelable(true)
                .create()
                .show();
    }

    private void showAppKeyDialog() {
        // Inflate dialog layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.app_key, null);
        final EditText tvId = Utility.findViewById(v, R.id.app_id);
        final EditText tvSecret = Utility.findViewById(v, R.id.app_secret);
        final EditText tvRedirect = Utility.findViewById(v, R.id.redirect_uri);
        final EditText tvScope = Utility.findViewById(v, R.id.scope);
        final EditText tvPkg = Utility.findViewById(v, R.id.app_pkg);

        if(DEBUG){
            Log.d(TAG, "tvId = " + tvId);
            Log.d(TAG, "tvSecret = " + tvSecret);
            Log.d(TAG, "tvRedirect = " + tvRedirect);
            Log.d(TAG, "tvPkg = " + tvPkg);
            Log.d(TAG, "tvScope = " + tvScope);
        }
        // Initialize values
        String[] val = PrivateKey.getAll();
        tvId.setText(val[0]);
        tvSecret.setText(val[1]);
        tvRedirect.setText(val[2]);
        tvPkg.setText(val[3]);
        tvScope.setText(val[4]);

        // Initialize default values
        String[] data = decodeLoginData("SSMjExMTYwNjc5OjoxZTZlMzNkYjA4ZjkxOTIzMDZjNGFmYTBhNjFhZDU2Yzo6aHR0cDovL29hdXRoLndlaWNvLmNjOjplbWFpbCxkaXJlY3RfbWVzc2FnZXNfcmVhZCxkaXJlY3RfbWVzc2FnZXNfd3JpdGUsZnJpZW5kc2hpcHNfZ3JvdXBzX3JlYWQsZnJpZW5kc2hpcHNfZ3JvdXBzX3dyaXRlLHN0YXR1c2VzX3RvX21lX3JlYWQsZm9sbG93X2FwcF9vZmZpY2lhbF9taWNyb2Jsb2csaW52aXRhdGlvbl93cml0ZTo6Y29tLmVpY28ud2VpY286OkVFEE");
        if(DEBUG){
            Log.e(TAG, "app_id = " + data[0].trim());
            Log.e(TAG, "app_secret = " + data[1].trim());
            Log.e(TAG, "redirect_uri = " + data[2].trim());
            Log.e(TAG, "scope = " + data[3].trim());
            Log.e(TAG, "app_pkg = " + data[4].trim());
        }
        tvId.setText(data[0].trim());
        tvSecret.setText(data[1].trim());
        tvRedirect.setText(data[2].trim());
        tvScope.setText(data[3].trim());
        tvPkg.setText(data[4].trim());

        // Text listener
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {

            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if(isLoginData(text.toString())) {
                    String[] data = decodeLoginData(text.toString());

                    if (data == null || data.length < 5) return;

                    tvId.setText(data[0].trim());
                    tvSecret.setText(data[1].trim());
                    tvRedirect.setText(data[2].trim());
                    tvScope.setText(data[3].trim());
                    tvPkg.setText(data[4].trim());
                }
            }
        };
        tvId.addTextChangedListener(watcher);
        tvSecret.addTextChangedListener(watcher);
        tvRedirect.addTextChangedListener(watcher);
        tvScope.addTextChangedListener(watcher);
        tvPkg.addTextChangedListener(watcher);

        // Build the dialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.custom)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {

                    }
                })
                .setNeutralButton(R.string.app_copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {

                    }
                })
                .create();

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = tvId.getText().toString().trim();
                String sec = tvSecret.getText().toString().trim();
                String uri = tvRedirect.getText().toString().trim();
                String scope = tvScope.getText().toString().trim();
                String pkg = tvPkg.getText().toString().trim();

                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(sec)
                        && !TextUtils.isEmpty(uri) && !TextUtils.isEmpty(scope)) {

                    PrivateKey.setPrivateKey(id, sec, uri, pkg, scope);
                    PrivateKey.writeToPref(LoginActivity.this);
                    dialog.dismiss();
                    mWeb.loadUrl(PrivateKey.getOauthLoginPage());

                }
            }
        });

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.copyToClipboard(LoginActivity.this, encodeLoginData(
                        tvId.getText().toString(), tvSecret.getText().toString(),
                        tvRedirect.getText().toString(), tvScope.getText().toString(),
                        tvPkg.getText().toString()));
            }
        });
    }

    private static final String SEPERATOR = "::";
    private static final String START = "SS",
            END = "EE";
    private String encodeLoginData(String id, String secret, String uri, String scope, String pkg) {
        return START + Base64.encodeToString(
                (id + SEPERATOR + secret + SEPERATOR + uri + SEPERATOR +
                        scope + SEPERATOR + pkg + SEPERATOR + END).getBytes(), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING | Base64.NO_CLOSE) + END;
    }

    private String[] decodeLoginData(String str) {
        if (!isLoginData(str))
            return null;

        String data = str.substring(START.length(), str.length() - END.length() - 1);

        if (DEBUG) {
            Log.d(TAG, data);
        }

        try {
            return new String(Base64.decode(data, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING | Base64.NO_CLOSE)).split(SEPERATOR);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isLoginData(String str) {
        return str.startsWith(START) && str.length() > START.length() + END.length() && str.endsWith(END);
    }

    private class LoginWebViewClient extends WebViewClient{
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
        protected Long doInBackground(String... params) {
            if (DEBUG) {
                Log.d(TAG, "doInBackground...");
            }

            if (!mIsMulti) {
                mLogin.login(params[0], params[1]);
                return mLogin.getExpireDate();
            } else {
                return mLogin.addUser(params[0], params[1]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

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

            if(mLogin.getAccessToken() != null){
                if(DEBUG){
                    Log.d(TAG, "Access Token:" + mLogin.getAccessToken());
                    Log.d(TAG, "Expires in:" + mLogin.getExpireDate());
                }
                mLogin.cache();
                BaseApi.setAccessToken(mLogin.getAccessToken());
            }
            // Expire day
            String msg = String.format(getResources().getString(R.string.expires_in), Utility.expireTimeInDays(mLogin.getExpireDate()));
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_MAIN);
                            i.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
    }
}
