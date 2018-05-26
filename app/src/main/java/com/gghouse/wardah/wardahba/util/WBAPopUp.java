package com.gghouse.wardah.wardahba.util;

import android.util.Log;
import android.widget.Toast;

import com.gghouse.wardah.wardahba.WardahApp;

/**
 * Created by ecquaria-macmini on 5/26/17.
 */

public abstract class WBAPopUp {
    public static void toastMessage(String text) {
        Toast.makeText(WardahApp.getInstance().getAppContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(String tag, String text) {
        Log.d(tag, text);
        Toast.makeText(WardahApp.getInstance().getAppContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(Integer resId) {
        Toast.makeText(WardahApp.getInstance().getAppContext(),
                WardahApp.getInstance().getAppContext().getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(String tag, Integer resId) {
        Log.d(tag, WardahApp.getInstance().getAppContext().getString(resId));
        Toast.makeText(WardahApp.getInstance().getAppContext(),
                WardahApp.getInstance().getAppContext().getString(resId), Toast.LENGTH_SHORT).show();
    }
}
