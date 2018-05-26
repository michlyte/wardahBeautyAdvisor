package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.dummy.QuestionerDummy;
import com.gghouse.wardah.wardahba.model.Questioner;
import com.gghouse.wardah.wardahba.screen.adapter.QuestionerAdapter;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.util.WBAUtil;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.request.QuestionerRequest;
import com.gghouse.wardah.wardahba.webservices.response.GenericResponse;
import com.gghouse.wardah.wardahba.webservices.response.QuestionerResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michaelhalim on 5/25/17.
 */

public class QuestionerActivity extends AppCompatActivity {

    public static final String TAG = QuestionerActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private QuestionerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Questioner> mDataSet;

    /*
     * Loading View
     */
    private DynamicBox mDynamicBox;
    private MaterialDialog mLoading;

    private Date mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner);

        Intent i = getIntent();
        if (i == null) {
            mDate = new Date();
        } else {
            mDate = WBAUtil.getDateFromParam(i, WBAParams.DATE);
        }

        String title = getSupportActionBar().getTitle().toString() + " - " + WBAProperties.sdfFilter.format(mDate);
        getSupportActionBar().setTitle(title);

        mDataSet = new ArrayList<Questioner>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new QuestionerAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mDynamicBox = new DynamicBox(this, mRecyclerView);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(null)
                .content(R.string.message_questioner_send)
                .cancelable(false)
                .progress(true, 0);
        mLoading = builder.build();

        ws_questioner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_questioner, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_warning)
                .content(R.string.message_questioner_cancel)
                .positiveText(R.string.action_ok)
                .positiveColorRes(R.color.colorPrimary)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ws_questioner();
                return true;
            case R.id.action_done:
                attemptDone();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptDone() {
        boolean cancel = false;

        for (Questioner questioner : mDataSet) {
            if (questioner.getRating() == null || questioner.getRating() < 1f) {
                cancel = true;
            }
        }

        if (cancel) {
            new MaterialDialog.Builder(this)
                    .title(R.string.title_warning)
                    .content(R.string.message_questioner_invalid)
                    .positiveText(R.string.action_ok)
                    .positiveColorRes(R.color.colorPrimary)
                    .show();
        } else {
            ws_postQuestioner();
        }
    }

    /*
     * Web Services
     */
    private void ws_getQuestioner() {
        mDynamicBox.showLoadingLayout();
        Call<QuestionerResponse> callQuestioner = ApiClient.getClient().apiQuestioner();
        callQuestioner.enqueue(new Callback<QuestionerResponse>() {
            @Override
            public void onResponse(Call<QuestionerResponse> call, Response<QuestionerResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);

                mDynamicBox.hideAll();

                if (response.isSuccessful()) {
                    QuestionerResponse rps = response.body();
                    switch (rps.getCode()) {
                        case WBAProperties.CODE_200:
                            mRecyclerView.setAdapter(null);
                            mDataSet.clear();
                            mDataSet.addAll(rps.getData());
                            mAdapter.setData(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                            break;
                        default:
                            break;
                    }
                } else {
                    WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                }
            }

            @Override
            public void onFailure(Call<QuestionerResponse> call, Throwable t) {
                WBAPopUp.toastMessage(TAG, t.getMessage());
            }
        });
    }

    private void ws_postQuestioner() {
        Long userId = WBASession.getUserId();
        if (userId == null) {
            WBASession.loggingOut(this);
        } else {
            mLoading.show();
            List<com.gghouse.wardah.wardahba.webservices.model.Questioner> questionerList = new ArrayList<com.gghouse.wardah.wardahba.webservices.model.Questioner>();
            for (Questioner questioner : mDataSet) {
                questionerList.add(new com.gghouse.wardah.wardahba.webservices.model.Questioner(questioner.getId(), questioner.getRating()));
            }

            QuestionerRequest questionerRequest = new QuestionerRequest(userId, mDate.getTime(), questionerList);

            Call<GenericResponse> callPostQuestioner = ApiClient.getClient().apiPostQuestioner(questionerRequest);
            callPostQuestioner.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);

                    mLoading.dismiss();

                    if (response.isSuccessful()) {
                        GenericResponse rps = response.body();
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
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    WBAPopUp.toastMessage(TAG, t.getMessage());

                    mLoading.dismiss();
                }
            });
        }
    }

    private void ws_questioner() {
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                mRecyclerView.setAdapter(null);
                mDataSet.clear();
                mDataSet.addAll(QuestionerDummy.ITEMS);
                mAdapter.setData(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
                break;
            case DEVELOPMENT:
            case PRODUCTION:
                ws_getQuestioner();
                break;
        }
    }
}
