package com.gghouse.wardah.wardahba.util;

import android.content.Intent;

import java.util.Date;

/**
 * Created by ecquaria-macmini on 5/26/17.
 */

public class WBAUtil {
    public static Date getDateFromParam(Intent intent, String param) {
        Object object = intent.getExtras().get(param);
        if (object == null) {
            return new Date();
        } else {
            return new Date((Long) object);
        }
    }

    public static Boolean getBoolFromParam(Intent intent, String param) {
        Object object = intent.getExtras().get(param);
        if (object == null) {
            return false;
        } else {
            return (Boolean) object;
        }
    }

    public static String constructImageUrl(String url) {
        return WBASession.loadIPAddress() + "/" + url;
    }
}
