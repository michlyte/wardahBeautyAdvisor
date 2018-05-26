package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.IntentQuestions;
import com.gghouse.wardah.wardahba.model.Question;
import com.gghouse.wardah.wardahba.screen.adapter.TestTakingAdapter;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.util.WBAUtil;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.request.AnswerRequest;
import com.gghouse.wardah.wardahba.webservices.request.AnswersRequest;
import com.gghouse.wardah.wardahba.webservices.response.GenericResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestTakingActivity extends AppCompatActivity {

    public static final String TAG = TestTakingActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TestTakingAdapter mAdapter;

    /*
     * Question
     */
    private Date mDate;
    private Boolean mLock;
    private List<Question> mQuestionList;

    /*
     * Loading view
     */
    MaterialDialog mMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_taking);

        Intent i = getIntent();
        if (i == null) {
            mQuestionList = new ArrayList<Question>();
            mDate = new Date();
            mLock = true;
        } else {
            IntentQuestions intentQuestions = (IntentQuestions) i.getSerializableExtra(WBAParams.DATA);
            mQuestionList = intentQuestions.getObjects();
            mDate = WBAUtil.getDateFromParam(i, WBAParams.DATE);
            mLock = WBAUtil.getBoolFromParam(i, WBAParams.LOCK);
        }

        WBALogger.log("[onCreate] Date: " + WBAProperties.sdfFilter.format(mDate));

        getSupportActionBar().setDisplayHomeAsUpEnabled(!mLock);
        String title = getSupportActionBar().getTitle().toString() + " - " + WBAProperties.sdfFilter.format(mDate);
        getSupportActionBar().setTitle(title);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ATT_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TestTakingAdapter(this, mQuestionList);
        mRecyclerView.setAdapter(mAdapter);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_loading)
                .content(R.string.title_submitting_answers)
                .cancelable(false)
                .progress(true, 0);
        mMaterialDialog = builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test_taking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                attemptCancel();
                return true;
            case R.id.action_done:
                attemptDone();
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
                    .content(R.string.message_test_cancel)
                    .positiveText(R.string.action_iya)
                    .positiveColorRes(R.color.colorPrimary)
                    .show();
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

    private void attemptDone() {
        /*
         * Validation (All questions must have an answer)
         */
        if (mQuestionList.size() != mAdapter.getResultMap().entrySet().size()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.title_warning)
                    .content(R.string.title_answer_all_questions)
                    .positiveText(R.string.action_iya)
                    .positiveColorRes(R.color.colorPrimary)
                    .show();
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.prompt_konfirmasi)
                    .content(R.string.message_test_confirmation)
                    .positiveText(R.string.action_iya)
                    .positiveColorRes(R.color.colorPrimary)
                    .negativeText(R.string.action_tidak)
                    .negativeColorRes(R.color.colorAccent)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ws_submittingAnswers();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        }
                    })
                    .show();
        }
    }

    private void ws_submittingAnswers() {
        mMaterialDialog.show();

        Long userId = WBASession.getUserId();
        if (userId == null) {
            mMaterialDialog.dismiss();
            WBASession.loggingOut(this);
        } else {
            List<AnswerRequest> answerRequestList = new ArrayList<AnswerRequest>();
            for (Map.Entry<Long, Object> entry : mAdapter.getResultMap().entrySet()) {
                answerRequestList.add(new AnswerRequest(entry.getKey(), (long) entry.getValue()));
            }
            AnswersRequest answersRequest = new AnswersRequest(userId, mDate.getTime(), answerRequestList);

            Call<GenericResponse> callSubmitTest = ApiClient.getClient().apiSubmitTest(answersRequest);
            callSubmitTest.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);
                    mMaterialDialog.dismiss();

                    if (response.isSuccessful()) {
                        GenericResponse genericResponse = response.body();
                        switch (genericResponse.getCode()) {
                            case WBAProperties.CODE_200:
                                setResult(Activity.RESULT_OK);
                                finish();
                                break;
                            default:
                                WBALogger.log("Status" + "[" + genericResponse.getCode() + "]: " + genericResponse.getStatus());
                                break;
                        }
                    } else {
                        WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    WBALogger.log(WBAProperties.ON_FAILURE + ": " + t.getMessage());
                    mMaterialDialog.dismiss();
                }
            });
        }
    }
}
