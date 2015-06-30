package com.huntdreams.weibo.ui.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.support.common.Utility;

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
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(mLayout);

        mToolbar = Utility.findViewById(this, R.id.toolbar);

        if (mToolbar != null) {
            mToolbar.bringToFront();
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (Build.VERSION.SDK_INT >= 21) {
//                TODO add a shadow
//                mToolbar.setElevation(getToolbarElevation());
//
//                View shadow = Utility.findViewById(this, R.id.action_shadow);
//
//                if (shadow != null) {
//                    shadow.setVisibility(View.GONE);
//                }
            }
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public float getToolbarElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            return 12.8f;
        } else {
            return -1;
        }
    }
}
