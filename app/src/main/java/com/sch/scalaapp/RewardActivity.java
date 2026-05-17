package com.sch.scalaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RewardActivity extends AppCompatActivity {

    TextView tvResult, tvCoin;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("결과");
        }

        tvResult = findViewById(R.id.tvResult);
        tvCoin = findViewById(R.id.tvCoin);
        btnHome = findViewById(R.id.btnHome);

        SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
        int coins = prefs.getInt("coins", 0);

        tvResult.setText("게임 종료!");
        tvCoin.setText("현재 보유 코인: " + coins + " C");

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}