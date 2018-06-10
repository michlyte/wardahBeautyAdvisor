package com.gghouse.wardah.wardahba.screen.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.enumeration.MainEnum;
import com.gghouse.wardah.wardahba.model.IntentProductHighlight;
import com.gghouse.wardah.wardahba.model.IntentQuestions;
import com.gghouse.wardah.wardahba.screen.ChangePasswordActivity;
import com.gghouse.wardah.wardahba.screen.ProfileActivity;
import com.gghouse.wardah.wardahba.screen.QuestionerActivity;
import com.gghouse.wardah.wardahba.screen.SalesInputActivity;
import com.gghouse.wardah.wardahba.screen.TestTakingActivity;
import com.gghouse.wardah.wardahba.screen.WelcomeActivity;
import com.gghouse.wardah.wardahba.screen.main_fragment.NotifFragment;
import com.gghouse.wardah.wardahba.screen.main_fragment.SalesFragment;
import com.gghouse.wardah.wardahba.screen.main_fragment.TestFragment;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBAPopUp;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.model.Lock;
import com.gghouse.wardah.wardahba.webservices.response.LockingResponse;
import com.gghouse.wardah.wardahba.webservices.response.ProfileResponse;
import com.gghouse.wardah.wardahba.webservices.response.SalesProductResponse;
import com.gghouse.wardah.wardahba.webservices.response.TestQuestionsResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BPMainActivity extends AppCompatActivity {

    public static final String TAG = BPMainActivity.class.getSimpleName();

    public static final int LOCK_INPUT_SALES = 20;
    public static final int LOCK_QUESTIONER = 21;
    public static final int LOCK_TEST = 22;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private Lock mLock;
    private MaterialDialog mLockingDialog;
    private MaterialDialog mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpmain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Loading
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_loading)
                .content(R.string.title_retrieving_data)
                .cancelable(false)
                .progress(true, 0);
        mLoading = builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                ws_profile();
                break;
            case R.id.action_change_password:
                Intent iChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(iChangePassword);
                return true;
            case R.id.action_logout:
                WBASession.loggingOut(this);

                Intent iLogin = new Intent(this, WelcomeActivity.class);
                startActivity(iLogin);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCK_INPUT_SALES) {
            if (resultCode == Activity.RESULT_OK) {
                WBASession.setSalesNeedRefresh(true);
                ws_checkLocking(false);
            }
        } else if (requestCode == LOCK_QUESTIONER || requestCode == LOCK_TEST) {
            if (resultCode == Activity.RESULT_OK) {
                ws_checkLocking(false);
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (MainEnum.getMainEnumById(position)) {
                case NOTIF:
                    return NotifFragment.newInstance();
                case TEST:
                    return TestFragment.newInstance();
                case SALES:
                    return SalesFragment.newInstance();
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return MainEnum.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(MainEnum.getMainEnumById(position).getTitleInt());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
            case DEVELOPMENT:
                /*
                 * RegId
                 */
                Log.d(TAG, "REG ID: " + FirebaseInstanceId.getInstance().getToken());
                break;
        }

        ws_checkLocking(true);
    }

    private DialogType getDialogType(Lock lock) {
        switch (lock.getInputSales()) {
            case WBAParams.Locked:
                return DialogType.SALES;
            default:
                break;
        }

        switch (lock.getInputTest()) {
            case WBAParams.Locked:
                return DialogType.TEST;
            default:
                break;
        }

        switch (lock.getInputQuestionnaire()) {
            case WBAParams.Locked:
                return DialogType.QUESTIONER;
            default:
                break;
        }

        return DialogType.SAFE;
    }

    private void directLocking(final long userId, final Lock lock) {
        switch (getDialogType(lock)) {
            case SALES:
                ws_salesProductHighlight(userId, lock.getSalesDateLong());
                break;
            case TEST:
                ws_testQuestions(userId, lock.getTestDateLong());
                break;
            case QUESTIONER:
                Intent iQuestioner = new Intent(getApplicationContext(), QuestionerActivity.class);
                iQuestioner.putExtra(WBAParams.DATE, lock.getQuestionnaireDateLong());
                startActivityForResult(iQuestioner, LOCK_QUESTIONER);
                break;
        }
    }

    private void showLockingDialog(final long userId, final Lock lock) {
        if (mLockingDialog != null) {
            mLockingDialog.dismiss();
        }

        MaterialDialog.Builder builder = null;
        switch (getDialogType(lock)) {
            case SALES:
                builder = new MaterialDialog.Builder(this)
                        .title(R.string.title_warning)
                        .content(R.string.message_sales_input_required, WBAProperties.sdfDate.format(new Date(lock.getSalesDateLong())))
                        .cancelable(false)
                        .positiveText(R.string.action_input_sales)
                        .positiveColorRes(R.color.colorPrimary)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ws_salesProductHighlight(userId, lock.getSalesDateLong());
                            }
                        });
                break;
            case TEST:
                builder = new MaterialDialog.Builder(this)
                        .title(R.string.title_warning)
                        .content(R.string.message_test_required, WBAProperties.sdfDate.format(new Date(lock.getTestDateLong())))
                        .cancelable(false)
                        .positiveText(R.string.action_mulai_tes)
                        .positiveColorRes(R.color.colorPrimary)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ws_testQuestions(userId, lock.getTestDateLong());
                            }
                        });
                break;
            case QUESTIONER:
                builder = new MaterialDialog.Builder(this)
                        .title(R.string.title_warning)
                        .content(R.string.message_questioner_required, WBAProperties.sdfDate.format(new Date(lock.getQuestionnaireDateLong())))
                        .cancelable(false)
                        .positiveText(R.string.action_isi_questionnnaire)
                        .positiveColorRes(R.color.colorPrimary)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent iQuestioner = new Intent(getApplicationContext(), QuestionerActivity.class);
                                iQuestioner.putExtra(WBAParams.DATE, lock.getQuestionnaireDateLong());
                                startActivityForResult(iQuestioner, LOCK_QUESTIONER);
                            }
                        });
                break;
            case SAFE:
                break;
        }

        if (builder != null) {
            mLockingDialog = builder.build();
            mLockingDialog.show();
        }
    }

    private enum DialogType {
        SALES,
        TEST,
        QUESTIONER,
        SAFE
    }

    /*
     * Web Services
     */

    private void ws_checkLocking(final boolean withDialog) {
        switch (WBAProperties.mode) {
            case DEVELOPMENT:
            case PRODUCTION:
                final Long userId = WBASession.getUserId();
                if (userId == null) {
                    WBASession.loggingOut(this);
                } else {
                    Call<LockingResponse> callLocking = ApiClient.getClient().apiCheckLocking(userId);
                    callLocking.enqueue(new Callback<LockingResponse>() {
                        @Override
                        public void onResponse(Call<LockingResponse> call, Response<LockingResponse> response) {
                            WBALogger.log(WBAProperties.ON_RESPONSE);

                            if (response.isSuccessful()) {
                                LockingResponse rps = response.body();
                                switch (rps.getCode()) {
                                    case WBAProperties.CODE_200:
                                        break;
                                    default:
                                        mLock = rps.getData();
                                        if (withDialog) {
                                            showLockingDialog(userId, mLock);
                                        } else {
                                            directLocking(userId, mLock);
                                        }
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LockingResponse> call, Throwable t) {
                            WBAPopUp.toastMessage(TAG, t.getMessage());
                        }
                    });
                }
                break;
        }
    }

    private void ws_salesProductHighlight(final long userId, final Long dateLong) {
        mLoading.show();
        Call<SalesProductResponse> callSalesProductHighlight = ApiClient.getClient().apiSalesProductHighlight(userId);
        callSalesProductHighlight.enqueue(new Callback<SalesProductResponse>() {
            @Override
            public void onResponse(Call<SalesProductResponse> call, Response<SalesProductResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);
                mLoading.dismiss();

                if (response.isSuccessful()) {
                    SalesProductResponse rps = response.body();
                    switch (rps.getCode()) {
                        case WBAProperties.CODE_200:
                            Intent iSalesInput = new Intent(getApplicationContext(), SalesInputActivity.class);
                            IntentProductHighlight mIntentProductHighlight = new IntentProductHighlight(rps.getData());
                            iSalesInput.putExtra(WBAParams.DATA, mIntentProductHighlight);
                            iSalesInput.putExtra(WBAParams.DATE, dateLong);
                            iSalesInput.putExtra(WBAParams.LOCK, true);
                            startActivityForResult(iSalesInput, LOCK_INPUT_SALES);
                            break;
                        default:
                            WBAPopUp.toastMessage(TAG, rps.getStatus());

                            showLockingDialog(userId, mLock);
                            break;
                    }
                } else {
                    WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                }
            }

            @Override
            public void onFailure(Call<SalesProductResponse> call, Throwable t) {
                WBAPopUp.toastMessage(TAG, t.getMessage());
                mLoading.dismiss();

                showLockingDialog(userId, mLock);
            }
        });
    }

    private void ws_testQuestions(final long userId, final Long dateLong) {
        mLoading.show();
        Call<TestQuestionsResponse> callTestList = ApiClient.getClient().apiTestQuestions(userId);
        callTestList.enqueue(new Callback<TestQuestionsResponse>() {
            @Override
            public void onResponse(Call<TestQuestionsResponse> call, Response<TestQuestionsResponse> response) {
                WBALogger.log(WBAProperties.ON_RESPONSE);
                mLoading.dismiss();

                if (response.isSuccessful()) {
                    TestQuestionsResponse rps = response.body();
                    switch (rps.getCode()) {
                        case WBAProperties.CODE_200:
                            Intent iTestTaking = new Intent(getApplicationContext(), TestTakingActivity.class);
                            IntentQuestions intentQuestions = new IntentQuestions(rps.getData());
                            iTestTaking.putExtra(WBAParams.DATA, intentQuestions);
                            iTestTaking.putExtra(WBAParams.DATE, dateLong);
                            iTestTaking.putExtra(WBAParams.LOCK, true);
                            startActivityForResult(iTestTaking, LOCK_TEST);
                            break;
                        default:
                            WBAPopUp.toastMessage(TAG, rps.getStatus());

                            showLockingDialog(userId, mLock);
                            break;
                    }
                } else {
                    WBAPopUp.toastMessage(TAG, R.string.message_retrofit_error);
                }
            }

            @Override
            public void onFailure(Call<TestQuestionsResponse> call, Throwable t) {
                WBAPopUp.toastMessage(TAG, t.getMessage());
                mLoading.dismiss();

                showLockingDialog(userId, mLock);
            }
        });
    }

    private void ws_profile() {
        Long userId = WBASession.getUserId();
        if (userId == null) {
            WBASession.loggingOut(this);
        } else {
            mLoading.show();
            Call<ProfileResponse> callProfile = ApiClient.getClient().apiProfile(userId);
            callProfile.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    WBALogger.log(WBAProperties.ON_RESPONSE);
                    mLoading.dismiss();

                    if (response.isSuccessful()) {
                        ProfileResponse rps = response.body();
                        switch (rps.getCode()) {
                            case WBAProperties.CODE_200:
                                Intent iProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                                iProfile.putExtra(WBAParams.DATA, rps.getData());
                                startActivity(iProfile);
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
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    WBAPopUp.toastMessage(TAG, t.getMessage());
                    mLoading.dismiss();
                }
            });
        }
    }
}
