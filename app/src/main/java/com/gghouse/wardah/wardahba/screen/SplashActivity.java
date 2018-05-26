package com.gghouse.wardah.wardahba.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gghouse.wardah.wardahba.common.WBAProperties;
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
                    intent = new Intent(getBaseContext(), MainActivity.class);
                } else {
                    intent = new Intent(getBaseContext(), WelcomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, WBAProperties.SPLASH_SCREEN_DELAY);
    }
}
