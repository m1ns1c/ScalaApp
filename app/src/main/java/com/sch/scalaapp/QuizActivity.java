package com.sch.scalaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestion, tvProgress;
    Button btnA, btnB, btnC, btnD;

    int currentQuestion = 0;
    int score = 0;

    String[] questions = {
            "순천향대학교가 위치한 도시는?",
            "순천향대학교의 마스코트 이름은?",
            "순천향대학교의 설립 연도는?",
            "순천향대학교의 영문 약자는?",
            "순천향대학교 학교 색상은?"
    };

    String[][] options = {
            {"아산", "천안", "서울", "수원"},
            {"스칼라", "호돌이", "해치", "무궁이"},
            {"1978년", "1980년", "1982년", "1975년"},
            {"SCH", "SCC", "SCU", "SCA"},
            {"파란색", "빨간색", "초록색", "노란색"}
    };

    int[] answers = {0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgress = findViewById(R.id.tvProgress);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        showQuestion();

        btnA.setOnClickListener(v -> checkAnswer(0));
        btnB.setOnClickListener(v -> checkAnswer(1));
        btnC.setOnClickListener(v -> checkAnswer(2));
        btnD.setOnClickListener(v -> checkAnswer(3));
    }

    private void showQuestion() {
        tvProgress.setText((currentQuestion + 1) + " / " + questions.length);
        tvQuestion.setText(questions[currentQuestion]);
        btnA.setText(options[currentQuestion][0]);
        btnB.setText(options[currentQuestion][1]);
        btnC.setText(options[currentQuestion][2]);
        btnD.setText(options[currentQuestion][3]);
    }

    private void checkAnswer(int selected) {
        if (selected == answers[currentQuestion]) {
            score++;
        }
        currentQuestion++;

        if (currentQuestion < questions.length) {
            showQuestion();
        } else {
            Intent intent = new Intent(this, RewardActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", questions.length);
            startActivity(intent);
            finish();
        }
    }
}