package com.gghouse.wardah.wardahba.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import static com.gghouse.wardah.wardahba.common.WBAProperties.LOG_BEGIN;
import static com.gghouse.wardah.wardahba.common.WBAProperties.LOG_END;

/**
 * Created by michaelhalim on 2/19/17.
 */

public class WBAScreen {
    public static float getWidth(Activity activity) {
        WBALogger.log(LOG_BEGIN + "getWidth");
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WBALogger.log(LOG_END + "getWidth " + metrics.widthPixels);
        return metrics.widthPixels;
    }

    public static float getWidth(Context context) {
//        WOILogger.log(LOG_BEGIN + "getWidth");
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        WOILogger.log(LOG_END + "getWidth " + metrics.widthPixels);
        return metrics.widthPixels;
    }

    public static float getHeight(Activity activity) {
        WBALogger.log(LOG_BEGIN + "getHeight");
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WBALogger.log(LOG_END + "getHeight " + metrics.widthPixels);
        return metrics.heightPixels;
    }

    public static float getHeight(Context context) {
        WBALogger.log(LOG_BEGIN + "getHeight");
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WBALogger.log(LOG_END + "getHeight " + metrics.widthPixels);
        return metrics.heightPixels;
    }
}
