package com.gghouse.wardah.wardahba.screen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.Questioner;

import java.util.List;

/**
 * Created by michaelhalim on 5/25/17.
 */

public class QuestionerAdapter extends RecyclerView.Adapter<QuestionerAdapter.ViewHolder> {
    private List<Questioner> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTVQuestion;
        public final RatingBar mRBRating;
        public Questioner mItem;

        public ViewHolder(View v) {
            super(v);
            mTVQuestion = (TextView) v.findViewById(R.id.tv_question);
            mRBRating = (RatingBar) v.findViewById(R.id.rb_rating);
        }
    }

    public QuestionerAdapter(List<Questioner> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public QuestionerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_questioner, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mItem = mDataset.get(position);
        holder.mTVQuestion.setText(holder.mItem.getContent());
        holder.mRBRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mDataset.get(position).setRating(rating);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(List<Questioner> dataSet) {
        mDataset = dataSet;
    }
}

