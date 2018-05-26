package com.gghouse.wardah.wardahba.screen.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.TestHistoryHeader;

/**
 * Created by michael on 5/4/2017.
 */

public class TestHistoryHeaderViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    public final TextView tvAverage;
    public final TextView tvRank;
    public final TextView tvSumTestTaken;
    public TestHistoryHeader mItem;

    public TestHistoryHeaderViewHolder(View view) {
        super(view);

        this.view = view;
        tvAverage = (TextView) view.findViewById(R.id.tv_average);
        tvRank = (TextView) view.findViewById(R.id.tv_rank);
        tvSumTestTaken = (TextView) view.findViewById(R.id.tv_sum_test_taken);
    }
}
