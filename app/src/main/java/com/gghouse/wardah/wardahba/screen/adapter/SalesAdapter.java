package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.screen.adapter.viewholder.SalesViewHolder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SalesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;

    private final Context mContext;
    private List<Sales> mValues;

    public SalesAdapter(Context context, List<Sales> items) {
        mContext = context;
        mValues = items;

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sales, parent, false);
        return new SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SalesViewHolder salesViewHolder = (SalesViewHolder) holder;
        salesViewHolder.mItem = mValues.get(position);
        String day = WBAProperties.sdfDay.format(salesViewHolder.mItem.getCreatedTime());
        String date = WBAProperties.sdfNotif.format(salesViewHolder.mItem.getCreatedTime());
        salesViewHolder.mTVTitle.setText(day);
        salesViewHolder.mTVDate.setText(date);
        salesViewHolder.mTVValue.setText(formatter.format(salesViewHolder.mItem.getSalesAmount().longValue()));
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void add(Sales sales) {
        mValues.add(sales);
    }

    public void setData(List<Sales> dataSet) {
        mValues = dataSet;
    }

    public Sales getItem(int position) {
        return mValues.get(position);
    }
}
