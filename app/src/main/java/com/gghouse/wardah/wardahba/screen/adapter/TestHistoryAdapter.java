package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Test;
import com.gghouse.wardah.wardahba.model.TestHistoryHeader;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.LoadingViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.TestHistoryHeaderViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.TestViewHolder;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by michael on 5/4/2017.
 */

public class TestHistoryAdapter extends WardahHistoryAdapter {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_HEADER = 2;

    private final boolean mHasHeader;
    private List<Test> mDataSet;

    /*
     * Decimal Format
     */
    private DecimalFormat formatter;

    public TestHistoryAdapter(Context context, RecyclerView recyclerView, List<Test> dataSet, boolean hasHeader) {
        super(context, recyclerView);
        mDataSet = dataSet;
        mHasHeader = hasHeader;

        formatter = new DecimalFormat("#0.0%");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_test, parent, false);
            return new TestViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        } else if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_history_header, parent, false);
            return new TestHistoryHeaderViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                TestHistoryHeaderViewHolder testHistoryHeaderViewHolder = (TestHistoryHeaderViewHolder) holder;

                testHistoryHeaderViewHolder.mItem = (TestHistoryHeader) mDataSet.get(position);
                testHistoryHeaderViewHolder.tvAverage.setText(String.format("%.2f", testHistoryHeaderViewHolder.mItem.getAverage()));
                testHistoryHeaderViewHolder.tvRank.setText(formatter.format(testHistoryHeaderViewHolder.mItem.getCorrectRate()));
                testHistoryHeaderViewHolder.tvSumTestTaken.setText(testHistoryHeaderViewHolder.mItem.getSumTestTaken() + "");
                break;
            case VIEW_TYPE_ITEM:
                final TestViewHolder testViewHolder = (TestViewHolder) holder;
                testViewHolder.mItem = mDataSet.get(position);
                testViewHolder.mTVScore.setText(testViewHolder.mItem.getScore() + "");
                String day = WBAProperties.sdfDay.format(testViewHolder.mItem.getCreatedTime());
                testViewHolder.mTVTitle.setText(day);
                testViewHolder.mTVDescription.setText(WardahApp.getInstance().getAppContext().getString(R.string.title_jumlah_soal, testViewHolder.mItem.getTotalSoal() + ""));
                String date = WBAProperties.sdfDateOnly.format(testViewHolder.mItem.getCreatedTime());
                testViewHolder.mTVDate.setText(date);
                String monthYear = WBAProperties.sdfMonthYear.format(testViewHolder.mItem.getCreatedTime());
                testViewHolder.mTVMonthYear.setText(monthYear);
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

    public void add(Test test) {
        mDataSet.add(test);
    }

    public void addAll(List<Test> testList) {
        mDataSet.addAll(testList);
    }

    public void remove(int i) {
        mDataSet.remove(i);
    }

    public void setData(List<Test> dataSet) {
        this.mDataSet = dataSet;
    }

    public void setHeaderData(TestHistoryHeader testHistoryHeader) {
        if (mDataSet.size() > 0) {
            if (mDataSet.get(0) instanceof TestHistoryHeader) {
                mDataSet.set(0, testHistoryHeader);
            } else {
                mDataSet.add(0, testHistoryHeader);
            }
        } else {
            mDataSet.add(testHistoryHeader);
        }
    }

    public List<Test> getData() {
        return mDataSet;
    }

    public void refreshData() {
        if (mHasHeader) {
            TestHistoryHeader testHistoryHeader = (TestHistoryHeader) mDataSet.get(0);
            mDataSet.clear();
            mDataSet.add(testHistoryHeader);
        } else {
            mDataSet.clear();
        }
    }
}
