package com.huntdreams.weibo.ui.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntdreams.weibo.R;
import com.huntdreams.weibo.cache.search.SearchHistoryCache;
import com.huntdreams.weibo.ui.common.ToolbarActivity;
import com.quinny898.library.persistentsearch.SearchBox;

import static com.huntdreams.weibo.BuildConfig.DEBUG;

/**
 * 应用程序主界面
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/6/1.
 */
public class MainActivity extends ToolbarActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    // 刷新接口
    public static interface Refresher{
        void doRefresh();
        void goToTop();
    }

    public static interface HeaderProvider{
        float getHeaderFactor();
    }

    public static final int REQUEST_LOGIN = 2333;
    public static final int HOME = 0;
    public static final int COMMENT = 1;
    public static final int MENTION = 2;
    public static final int MENTION_CMT = 3;
    public static final int DM = 4;
    public static final int FAV = 5;

    private static final String BILATERAL = "bilateral";

    // Views
    private DrawerLayout mDrawer;
    private int mDrawerGravity;
    private ActionBarDrawerToggle mToggle;
    private SearchBox mSearchBox;
    private SearchHistoryCache mSearhHistory;
    private View mDim;

    // Drawer content
    private View mDrawerWrapper;
    private TextView mName;
    private View mAccountSwitch, mAccountSwitchIcon;
    private ImageView mAvatar;
    private ImageView mCover;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(DEBUG){
            Log.d(TAG, "onCreate()");
        }

    }
}
