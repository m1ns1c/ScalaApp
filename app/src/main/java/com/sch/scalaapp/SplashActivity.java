package com.sch.scalaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}