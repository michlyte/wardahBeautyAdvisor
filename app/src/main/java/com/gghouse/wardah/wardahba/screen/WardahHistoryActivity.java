package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class WardahHistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button mBReset;
    private Menu mMenu;

    /*
     * Refresh
     */
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    /*
     * Filters
     */
    protected LinearLayout mLlFilter;
    protected TextView mTvFilter;
    protected Drawable mDFilter;
    protected Date mDBegin;
    protected Date mDEnd;

    /*
     * Editable
     */
    protected boolean mIsEdited;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ws(WsMode.REFRESH);
            }
        });

        /*
         * Filter
         */
        mLlFilter = (LinearLayout) findViewById(R.id.ll_filter);
        mTvFilter = (TextView) findViewById(R.id.tv_filter);
        mBReset = (Button) findViewById(R.id.b_reset);
        mBReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBegin = null;
                mDEnd = null;

                mMenu.findItem(R.id.action_filter).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_filter));
                mDFilter = mMenu.findItem(R.id.action_filter).getIcon();
                mDFilter.setColorFilter(ContextCompat.getColor(getBaseContext(), android.R.color.white), PorterDuff.Mode.SRC_ATOP);

                mLlFilter.setVisibility(View.GONE);

                ws(WsMode.REFRESH);
            }
        });

        mIsEdited = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_history, menu);
        mDFilter = menu.findItem(R.id.action_filter).getIcon();
        mDFilter.mutate();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mIsEdited) {
                    setResult(Activity.RESULT_OK);
                }
                finish();
                return true;
            case R.id.action_filter:
                Calendar cFrom = Calendar.getInstance();
                cFrom.setTime((mDBegin == null ? new Date() : mDBegin));
                Calendar cTo = Calendar.getInstance();
                cTo.setTime((mDEnd == null ? new Date() : mDEnd));
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        this,
                        cFrom.get(Calendar.YEAR),
                        cFrom.get(Calendar.MONTH),
                        cFrom.get(Calendar.DAY_OF_MONTH),
                        cTo.get(Calendar.YEAR),
                        cTo.get(Calendar.MONTH),
                        cTo.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsEdited) {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        try {
            String from = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            String to = dayOfMonthEnd + "/" + (monthOfYearEnd + 1) + "/" + yearEnd;
            mDBegin = WBAProperties.sdfFilter.parse(from);
            mDEnd = WBAProperties.sdfFilter.parse(to);

            /*
             * Change filter icon color
             */
            mMenu.findItem(R.id.action_filter).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_filter_fill));
            mDFilter = mMenu.findItem(R.id.action_filter).getIcon();
            mDFilter.mutate();
            mDFilter.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);

            /*
             * Refresh list based on filter
             */

            ws(WsMode.REFRESH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void ws(WsMode wsMode) {

    }
}
