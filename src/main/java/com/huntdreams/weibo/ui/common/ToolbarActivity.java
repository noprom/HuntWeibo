package com.huntdreams.weibo.ui.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

/**
 * Activity基类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class ToolbarActivity extends ActionBarActivity {

    protected Toolbar mToolbar;
    protected int mLayout = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= 21){
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(mLayout);

    }
}
