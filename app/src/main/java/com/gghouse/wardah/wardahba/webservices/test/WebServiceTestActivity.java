package com.gghouse.wardah.wardahba.webservices.test;

import android.app.Activity;
import android.os.Bundle;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.util.WBALogger;
import com.gghouse.wardah.wardahba.webservices.ApiClient;
import com.gghouse.wardah.wardahba.webservices.model.Dummy;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service_test);

//        wsRunTest();
    }

    private void wsRunTest() {
        // Michael Halim : Retrofit get only example
        Call<List<Dummy>> callDummyList = ApiClient.getClient().dummyGetList();
        callDummyList.enqueue(new Callback<List<Dummy>>() {
            @Override
            public void onResponse(Call<List<Dummy>> call, Response<List<Dummy>> response) {
                WBALogger.log("onResponse");
                List<Dummy> dummies = response.body();
                for (Dummy dummy : dummies) {
                    WBALogger.log(dummy.getUserId() + ", " + dummy.getId());
                }
            }

            @Override
            public void onFailure(Call<List<Dummy>> call, Throwable t) {
                WBALogger.log("onFailure : " + t.getMessage());
            }
        });
    }
}
