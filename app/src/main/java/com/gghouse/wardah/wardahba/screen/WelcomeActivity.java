package com.gghouse.wardah.wardahba.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.util.FileUtil;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.common.WBAUser;
import com.gghouse.wardah.wardahba.enumeration.UserTypeEnum;
import com.gghouse.wardah.wardahba.model.User;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.util.WBASystem;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.request.LoginRequest;
import com.gghouse.wardah.wardahba.webservices.response.LoginResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gghouse.wardah.wardahba.util.WBAValidation.isPasswordValid;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = WelcomeActivity.class.getSimpleName();

    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 77;

    private ImageView mIVImage;
    private EditText mETUsername;
    private EditText mETPassword;
    private Button mBSignIn;

    private MaterialDialog mMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(null)
                .content(R.string.title_logging_in)
                .cancelable(false)
                .progress(true, 0);
        mMaterialDialog = builder.build();

        requestPermission();

        mIVImage = (ImageView) findViewById(R.id.iv_image);
        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
            case DEVELOPMENT:
                mIVImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String curIP = WBASession.loadIPAddress();
                        new MaterialDialog.Builder(v.getContext())
                                .title(R.string.title_ip_address)
                                .content(null)
                                .inputType(InputType.TYPE_CLASS_TEXT)
                                .input(null, curIP, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        WBASession.saveIPAddress(input.toString());
                                    }
                                })
                                .positiveColorRes(R.color.colorPrimary)
                                .positiveText(R.string.action_ok)
                                .negativeColorRes(R.color.colorAccent)
                                .negativeText(R.string.action_cancel)
                                .cancelable(false)
                                .show();
                        return true;
                    }
                });
                break;
            case PRODUCTION:
                break;
        }
        mETUsername = (EditText) findViewById(R.id.username);
        mETPassword = (EditText) findViewById(R.id.password);
        mETPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mBSignIn = (Button) findViewById(R.id.sign_in);
        mBSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
                mETUsername.setText(WBAUser.DUMMY_NIP);
                mETPassword.setText(WBAUser.DUMMY_PASSWORD);
                break;
            case DEVELOPMENT:
            case PRODUCTION:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void attemptLogin() {
        // Reset errors.
        mETUsername.setError(null);
        mETPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = mETUsername.getText().toString();
        String password = mETPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (WBAProperties.BYPASS_LOGIN_VALIDATION) {

        } else {
            // Check for a valid email address.
            if (TextUtils.isEmpty(username)) {
                mETUsername.setError(getString(R.string.error_field_required));
                focusView = mETUsername;
                cancel = true;
            }
            // Check for a valid password, if the user entered one.
            else if (TextUtils.isEmpty(password)) {
                mETPassword.setError(getString(R.string.error_field_required));
                focusView = mETPassword;
                cancel = true;
            } else if (!isPasswordValid(password)) {
                mETPassword.setError(getString(R.string.error_password_too_short));
                focusView = mETPassword;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            switch (WBAProperties.mode) {
                case DUMMY_DEVELOPMENT:
                    User user = WardahApp.getInstance().getGson().fromJson(FileUtil.Companion.loadJSONFileToString(R.raw.user), User.class);
                    actionLogin(user);
                    break;
                case DEVELOPMENT:
                case PRODUCTION:
                    ws_login(username, password);
                    break;
            }
        }
    }

    private void ws_login(String username, String password) {
        mMaterialDialog.show();

        Call<LoginResponse> callUserlogin = ApiClient.getClient().apiLogin(new LoginRequest(
                username, password, WBASystem.getImei(this), FirebaseInstanceId.getInstance().getToken()));
        callUserlogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);
                mMaterialDialog.dismiss();

                if (response.isSuccessful()) {
                    LoginResponse rps = response.body();
                    switch (rps.getCode()) {
                        case WBAProperties.CODE_200:
                            actionLogin(rps.getData());
                            break;
                        default:
                            WBAPopUp.toastMessage(TAG, R.string.message_login_invaled);
                            break;
                    }
                } else {
                    WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                WBAPopUp.toastMessage(TAG, t.getMessage());
                mMaterialDialog.dismiss();
            }
        });
    }

    private void actionLogin(User user) {
        WBASession.loggingIn(user);

        /*
         * Checking User Type (BA, BP, BP Leader or FC)
         */
        try {
            UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(user.getUserType());
            switch (userTypeEnum) {
                case BEAUTY_ADVISER:
                case BEAUTY_PROMOTER:
                case FIELD_CONTROLLER:
                case BEAUTY_PROMOTER_LEADER:
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        } catch (IllegalArgumentException iae) {
            Log.e(this.getClass().getSimpleName(), iae.getMessage());
        }
    }

    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
