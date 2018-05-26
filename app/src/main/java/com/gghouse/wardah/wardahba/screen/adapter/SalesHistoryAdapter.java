package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.model.SalesHistoryHeader;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.LoadingViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.SalesHistoryHeaderViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.SalesViewHolder;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.SalesListener;
import com.gghouse.wardah.wardahba.util.WBASystem;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by michael on 5/4/2017.
 */

public class SalesHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_HEADER = 2;

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private final boolean mHasHeader;
    private List<Sales> mDataSet;

    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;

    private SalesListener mSalesListener;

    public SalesHistoryAdapter(Context context, RecyclerView recyclerView, List<Sales> dataSet, boolean hasHeader, SalesListener salesListener) {
        mContext = context;
        mRecyclerView = recyclerView;
        mDataSet = dataSet;
        mHasHeader = hasHeader;

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);

        mSalesListener = salesListener;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + WBAProperties.SALES_ITEM_PER_PAGE)) {
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
                    .inflate(R.layout.view_sales, parent, false);
            return new SalesViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        } else if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_sales_history_header, parent, false);
            return new SalesHistoryHeaderViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                SalesHistoryHeaderViewHolder salesHistoryHeaderViewHolder = (SalesHistoryHeaderViewHolder) holder;
                salesHistoryHeaderViewHolder.mItem = (SalesHistoryHeader) mDataSet.get(position);

                String average = WBASystem.withSuffix(salesHistoryHeaderViewHolder.mItem.getAverage() != null ? salesHistoryHeaderViewHolder.mItem.getAverage() : 0.00, 0);
                String countByMonth = WBASystem.withSuffix(salesHistoryHeaderViewHolder.mItem.getCountByMonth() != null ? salesHistoryHeaderViewHolder.mItem.getCountByMonth() : 0.00, 0);
                String total = WBASystem.withSuffix(salesHistoryHeaderViewHolder.mItem.getCount() != null ? salesHistoryHeaderViewHolder.mItem.getCount() : 0.00, 0);
                String highestPerMonth = WBASystem.withSuffix(salesHistoryHeaderViewHolder.mItem.getHighestPerMonth() != null ? salesHistoryHeaderViewHolder.mItem.getHighestPerMonth() : 0.00, 0);

                salesHistoryHeaderViewHolder.tvAverage.setText(average);
                salesHistoryHeaderViewHolder.tvCountByMonth.setText(countByMonth);
                salesHistoryHeaderViewHolder.tvCount.setText(total);
                salesHistoryHeaderViewHolder.tvHighestPerMonth.setText(highestPerMonth);
                break;
            case VIEW_TYPE_ITEM:
                final SalesViewHolder salesViewHolder = (SalesViewHolder) holder;
                salesViewHolder.mItem = mDataSet.get(position);
                String day = WBAProperties.sdfDay.format(salesViewHolder.mItem.getCreatedTime());
                String date = WBAProperties.sdfNotif.format(salesViewHolder.mItem.getCreatedTime());
                salesViewHolder.mTVTitle.setText(day);
                salesViewHolder.mTVDate.setText(date);
                salesViewHolder.mTVValue.setText(formatter.format(salesViewHolder.mItem.getSalesAmount().longValue()));

                if (salesViewHolder.mItem.isEditable()) {
//                    salesViewHolder.mIVEdit.setVisibility(View.VISIBLE);
                    salesViewHolder.mLlContainer.setBackgroundResource(R.color.colorEditable);
                    salesViewHolder.mLlContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSalesListener.onClick(salesViewHolder.mItem);
                        }
                    });
                } else {
//                    salesViewHolder.mIVEdit.setVisibility(View.GONE);
                    salesViewHolder.mLlContainer.setBackgroundResource(android.R.color.transparent);
                    salesViewHolder.mLlContainer.setOnClickListener(null);
                }

//                salesViewHolder.mIVEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mSalesListener.onClick(salesViewHolder.mItem);
//                    }
//                });
                break;
            case VIEW_TYPE_LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeader) {
            switch (position) {
                /*
                 * Header
                 */
                case 0:
                    return VIEW_TYPE_HEADER;
                default:
                    return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
            }
        } else {
            return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }
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

    public void add(Sales sales) {
        mDataSet.add(sales);
    }

    public void addAll(List<Sales> salesList) {
        mDataSet.addAll(salesList);
    }

    public void remove(int i) {
        mDataSet.remove(i);
    }

    public void setData(List<Sales> dataSet) {
        this.mDataSet = dataSet;
    }

    public Sales getItem(int position) {
        return mDataSet.get(position);
    }

    public void setHeaderData(SalesHistoryHeader salesHistoryHeader) {
        if (mDataSet.size() > 0) {
            if (mDataSet.get(0) instanceof SalesHistoryHeader) {
                mDataSet.set(0, salesHistoryHeader);
            } else {
                mDataSet.add(0, salesHistoryHeader);
            }
        } else {
            mDataSet.add(salesHistoryHeader);
        }
    }

    public List<Sales> getData() {
        return mDataSet;
    }

    public void refreshData() {
        if (mHasHeader) {
            SalesHistoryHeader salesHistoryHeader = (SalesHistoryHeader) mDataSet.get(0);
            mDataSet.clear();
            mDataSet.add(salesHistoryHeader);
        } else {
            mDataSet.clear();
        }
    }
}
