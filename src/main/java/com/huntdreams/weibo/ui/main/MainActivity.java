package com.huntdreams.weibo.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.huntdreams.weibo.R;

import static com.huntdreams.weibo.BuildConfig.DEBUG;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(DEBUG){
            Log.d(TAG, "onCreate()");
        }

    }
}
