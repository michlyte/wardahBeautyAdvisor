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
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.PelangganHistoryDummy;
import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.model.Pelanggan;
import com.gghouse.wardah.wardahba.screen.adapter.PelangganHistoryAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

public class PelangganHistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = PelangganHistoryActivity.class.getSimpleName();

    static final int PELANGGAN_EDIT_ACTIVITY = 15;

    private LinearLayout mLlFilter;
    private TextView mTvFilter;
    private Button mBReset;

    private RecyclerView mRecyclerView;
    private PelangganHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTvMessage;

    /*
     * Refresh
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /*
     * Data Source and Pagination
     */
    private List<Pelanggan> mDataSet;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int mPage;

    /*
     * Filters
     */
    private Drawable mDFilter;
    private Date mDBegin;
    private Date mDEnd;

    /*
     * Loading view
     */
    private DynamicBox mDynamicBox;

    /*
     * Menu
     */
    private Menu mMenu;

    /*
     * Editable
     */
    private boolean mIsEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                ws_pelanggan(WsMode.LOAD_MORE);
            }
        };

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ws_pelanggan(WsMode.REFRESH);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PelangganHistoryAdapter(this, mRecyclerView, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        /*
         * Filter
         */
        mLlFilter = (LinearLayout) findViewById(R.id.ll_filter);
        mTvFilter = (TextView) findViewById(R.id.tv_filter);
        mBReset = (Button) findViewById(R.id.b_reset);
        mBReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBegin = null;
                mDEnd = null;

                mMenu.findItem(R.id.action_filter).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_filter));
                mDFilter = mMenu.findItem(R.id.action_filter).getIcon();
                mDFilter.setColorFilter(ContextCompat.getColor(getBaseContext(), android.R.color.white), PorterDuff.Mode.SRC_ATOP);

                mLlFilter.setVisibility(View.GONE);

                ws_pelanggan(WsMode.REFRESH);
            }
        });

        /*
         * Empty Message
         */
        mTvMessage = (TextView) findViewById(R.id.tv_message);

        mDynamicBox = new DynamicBox(this, mRecyclerView);

        /*
         * Editable
         */
        mIsEdited = false;

        /*
         * Web services or Dummy
         */
        ws_pelanggan(WsMode.REFRESH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_history, menu);
        mDFilter = menu.findItem(R.id.action_filter).getIcon();
        mDFilter.mutate();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mIsEdited) {
                    setResult(Activity.RESULT_OK);
                }
                finish();
                return true;
            case R.id.action_filter:
                Calendar cFrom = Calendar.getInstance();
                cFrom.setTime((mDBegin == null ? new Date() : mDBegin));
                Calendar cTo = Calendar.getInstance();
                cTo.setTime((mDEnd == null ? new Date() : mDEnd));
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PelangganHistoryActivity.this,
                        cFrom.get(Calendar.YEAR),
                        cFrom.get(Calendar.MONTH),
                        cFrom.get(Calendar.DAY_OF_MONTH),
                        cTo.get(Calendar.YEAR),
                        cTo.get(Calendar.MONTH),
                        cTo.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsEdited) {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        try {
            String from = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            String to = dayOfMonthEnd + "/" + (monthOfYearEnd + 1) + "/" + yearEnd;
            mDBegin = WBAProperties.sdfFilter.parse(from);
            mDEnd = WBAProperties.sdfFilter.parse(to);

            /*
             * Change filter icon color
             */
            mMenu.findItem(R.id.action_filter).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_filter_fill));
            mDFilter = mMenu.findItem(R.id.action_filter).getIcon();
            mDFilter.mutate();
            mDFilter.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);

            /*
             * Refresh list based on filter
             */

            ws_pelanggan(WsMode.REFRESH);
        } catch (ParseException e) {
            e.printStackTrace();
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
                ws_pelanggan(WsMode.REFRESH);
            }
        }
    }

    /*
     * Web services
     */
    private void ws_pelanggan(WsMode wsMode) {
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                switch (wsMode) {
                    case REFRESH:
                        mDynamicBox.showLoadingLayout();

                        mRecyclerView.setAdapter(null);
                        mAdapter.getData().clear();
                        mAdapter.addAll(PelangganHistoryDummy.ITEMS);
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
