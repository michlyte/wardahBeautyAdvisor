package com.gghouse.wardah.wardahba.screen;

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
import com.gghouse.wardah.wardahba.dummy.WardahDummy;
import com.gghouse.wardah.wardahba.model.Pagination;
import com.gghouse.wardah.wardahba.model.Test;
import com.gghouse.wardah.wardahba.model.TestHistoryHeader;
import com.gghouse.wardah.wardahba.screen.adapter.TestHistoryAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.OnLoadMoreListener;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.response.TestAverageResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestListResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestRateResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestTotalResponse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestHistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = TestHistoryActivity.class.getSimpleName();

    private LinearLayout mLlFilter;
    private TextView mTvFilter;
    private Button mBReset;

    private RecyclerView mRecyclerView;
    private TestHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mTvMessage;

    /*
     * Refresh
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /*
     * Data Source and Pagination
     */
    private TestHistoryHeader mHeaderData;
    private List<Test> mDataSet;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /*
         * Dataset
         */
        mDataSet = new ArrayList<Test>();
        mHeaderData = new TestHistoryHeader();
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
                ws_test(WsMode.LOAD_MORE);
            }
        };

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ws_test(WsMode.REFRESH);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TestHistoryAdapter(this, mRecyclerView, mDataSet, true);
        mAdapter.setHeaderData(mHeaderData);
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

                ws_test(WsMode.REFRESH);
            }
        });

        /*
         * Empty Message
         */
        mTvMessage = (TextView) findViewById(R.id.tv_message);

        mDynamicBox = new DynamicBox(this, mRecyclerView);

        /*
         * Web services or Dummy
         */
        ws_test(WsMode.REFRESH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_test_history, menu);
        mDFilter = menu.findItem(R.id.action_filter).getIcon();
        mDFilter.mutate();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_filter:
                Calendar cFrom = Calendar.getInstance();
                cFrom.setTime((mDBegin == null ? new Date() : mDBegin));
                Calendar cTo = Calendar.getInstance();
                cTo.setTime((mDEnd == null ? new Date() : mDEnd));
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TestHistoryActivity.this,
                        cFrom.get(Calendar.YEAR),
                        cFrom.get(Calendar.MONTH),
                        cFrom.get(Calendar.DAY_OF_MONTH),
                        cTo.get(Calendar.YEAR),
                        cTo.get(Calendar.MONTH),
                        cTo.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
        }
        return super.onOptionsItemSelected(item);
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

            ws_test(WsMode.REFRESH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setTestHeaderValue(Double average, Float correctRate, Integer sumTestTaken, boolean refresh) {
        if (average != null) {
            mHeaderData.setAverage(average);
        }
        if (correctRate != null) {
            mHeaderData.setCorrectRate(correctRate);
        }
        if (sumTestTaken != null) {
            mHeaderData.setSumTestTaken(sumTestTaken);
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

    /*
     * Web services
     */
    private void ws_testHeader(long userId) {
        Call<TestAverageResponse> callTestAverage = ApiClient.getClient().apiTestAverage(userId);
        callTestAverage.enqueue(new Callback<TestAverageResponse>() {
            @Override
            public void onResponse(Call<TestAverageResponse> call, Response<TestAverageResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    TestAverageResponse testAverageResponse = response.body();
                    switch (testAverageResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setTestHeaderValue(testAverageResponse.getData(), null, null, true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + testAverageResponse.getCode() + "]: " + testAverageResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<TestAverageResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });

        Call<TestTotalResponse> callTestTotal = ApiClient.getClient().apiTestTotal(userId);
        callTestTotal.enqueue(new Callback<TestTotalResponse>() {
            @Override
            public void onResponse(Call<TestTotalResponse> call, Response<TestTotalResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    TestTotalResponse testTotalResponse = response.body();
                    switch (testTotalResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setTestHeaderValue(null, null, testTotalResponse.getData(), true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + testTotalResponse.getCode() + "]: " + testTotalResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<TestTotalResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });

        Call<TestRateResponse> callTestRate = ApiClient.getClient().apiTestRate(userId);
        callTestRate.enqueue(new Callback<TestRateResponse>() {
            @Override
            public void onResponse(Call<TestRateResponse> call, Response<TestRateResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    TestRateResponse testRateResponse = response.body();
                    switch (testRateResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setTestHeaderValue(null, testRateResponse.getData(), null, true);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + testRateResponse.getCode() + "]: " + testRateResponse.getStatus());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<TestRateResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }

    private void ws_test(WsMode wsMode) {
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                switch (wsMode) {
                    case REFRESH:
                        mDynamicBox.showLoadingLayout();

                        mRecyclerView.setAdapter(null);
                        mAdapter.refreshData();
                        setTestHeaderValue(70.7, 7.5f, 8, false);
                        mAdapter.addAll(WardahDummy.TEST_HISTORY);
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
                                    Test test = new Test(80, "Title " + i, 7, new Date().getTime());
                                    mAdapter.add(test);
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

                            ws_testHeader(userId);

                            Call<TestListResponse> callTestList = ApiClient.getClient().apiTestList(userId, 0, WBAProperties.HISTORY_ITEM_PER_PAGE, beginDate, endDate);
                            callTestList.enqueue(new Callback<TestListResponse>() {
                                @Override
                                public void onResponse(Call<TestListResponse> call, Response<TestListResponse> response) {
                                    WBALogger.log(WBAProperties.ON_RESPONSE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mDynamicBox.hideAll();

                                    if (response.isSuccessful()) {
                                        TestListResponse testListResponse = response.body();
                                        switch (testListResponse.getCode()) {
                                            case WBAProperties.CODE_200:
                                                manageOnLoadMoreListener(testListResponse.getPagination());
                                                mRecyclerView.setAdapter(null);
                                                mAdapter.refreshData();
                                            /*
                                             * Update test history data
                                             */
                                                mAdapter.addAll(testListResponse.getData());
                                                mRecyclerView.setAdapter(mAdapter);
                                                mAdapter.notifyDataSetChanged();

                                                // Filter
                                                if (mDBegin != null && mDEnd != null) {
                                                    mTvFilter.setText(WBAProperties.sdfFilter.format(mDBegin) + " - " + WBAProperties.sdfFilter.format(mDEnd));
                                                    mLlFilter.setVisibility(View.VISIBLE);
                                                }

                                                // Empty Message
                                                if (testListResponse.getData().size() <= 0) {
                                                    mTvMessage.setVisibility(View.VISIBLE);
                                                }
                                                break;
                                            default:
                                                WBALogger.log("Status" + "[" + testListResponse.getCode() + "]: " + testListResponse.getStatus());
                                                break;
                                        }
                                    } else {
                                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<TestListResponse> call, Throwable t) {
                                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mAdapter.setLoaded();

                                    mDynamicBox.hideAll();
                                }
                            });
                            break;
                        case LOAD_MORE:
                            Call<TestListResponse> callTestListLoadMore = ApiClient.getClient().apiTestList(userId, ++mPage, WBAProperties.HISTORY_ITEM_PER_PAGE, beginDate, endDate);
                            callTestListLoadMore.enqueue(new Callback<TestListResponse>() {
                                @Override
                                public void onResponse(Call<TestListResponse> call, Response<TestListResponse> response) {
                                    WBALogger.log(WBAProperties.ON_RESPONSE);

                                    // Remove loading item
                                    mAdapter.remove(mAdapter.getItemCount() - 1);
                                    mAdapter.notifyItemRemoved(mAdapter.getItemCount());

                                    if (response.isSuccessful()) {
                                        TestListResponse salesListResponse = response.body();
                                        switch (salesListResponse.getCode()) {
                                            case WBAProperties.CODE_200:
                                                manageOnLoadMoreListener(salesListResponse.getPagination());
                                                List<Test> newDataSet = salesListResponse.getData();
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
                                public void onFailure(Call<TestListResponse> call, Throwable t) {
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
