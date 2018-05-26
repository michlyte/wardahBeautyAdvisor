package com.gghouse.wardah.wardahba;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.github.pwittchen.prefser.library.Prefser;
import com.google.gson.Gson;

/**
 * Created by michaelhalim on 5/6/17.
 */

public class WardahApp extends Application {
    private static WardahApp instance;

    private static Context context;
    private static Prefser prefser;
    private static NotificationManager mNotificationManager;
    private static Gson gson;

    public WardahApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WardahApp.context = getApplicationContext();
        prefser = new Prefser(getApplicationContext());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        gson = new Gson();
    }

    public static WardahApp getInstance() {
        return instance;
    }

    public Context getAppContext() {
        return WardahApp.context;
    }

    public Prefser getPrefser() {
        return prefser;
    }

    public NotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    public Gson getGson() {
        return gson;
    }
}
