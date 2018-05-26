package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.ProductHighlight;
import com.gghouse.wardah.wardahba.util.WBAUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by michaelhalim on 2/26/17.
 */

public class SalesInputAdapter extends RecyclerView.Adapter<SalesInputAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ProductHighlight> mValues;

    public SalesInputAdapter(Context context, List<ProductHighlight> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_product_highlight, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTVTitle.setText(holder.mItem.getName());
        if (holder.mItem.getFileLocation() != null && !holder.mItem.getFileLocation().getThumbnailUrl().isEmpty()) {
            Picasso.with(mContext)
                    .load(WBAUtil.constructImageUrl(holder.mItem.getFileLocation().getThumbnailUrl()))
                    .fit()
                    .centerCrop()
                    .into(holder.mIVImage);
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.pic_image_not_found)
                    .fit()
                    .centerCrop()
                    .into(holder.mIVImage);
        }

        holder.mBAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.parseInt(holder.mETQty.getText().toString());
                holder.mETQty.setText(++qty + "");
                mValues.get(position).setQty(qty);
            }
        });

        holder.mBRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.parseInt(holder.mETQty.getText().toString());
                if (qty > 0) {
                    holder.mETQty.setText(--qty + "");
                    mValues.get(position).setQty(qty);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIVImage;
        public final TextView mTVTitle;
        public final TextView mTVDescription;
        public final EditText mETQty;
        public final Button mBRemove;
        public final Button mBAdd;
        public ProductHighlight mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIVImage = (ImageView) view.findViewById(R.id.iv_image);
            mTVTitle = (TextView) view.findViewById(R.id.tv_title);
            mTVDescription = (TextView) view.findViewById(R.id.tv_description);
            mETQty = (EditText) view.findViewById(R.id.et_qty);
            mBRemove = (Button) view.findViewById(R.id.b_remove);
            mBAdd = (Button) view.findViewById(R.id.b_add);
        }
    }
}
