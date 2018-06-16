package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Pelanggan;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.LoadingViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.PelangganViewHolder;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;

import java.util.List;

public class PelangganHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;

    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private List<Pelanggan> mDataSet;

    public PelangganHistoryAdapter(Context context, RecyclerView recyclerView, List<Pelanggan> dataSet) {
        mContext = context;
        mRecyclerView = recyclerView;
        mDataSet = dataSet;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + WBAProperties.PELANGGAN_ITEM_PER_PAGE)) {
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
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_pelanggan, parent, false);
            return new PelangganViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                final PelangganViewHolder pelangganViewHolder = (PelangganViewHolder) holder;
                pelangganViewHolder.mItem = (Pelanggan) mDataSet.get(position);
                pelangganViewHolder.mTVName.setText(pelangganViewHolder.mItem.getName());
                pelangganViewHolder.mTVEmail.setText(pelangganViewHolder.mItem.getEmail());
                break;
            case VIEW_TYPE_LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
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

    public void add(Pelanggan pelanggan) {
        mDataSet.add(pelanggan);
    }

    public void addAll(List<Pelanggan> list) {
        mDataSet.addAll(list);
    }

    public void remove(int i) {
        mDataSet.remove(i);
    }

    public void setData(List<Pelanggan> dataSet) {
        this.mDataSet = dataSet;
    }

    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    public List<?> getData() {
        return mDataSet;
    }
}
