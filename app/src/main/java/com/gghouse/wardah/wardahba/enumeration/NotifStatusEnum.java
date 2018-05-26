package com.gghouse.wardah.wardahba.enumeration;

import com.gghouse.wardah.wardahba.util.WBALogger;

/**
 * Created by michael on 6/23/2017.
 */

public enum NotifStatusEnum {
    ACTIVE("active"),
    NONACTIVE("non-active"),
    UNDEFINE("undefine");

    private String value;

    NotifStatusEnum(String value) {
        this.value = value;
    }

    public static NotifStatusEnum getByValue(String value) {
        for (NotifStatusEnum notifStatusEnum: NotifStatusEnum.values()) {
            if (notifStatusEnum.getValue().equals(value)) {
                return notifStatusEnum;
            }
        }
        WBALogger.log("value [" + value + "] is not supported.");
        return NotifStatusEnum.UNDEFINE;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
