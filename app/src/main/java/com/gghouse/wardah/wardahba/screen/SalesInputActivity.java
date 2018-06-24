package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.xw.repo.BubbleSeekBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class SalesInputActivity extends AppCompatActivity {

    public static final String TAG = SalesInputActivity.class.getSimpleName();

    /**
     * Sales input model
     */
    private class SalesInputModel {
        /**
         * Product Highlight
         */
        public List<ProductHighlight> mProductHighlightList;
        public Date mDate;
        /**
         * Locking
         */
        public Boolean mLock;
        /**
         * Konten 1x isi per hari (isReadOnly)
         */
        public Boolean isReadOnly;
        /**
         * Edit
         */
        public Boolean mEdit;
        public Sales mSales;

        SalesInputModel() {
            Intent i = getIntent();
            if (i == null) {
                mProductHighlightList = new ArrayList<ProductHighlight>();
                mDate = new Date();
                mLock = true;
                isReadOnly = false;
            } else {
                IntentProductHighlight intentProductHighlight = (IntentProductHighlight) i.getSerializableExtra(WBAParams.DATA);
                mProductHighlightList = intentProductHighlight.getObjects();
                mDate = WBAUtil.getDateFromParam(i, WBAParams.DATE);
                mLock = WBAUtil.getBoolFromParam(i, WBAParams.LOCK);
                isReadOnly = WBAUtil.getBoolFromParam(i, WBAParams.SALES_IS_READ_ONLY);
                mEdit = WBAUtil.getBoolFromParam(i, WBAParams.EDIT);
                mSales = (Sales) i.getExtras().get(WBAParams.SALES);
            }
        }

        public List<Product> getProductList() {
            List<Product> products = new ArrayList<Product>();

            for (ProductHighlight productHighlight : mProductHighlightList) {
                if (productHighlight.getQty() == null) {
                    products.add(new Product(productHighlight.getId(), 0));
                } else {
                    products.add(new Product(productHighlight.getId(), productHighlight.getQty()));
                }
            }

            return products;
        }
    }

    /**
     * Binding all views class
     */
    private class SalesInputBindViews {
        public final TextView dateTextView;
        public final EditText nominalEditText;
        public final TextView productHighlightTextView;
        public final RecyclerView recyclerView;
        public final RecyclerView.Adapter mAdapter;
        public final RecyclerView.LayoutManager mLayoutManager;

        public final TextView onceADayContentTextView;
        public final LinearLayout onceADayContentLinearLayout;
        public final EditText pcsEditText;
        public final EditText visitorEditText;
        public final EditText billEditText;
        public final BubbleSeekBar buyingPowerBubbleSeekBar;

        public final MaterialDialog materialDialog;

        SalesInputBindViews(Activity activity, SalesInputModel salesInputModel) {
            /**
             * Jumlah Penjualan
             */
            dateTextView = (TextView) findViewById(R.id.tv_date);
            nominalEditText = (EditText) findViewById(R.id.et_ASI_sales);
            productHighlightTextView = (TextView) findViewById(R.id.tv_product_highlight);
            recyclerView = (RecyclerView) findViewById(R.id.rv_ASI_list);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new SalesInputAdapter(activity, salesInputModel.mProductHighlightList);
            recyclerView.setAdapter(mAdapter);

            /**
             * Konten 1x isi per hari
             */
            onceADayContentTextView = (TextView) findViewById(R.id.tvOnceADayContent);
            onceADayContentLinearLayout = (LinearLayout) findViewById(R.id.llOnceADayContent);
            pcsEditText = (EditText) findViewById(R.id.etPcs);
            visitorEditText = (EditText) findViewById(R.id.etVisitor);
            billEditText = (EditText) findViewById(R.id.etBill);
            buyingPowerBubbleSeekBar = (BubbleSeekBar) findViewById(R.id.bsbBuyingPower);

            /**
             * Material Loading
             */
            MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                    .title(null)
                    .content(R.string.message_sales_input_send)
                    .cancelable(false)
                    .progress(true, 0);
            materialDialog = builder.build();

            setIsReadOnly(salesInputModel.isReadOnly);
        }

        public void setIsReadOnly(boolean isReadOnly) {
            if (isReadOnly) {
                onceADayContentLinearLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                onceADayContentLinearLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
            pcsEditText.setEnabled(!isReadOnly);
            visitorEditText.setEnabled(!isReadOnly);
            billEditText.setEnabled(!isReadOnly);
            buyingPowerBubbleSeekBar.setEnabled(!isReadOnly);
        }
    }

    /**
     * Object sales input model
     */
    private SalesInputModel bModel;
    /**
     * Object binding views
     */
    private SalesInputBindViews bView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_input);
        /**
         * Get parameter from previous activity
         */
        bModel = new SalesInputModel();
        getSupportActionBar().setDisplayHomeAsUpEnabled(!bModel.mLock);

        /**
         * Init bind views
         */
        bView = new SalesInputBindViews(this, bModel);

        /**
         * Set listeners
         */
        bView.nominalEditText.addTextChangedListener(new NumberTextWatcher(bView.nominalEditText));
        if (bModel.mEdit)
            bView.nominalEditText.setText(String.valueOf(bModel.mSales.getSalesAmount()));

        bView.buyingPowerBubbleSeekBar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0, "<50rb");
                array.put(1, "<100rb");
                array.put(2, "<150rb");
                array.put(3, "<300rb");
                array.put(4, ">300rb");
                return array;
            }
        });

        bView.dateTextView.setText(WBAProperties.sdfDate.format(bModel.mDate));
        if (bModel.mProductHighlightList.size() <= 0) {
            bView.productHighlightTextView.setVisibility(View.GONE);
            bView.recyclerView.setVisibility(View.GONE);
        }
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
        if (bModel.mLock) {
            new MaterialDialog.Builder(this)
                    .title(R.string.title_warning)
                    .content(R.string.message_sales_input_cancel)
                    .positiveText(R.string.action_iya)
                    .positiveColorRes(R.color.colorPrimary)
                    .show();
        } else {
            if (bModel.mEdit) {
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
        bView.nominalEditText.setError(null);
        bView.pcsEditText.setError(null);
        bView.visitorEditText.setError(null);
        bView.billEditText.setError(null);

        String nominalStr = bView.nominalEditText.getText().toString().replace(",", "");
        Double nominal = null;

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nominalStr)) {
            bView.nominalEditText.setError(getString(R.string.error_field_required));
            focusView = bView.nominalEditText;
            cancel = true;
        } else {
            try {
                nominal = Double.parseDouble(nominalStr);
            } catch (Exception e) {
                bView.nominalEditText.setError(getString(R.string.error_field_invalid));
                focusView = bView.nominalEditText;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            final Double fNominal = nominal;
            if (bModel.mEdit) {
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
            bView.materialDialog.show();

            SalesRequest salesRequest = new SalesRequest(userId, nominal, bModel.mDate.getTime(), bModel.getProductList());

            Call<SalesResponse> callSalesSubmit = ApiClient.getClient().apiSales(salesRequest);
            callSalesSubmit.enqueue(new Callback<SalesResponse>() {
                @Override
                public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);

                    bView.materialDialog.dismiss();

                    if (response.isSuccessful()) {
                        SalesResponse rps = response.body();
                        switch (rps.getCode()) {
                            case WBAProperties.CODE_200:
                                if (!bModel.mLock) {
                                    Intent iQuestioner = new Intent(getApplicationContext(), QuestionerActivity.class);
                                    iQuestioner.putExtra(WBAParams.DATE, bModel.mDate.getTime());
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
                    bView.materialDialog.dismiss();
                }
            });
        }
    }

    private void ws_salesEdit(Double nominal) {
        Long userId = WBASession.getUserId();
        if (userId == null) {
            WBASession.loggingOut(this);
        } else {
            bView.materialDialog.show();

            SalesEditRequest salesEditRequest = new SalesEditRequest(bModel.mSales.getId(), userId, nominal, new ArrayList<Product>());

            Call<SalesResponse> callSalesEdit = ApiClient.getClient().apiSalesEdit(salesEditRequest);
            callSalesEdit.enqueue(new Callback<SalesResponse>() {
                @Override
                public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);

                    bView.materialDialog.dismiss();

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
                    bView.materialDialog.dismiss();
                }
            });
        }
    }
}
