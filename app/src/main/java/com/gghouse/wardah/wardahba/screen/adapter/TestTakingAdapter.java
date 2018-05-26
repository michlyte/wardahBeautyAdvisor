package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.model.Answer;
import com.gghouse.wardah.wardahba.model.Question;
import com.gghouse.wardah.wardahba.util.WBALogger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by michael on 2/18/2017.
 */

public class TestTakingAdapter extends RecyclerView.Adapter<TestTakingAdapter.ViewHolder> {

    private final Context mContext;
    private List<Question> mValues;
    private HashMap<Long, Object> resultMap;

    private final GradientDrawable gdUnselected;
    private final GradientDrawable gdSelected;

    public TestTakingAdapter(Context context, List<Question> items) {
        mContext = context;
        mValues = items;
        resultMap = new HashMap<Long, Object>();

        gdUnselected = new GradientDrawable();
        gdUnselected.setColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        gdUnselected.setCornerRadius(5);
        gdUnselected.setStroke(2, ContextCompat.getColor(mContext, R.color.colorPrimary));

        gdSelected = new GradientDrawable();
        gdSelected.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        gdSelected.setCornerRadius(5);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.screen_test_taking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (holder.mLLChoices.getChildCount() != holder.mItem.getAnswers().size()) {
            for (Answer answer : holder.mItem.getAnswers()) {
                View vChoice = LayoutInflater.from(mContext).inflate(R.layout.item_test_taking_choice, null);
                TextView tvText = (TextView) vChoice.findViewById(R.id.tv_ITTC_text);
                vChoice.setTag(answer.getId());
                tvText.setText(answer.getContent());
                setUnselected(tvText);

                vChoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultMap.put(holder.mItem.getId(), v.getTag());

                        for (int i = 0; i < holder.mLLChoices.getChildCount(); i++) {
                            View curView = holder.mLLChoices.getChildAt(i);
                            TextView tvText = (TextView) curView.findViewById(R.id.tv_ITTC_text);
                            if (tvText != null) {
                                if (v.getTag() == curView.getTag()) {
                                    setSelected(tvText);
                                } else {
                                    setUnselected(tvText);
                                }
                            } else {
                                WBALogger.log("tvText is null.");
                            }
                        }
                    }
                });

                holder.mLLChoices.addView(vChoice);
            }
        } else {
            for (int i = 0; i < holder.mLLChoices.getChildCount(); i++) {
                View vChoice = holder.mLLChoices.getChildAt(i);
                TextView tvText = (TextView) vChoice.findViewById(R.id.tv_ITTC_text);
                vChoice.setTag(holder.mItem.getAnswers().get(i).getId());
                if (tvText != null) {
                    tvText.setText(holder.mItem.getAnswers().get(i).getContent());
                    if (resultMap.containsKey(holder.mItem.getId()) &&
                            resultMap.get(holder.mItem.getId()) == vChoice.getTag()) {
                        setSelected(tvText);
                    } else {
                        setUnselected(tvText);
                    }
                } else {
                    WBALogger.log("tvText is null.");
                }
            }
        }

        holder.mTVNumber.setText((position + 1) + ". ");
        holder.mTVQuestion.setText(holder.mItem.getContent());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTVNumber;
        public final TextView mTVQuestion;
        public final LinearLayout mLLChoices;
        public Question mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTVNumber = (TextView) view.findViewById(R.id.tv_STTI_number);
            mTVQuestion = (TextView) view.findViewById(R.id.tv_STTI_question);
            mLLChoices = (LinearLayout) view.findViewById(R.id.ll_STTI_choices);
        }
    }

    public HashMap<Long, Object> getResultMap() {
        return resultMap;
    }

    private void setUnselected(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(mContext, android.R.color.secondary_text_light));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(gdUnselected);
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
    }

    private void setSelected(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(gdSelected);
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
    }

    public void setData(List<Question> questionList) {
        this.mValues = questionList;
    }
}
