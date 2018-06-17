package com.gghouse.wardah.wardahba.screen.main_activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.enumeration.BPTabEnum;
import com.gghouse.wardah.wardahba.screen.main_fragment.TestFragment;
import com.google.firebase.iid.FirebaseInstanceId;

public class BPMainActivity extends WardahMainActivity {

    public static final String TAG = BPMainActivity.class.getSimpleName();

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpmain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (BPTabEnum.getBPTabEnumById(position)) {
                case TEST:
                    return TestFragment.newInstance();
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return BPTabEnum.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(BPTabEnum.getBPTabEnumById(position).getTitleInt());
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
}
