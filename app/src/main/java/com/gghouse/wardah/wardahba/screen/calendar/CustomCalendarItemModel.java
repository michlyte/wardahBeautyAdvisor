package com.gghouse.wardah.wardahba.screen.calendar;

import com.kelin.calendarlistview.library.BaseCalendarItemModel;

public class CustomCalendarItemModel extends BaseCalendarItemModel {
    private int newsCount;
    private boolean isFav;

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
