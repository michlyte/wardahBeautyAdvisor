package com.gghouse.wardah.wardahba.screen.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.SalesHistoryHeader;

/**
 * Created by michael on 5/4/2017.
 */

public class SalesHistoryHeaderViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    public final TextView tvAverage;
    public final TextView tvCountByMonth;
    public final TextView tvCount;
    public final TextView tvHighestPerMonth;
    public SalesHistoryHeader mItem;

    public SalesHistoryHeaderViewHolder(View view) {
        super(view);

        this.view = view;
        tvAverage = (TextView) view.findViewById(R.id.tv_average);
        tvCountByMonth = (TextView) view.findViewById(R.id.tv_count_by_month);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
        tvHighestPerMonth = (TextView) view.findViewById(R.id.tv_highest_per_month);
    }
}
