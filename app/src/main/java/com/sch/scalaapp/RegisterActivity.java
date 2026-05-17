package com.sch.scalaapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etRegId, etRegPw, etRegPwCheck;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("회원가입");
        }

        etRegId = findViewById(R.id.etRegId);
        etRegPw = findViewById(R.id.etRegPw);
        etRegPwCheck = findViewById(R.id.etRegPwCheck);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String id = etRegId.getText().toString().trim();
            String pw = etRegPw.getText().toString().trim();
            String pwCheck = etRegPwCheck.getText().toString().trim();

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pw.equals(pwCheck)) {
                Toast.makeText(this, "비밀번호가 일치하지 않아요", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("userId", id)
                    .putString("userPw", pw)
                    .putInt("coins", 0)
                    .apply();

            Toast.makeText(this, "가입 완료! 로그인해주세요", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}