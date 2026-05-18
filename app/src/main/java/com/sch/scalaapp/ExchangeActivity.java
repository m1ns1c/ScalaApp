package com.sch.scalaapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ExchangeActivity extends BaseActivity {

    TextView tvCoin;
    ListView listView;

    String[] drinkNames = {
            "아이스 아메리카노", "핫 아메리카노", "카페라떼", "아이스 카페라떼",
            "바닐라라떼", "카라멜 마키아토", "프라푸치노", "녹차 라떼",
            "초코 라떼", "딸기 라떼", "에스프레소", "콜드브루",
            "더치커피", "카푸치노", "플랫화이트", "아이스티",
            "레모네이드", "자몽 허니 블랙티"
    };

    String[] drinkEmojis = {
            "☕", "☕", "🥛", "🥛",
            "🍦", "🍮", "🧋", "🍵",
            "🍫", "🍓", "☕", "🧊",
            "🧊", "☕", "☕", "🍵",
            "🍋", "🍊"
    };

    int[] drinkCosts = {
            10, 10, 15, 15,
            20, 20, 25, 20,
            20, 25, 8, 18,
            18, 15, 20, 12,
            12, 15
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("음료 교환");
        }

        tvCoin = findViewById(R.id.tvCoin);
        listView = findViewById(R.id.listView);

        loadCoin();
        setupList();
    }

    private void setupList() {
        String[] displayItems = new String[drinkNames.length];
        for (int i = 0; i < drinkNames.length; i++) {
            displayItems[i] = drinkEmojis[i] + "  " + drinkNames[i] + "   |   " + drinkCosts[i] + " C";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(Color.parseColor("#0D1B2A"));
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setTextSize(15);
                tv.setPadding(40, 30, 40, 30);
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) ->
                exchange(drinkNames[position], drinkCosts[position])
        );
    }

    private void exchange(String drink, int cost) {
        SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
        int coins = prefs.getInt("coins", 0);

        if (coins < cost) {
            Toast.makeText(this, "코인이 부족해요! (" + coins + " / " + cost + " C)", Toast.LENGTH_SHORT).show();
            return;
        }

        coins -= cost;
        prefs.edit().putInt("coins", coins).apply();
        tvCoin.setText(coins + " C");
        Toast.makeText(this, drink + " 교환 완료! 🎉", Toast.LENGTH_SHORT).show();
    }

    private void loadCoin() {
        SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
        int coins = prefs.getInt("coins", 0);
        tvCoin.setText(coins + " C");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}