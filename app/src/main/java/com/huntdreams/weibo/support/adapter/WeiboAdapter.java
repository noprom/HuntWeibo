package com.huntdreams.weibo.support.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 微博Adapter
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/6/1.
 */
public class WeiboAdapter extends HeaderViewAdapter<WeiboAdapter.ViewHolder>{

    public WeiboAdapter(RecyclerView v) {
        super(v);
    }

    @Override
    int getCount() {
        return 0;
    }

    @Override
    int getViewType(int position) {
        return 0;
    }

    @Override
    long getItemViewId(int position) {
        return 0;
    }

    @Override
    void doRecycleView(ViewHolder h) {

    }

    @Override
    ViewHolder doCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    ViewHolder doCreateHeaderHolder(View header) {
        return null;
    }

    @Override
    void doBindViewHolder(ViewHolder h, int position) {

    }

    public static class ViewHolder extends HeaderViewAdapter.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
