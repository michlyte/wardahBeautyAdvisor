package com.gghouse.wardah.wardahba.enumeration;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.util.WBALogger;

import static android.R.attr.id;

/**
 * Created by michaelhalim on 2/19/17.
 */

public enum NotifEnum {
    PRODUCT_LAUNCH("PRODUCT_LAUNCH", R.drawable.ic_new_product_launch, "PRODUCT LAUNCH"),
    QUOTE("QUOTES", R.drawable.ic_quote, "QUOTES"),
    PROMOTION("PROMO", R.drawable.ic_promotion, "PROMO");

    private String type;
    private Integer iconResId;
    private String name;

    NotifEnum(String type, Integer iconResId, String name) {
        this.type = type;
        this.iconResId = iconResId;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIconResId() {
        return iconResId;
    }

    public void setIconResId(Integer iconResId) {
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static NotifEnum getNotifEnumById(String type) {
        for (NotifEnum notifEnum : NotifEnum.values()) {
            if (notifEnum.getType().equals(type)) {
                return notifEnum;
            }
        }
        WBALogger.log("id [" + id + "] is not supported.");
        return null;
    }
}
