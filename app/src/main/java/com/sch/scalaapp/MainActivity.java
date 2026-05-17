package com.sch.scalaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvCoin, tvWelcome;
    Button btnQuiz, btnExchange, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCoin = findViewById(R.id.tvCoin);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnExchange = findViewById(R.id.btnExchange);
        btnLogout = findViewById(R.id.btnLogout);

        loadCoin();

        btnQuiz.setOnClickListener(v ->
                startActivity(new Intent(this, ModeSelectActivity.class))
        );

        btnExchange.setOnClickListener(v ->
                startActivity(new Intent(this, ExchangeActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCoin();
    }

    private void loadCoin() {
        SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
        int coins = prefs.getInt("coins", 0);
        String userId = prefs.getString("userId", "");
        tvWelcome.setText(userId + "님, 안녕하세요!");
        tvCoin.setText("보유 코인: " + coins + " C");
    }
}