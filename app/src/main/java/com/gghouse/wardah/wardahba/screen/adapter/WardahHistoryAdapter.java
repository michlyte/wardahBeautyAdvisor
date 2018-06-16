package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;

public class WardahHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected OnLoadMoreListener mOnLoadMoreListener;
    protected boolean isLoading;
    protected int lastVisibleItem, totalItemCount;

    protected final Context mContext;
    protected final RecyclerView mRecyclerView;

    public WardahHistoryAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + WBAProperties.HISTORY_ITEM_PER_PAGE)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void removeOnLoadMoreListener() {
        this.mOnLoadMoreListener = null;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }
}
