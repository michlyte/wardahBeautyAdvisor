package com.gghouse.wardah.wardahba.screen.main_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAIntent;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.NotifDummy;
import com.gghouse.wardah.wardahba.model.Notif;
import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.screen.adapter.NotifAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.response.NotificationsResponse;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gghouse.wardah.wardahba.common.WBAProperties.notificationSort;

public class NotifFragment extends Fragment {

    public static final String TAG = NotifFragment.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private NotifAdapter mAdapter;
    private List<Notif> mDataSet;

    /*
     * Pagination
     */
    private int mPage;
    private OnLoadMoreListener mOnLoadMoreListener;

    /*
     * DynamicBox
     */
    private DynamicBox mDynamicBox;

    public NotifFragment() {
    }

    public static NotifFragment newInstance() {
        NotifFragment fragment = new NotifFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().registerReceiver(brIncomingFCMMessage, new IntentFilter(WBAIntent.NOTIF));

        mDataSet = new ArrayList<Notif>();
        mPage = 0;

        mOnLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.add(null);
                        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                    }
                });
                ws_notifications(NotifMode.LOAD_MORE);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().unregisterReceiver(brIncomingFCMMessage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notif_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_FNL_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ws_notifications(NotifMode.REFRESH);
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_FNL_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NotifAdapter(getContext(), mRecyclerView, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mDynamicBox = new DynamicBox(getActivity(), mRecyclerView);
        View emptyView = inflater.inflate(R.layout.dynamic_box_empty, container, false);
        TextView tvMessage = (TextView) emptyView.findViewById(R.id.tv_message);
        tvMessage.setText(getString(R.string.message_empty_notification));
        mDynamicBox.addCustomView(emptyView, WBAParams.EMPTY_VIEW);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ws_notifications(NotifMode.REFRESH);
    }

    private void ws_notifications(NotifMode notifMode) {
        Long userLocationId = WBASession.getUserLocationId();
        if (userLocationId == null) {
            WBASession.loggingOut(getActivity());
        } else {
            switch (WBAProperties.mode) {
                case DUMMY_DEVELOPMENT:
                    switch (notifMode) {
                        case REFRESH:
                            if (!mSwipeRefreshLayout.isRefreshing()) {
                                mDynamicBox.showLoadingLayout();
                            }

                            mRecyclerView.setAdapter(null);
                            mDataSet.clear();
                            mDataSet.addAll(NotifDummy.ITEMS);
                            mAdapter.setData(mDataSet);
                            mAdapter.setOnLoadMoreListener(mOnLoadMoreListener);
                            mRecyclerView.setAdapter(mAdapter);

                            mSwipeRefreshLayout.setRefreshing(false);
                            mDynamicBox.hideAll();
                            break;
                        case LOAD_MORE:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Remove loading item
                                    mAdapter.remove(mAdapter.getItemCount() - 1);
                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());

                                    //Load data
                                    int index = mAdapter.getItemCount();
                                    int end = index + WBAProperties.NOTIF_ITEM_PER_PAGE;

                                    for (Long i = 0L; i < WBAProperties.NOTIF_ITEM_PER_PAGE; i++) {
                                        Notif notif = new Notif(
                                                i,
                                                "Description is the fiction-writing mode for transmitting a mental image of the particulars of a story. Together with dialogue, narration, exposition, and summarization, description is one of the most widely recognized of the fiction-writing modes. As stated in Writing from A to Z, edited by Kirk Polking, description is more than the amassing of details; it is bringing something to life by carefully choosing and arranging words and phrases to produce the desired effect.[3] The most appropriate and effective techniques for presenting description are a matter of ongoing discussion among writers and writing coaches.",
                                                "PRODUCT_LAUNCH",
                                                R.drawable.pic_quote_1,
                                                System.currentTimeMillis());
                                        mAdapter.add(notif);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setLoaded();
                                }
                            }, WBAProperties.DELAY_LOAD_MORE);
                            break;
                    }
                    break;
                case DEVELOPMENT:
                case PRODUCTION:
                    switch (notifMode) {
                        case REFRESH:
                            if (!mSwipeRefreshLayout.isRefreshing()) {
                                mDynamicBox.showLoadingLayout();
                            }

                            Call<NotificationsResponse> callNotifRefresh = ApiClient.getClient().apiNotifications(userLocationId, 0, WBAProperties.NOTIF_ITEM_PER_PAGE, notificationSort);
                            callNotifRefresh.enqueue(new Callback<NotificationsResponse>() {
                                @Override
                                public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                                    WBALogger.log(WBAProperties.ON_RESPONSE);

                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mDynamicBox.hideAll();

                                    if (response.isSuccessful()) {
                                        NotificationsResponse rps = response.body();
                                        switch (rps.getCode()) {
                                            case WBAProperties.CODE_200:
                                                manageOnLoadMoreListener(rps.getPagination());
                                                mRecyclerView.setAdapter(null);
                                                mDataSet = rps.getData();
                                                mAdapter.setData(mDataSet);
                                                mRecyclerView.setAdapter(mAdapter);

                                                if (mDataSet.size() <= 0) {
                                                    mDynamicBox.showCustomView(WBAParams.EMPTY_VIEW);
                                                }
                                                break;
                                            default:
                                                WBAPopUp.toastMessage(TAG, rps.getStatus());
                                                break;
                                        }
                                    } else {
                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                                    WBAPopUp.toastMessage(TAG, t.getMessage());

                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mDynamicBox.hideAll();
                                }
                            });
                            break;
                        case LOAD_MORE:
                            Call<NotificationsResponse> callNotifLoadMore = ApiClient.getClient().apiNotifications(userLocationId, ++mPage, WBAProperties.NOTIF_ITEM_PER_PAGE, WBAProperties.notificationSort);
                            callNotifLoadMore.enqueue(new Callback<NotificationsResponse>() {
                                @Override
                                public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                                    WBALogger.log(WBAProperties.ON_RESPONSE);

                                    //Remove loading item
                                    mAdapter.remove(mAdapter.getItemCount() - 1);
                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());

                                    if (response.isSuccessful()) {
                                        NotificationsResponse notificationsResponse = response.body();
                                        switch (notificationsResponse.getCode()) {
                                            case WBAProperties.CODE_200:
                                                manageOnLoadMoreListener(notificationsResponse.getPagination());
                                                List<Notif> newDataSet = notificationsResponse.getData();
                                                mDataSet.addAll(newDataSet);
                                                break;
                                            default:
                                                WBALogger.log("Status" + "[" + notificationsResponse.getCode() + "]: " + notificationsResponse.getStatus());
                                                break;
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());

                                    //Remove loading item
                                    mAdapter.remove(mAdapter.getItemCount() - 1);
                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                                    mAdapter.setLoaded();
                                }
                            });
                            break;
                    }
                    break;
            }
        }
    }

    private void manageOnLoadMoreListener(Pagination pagination) {
        /*
         * Controlling load more
         */
        mPage = pagination.getNumber();
        if (pagination.isLast()) {
            mAdapter.removeOnLoadMoreListener();
        } else {
            mAdapter.setLoaded();
            mAdapter.setOnLoadMoreListener(mOnLoadMoreListener);
        }
    }

    enum NotifMode {
        REFRESH,
        LOAD_MORE;
    }

    /*
     * Broadcast
     */
    BroadcastReceiver brIncomingFCMMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ws_notifications(NotifMode.REFRESH);
        }
    };

}