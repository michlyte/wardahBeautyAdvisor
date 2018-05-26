package com.gghouse.wardah.wardahba.screen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.enumeration.NotifEnum;
import com.gghouse.wardah.wardahba.enumeration.NotifStatusEnum;
import com.gghouse.wardah.wardahba.model.Notif;
import com.gghouse.wardah.wardahba.screen.PictureActivity;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAScreen;
import com.gghouse.wardah.wardahba.util.WBAUtil;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by michaelhalim on 2/19/17.
 */

public class NotifAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = NotifAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView mRecyclerView;

    private final Context mContext;
    private List<Notif> mValues;

    public NotifAdapter(Context context, RecyclerView recyclerView, List<Notif> items) {
        mContext = context;
        mRecyclerView = recyclerView;
        mValues = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_notif_intragram, parent, false);
            return new NotifViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotifViewHolder) {
            final NotifViewHolder notifViewHolder = (NotifViewHolder) holder;
            notifViewHolder.mItem = mValues.get(position);

            final NotifEnum notifEnum = NotifEnum.getNotifEnumById(notifViewHolder.mItem.getType());
            if (notifEnum != null) {
                notifViewHolder.mTVTitle.setText(notifEnum.getName());

                Picasso.with(mContext)
                        .load(notifEnum.getIconResId())
                        .fit()
                        .centerInside()
                        .into(notifViewHolder.mIVIcon);
            } else {
                notifViewHolder.mTVTitle.setText(notifViewHolder.mItem.getType());
                WBALogger.log("Type [" + notifViewHolder.mItem.getType() + "] is not supported.");
            }

            switch (WBAProperties.mode) {
                case DUMMY_DEVELOPMENT:
                    if (notifViewHolder.mItem.getDrawable() != null) {
                        notifViewHolder.mIVImage.getLayoutParams().height = getPreferredHeight(notifViewHolder.mItem.getDrawable());
                        Picasso.with(mContext)
                                .load(notifViewHolder.mItem.getDrawable())
                                .placeholder(R.drawable.progress_animation)
                                .error(R.drawable.pic_image_not_found)
                                .fit()
                                .centerCrop()
                                .into(notifViewHolder.mIVImage);
                    } else {
                        notifViewHolder.mIVImage.getLayoutParams().height = 0;
                        notifViewHolder.mIVImage.setImageDrawable(null);
                    }
                    break;
                case DEVELOPMENT:
                case PRODUCTION:
                    if (notifViewHolder.mItem.getFileLocation() != null &&
                            !notifViewHolder.mItem.getFileLocation().getThumbnailUrl().isEmpty()) {
                        notifViewHolder.mIVImage.getLayoutParams().height = 500;
                        Picasso.with(mContext)
                                .load(WBAUtil.constructImageUrl(notifViewHolder.mItem.getFileLocation().getThumbnailUrl()))
                                .placeholder(R.drawable.progress_animation)
                                .error(R.drawable.pic_image_not_found)
                                .fit()
                                .centerCrop()
                                .into(notifViewHolder.mIVImage);
                    } else {
                        notifViewHolder.mIVImage.getLayoutParams().height = 0;
                    }
                    break;
            }
            notifViewHolder.mIVImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iPicture = new Intent(mContext, PictureActivity.class);
                    iPicture.putExtra(WBAParams.DATA, notifViewHolder.mItem);
                    mContext.startActivity(iPicture);
                }
            });

            notifViewHolder.mTVDescription.setText(notifViewHolder.mItem.getDescription());
            notifViewHolder.mTVDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifViewHolder.mTVDescription.toggle();
                }
            });

            notifViewHolder.mTVMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifViewHolder.mTVDescription.toggle();
                    notifViewHolder.mTVMore.setText(
                            notifViewHolder.mTVDescription.isExpanded() ? R.string.prompt_selengkapnya : R.string.prompt_singkatnya);
                }
            });

            switch (NotifStatusEnum.getByValue(notifViewHolder.mItem.getStatus())) {
                case ACTIVE:
                    long curTime = System.currentTimeMillis();
                    if (curTime > notifViewHolder.mItem.getNotificationTime()) {
                        notifViewHolder.mTVDate.setText(WardahApp.getInstance().getAppContext().getString(R.string.title_today));
                    } else {
                        Calendar calendar = Calendar.getInstance(WBAProperties.locale);
                        calendar.setTimeInMillis(notifViewHolder.mItem.getNotificationTime());
                        calendar.add(Calendar.DAY_OF_YEAR, -1);
                        String notifDate = WBAProperties.sdfNotif.format(new Date(calendar.getTimeInMillis()));
                        notifViewHolder.mTVDate.setText(notifDate);
                    }
                    break;
                case NONACTIVE:
                    String endDate = WBAProperties.sdfNotif.format(
                            new Date(notifViewHolder.mItem.getPeriodEnd()));
                    notifViewHolder.mTVDate.setText(endDate);
                    break;
            }
//            String date = WBAProperties.sdfNotif.format(new Date(notifViewHolder.mItem.getPeriodStart()));
//            notifViewHolder.mTVDate.setText(date);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    static class NotifViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIVIcon;
        public final ImageView mIVImage;
        public final TextView mTVTitle;
//        public final TextView mTVIndicator;
        public final ExpandableTextView mTVDescription;
        public final TextView mTVMore;
        public final TextView mTVDate;
        public Notif mItem;

        public NotifViewHolder(View view) {
            super(view);
            mView = view;
            mIVIcon = (ImageView) view.findViewById(R.id.civ_FNI_icon);
            mIVImage = (ImageView) view.findViewById(R.id.iv_FNI_image);
            mTVTitle = (TextView) view.findViewById(R.id.tv_FNI_title);
//            mTVIndicator = (TextView) view.findViewById(R.id.tv_indicator);
            mTVDescription = (ExpandableTextView) view.findViewById(R.id.tv_FNI_description);
            mTVMore = (TextView) view.findViewById(R.id.tv_FNI_more);
            mTVDate = (TextView) view.findViewById(R.id.tv_FNI_date);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void removeOnLoadMoreListener() {
        this.mOnLoadMoreListener = null;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void add(Notif notif) {
        mValues.add(notif);
    }

    public void remove(int i) {
        mValues.remove(i);
    }

    public void setData(List<Notif> dataSet) {
        mValues = dataSet;
    }

    private int getPreferredHeight(int resID) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(resID);
        return getPreferredHeight(imageView.getDrawable().getIntrinsicWidth(), imageView.getDrawable().getIntrinsicHeight());
    }

    private int getPreferredHeight(int width, int height) {
        float heightRatio = WBAScreen.getWidth(mContext) / width;
        float heightNewFloat = heightRatio * height;
        int heightNew = Math.round(heightNewFloat);
        // Min height = Screen width / 2
        int minHeight = Math.round(WBAScreen.getWidth(mContext) / 2);
        // Max height = Screen width / 2 * 3
        int maxHeight = Math.round(WBAScreen.getWidth(mContext) / 2 * 3);

        if (heightNew < minHeight) {
            return minHeight;
        } else if (heightNew > maxHeight) {
            return maxHeight;
        } else {
            return heightNew;
        }
    }

    private boolean isExpired(long endDateLong) {
        Date current = new Date();
        Date endDate = new Date(endDateLong);
        if (endDate.after(current)) {
            return true;
        } else {
            return false;
        }
    }
}
