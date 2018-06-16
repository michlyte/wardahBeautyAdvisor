package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.WardahDummy;
import com.gghouse.wardah.wardahba.model.IntentProductHighlight;
import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.model.ProductHighlight;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.model.SalesHistoryHeader;
import com.gghouse.wardah.wardahba.screen.adapter.SalesHistoryAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.SalesListener;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.response.SalesAverageResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesByMonthResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesHighestPerMonthResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesListResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesTotalResponse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesHistoryActivity extends WardahHistoryActivity implements SalesListener {

    public static final String TAG = SalesHistoryActivity.class.getSimpleName();

    static final int SALES_EDIT_ACTIVITY = 15;

    private RecyclerView mRecyclerView;
    private SalesHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTvMessage;

    /*
     * Data Source and Pagination
     */
    private SalesHistoryHeader mHeaderData;
    private List<Sales> mDataSet;
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
        mDataSet = new ArrayList<Sales>();
        mHeaderData = new SalesHistoryHeader();
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
        mAdapter = new SalesHistoryAdapter(this, mRecyclerView, mDataSet, true, this);
        mAdapter.setHeaderData(mHeaderData);
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

    private void setSalesHeaderValue(Double average, Double countByMonth, Double count, Double highestPerMonth, boolean refresh) {
        if (average != null) {
            mHeaderData.setAverage(average);
        }
        if (countByMonth != null) {
            mHeaderData.setCountByMonth(countByMonth);
        }
        if (count != null) {
            mHeaderData.setCount(count);
        }
        if (highestPerMonth != null) {
            mHeaderData.setHighestPerMonth(highestPerMonth);
        }
        if (refresh) {
            mAdapter.notifyItemChanged(0);
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

    @Override
    public void onClick(Sales sales) {
        Intent iSalesInput = new Intent(this, SalesInputActivity.class);
        IntentProductHighlight intentProductHighlight = new IntentProductHighlight(new ArrayList<ProductHighlight>());
        iSalesInput.putExtra(WBAParams.DATA, intentProductHighlight);
        iSalesInput.putExtra(WBAParams.DATE, sales.getCreatedTime());
        iSalesInput.putExtra(WBAParams.LOCK, false);
        iSalesInput.putExtra(WBAParams.SALES, sales);
        iSalesInput.putExtra(WBAParams.EDIT, true);
        startActivityForResult(iSalesInput, SALES_EDIT_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SALES_EDIT_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                mIsEdited = true;
                ws(WsMode.REFRESH);
            }
        }
    }

    /*
     * Web services
     */
    private void ws_salesHeader(long userId) {
        Call<SalesAverageResponse> callSalesAverage = ApiClient.getClient().apiSalesAverage(userId);
        callSalesAverage.enqueue(new Callback<SalesAverageResponse>() {
            @Override
            public void onResponse(Call<SalesAverageResponse> call, Response<SalesAverageResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    SalesAverageResponse salesAverageResponse = response.body();
                    switch (salesAverageResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setSalesHeaderValue(salesAverageResponse.getData(), null, null, null, true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + salesAverageResponse.getCode() + "]: " + salesAverageResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesAverageResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });

        Call<SalesByMonthResponse> callSalesByMonth = ApiClient.getClient().apiSalesByMonth(userId);
        callSalesByMonth.enqueue(new Callback<SalesByMonthResponse>() {
            @Override
            public void onResponse(Call<SalesByMonthResponse> call, Response<SalesByMonthResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    SalesByMonthResponse salesByMonthResponse = response.body();
                    switch (salesByMonthResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setSalesHeaderValue(null, salesByMonthResponse.getData(), null, null, true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + salesByMonthResponse.getCode() + "]: " + salesByMonthResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesByMonthResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });

        Call<SalesTotalResponse> callSalesTotal = ApiClient.getClient().apiSalesTotal(userId);
        callSalesTotal.enqueue(new Callback<SalesTotalResponse>() {
            @Override
            public void onResponse(Call<SalesTotalResponse> call, Response<SalesTotalResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    SalesTotalResponse salesTotalResponse = response.body();
                    switch (salesTotalResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setSalesHeaderValue(null, null, salesTotalResponse.getData(), null, true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + salesTotalResponse.getCode() + "]: " + salesTotalResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesTotalResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });

        Call<SalesHighestPerMonthResponse> callSalesHighestPerMonth = ApiClient.getClient().apiSalesHighestPerMonth(userId);
        callSalesHighestPerMonth.enqueue(new Callback<SalesHighestPerMonthResponse>() {
            @Override
            public void onResponse(Call<SalesHighestPerMonthResponse> call, Response<SalesHighestPerMonthResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    SalesHighestPerMonthResponse salesHighestPerMonthResponse = response.body();
                    switch (salesHighestPerMonthResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setSalesHeaderValue(null, null, null, salesHighestPerMonthResponse.getData(), true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + salesHighestPerMonthResponse.getCode() + "]: " + salesHighestPerMonthResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesHighestPerMonthResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }

    @Override
    protected void ws(WsMode wsMode) {
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                switch (wsMode) {
                    case REFRESH:
                        mDynamicBox.showLoadingLayout();

                        mRecyclerView.setAdapter(null);
                        mAdapter.refreshData();
                        setSalesHeaderValue(0.00, 0.00, 0.00, 0.00, false);
                        mAdapter.addAll(WardahDummy.SALES_HISTORY);
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
                                    Sales sales = new Sales(new Date().getTime(), i, Double.parseDouble(i + ""), i, false);
                                    mAdapter.add(sales);
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
                Long userId = WBASession.getUserId();
                if (userId == null) {
                    WBASession.loggingOut(this);
                } else {
                    String beginDate = null;
                    String endDate = null;
                    if (mDBegin != null && mDEnd != null) {
                        beginDate = WBAProperties.sdfFilter.format(mDBegin);
                        endDate = WBAProperties.sdfFilter.format(mDEnd);
                    }

                    switch (wsMode) {
                        case REFRESH:
                            if (!mSwipeRefreshLayout.isRefreshing()) {
                                mDynamicBox.showLoadingLayout();
                            }
                            mTvMessage.setVisibility(View.GONE);

                            ws_salesHeader(userId);

                            Call<SalesListResponse> callSalesList = ApiClient.getClient().apiSalesList(userId, 0, WBAProperties.HISTORY_ITEM_PER_PAGE, beginDate, endDate);
                            callSalesList.enqueue(new Callback<SalesListResponse>() {
                                @Override
                                public void onResponse(Call<SalesListResponse> call, Response<SalesListResponse> response) {
                                    WBALogger.log(WBAProperties.ON_RESPONSE);

                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mDynamicBox.hideAll();

                                    if (response.isSuccessful()) {
                                        SalesListResponse salesListResponse = response.body();
                                        switch (salesListResponse.getCode()) {
                                            case WBAProperties.CODE_200:
                                                manageOnLoadMoreListener(salesListResponse.getPagination());
                                                mRecyclerView.setAdapter(null);
                                                mAdapter.refreshData();
                                                /*
                                                 * Update sales history data
                                                 */
                                                mAdapter.addAll(salesListResponse.getData());
                                                mRecyclerView.setAdapter(mAdapter);
                                                mAdapter.notifyDataSetChanged();

                                                // Filter
                                                if (mDBegin != null && mDEnd != null) {
                                                    String dateStr = WBAProperties.sdfFilter.format(mDBegin) + " - " + WBAProperties.sdfFilter.format(mDEnd);
                                                    mTvFilter.setText(dateStr);
                                                    mLlFilter.setVisibility(View.VISIBLE);
                                                }

                                                // Empty Message
                                                if (salesListResponse.getData().size() <= 0) {
                                                    mTvMessage.setVisibility(View.VISIBLE);
                                                }
                                                break;
                                            default:
                                                WBALogger.log("Status" + "[" + salesListResponse.getCode() + "]: " + salesListResponse.getStatus());
                                                break;
                                        }
                                    } else {
                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<SalesListResponse> call, Throwable t) {
                                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mAdapter.setLoaded();

                                    mDynamicBox.hideAll();
                                }
                            });
                            break;
                        case LOAD_MORE:
                            Call<SalesListResponse> callSalesListLoadMore = ApiClient.getClient().apiSalesList(userId, ++mPage, WBAProperties.HISTORY_ITEM_PER_PAGE, beginDate, endDate);
                            callSalesListLoadMore.enqueue(new Callback<SalesListResponse>() {
                                @Override
                                public void onResponse(Call<SalesListResponse> call, Response<SalesListResponse> response) {
                                    WBALogger.log(WBAProperties.ON_RESPONSE);

                                    // Remove loading item
                                    mAdapter.remove(mAdapter.getItemCount() - 1);
                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());

                                    if (response.isSuccessful()) {
                                        SalesListResponse salesListResponse = response.body();
                                        switch (salesListResponse.getCode()) {
                                            case WBAProperties.CODE_200:
                                                manageOnLoadMoreListener(salesListResponse.getPagination());
                                                List<Sales> newDataSet = salesListResponse.getData();
                                                mAdapter.addAll(newDataSet);
                                                mAdapter.notifyDataSetChanged();
                                                break;
                                            default:
                                                WBALogger.log("Status" + "[" + salesListResponse.getCode() + "]: " + salesListResponse.getStatus());
                                                break;
                                        }
                                    } else {
                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<SalesListResponse> call, Throwable t) {
                                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());

                                    //Remove loading item
                                    mAdapter.remove(mAdapter.getItemCount() - 1);
                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                                    mAdapter.setLoaded();
                                }
                            });
                            break;
                    }
                }
                break;
        }
    }
}