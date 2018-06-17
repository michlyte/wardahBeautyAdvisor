package com.gghouse.wardah.wardahba.enumeration;

import com.gghouse.wardah.wardahba.R;

public enum BPLTabEnum {
    INFO(0, "INFO", R.string.prompt_info),
    EVENT(1, "EVENT", R.string.prompt_event);

    private Integer id;
    private String title;
    private Integer titleInt;

    BPLTabEnum(Integer id, String title, Integer titleInt) {
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

    public static BPLTabEnum getBPLTabEnumById(int id) {
        for (BPLTabEnum bplTabEnum : BPLTabEnum.values()) {
            if (bplTabEnum.getId() == id) {
                return bplTabEnum;
            }
        }
        return null;
    }
}
