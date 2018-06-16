package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.enumeration.SimpleAdapterTypeEnum;
import com.gghouse.wardah.wardahba.model.Pelanggan;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.model.Test;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.PelangganViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.SalesViewHolder;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.TestViewHolder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WardahSimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final SimpleAdapterTypeEnum mSimpleAdapterTypeEnum;
    private List<?> mValues;

    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;

    public WardahSimpleAdapter(Context context, SimpleAdapterTypeEnum simpleAdapterTypeEnum, List<?> items) {
        mContext = context;
        mSimpleAdapterTypeEnum = simpleAdapterTypeEnum;
        mValues = items;

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (mSimpleAdapterTypeEnum) {
            case PELANGGAN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pelanggan, parent, false);
                return new PelangganViewHolder(view);
            case TEST:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_test, parent, false);
                return new TestViewHolder(view);
            case SALES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_sales, parent, false);
                return new SalesViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mSimpleAdapterTypeEnum) {
            case PELANGGAN:
                final PelangganViewHolder pelangganViewHolder = (PelangganViewHolder) holder;
                pelangganViewHolder.mItem = (Pelanggan) mValues.get(position);
                pelangganViewHolder.mTVName.setText(pelangganViewHolder.mItem.getName());
                pelangganViewHolder.mTVEmail.setText(pelangganViewHolder.mItem.getEmail());
                break;
            case TEST:
                final TestViewHolder testViewHolder = (TestViewHolder) holder;
                testViewHolder.mItem = (Test) mValues.get(position);
                // Score
                testViewHolder.mTVScore.setText(testViewHolder.mItem.getScore() + "");
                String testDay = WBAProperties.sdfDay.format(testViewHolder.mItem.getCreatedTime());
                testViewHolder.mTVTitle.setText(testDay);
                testViewHolder.mTVDescription.setText(WardahApp.getInstance().getAppContext().getString(R.string.title_jumlah_soal, testViewHolder.mItem.getTotalSoal() + ""));
                String testDate = WBAProperties.sdfDateOnly.format(testViewHolder.mItem.getCreatedTime());
                testViewHolder.mTVDate.setText(testDate);
                String monthYear = WBAProperties.sdfMonthYear.format(testViewHolder.mItem.getCreatedTime());
                testViewHolder.mTVMonthYear.setText(monthYear);
                break;
            case SALES:
                final SalesViewHolder salesViewHolder = (SalesViewHolder) holder;
                salesViewHolder.mItem = (Sales) mValues.get(position);
                String day = WBAProperties.sdfDay.format(salesViewHolder.mItem.getCreatedTime());
                String date = WBAProperties.sdfNotif.format(salesViewHolder.mItem.getCreatedTime());
                salesViewHolder.mTVTitle.setText(day);
                salesViewHolder.mTVDate.setText(date);
                salesViewHolder.mTVValue.setText(formatter.format(salesViewHolder.mItem.getSalesAmount().longValue()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setData(List<?> mValues) {
        this.mValues = mValues;
    }
}
