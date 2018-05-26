package com.gghouse.wardah.wardahba.screen.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.Test;

/**
 * Created by michael on 5/4/2017.
 */

public class TestViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mTVScore;
    public final TextView mTVTitle;
    public final TextView mTVDescription;
    public final TextView mTVDate;
    public final TextView mTVMonthYear;
    public Test mItem;

    public TestViewHolder(View view) {
        super(view);
        mView = view;
        mTVScore = (TextView) view.findViewById(R.id.tv_score);
        mTVTitle = (TextView) view.findViewById(R.id.tv_title);
        mTVDescription = (TextView) view.findViewById(R.id.tv_description);
        mTVDate = (TextView) view.findViewById(R.id.tv_date);
        mTVMonthYear = (TextView) view.findViewById(R.id.tv_month_year);
    }
}