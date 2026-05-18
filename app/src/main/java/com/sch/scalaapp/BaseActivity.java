package com.sch.scalaapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#0D1B2A")));
            if (getSupportActionBar().getThemedContext() != null) {
                android.graphics.drawable.Drawable arrow =
                        ContextCompat.getDrawable(this,
                                androidx.appcompat.R.drawable.abc_ic_ab_back_material);
                if (arrow != null) {
                    arrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(arrow);
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}