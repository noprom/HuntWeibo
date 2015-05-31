package com.huntdreams.weibo.ui.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.huntdreams.weibo.support.common.Settings;
import com.huntdreams.weibo.support.common.ShakeDetector;
import com.huntdreams.weibo.support.common.Utility;

/**
 * AbsActivity
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class AbsActivity extends ToolbarActivity implements ShakeDetector.ShakeListener {

    private ShakeDetector mDetector;
    private Settings mSettings;
    private int mLang = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO change language
        Utility.initDarkMode(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onShake() {

    }

    @TargetApi(21)
    private void swipeInit(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // TODO init SlidingPaneLayout

    }
}
