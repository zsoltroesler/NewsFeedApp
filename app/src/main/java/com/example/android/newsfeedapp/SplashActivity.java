package com.example.android.newsfeedapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;

/**
 * Created by Zsolt on 2017. 10. 18..
 */

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // MainActivity will start after timer is over
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                // Close splash activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
