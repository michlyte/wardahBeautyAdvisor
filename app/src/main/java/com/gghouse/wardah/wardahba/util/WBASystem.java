package com.gghouse.wardah.wardahba.util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by michael on 3/10/2017.
 */

public abstract class WBASystem {
    public static String getImei(Context context) {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    public static String withSuffix(double count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    public static String withSuffix(double count, int decimal) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%." + decimal + "f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }
}
