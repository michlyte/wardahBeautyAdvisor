package com.gghouse.wardah.wardahba.screen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Test;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.TestViewHolder;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Test> mDataSet;

    public TestAdapter(List<Test> items) {
        mDataSet = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final TestViewHolder testViewHolder = (TestViewHolder) holder;
        testViewHolder.mItem = mDataSet.get(position);
        // Score
        testViewHolder.mTVScore.setText(testViewHolder.mItem.getScore() + "");
        String day = WBAProperties.sdfDay.format(testViewHolder.mItem.getCreatedTime());
        testViewHolder.mTVTitle.setText(day);
        testViewHolder.mTVDescription.setText(WardahApp.getInstance().getAppContext().getString(R.string.title_jumlah_soal, testViewHolder.mItem.getTotalSoal() + ""));
        String date = WBAProperties.sdfDateOnly.format(testViewHolder.mItem.getCreatedTime());
        testViewHolder.mTVDate.setText(date);
        String monthYear = WBAProperties.sdfMonthYear.format(testViewHolder.mItem.getCreatedTime());
        testViewHolder.mTVMonthYear.setText(monthYear);
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void add(Test test) {
        mDataSet.add(test);
    }

    public void setData(List<Test> dataSet) {
        mDataSet = dataSet;
    }
}
