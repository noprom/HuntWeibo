package com.huntdreams.weibo.ui.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import static com.huntdreams.weibo.BuildConfig.DEBUG;

public class HackyViewPager extends ViewPager
{
	public HackyViewPager(Context context) {
		super(context);
	}
	
	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// Hack: Avoid a crash due to support library's exception
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			if (DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
	}
}
