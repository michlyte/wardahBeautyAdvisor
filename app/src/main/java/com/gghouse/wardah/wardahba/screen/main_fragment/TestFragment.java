package com.gghouse.wardah.wardahba.screen.main_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAImages;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.WardahDummy;
import com.gghouse.wardah.wardahba.enumeration.SimpleAdapterTypeEnum;
import com.gghouse.wardah.wardahba.model.IntentQuestions;
import com.gghouse.wardah.wardahba.model.Test;
import com.gghouse.wardah.wardahba.screen.MainActivity;
import com.gghouse.wardah.wardahba.screen.TestHistoryActivity;
import com.gghouse.wardah.wardahba.screen.TestTakingActivity;
import com.gghouse.wardah.wardahba.screen.adapter.WardahSimpleAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WardahTabInterface;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.response.TestListResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestQuestionsResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestTodayResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestFragment extends Fragment implements WardahTabInterface, View.OnClickListener {

    public static final String TAG = TestFragment.class.getSimpleName();

    static final int TEST_HISTORY_ACTIVITY = 2;
    static final int TEST_TAKING_ACTIVITY = 3;

    private FrameLayout mFlTakeATest;
    private ImageView mIvImage;
    private Button mBStartTest;

    private FrameLayout mFlTestHasTaken;
    private ImageView mIvImageDone;

    private FrameLayout mFlTestNoMore;
    private ImageView mIvImageNoMore;

    private LinearLayout mLlSectionHeader;

    private TextView mTVMore;
    private ImageView mIVMore;

    private View mVLine;

    private RecyclerView mRecyclerView;
    private WardahSimpleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Test> mDataSet;

    /*
     * Loading view
     */
    private DynamicBox mDynamicBox;
    private MaterialDialog mMaterialDialog;

    public TestFragment() {
    }

    @SuppressWarnings("unused")
    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ws(WsMode.REFRESH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_fragment_test, container, false);

        populateContainerTopViews(view);

        mTVMore = (TextView) view.findViewById(R.id.tv_FTL_more);
        mIVMore = (ImageView) view.findViewById(R.id.iv_FTL_more);
        mIVMore.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mTVMore.setOnClickListener(this);
        mIVMore.setOnClickListener(this);

        mDataSet = new ArrayList<Test>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_FTL_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new WardahSimpleAdapter(getContext(), SimpleAdapterTypeEnum.TEST, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mDynamicBox = new DynamicBox(getActivity(), mRecyclerView);
        View emptyView = inflater.inflate(R.layout.dynamic_box_empty, container, false);
        mDynamicBox.addCustomView(emptyView, WBAParams.EMPTY_VIEW);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(R.string.title_loading)
                .content(R.string.title_retrieving_data)
                .cancelable(false)
                .progress(true, 0);
        mMaterialDialog = builder.build();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ws(WsMode.REFRESH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEST_TAKING_ACTIVITY || requestCode == MainActivity.LOCK_TEST) {
            if (resultCode == Activity.RESULT_OK) {
                ws(WsMode.REFRESH);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_FTL_more:
            case R.id.iv_FTL_more:
                Intent iTestHistory = new Intent(getContext(), TestHistoryActivity.class);
                startActivityForResult(iTestHistory, TEST_HISTORY_ACTIVITY);
                break;
            case R.id.b_start_test:
                ws_testQuestions();
                break;
        }
    }

    private void populateContainerTopViews(View view) {
        /*
         * Take a test view
         */
        mFlTakeATest = (FrameLayout) view.findViewById(R.id.fl_take_a_test);
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        Picasso.with(getContext())
                .load(WBAImages.testStart)
                .fit()
                .centerCrop()
                .into(mIvImage);
        mBStartTest = (Button) view.findViewById(R.id.b_start_test);
        mBStartTest.setOnClickListener(this);

        /*
         * Test has taken view
         */
        mFlTestHasTaken = (FrameLayout) view.findViewById(R.id.fl_test_is_taken);
        mIvImageDone = (ImageView) view.findViewById(R.id.iv_image_done);
        Picasso.with(getContext())
                .load(WBAImages.testDone)
                .fit()
                .centerCrop()
                .into(mIvImageDone);

        /*
         * Test no more
         */
        mFlTestNoMore = (FrameLayout) view.findViewById(R.id.fl_test_no_more);
        mIvImageNoMore = (ImageView) view.findViewById(R.id.iv_image_no_more);
        Picasso.with(getContext())
                .load(WBAImages.testDone)
                .fit()
                .centerCrop()
                .into(mIvImageNoMore);

        /*
         * Section header (Riwayat Tes)
         */
        mLlSectionHeader = (LinearLayout) view.findViewById(R.id.ll_section_header);

        /*
         * Line
         */
        mVLine = view.findViewById(R.id.v_line);
    }

    private enum Mode {
        TAKE_A_TEST,
        TEST_IS_TAKEN,
        TEST_NO_MORE;
    }

    private void setViewsByMode(Mode mode) {
        mLlSectionHeader.setVisibility(View.VISIBLE);
        mVLine.setVisibility(View.VISIBLE);

        switch (mode) {
            case TAKE_A_TEST:
                mFlTakeATest.setVisibility(View.VISIBLE);
                mFlTestHasTaken.setVisibility(View.GONE);
                mFlTestNoMore.setVisibility(View.GONE);
                break;
            case TEST_IS_TAKEN:
                mFlTakeATest.setVisibility(View.GONE);
                mFlTestHasTaken.setVisibility(View.VISIBLE);
                mFlTestNoMore.setVisibility(View.GONE);
                break;
            case TEST_NO_MORE:
                mFlTakeATest.setVisibility(View.GONE);
                mFlTestHasTaken.setVisibility(View.GONE);
                mFlTestNoMore.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initViews() {
        mFlTakeATest.setVisibility(View.GONE);
        mFlTestHasTaken.setVisibility(View.GONE);
        mFlTestNoMore.setVisibility(View.GONE);
        mLlSectionHeader.setVisibility(View.GONE);
        mVLine.setVisibility(View.GONE);
    }

    /*
     * Web services
     */

    private void ws_testCheckToday(long userId) {
        Call<TestTodayResponse> callTestToday = ApiClient.getClient().apiTestToday(userId);
        callTestToday.enqueue(new Callback<TestTodayResponse>() {
            @Override
            public void onResponse(Call<TestTodayResponse> call, Response<TestTodayResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    TestTodayResponse testTodayResponse = response.body();
                    switch (testTodayResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setViewsByMode(Mode.TEST_IS_TAKEN);
                            break;
                        case WBAProperties.CODE_211:
                            setViewsByMode(Mode.TEST_NO_MORE);
                            break;
                        case WBAProperties.CODE_401:
                            setViewsByMode(Mode.TAKE_A_TEST);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + testTodayResponse.getCode() + "]: " + testTodayResponse.getStatus());
                            break;
                    }
                } else {
                    Log.d(TAG, WBAProperties.ON_ERROR + ": [ws_testCheckToday]");
                }
            }

            @Override
            public void onFailure(Call<TestTodayResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }

    private void ws_testLatest(long userId) {
        Call<TestListResponse> callTestList = ApiClient.getClient().apiTestList(userId, 0, 5, null, null);
        callTestList.enqueue(new Callback<TestListResponse>() {
            @Override
            public void onResponse(Call<TestListResponse> call, Response<TestListResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);
                mDynamicBox.hideAll();

                if (response.isSuccessful()) {
                    TestListResponse testListResponse = response.body();
                    switch (testListResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            mRecyclerView.setAdapter(null);
                            mDataSet.clear();
                            mDataSet.addAll(testListResponse.getData());
                            mAdapter.setData(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);

                            if (mDataSet.size() <= 0) {
                                mDynamicBox.showCustomView(WBAParams.EMPTY_VIEW);
                            }
                            break;
                        default:
                            WBALogger.log("Status" + "[" + testListResponse.getCode() + "]: " + testListResponse.getStatus());
                            break;
                    }
                } else {
                    Log.d(TAG, WBAProperties.ON_ERROR + ": [ws_testLatest]");
                }
            }

            @Override
            public void onFailure(Call<TestListResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
                mDynamicBox.hideAll();
            }
        });
    }

    @Override
    public void ws(WsMode wsMode) {
        initViews();

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                setViewsByMode(Mode.TAKE_A_TEST);

                mRecyclerView.setAdapter(null);
                mDataSet.clear();
                mDataSet.addAll(WardahDummy.TEST_SIMPLE);
                mAdapter.setData(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
                break;
            case DEVELOPMENT:
            case PRODUCTION:
                switch (wsMode) {
                    case REFRESH:
                        mDynamicBox.showLoadingLayout();

                        Long userId = WBASession.getUserId();
                        if (userId == null) {
                            WBASession.loggingOut(getActivity());
                        } else {
                            ws_testCheckToday(userId);
                            ws_testLatest(userId);
                        }
                        break;
                }
                break;
        }
    }

    private void ws_testQuestions() {
        mMaterialDialog.show();

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                Intent iTestTaking = new Intent(getContext(), TestTakingActivity.class);
                IntentQuestions intentQuestions = new IntentQuestions(WardahDummy.QUESTION_SIMPLE);
                iTestTaking.putExtra(WBAParams.DATA, intentQuestions);
                startActivityForResult(iTestTaking, TEST_TAKING_ACTIVITY);
                mMaterialDialog.dismiss();
                break;
            case DEVELOPMENT:
            case PRODUCTION:
                Long userId = WBASession.getUserId();
                if (userId == null) {
                    mMaterialDialog.dismiss();
                    WBASession.loggingOut(getActivity());
                } else {
                    Call<TestQuestionsResponse> callTestList = ApiClient.getClient().apiTestQuestions(userId);
                    callTestList.enqueue(new Callback<TestQuestionsResponse>() {
                        @Override
                        public void onResponse(Call<TestQuestionsResponse> call, Response<TestQuestionsResponse> response) {
                            WBALogger.log(WBAProperties.ON_RESPONSE);
                            mMaterialDialog.dismiss();

                            if (response.isSuccessful()) {
                                TestQuestionsResponse rps = response.body();
                                switch (rps.getCode()) {
                                    case WBAProperties.CODE_200:
                                        if (rps.getData().size() > 0) {
                                            Intent iTestTaking = new Intent(getContext(), TestTakingActivity.class);
                                            IntentQuestions intentQuestions = new IntentQuestions(rps.getData());
                                            iTestTaking.putExtra(WBAParams.DATA, intentQuestions);
                                            iTestTaking.putExtra(WBAParams.DATE, new Date().getTime());
                                            iTestTaking.putExtra(WBAParams.LOCK, false);
                                            startActivityForResult(iTestTaking, TEST_TAKING_ACTIVITY);
                                        } else {
                                            WBAPopUp.toastMessage(TAG, getString(R.string.message_test_no_more));
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
                        public void onFailure(Call<TestQuestionsResponse> call, Throwable t) {
                            WBAPopUp.toastMessage(TAG, t.getMessage());
                            mMaterialDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }
}
