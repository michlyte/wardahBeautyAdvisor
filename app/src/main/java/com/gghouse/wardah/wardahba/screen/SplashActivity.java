package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.enumeration.UserTypeEnum;
import com.gghouse.wardah.wardahba.screen.main_activity.BPLMainActivity;
import com.gghouse.wardah.wardahba.screen.main_activity.BPMainActivity;
import com.gghouse.wardah.wardahba.screen.main_activity.MainActivity;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.util.WBASession;
import com.gghouse.wardah.wardahba.webservices.ApiClient;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (WBAProperties.mode) {
            case DUMMY_DEVELOPMENT:
            case DEVELOPMENT:
                ApiClient.generateClientWithNewIP(WBASession.loadIPAddress());
                break;
            case PRODUCTION:
                ApiClient.generateClientWithNewIP(WBAProperties.PROD_URL);
                break;
        }

        // Delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (WBASession.isLoggedIn()) {
                    try {
                        UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(WBASession.getUserType());
                        switch (userTypeEnum) {
                            case BEAUTY_PROMOTER:
                                intent = new Intent(getBaseContext(), BPMainActivity.class);
                                break;
                            case BEAUTY_PROMOTER_LEADER:
                                intent = new Intent(getBaseContext(), BPLMainActivity.class);
                                break;
                            default:
                                intent = new Intent(getBaseContext(), MainActivity.class);
                                break;
                        }
                        startActivity(intent);
                    } catch (IllegalArgumentException iae) {
                        WBALogger.error(this.getClass().getSimpleName(), iae.getMessage());
                    }
                } else {
                    intent = new Intent(getBaseContext(), WelcomeActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, WBAProperties.SPLASH_SCREEN_DELAY);
    }
}
