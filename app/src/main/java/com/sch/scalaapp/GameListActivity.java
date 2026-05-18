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

public class GameListActivity extends BaseActivity {

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

    String[] gameEmojis = {
            "3️⃣", "🔫", "💬", "🍓", "🐋", "🍞", "🚇", "🍓", "🌿", "💀", "🫘"
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, games) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(Color.parseColor("#0D1B2A"));

                TextView text1 = view.findViewById(android.R.id.text1);
                text1.setText(gameEmojis[position] + "  " + games[position]);
                text1.setTextColor(Color.parseColor("#FFFFFF"));
                text1.setTextSize(16);
                text1.setPadding(40, 20, 40, 4);

                TextView text2 = view.findViewById(android.R.id.text2);
                text2.setText("탭하여 규칙 보기");
                text2.setTextColor(Color.parseColor("#7B9EC7"));
                text2.setTextSize(11);
                text2.setPadding(40, 0, 40, 20);

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