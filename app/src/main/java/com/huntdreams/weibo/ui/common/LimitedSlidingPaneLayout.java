package com.huntdreams.weibo.ui.common;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义SlidingPaneLayout
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/31.
 */
public class LimitedSlidingPaneLayout extends SlidingPaneLayout {

    private boolean mIsSliding = false;

    public LimitedSlidingPaneLayout(Context context) {
        this(context, null);
    }

    public LimitedSlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitedSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            if (ev.getX() >= getWidth() * 0.1f) {
                mIsSliding = false;
                return false;
            } else {
                mIsSliding = true;
            }
        } else if (action == MotionEvent.ACTION_MOVE && !mIsSliding) {
            return false;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            boolean shouldRet = false;
            if (!mIsSliding) {
                shouldRet = true;
            }

            mIsSliding = false;

            if (shouldRet) return false;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
