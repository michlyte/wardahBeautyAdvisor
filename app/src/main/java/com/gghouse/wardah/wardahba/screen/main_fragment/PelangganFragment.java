package com.gghouse.wardah.wardahba.screen.main_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.PelangganSimpleDummy;
import com.gghouse.wardah.wardahba.enumeration.SimpleAdapterTypeEnum;
import com.gghouse.wardah.wardahba.model.Pelanggan;
import com.gghouse.wardah.wardahba.screen.adapter.WardahSimpleAdapter;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBASession;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

public class PelangganFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PelangganFragment.class.getSimpleName();

    static final int SALES_INPUT_ACTIVITY = 11;
    static final int QUESTIONER_ACTIVITY = 12;
    static final int SALES_HISTORY_ACTIVITY = 13;

//    private FrameLayout mFlInputSales;
//    private ImageView mIvImage;
//    private Button mBInputSales;
//
//    private FrameLayout mFlSalesDone;
//    private ImageView mIvImageDone;
//
//    private LinearLayout mLlSectionHeader;

    private TextView mTVMore;
    private ImageView mIVMore;

    private View mVLine;

    private RecyclerView mRecyclerView;
    private WardahSimpleAdapter mAdapter;
    private List<Pelanggan> mDataSet;

    /*
     * Loading view
     */
    private DynamicBox mDynamicBox;
    private MaterialDialog mMaterialDialog;

    /*
     * Product Highlight
     */
//    private IntentProductHighlight mIntentProductHighlight;

    public PelangganFragment() {
    }

    public static PelangganFragment newInstance() {
        PelangganFragment fragment = new PelangganFragment();
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
                ws_salesFragment(WsMode.REFRESH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_fragment_pelanggan, container, false);

//        initContainerTopViews(view, inflater, container);

        mTVMore = (TextView) view.findViewById(R.id.tv_more);
        mIVMore = (ImageView) view.findViewById(R.id.iv_more);
        mIVMore.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mTVMore.setOnClickListener(this);
        mIVMore.setOnClickListener(this);

        mDataSet = new ArrayList<Pelanggan>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WardahSimpleAdapter(getContext(), SimpleAdapterTypeEnum.PELANGGAN, mDataSet);
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

        ws_salesFragment(WsMode.REFRESH);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (WBASession.doesSalesNeedRefresh()) {
            WBASession.setSalesNeedRefresh(false);
            ws_salesFragment(WsMode.REFRESH);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SALES_INPUT_ACTIVITY || requestCode == QUESTIONER_ACTIVITY || requestCode == SALES_HISTORY_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                ws_salesFragment(WsMode.REFRESH);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
            case R.id.iv_more:
//                Intent iSalesHistory = new Intent(getContext(), SalesHistoryActivity.class);
//                startActivityForResult(iSalesHistory, SALES_HISTORY_ACTIVITY);
                break;
            case R.id.b_input_pelanggan:
                WBALogger.log(TAG, "Button input pelanggan is pressed.");
                break;
        }
    }

    /*
     * Top container
     */
//    private void initContainerTopViews(View view, LayoutInflater inflater, ViewGroup container) {
//        /*
//         * Take a test view
//         */
//        mFlInputSales = (FrameLayout) view.findViewById(R.id.fl_input_sales);
//        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
//        Picasso.with(getContext())
//                .load(WBAImages.salesInput)
//                .fit()
//                .centerCrop()
//                .into(mIvImage);
//        mBInputSales = (Button) view.findViewById(R.id.b_input_sales);
//        mBInputSales.setOnClickListener(this);
//
//        /*
//         * Test has taken view
//         */
//        mFlSalesDone = (FrameLayout) view.findViewById(R.id.fl_sales_done);
//        mIvImageDone = (ImageView) view.findViewById(R.id.iv_image_done);
//        Picasso.with(getContext())
//                .load(WBAImages.salesInputDone)
//                .fit()
//                .centerCrop()
//                .into(mIvImageDone);
//
//        /*
//         * Section header (Riwayat Tes)
//         */
//        mLlSectionHeader = (LinearLayout) view.findViewById(R.id.ll_section_header);
//
//        /*
//         * Line
//         */
//        mVLine = view.findViewById(R.id.v_line);
//    }

//    private enum Mode {
//        INPUT_SALES,
//        SALES_DONE;
//    }

//    private void setViewsByMode(Mode mode) {
//        mLlSectionHeader.setVisibility(View.VISIBLE);
//        mVLine.setVisibility(View.VISIBLE);
//
//        switch (mode) {
//            case INPUT_SALES:
//                mFlInputSales.setVisibility(View.VISIBLE);
//                mFlSalesDone.setVisibility(View.GONE);
//                break;
//            case SALES_DONE:
//                mFlInputSales.setVisibility(View.GONE);
//                mFlSalesDone.setVisibility(View.VISIBLE);
//                break;
//        }
//    }

//    private void initViews() {
//        mFlInputSales.setVisibility(View.GONE);
//        mFlSalesDone.setVisibility(View.GONE);
//        mLlSectionHeader.setVisibility(View.GONE);
//        mVLine.setVisibility(View.GONE);
//    }

    private void ws_salesFragment(WsMode wsMode) {
//        initViews();

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                mRecyclerView.setAdapter(null);
                mDataSet.clear();
                mDataSet.addAll(PelangganSimpleDummy.ITEMS);
                mAdapter.setData(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
                break;
            case DEVELOPMENT:
            case PRODUCTION:
//                switch (wsMode) {
//                    case REFRESH:
//                        mDynamicBox.showLoadingLayout();
//
//                        Long userId = WBASession.getUserId();
//                        if (userId == null) {
//                            WBASession.loggingOut(getActivity());
//                        } else {
//                            ws_salesToday(userId);
//                            ws_salesLatest(userId);
//                        }
//                        break;
//                }
                break;
        }
    }
}