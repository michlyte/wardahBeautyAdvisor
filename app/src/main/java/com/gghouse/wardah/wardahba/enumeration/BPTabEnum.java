package com.gghouse.wardah.wardahba.enumeration;

import com.gghouse.wardah.wardahba.R;

public enum BPTabEnum {
    INFO(0, "INFO", R.string.prompt_info),
    EVENT(1, "EVENT", R.string.prompt_event),
    TEST(2, "TES", R.string.prompt_tes);

    private Integer id;
    private String title;
    private Integer titleInt;

    BPTabEnum(Integer id, String title, Integer titleInt) {
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

    public static BPTabEnum getBPTabEnumById(int id) {
        for (BPTabEnum bpTabEnum : BPTabEnum.values()) {
            if (bpTabEnum.getId() == id) {
                return bpTabEnum;
            }
        }
        return null;
    }
}
