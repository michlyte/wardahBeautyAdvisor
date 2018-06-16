package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.WardahDummy;
import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.model.Pelanggan;
import com.gghouse.wardah.wardahba.screen.adapter.PelangganHistoryAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

public class PelangganHistoryActivity extends WardahHistoryActivity {

    public static final String TAG = PelangganHistoryActivity.class.getSimpleName();

    static final int PELANGGAN_EDIT_ACTIVITY = 15;

    private RecyclerView mRecyclerView;
    private PelangganHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTvMessage;

    /*
     * Data Source and Pagination
     */
    private List<Pelanggan> mDataSet;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int mPage;

    /*
     * Loading view
     */
    private DynamicBox mDynamicBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Dataset
         */
        mDataSet = new ArrayList<Pelanggan>();
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
                ws(WsMode.LOAD_MORE);
            }
        };

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PelangganHistoryAdapter(this, mRecyclerView, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        /*
         * Empty Message
         */
        mTvMessage = (TextView) findViewById(R.id.tv_message);

        mDynamicBox = new DynamicBox(this, mRecyclerView);

        /*
         * Web services or Dummy
         */
        ws(WsMode.REFRESH);
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

//    @Override
//    public void onClick(Sales sales) {
//        Intent iSalesInput = new Intent(this, SalesInputActivity.class);
//        IntentProductHighlight intentProductHighlight = new IntentProductHighlight(new ArrayList<ProductHighlight>());
//        iSalesInput.putExtra(WBAParams.DATA, intentProductHighlight);
//        iSalesInput.putExtra(WBAParams.DATE, sales.getCreatedTime());
//        iSalesInput.putExtra(WBAParams.LOCK, false);
//        iSalesInput.putExtra(WBAParams.SALES, sales);
//        iSalesInput.putExtra(WBAParams.EDIT, true);
//        startActivityForResult(iSalesInput, SALES_EDIT_ACTIVITY);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PELANGGAN_EDIT_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                mIsEdited = true;
                ws(WsMode.REFRESH);
            }
        }
    }

    /*
     * Web services
     */
    @Override
    protected void ws(WsMode wsMode) {
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                switch (wsMode) {
                    case REFRESH:
                        mDynamicBox.showLoadingLayout();

                        mRecyclerView.setAdapter(null);
                        mAdapter.getData().clear();
                        mAdapter.addAll(WardahDummy.PELANGGAN_HISTORY);
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
                                int end = index + 20;
                                for (long i = index; i < end; i++) {
                                    Pelanggan pelanggan = new Pelanggan(
                                            i, "Name " + (i + 1), "name" + (i + 1) + "@mmail.com");
                                    mAdapter.add(pelanggan);
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
//                Long userId = WBASession.getUserId();
//                if (userId == null) {
//                    WBASession.loggingOut(this);
//                } else {
//                    String beginDate = null;
//                    String endDate = null;
//                    if (mDBegin != null && mDEnd != null) {
//                        beginDate = WBAProperties.sdfFilter.format(mDBegin);
//                        endDate = WBAProperties.sdfFilter.format(mDEnd);
//                    }
//
//                    switch (wsMode) {
//                        case REFRESH:
//                            if (!mSwipeRefreshLayout.isRefreshing()) {
//                                mDynamicBox.showLoadingLayout();
//                            }
//                            mTvMessage.setVisibility(View.GONE);
//
//                            ws_salesHeader(userId);
//
//                            Call<SalesListResponse> callSalesList = ApiClient.getClient().apiSalesList(userId, 0, WBAProperties.SALES_ITEM_PER_PAGE, beginDate, endDate);
//                            callSalesList.enqueue(new Callback<SalesListResponse>() {
//                                @Override
//                                public void onResponse(Call<SalesListResponse> call, Response<SalesListResponse> response) {
//                                    WBALogger.log(WBAProperties.ON_RESPONSE);
//
//                                    mSwipeRefreshLayout.setRefreshing(false);
//                                    mDynamicBox.hideAll();
//
//                                    if (response.isSuccessful()) {
//                                        SalesListResponse salesListResponse = response.body();
//                                        switch (salesListResponse.getCode()) {
//                                            case WBAProperties.CODE_200:
//                                                manageOnLoadMoreListener(salesListResponse.getPagination());
//                                                mRecyclerView.setAdapter(null);
//                                                mAdapter.refreshData();
//                                                /*
//                                                 * Update sales history data
//                                                 */
//                                                mAdapter.addAll(salesListResponse.getData());
//                                                mRecyclerView.setAdapter(mAdapter);
//                                                mAdapter.notifyDataSetChanged();
//
//                                                // Filter
//                                                if (mDBegin != null && mDEnd != null) {
//                                                    mTvFilter.setText(WBAProperties.sdfFilter.format(mDBegin) + " - " + WBAProperties.sdfFilter.format(mDEnd));
//                                                    mLlFilter.setVisibility(View.VISIBLE);
//                                                }
//
//                                                // Empty Message
//                                                if (salesListResponse.getData().size() <= 0) {
//                                                    mTvMessage.setVisibility(View.VISIBLE);
//                                                }
//                                                break;
//                                            default:
//                                                WBALogger.log("Status" + "[" + salesListResponse.getCode() + "]: " + salesListResponse.getStatus());
//                                                break;
//                                        }
//                                    } else {
//                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<SalesListResponse> call, Throwable t) {
//                                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
//                                    mSwipeRefreshLayout.setRefreshing(false);
//                                    mAdapter.setLoaded();
//
//                                    mDynamicBox.hideAll();
//                                }
//                            });
//                            break;
//                        case LOAD_MORE:
//                            Call<SalesListResponse> callSalesListLoadMore = ApiClient.getClient().apiSalesList(userId, ++mPage, WBAProperties.SALES_ITEM_PER_PAGE, beginDate, endDate);
//                            callSalesListLoadMore.enqueue(new Callback<SalesListResponse>() {
//                                @Override
//                                public void onResponse(Call<SalesListResponse> call, Response<SalesListResponse> response) {
//                                    WBALogger.log(WBAProperties.ON_RESPONSE);
//
//                                    // Remove loading item
//                                    mAdapter.remove(mAdapter.getItemCount() - 1);
//                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());
//
//                                    if (response.isSuccessful()) {
//                                        SalesListResponse salesListResponse = response.body();
//                                        switch (salesListResponse.getCode()) {
//                                            case WBAProperties.CODE_200:
//                                                manageOnLoadMoreListener(salesListResponse.getPagination());
//                                                List<Sales> newDataSet = salesListResponse.getData();
//                                                mAdapter.addAll(newDataSet);
//                                                mAdapter.notifyDataSetChanged();
//                                                break;
//                                            default:
//                                                WBALogger.log("Status" + "[" + salesListResponse.getCode() + "]: " + salesListResponse.getStatus());
//                                                break;
//                                        }
//                                    } else {
//                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<SalesListResponse> call, Throwable t) {
//                                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
//
//                                    //Remove loading item
//                                    mAdapter.remove(mAdapter.getItemCount() - 1);
//                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());
//                                    mAdapter.setLoaded();
//                                }
//                            });
//                            break;
//                    }
//                }
                break;
        }
    }
}
