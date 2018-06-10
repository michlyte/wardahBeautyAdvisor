package com.gghouse.wardah.wardahba.util;

import android.app.Activity;
import android.util.Log;

import com.gghouse.wardah.wardahba.WardahApp;
import com.gghouse.wardah.wardahba.model.User;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.github.pwittchen.prefser.library.Prefser;

import static com.gghouse.wardah.wardahba.common.WBAProperties.BASE_URL;
import static com.gghouse.wardah.wardahba.common.WBAProperties.LOG_BEGIN;
import static com.gghouse.wardah.wardahba.common.WBAProperties.LOG_END;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_EMAIL;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_FULL_NAME;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_ID;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_IP_ADDRESS;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_IS_LOGGED_IN;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_LAST_QUESTION_ID;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_LOCATION_ID;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_SALES_NEED_REFRESH;
import static com.gghouse.wardah.wardahba.common.WBAUser.SP_USER_TYPE;

/**
 * Created by michaelhalim on 1/25/17.
 */

public abstract class WBASession {
    public static String TAG = WBASession.class.getSimpleName();

    public static boolean isLoggedIn() {
        String methodName = "isLoggedIn";
        WBALogger.log(LOG_BEGIN + methodName);
        boolean isLoggedIn;
        Prefser prefser = WardahApp.getInstance().getPrefser();
        if (prefser.contains(SP_IS_LOGGED_IN) && prefser.get(SP_IS_LOGGED_IN, Boolean.class, false)) {
            isLoggedIn = true;
        } else {
            isLoggedIn = false;
        }
        WBALogger.log(LOG_END + methodName + " " + isLoggedIn);
        return isLoggedIn;
    }

    public static void loggingIn(User user) {
        String methodName = "loggingIn";
        WBALogger.log(LOG_BEGIN + methodName);
        Prefser prefser = WardahApp.getInstance().getPrefser();
        if (prefser.contains(SP_IS_LOGGED_IN) && prefser.get(SP_IS_LOGGED_IN, Boolean.class, false)) {
            WBALogger.log(LOG_END + methodName + " : Something wrong you are logged in already.");
        } else {
            prefser.put(SP_IS_LOGGED_IN, true);

            prefser.put(SP_FULL_NAME, user.getFullName());
            prefser.put(SP_LAST_QUESTION_ID, user.getLastQuestionId());
            prefser.put(SP_EMAIL, user.getEmail());
            prefser.put(SP_ID, user.getId());
            prefser.put(SP_LOCATION_ID, user.getLocation().getId());
            prefser.put(SP_USER_TYPE, user.getUserType());

            WBALogger.log(TAG, SP_FULL_NAME + ": " + user.getFullName());
            WBALogger.log(TAG, SP_LAST_QUESTION_ID + ": " + user.getLastQuestionId());
            WBALogger.log(TAG, SP_EMAIL + ": " + user.getEmail());
            WBALogger.log(TAG, SP_ID + ": " + user.getId());
            WBALogger.log(TAG, SP_LOCATION_ID + ": " + user.getLocation().getId());
            WBALogger.log(TAG, SP_USER_TYPE + ": " + user.getUserType());

            WBALogger.log(LOG_END + methodName + " success.");
        }
    }

    public static void loggingOut(Activity activity) {
        String methodName = "loggingOut";
        WBALogger.log(LOG_BEGIN + methodName);
        Prefser prefser = new Prefser(activity);
        if (prefser.contains(SP_IS_LOGGED_IN)) {
            if (prefser.get(SP_IS_LOGGED_IN, Boolean.class, false)) {
                prefser.put(SP_IS_LOGGED_IN, false);

                WBALogger.log(LOG_END + methodName + " success.");
            } else {
                WBALogger.log(LOG_END + methodName + " : Something wrong you are logged out already.");
            }
        } else {
            WBALogger.log(methodName + " : Something wrong you do not have log in session.");
        }
    }

    public static Long getUserId() {
        Prefser prefser = WardahApp.getInstance().getPrefser();

        if (prefser.contains(SP_ID)) {
            return prefser.get(SP_ID, Long.class, 0L);
        } else {
            WBALogger.log("Error: could not find user id.");
            return null;
        }
    }

    public static Long getUserLocationId() {
        Prefser prefser = WardahApp.getInstance().getPrefser();

        if (prefser.contains(SP_LOCATION_ID)) {
            return prefser.get(SP_LOCATION_ID, Long.class, 0L);
        } else {
            WBALogger.log("Error: could not find user location id.");
            return null;
        }
    }

    public static String getUserType() {
        Prefser prefser = WardahApp.getInstance().getPrefser();

        if (prefser.contains(SP_USER_TYPE)) {
            return prefser.get(SP_USER_TYPE, String.class, "");
        } else {
            WBALogger.error("Could not find user type");
            return null;
        }
    }

    public static void saveIPAddress(String ipAddress) {
        Prefser prefser = WardahApp.getInstance().getPrefser();
        prefser.put(SP_IP_ADDRESS, ipAddress);
        ApiClient.generateClientWithNewIP(ipAddress);
    }

    public static String loadIPAddress() {
        Prefser prefser = WardahApp.getInstance().getPrefser();
        return prefser.get(SP_IP_ADDRESS, String.class, BASE_URL);
    }

    public static void setSalesNeedRefresh(boolean value) {
        Prefser prefser = WardahApp.getInstance().getPrefser();
        prefser.put(SP_SALES_NEED_REFRESH, value);
    }

    public static boolean doesSalesNeedRefresh() {
        Prefser prefser = WardahApp.getInstance().getPrefser();
        return prefser.get(SP_SALES_NEED_REFRESH, Boolean.class, false);
    }
}
