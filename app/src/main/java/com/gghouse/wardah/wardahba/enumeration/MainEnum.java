package com.gghouse.wardah.wardahba.enumeration;

import android.support.v4.content.ContextCompat;

import com.gghouse.wardah.wardahba.R;

/**
 * Created by michaelhalim on 1/31/17.
 */

public enum MainEnum {
    NOTIF(0, "NOTIF", R.string.prompt_notif),
    SALES(1, "PENJUALAN", R.string.prompt_sales),
    TEST(2, "TES", R.string.prompt_tes);

    private Integer id;
    private String title;
    private Integer titleInt;

    MainEnum(Integer id, String title, Integer titleInt) {
        this.id = id;
        this.title = title;
        this.titleInt = titleInt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleInt() {
        return titleInt;
    }

    public void setTitleInt(Integer titleInt) {
        this.titleInt = titleInt;
    }

    public static MainEnum getMainEnumById(int id) {
        for (MainEnum mainEnum : MainEnum.values()) {
            if (mainEnum.getId() == id) {
                return mainEnum;
            }
        }
        return null;
    }
}
