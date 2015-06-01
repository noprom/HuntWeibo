package com.huntdreams.weibo.support.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A adapter which adds HeaderView
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/6/1.
 */
public abstract class HeaderViewAdapter<VH extends HeaderViewAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    private View mHeader = null;
    private RecyclerView mRecyclerView;
    protected RecyclerView.OnScrollListener mListener;
    protected List<RecyclerView.OnScrollListener> mListeners = new ArrayList<RecyclerView.OnScrollListener>();

    public HeaderViewAdapter(RecyclerView v){
        mRecyclerView = v;
        mRecyclerView.setOnScrollListener((mListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                for(RecyclerView.OnScrollListener listener : mListeners){
                    listener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                for (RecyclerView.OnScrollListener listener : mListeners) {
                    listener.onScrolled(recyclerView, dx, dy);
                }
            }
        }));
    }

    public void setHeaderView(View header){
        mHeader = header;
        notifyDataSetChanged();
    }

    public View getHeaderView(){return mHeader;}

    public boolean hasHeaderView(){return mHeader != null;}

    public void addOnScrollListener(RecyclerView.OnScrollListener listener){
        mListeners.add(listener);
    }

    public void notifyDataSetChangedAndClone(){
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeader != null && viewType == Integer.MIN_VALUE){
            return doCreateHeaderHolder(mHeader);
        }else{
            return doCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(mHeader != null && position != 0){
            position --;
        }
        if(!holder.isHeader){
            doBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        int count = getCount();
        if(mHeader != null){
            ++ count;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeader != null){
            if(position == 0){
                return Integer.MIN_VALUE;
            }else{
                -- position;
            }
        }
        return getViewType(position);
    }

    @Override
    public long getItemId(int position) {
        if(mHeader != null){
            if(position == 0){
                return 0;
            }else{
                position --;
            }
        }
        return getItemViewId(position);
    }

    @Override
    public void onViewRecycled(VH holder) {
        if(!holder.isHeader){
            doRecycleView(holder);
        }
    }

    abstract int getCount();
    abstract int getViewType(int position);
    abstract long getItemViewId(int position);
    abstract void doRecycleView(VH h);
    abstract VH doCreateViewHolder(ViewGroup parent, int viewType);
    abstract VH doCreateHeaderHolder(View header);
    abstract void doBindViewHolder(VH h, int position);

    public static abstract class ViewHolder extends RecyclerView.ViewHolder{

        public boolean isHeader = false;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
