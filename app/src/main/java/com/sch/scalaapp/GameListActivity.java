package com.sch.scalaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameListActivity extends AppCompatActivity {

    ListView listView;
    String mode, hostIp;

    String[] games = {
            "369 게임",
            "007 빵",
            "끝말잇기",
            "딸기당근수박참외메론",
            "고래고래",
            "가자빵집으로",
            "지하철 1호선",
            "딸기가 좋아",
            "홍삼 게임",
            "더 게임 오브 데스",
            "두부 게임"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("게임 선택");
        }

        mode = getIntent().getStringExtra("mode");
        hostIp = getIntent().getStringExtra("hostIp");

        listView = findViewById(R.id.listView);
        listView.setPadding(0, 16, 0, 0);
        listView.setClipToPadding(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#1B3A6B"));
                tv.setTextSize(18);
                tv.setPadding(40, 30, 40, 30);
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, GameRuleActivity.class);
            intent.putExtra("gameName", games[position]);
            intent.putExtra("mode", mode);
            intent.putExtra("hostIp", hostIp);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}