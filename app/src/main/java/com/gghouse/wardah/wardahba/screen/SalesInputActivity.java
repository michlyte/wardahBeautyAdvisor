package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.helper.NumberTextWatcher;
import com.gghouse.wardah.wardahba.model.IntentProductHighlight;
import com.gghouse.wardah.wardahba.model.ProductHighlight;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.screen.adapter.SalesInputAdapter;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.util.WBAUtil;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.model.Product;
import com.gghouse.wardah.wardahba.webservices.request.SalesEditRequest;
import com.gghouse.wardah.wardahba.webservices.request.SalesRequest;
import com.gghouse.wardah.wardahba.webservices.response.SalesResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class SalesInputActivity extends AppCompatActivity {

    public static final String TAG = SalesInputActivity.class.getSimpleName();

    private TextView mTVDate;
    private TextView mTVProductHighlight;
    private EditText mETNominal;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /*
     * Product Highlight
     */

    private List<ProductHighlight> mProductHighlightList;
    private Date mDate;
    private Boolean mLock;

    /*
     * Edit
     */
    private Boolean mEdit;
    private Sales mSales;

    /*
     * Loading View
     */

    MaterialDialog mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_input);

        Intent i = getIntent();
        if (i == null) {
            mProductHighlightList = new ArrayList<ProductHighlight>();
            mDate = new Date();
            mLock = true;
        } else {
            IntentProductHighlight intentProductHighlight = (IntentProductHighlight) i.getSerializableExtra(WBAParams.DATA);
            mProductHighlightList = intentProductHighlight.getObjects();
            mDate = WBAUtil.getDateFromParam(i, WBAParams.DATE);
            mLock = WBAUtil.getBoolFromParam(i, WBAParams.LOCK);
            mEdit = WBAUtil.getBoolFromParam(i, WBAParams.EDIT);
            mSales = (Sales) i.getExtras().get(WBAParams.SALES);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(!mLock);

        mTVProductHighlight = (TextView) findViewById(R.id.tv_product_highlight);
        mTVDate = (TextView) findViewById(R.id.tv_date);
        mETNominal = (EditText) findViewById(R.id.et_ASI_sales);
        mETNominal.addTextChangedListener(new NumberTextWatcher(mETNominal));
        if (mEdit)
            mETNominal.setText(mSales.getSalesAmount() + "");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ASI_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SalesInputAdapter(this, mProductHighlightList);
        mRecyclerView.setAdapter(mAdapter);

        mTVDate.setText(WBAProperties.sdfDate.format(mDate));

        if (mProductHighlightList.size() <= 0) {
            mTVProductHighlight.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(null)
                .content(R.string.message_sales_input_send)
                .cancelable(false)
                .progress(true, 0);
        mLoading = builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sales_product_highlight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                attemptCancel();
                return true;
            case R.id.action_done:
                attemptSubmit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        attemptCancel();
    }

    private void attemptCancel() {
        if (mLock) {
            new MaterialDialog.Builder(this)
                    .title(R.string.title_warning)
                    .content(R.string.message_sales_input_cancel)
                    .positiveText(R.string.action_iya)
                    .positiveColorRes(R.color.colorPrimary)
                    .show();
        } else {
            if (mEdit) {
                finish();
            } else {
                new MaterialDialog.Builder(this)
                        .title(R.string.prompt_konfirmasi)
                        .content(R.string.message_common_cancel)
                        .positiveText(R.string.action_iya)
                        .positiveColorRes(R.color.colorPrimary)
                        .negativeText(R.string.action_tidak)
                        .negativeColorRes(R.color.colorAccent)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

    private void attemptSubmit() {
        mETNominal.setError(null);

        String nominalStr = mETNominal.getText().toString().replace(",", "");
        Double nominal = null;

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nominalStr)) {
            mETNominal.setError(getString(R.string.error_field_required));
            focusView = mETNominal;
            cancel = true;
        } else {
            try {
                nominal = Double.parseDouble(nominalStr);
            } catch (Exception e) {
                mETNominal.setError(getString(R.string.error_field_invalid));
                focusView = mETNominal;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            final Double fNominal = nominal;
            if (mEdit) {
                new MaterialDialog.Builder(this)
                        .title(R.string.prompt_konfirmasi)
                        .content(R.string.message_sales_edit_confirmation)
                        .positiveText(R.string.action_iya)
                        .positiveColorRes(R.color.colorPrimary)
                        .negativeText(R.string.action_tidak)
                        .negativeColorRes(R.color.colorAccent)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ws_salesEdit(fNominal);
                            }
                        })
                        .show();
            } else {
                new MaterialDialog.Builder(this)
                        .title(R.string.prompt_konfirmasi)
                        .content(R.string.message_sales_input_confirmation)
                        .positiveText(R.string.action_iya)
                        .positiveColorRes(R.color.colorPrimary)
                        .negativeText(R.string.action_tidak)
                        .negativeColorRes(R.color.colorAccent)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ws_salesSubmit(fNominal);
                            }
                        })
                        .show();
            }
        }
    }

    private void ws_salesSubmit(Double nominal) {
        Long userId = WBASession.getUserId();
        if (userId == null) {
            WBASession.loggingOut(this);
        } else {
            mLoading.show();

            List<Product> products = new ArrayList<Product>();

            for (ProductHighlight productHighlight : mProductHighlightList) {
                if (productHighlight.getQty() == null) {
                    products.add(new Product(productHighlight.getId(), 0));
                } else {
                    products.add(new Product(productHighlight.getId(), productHighlight.getQty()));
                }
            }
            SalesRequest salesRequest = new SalesRequest(userId, nominal, mDate.getTime(), products);

            Call<SalesResponse> callSalesSubmit = ApiClient.getClient().apiSales(salesRequest);
            callSalesSubmit.enqueue(new Callback<SalesResponse>() {
                @Override
                public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);

                    mLoading.dismiss();

                    if (response.isSuccessful()) {
                        SalesResponse rps = response.body();
                        switch (rps.getCode()) {
                            case WBAProperties.CODE_200:
                                if (!mLock) {
                                    Intent iQuestioner = new Intent(getApplicationContext(), QuestionerActivity.class);
                                    iQuestioner.putExtra(WBAParams.DATE, mDate.getTime());
                                    startActivity(iQuestioner);
                                }

                                setResult(Activity.RESULT_OK);
                                finish();
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
                public void onFailure(Call<SalesResponse> call, Throwable t) {
                    WBAPopUp.toastMessage(TAG, t.getMessage());
                    mLoading.dismiss();
                }
            });
        }
    }

    private void ws_salesEdit(Double nominal) {
        Long userId = WBASession.getUserId();
        if (userId == null) {
            WBASession.loggingOut(this);
        } else {
            mLoading.show();

            SalesEditRequest salesEditRequest = new SalesEditRequest(mSales.getId(), userId, nominal, new ArrayList<Product>());

            Call<SalesResponse> callSalesEdit = ApiClient.getClient().apiSalesEdit(salesEditRequest);
            callSalesEdit.enqueue(new Callback<SalesResponse>() {
                @Override
                public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);

                    mLoading.dismiss();

                    if (response.isSuccessful()) {
                        SalesResponse rps = response.body();
                        switch (rps.getCode()) {
                            case WBAProperties.CODE_200:
                                setResult(Activity.RESULT_OK);
                                finish();
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
                public void onFailure(Call<SalesResponse> call, Throwable t) {
                    WBAPopUp.toastMessage(TAG, t.getMessage());
                    mLoading.dismiss();
                }
            });
        }
    }
}
