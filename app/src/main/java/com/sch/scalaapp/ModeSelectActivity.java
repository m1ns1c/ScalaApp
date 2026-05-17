package com.sch.scalaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ModeSelectActivity extends AppCompatActivity {

    Button btnAiMode, btnMultiMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_select);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("게임 모드 선택");
        }

        btnAiMode = findViewById(R.id.btnAiMode);
        btnMultiMode = findViewById(R.id.btnMultiMode);

        btnAiMode.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameListActivity.class);
            intent.putExtra("mode", "ai");
            startActivity(intent);
        });

        btnMultiMode.setOnClickListener(v -> {
            Intent intent = new Intent(this, RoomActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}