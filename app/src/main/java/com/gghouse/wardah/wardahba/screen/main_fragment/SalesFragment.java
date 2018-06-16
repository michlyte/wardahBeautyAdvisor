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
import com.gghouse.wardah.wardahba.dummy.SalesDummy;
import com.gghouse.wardah.wardahba.model.IntentProductHighlight;
import com.gghouse.wardah.wardahba.model.ProductHighlight;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.screen.SalesHistoryActivity;
import com.gghouse.wardah.wardahba.screen.SalesInputActivity;
import com.gghouse.wardah.wardahba.screen.adapter.SalesAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WardahTabInterface;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.response.GenericResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesLatestResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesProductResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesFragment extends Fragment implements WardahTabInterface, View.OnClickListener {

    public static final String TAG = SalesFragment.class.getSimpleName();

    static final int SALES_INPUT_ACTIVITY = 11;
    static final int QUESTIONER_ACTIVITY = 12;
    static final int SALES_HISTORY_ACTIVITY = 13;

    private FrameLayout mFlInputSales;
    private ImageView mIvImage;
    private Button mBInputSales;

    private FrameLayout mFlSalesDone;
    private ImageView mIvImageDone;

    private LinearLayout mLlSectionHeader;

    private TextView mTVMore;
    private ImageView mIVMore;

    private View mVLine;

    private RecyclerView mRecyclerView;
    private SalesAdapter mAdapter;
    private List<Sales> mDataSet;

    /*
     * Loading view
     */
    private DynamicBox mDynamicBox;
    private MaterialDialog mMaterialDialog;

    /*
     * Product Highlight
     */
    private IntentProductHighlight mIntentProductHighlight;

    public SalesFragment() {
    }

    public static SalesFragment newInstance() {
        SalesFragment fragment = new SalesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sales_fragment, menu);
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
        View view = inflater.inflate(R.layout.screen_fragment_sales, container, false);

        initContainerTopViews(view, inflater, container);

        mTVMore = (TextView) view.findViewById(R.id.tv_more);
        mIVMore = (ImageView) view.findViewById(R.id.iv_more);
        mIVMore.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mTVMore.setOnClickListener(this);
        mIVMore.setOnClickListener(this);

        mDataSet = new ArrayList<Sales>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SalesAdapter(getContext(), mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mDynamicBox = new DynamicBox(getActivity(), mRecyclerView);
        View emptyView = inflater.inflate(R.layout.dynamic_box_empty, container, false);
        mDynamicBox.addCustomView(emptyView, WBAParams.EMPTY_VIEW);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(R.string.title_loading)
                .content(R.string.title_retrieving_data)
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
    public void onResume() {
        super.onResume();

        if (WBASession.doesSalesNeedRefresh()) {
            WBASession.setSalesNeedRefresh(false);
            ws(WsMode.REFRESH);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SALES_INPUT_ACTIVITY || requestCode == QUESTIONER_ACTIVITY || requestCode == SALES_HISTORY_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                ws(WsMode.REFRESH);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
            case R.id.iv_more:
                Intent iSalesHistory = new Intent(getContext(), SalesHistoryActivity.class);
                startActivityForResult(iSalesHistory, SALES_HISTORY_ACTIVITY);
                break;
            case R.id.b_input_sales:
                Long userId = WBASession.getUserId();
                if (userId == null) {
                    WBASession.loggingOut(getActivity());
                } else {
                    ws_salesProductHighlight(userId);
                }
                break;
        }
    }

    /*
         * Top container
         */
    private void initContainerTopViews(View view, LayoutInflater inflater, ViewGroup container) {
        /*
         * Take a test view
         */
        mFlInputSales = (FrameLayout) view.findViewById(R.id.fl_input_sales);
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        Picasso.with(getContext())
                .load(WBAImages.salesInput)
                .fit()
                .centerCrop()
                .into(mIvImage);
        mBInputSales = (Button) view.findViewById(R.id.b_input_sales);
        mBInputSales.setOnClickListener(this);

        /*
         * Test has taken view
         */
        mFlSalesDone = (FrameLayout) view.findViewById(R.id.fl_sales_done);
        mIvImageDone = (ImageView) view.findViewById(R.id.iv_image_done);
        Picasso.with(getContext())
                .load(WBAImages.salesInputDone)
                .fit()
                .centerCrop()
                .into(mIvImageDone);

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
        INPUT_SALES,
        SALES_DONE;
    }

    private void setViewsByMode(Mode mode) {
        mLlSectionHeader.setVisibility(View.VISIBLE);
        mVLine.setVisibility(View.VISIBLE);

        switch (mode) {
            case INPUT_SALES:
                mFlInputSales.setVisibility(View.VISIBLE);
                mFlSalesDone.setVisibility(View.GONE);
                break;
            case SALES_DONE:
                mFlInputSales.setVisibility(View.GONE);
                mFlSalesDone.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initViews() {
        mFlInputSales.setVisibility(View.GONE);
        mFlSalesDone.setVisibility(View.GONE);
        mLlSectionHeader.setVisibility(View.GONE);
        mVLine.setVisibility(View.GONE);
    }

    /*
     * Web services
     */
    private void ws_salesToday(long userId) {
        Call<GenericResponse> callSalesToday = ApiClient.getClient().apiSalesToday(userId);
        callSalesToday.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                if (response.isSuccessful()) {
                    GenericResponse genericResponse = response.body();
                    switch (genericResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            setViewsByMode(Mode.SALES_DONE);
                            break;
                        case WBAProperties.CODE_401:
                            setViewsByMode(Mode.INPUT_SALES);
                            break;
                        default:
                            WBALogger.log("Status" + "[" + genericResponse.getCode() + "]: " + genericResponse.getStatus());
                            break;
                    }
                } else {
                    Log.d(TAG, WBAProperties.ON_ERROR + ": [ws_salesToday]");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }

    private void ws_salesLatest(long userId) {
        Call<SalesLatestResponse> callSalesLatest = ApiClient.getClient().apiSalesLatest(userId);
        callSalesLatest.enqueue(new Callback<SalesLatestResponse>() {
            @Override
            public void onResponse(Call<SalesLatestResponse> call, Response<SalesLatestResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);
                mDynamicBox.hideAll();

                if (response.isSuccessful()) {
                    SalesLatestResponse salesLatestResponse = response.body();
                    switch (salesLatestResponse.getCode()) {
                        case WBAProperties.CODE_200:
                            mRecyclerView.setAdapter(null);
                            mDataSet.clear();
                            mDataSet.addAll(salesLatestResponse.getData());
                            mAdapter.setData(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);

                            if (mDataSet.size() <= 0) {
                                mDynamicBox.showCustomView(WBAParams.EMPTY_VIEW);
                            }
                            break;
                        default:
                            WBALogger.log("Status" + "[" + salesLatestResponse.getCode() + "]: " + salesLatestResponse.getStatus());
                            break;
                    }
                } else {
                    Log.d(TAG, WBAProperties.ON_ERROR + ": [ws_salesLatest]");
                }
            }

            @Override
            public void onFailure(Call<SalesLatestResponse> call, Throwable t) {
                WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
                mDynamicBox.hideAll();
            }
        });
    }

    private void ws_salesProductHighlight(long userId) {
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                Intent iSalesInput = new Intent(getActivity(), SalesInputActivity.class);
                mIntentProductHighlight = new IntentProductHighlight(new ArrayList<ProductHighlight>());
                iSalesInput.putExtra(WBAParams.DATA, mIntentProductHighlight);
                iSalesInput.putExtra(WBAParams.DATE, new Date().getTime());
                iSalesInput.putExtra(WBAParams.LOCK, false);
                startActivityForResult(iSalesInput, SALES_INPUT_ACTIVITY);
                break;
            case DEVELOPMENT:
            case PRODUCTION:
                mMaterialDialog.show();
                Call<SalesProductResponse> callSalesProductHighlight = ApiClient.getClient().apiSalesProductHighlight(userId);
                callSalesProductHighlight.enqueue(new Callback<SalesProductResponse>() {
                    @Override
                    public void onResponse(Call<SalesProductResponse> call, Response<SalesProductResponse> response) {
                        WBALogger.log(WBAProperties.ON_RESPONSE);
                        mMaterialDialog.dismiss();

                        if (response.isSuccessful()) {
                            SalesProductResponse rps = response.body();
                            switch (rps.getCode()) {
                                case WBAProperties.CODE_200:
                                    Intent iSalesInput = new Intent(getActivity(), SalesInputActivity.class);
                                    mIntentProductHighlight = new IntentProductHighlight(rps.getData());
                                    iSalesInput.putExtra(WBAParams.DATA, mIntentProductHighlight);
                                    iSalesInput.putExtra(WBAParams.DATE, new Date().getTime());
                                    iSalesInput.putExtra(WBAParams.LOCK, false);
                                    startActivityForResult(iSalesInput, SALES_INPUT_ACTIVITY);
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
                    public void onFailure(Call<SalesProductResponse> call, Throwable t) {
                        WBAPopUp.toastMessage(TAG, t.getMessage());
                        mMaterialDialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void ws(WsMode wsMode) {
        initViews();

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                setViewsByMode(Mode.INPUT_SALES);

                mRecyclerView.setAdapter(null);
                mDataSet.clear();
                mDataSet.addAll(SalesDummy.ITEMS);
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
                            ws_salesToday(userId);
                            ws_salesLatest(userId);
                        }
                        break;
                }
                break;
        }
    }
}