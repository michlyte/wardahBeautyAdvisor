package com.gghouse.wardah.wardahba.screen.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.Sales;

/**
 * Created by michaelhalim on 5/9/17.
 */

public class SalesViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final LinearLayout mLlContainer;
    public final TextView mTVTitle;
    public final TextView mTVValue;
    public final TextView mTVDate;
    public final ImageView mIVEdit;
    public Sales mItem;

    public SalesViewHolder(View view) {
        super(view);
        mView = view;
        mLlContainer = (LinearLayout) mView.findViewById(R.id.ll_container);
        mTVTitle = (TextView) mView.findViewById(R.id.tv_title);
        mTVValue = (TextView) mView.findViewById(R.id.tv_value);
        mTVDate = (TextView) mView.findViewById(R.id.tv_date);
        mIVEdit = (ImageView) mView.findViewById(R.id.iv_edit);
    }
}
