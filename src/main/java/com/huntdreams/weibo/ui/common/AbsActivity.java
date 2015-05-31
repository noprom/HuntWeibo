package com.huntdreams.weibo.ui.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huntdreams.weibo.R;
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

    protected View getSwipeView(){
        return ((ViewGroup) getWindow().getDecorView()).getChildAt(0);
    }

    @TargetApi(21)
    private void swipeInit(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SlidingPaneLayout v = (SlidingPaneLayout) inflater.inflate(R.layout.swipe_decor_wrapper, null);
        final ViewGroup frame = (ViewGroup) v.findViewById(R.id.swipe_container);
        View swipeView = getSwipeView();
        ViewGroup decor = (ViewGroup) swipeView.getParent();
        ViewGroup.LayoutParams params = swipeView.getLayoutParams();
        decor.removeView(swipeView);
        frame.addView(swipeView);
        decor.addView(v, params);
        decor.setBackgroundColor(0);

        // Elevation
        if(Build.VERSION.SDK_INT >= 21){
            frame.setElevation(11.8f);
        } else {
            v.setShadowResource(R.drawable.panel_shadow);
        }



    }
}
