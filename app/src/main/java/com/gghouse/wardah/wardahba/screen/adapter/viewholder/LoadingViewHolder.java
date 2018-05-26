package com.gghouse.wardah.wardahba.screen.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.view.View;

import com.gghouse.wardah.wardahba.R;

/**
 * Created by michael on 5/4/2017.
 */

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}
