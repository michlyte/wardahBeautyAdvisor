package com.gghouse.wardah.wardahba.screen.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.Pelanggan;

public class PelangganViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mTVName;
    public final TextView mTVEmail;
    public Pelanggan mItem;

    public PelangganViewHolder(View view) {
        super(view);
        mView = view;
        mTVName = (TextView) mView.findViewById(R.id.tv_name);
        mTVEmail = (TextView) mView.findViewById(R.id.tv_email);
    }
}
