package com.gghouse.wardah.wardahba.screen.main_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WardahTabInterface;
import com.gghouse.wardah.wardahba.screen.main_fragment.interfaces.WsMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.co.recruit_mp.android.lightcalendarview.LightCalendarView;
import jp.co.recruit_mp.android.lightcalendarview.MonthView;
import jp.co.recruit_mp.android.lightcalendarview.accent.Accent;
import jp.co.recruit_mp.android.lightcalendarview.accent.DotAccent;

public class EventLightCalendarFragment extends Fragment implements WardahTabInterface {

    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    public EventLightCalendarFragment() {
    }

    public static EventLightCalendarFragment newInstance() {
        EventLightCalendarFragment fragment = new EventLightCalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ws(WsMode.REFRESH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_fragment_event_light_calendar, container, false);
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        calFrom.set(Calendar.MONTH, 0);
        calTo.set(Calendar.MONTH, 11);

        LightCalendarView calendarView = (LightCalendarView) view.findViewById(R.id.calendarView);
        calendarView.setMonthFrom(calFrom.getTime());
        calendarView.setMonthTo(calTo.getTime());
        calendarView.setMonthCurrent(calNow.getTime());
        calendarView.setOnStateUpdatedListener(new LightCalendarView.OnStateUpdatedListener() {
            @Override
            public void onMonthSelected(Date date, MonthView monthView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Calendar cal = Calendar.getInstance();
                        List<Date> dates = new ArrayList<Date>();
                        for (int i = 0; i < 31; i++) {
                            if (i % 2 == 0) {
                                cal.set(monthView.getMonth().getYear() + 1900, monthView.getMonth().getMonth(), i);
                                dates.add(cal.getTime());
                            }
                        }
                        HashMap<Date, List<Accent>> map = new HashMap<>();
                        for (Date date : dates) {
                            List<Accent> accents = new ArrayList<>();
                            for (int i = 0; i <= (date.getDate() % 3); i++) {
                                accents.add(new DotAccent(10f, null, formatter.format(date) + "-" + i));
                            }
                            map.put(date, accents);
                        }
                        monthView.setAccents(map);
                    }
                }, 1000);

                Log.i("JavaMainActivity", "onMonthSelected: date = " + date);
            }

            @Override
            public void onDateSelected(Date date) {
                Log.i("JavaMainActivity", "onDateSelected: date = " + date);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ws(WsMode.REFRESH);
    }

    @Override
    public void ws(WsMode wsMode) {

    }
}
