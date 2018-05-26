package com.gghouse.wardah.wardahba.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Profile;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout mLLEmail;
    private LinearLayout mLLMobileNumber;

    private TextView mTVNip;
    private TextView mTVFullName;
    private TextView mTVEmail;
    private TextView mTVMobileNumber;
    private TextView mTVFirstDateWork;
    private TextView mTVPosition;
    private TextView mTVAddress;
    private TextView mTVCity;
    private TextView mTVProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLLEmail = (LinearLayout) findViewById(R.id.ll_email);
        mLLMobileNumber = (LinearLayout) findViewById(R.id.ll_mobile_number);

        mTVNip = (TextView) findViewById(R.id.tv_nip);
        mTVFullName = (TextView) findViewById(R.id.tv_full_name);
        mTVEmail = (TextView) findViewById(R.id.tv_email);
        mTVMobileNumber = (TextView) findViewById(R.id.tv_mobile_number);
        mTVFirstDateWork = (TextView) findViewById(R.id.tv_first_date_work);
        mTVPosition = (TextView) findViewById(R.id.tv_position);
        mTVAddress = (TextView) findViewById(R.id.tv_address);
        mTVCity = (TextView) findViewById(R.id.tv_city);
        mTVProvince = (TextView) findViewById(R.id.tv_provinve);

        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra(WBAParams.DATA) != null) {
            Profile profile = (Profile) intent.getSerializableExtra(WBAParams.DATA);
            mTVNip.setText(profile.getNip().toString());
            mTVFullName.setText(profile.getFullName());
            mTVEmail.setText(profile.getEmail());
            mTVMobileNumber.setText(profile.getMobileNumber());
            mTVFirstDateWork.setText(WBAProperties.sdfDate.format(profile.getFirstDateWork()));
            mTVPosition.setText(profile.getPosition());

            mTVAddress.setText(profile.getLocation().getName());
            mTVCity.setText(profile.getLocation().getCity());
            mTVProvince.setText(profile.getLocation().getProvince());
        }
    }
}
