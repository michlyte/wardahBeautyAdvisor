package com.gghouse.wardah.wardahba.screen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.util.WBAValidation;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.request.ChangePasswordRequest;
import com.gghouse.wardah.wardahba.webservices.response.GenericResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String TAG = ChangePasswordActivity.class.getSimpleName();

    private EditText mEtCurPassword;
    private EditText mEtNewPassword;
    private EditText mEtConfirmNewPassword;

    private MaterialDialog mLoading;
    private MaterialDialog mSuccessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtCurPassword = (EditText) findViewById(R.id.et_cur_password);
        mEtNewPassword = (EditText) findViewById(R.id.et_new_password);
        mEtConfirmNewPassword = (EditText) findViewById(R.id.et_confirm_new_password);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_loading)
                .content(R.string.title_processing)
                .cancelable(false)
                .progress(true, 0);
        mLoading = builder.build();

        MaterialDialog.Builder successDialogBuilder = new MaterialDialog.Builder(this)
                .title(null)
                .content(R.string.message_change_password_success)
                .positiveText(R.string.action_iya)
                .positiveColorRes(R.color.colorPrimary)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                });
        mSuccessDialog = successDialogBuilder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply:
                attemptChangePassword();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attemptChangePassword() {
        // Reset errors.
        mEtCurPassword.setError(null);
        mEtNewPassword.setError(null);
        mEtConfirmNewPassword.setError(null);

        // Store values at the time of the login attempt.
        final String curPassword = mEtCurPassword.getText().toString();
        final String newPassword = mEtNewPassword.getText().toString();
        String confirmNewPassword = mEtConfirmNewPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(curPassword)) {
            mEtCurPassword.setError(getString(R.string.error_field_required));
            focusView = mEtCurPassword;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(newPassword)) {
            mEtNewPassword.setError(getString(R.string.error_field_required));
            focusView = mEtNewPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(confirmNewPassword)) {
            mEtConfirmNewPassword.setError(getString(R.string.error_field_required));
            focusView = mEtConfirmNewPassword;
            cancel = true;
        } else if (!WBAValidation.isPasswordMatch(newPassword, confirmNewPassword)) {
            mEtNewPassword.setError(getString(R.string.error_password_do_not_match));
            focusView = mEtNewPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.prompt_konfirmasi)
                    .content(R.string.message_change_password_confirmation)
                    .positiveText(R.string.action_iya)
                    .positiveColorRes(R.color.colorPrimary)
                    .negativeText(R.string.action_tidak)
                    .negativeColorRes(R.color.colorAccent)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ws_changePassword(curPassword, newPassword);
                        }
                    })
                    .show();
        }
    }

    private void ws_changePassword(String curPassword, String newPassword) {
        Long userId = WBASession.getUserId();
        if (userId == null) {
            WBASession.loggingOut(this);
        } else {
            mLoading.show();

            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(curPassword, newPassword);
            Call<GenericResponse> callChangePassword = ApiClient.getClient().apiChangePassword(userId, changePasswordRequest);
            callChangePassword.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);
                    mLoading.dismiss();

                    if (response.isSuccessful()) {
                        GenericResponse rps = response.body();
                        switch (rps.getCode()) {
                            case WBAProperties.CODE_200:
                                mSuccessDialog.show();
                                break;
                            case WBAProperties.CODE_99:
                                WBAPopUp.toastMessage(TAG, WardahApp.getInstance().getString(R.string.message_change_password_invalid));
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
}
