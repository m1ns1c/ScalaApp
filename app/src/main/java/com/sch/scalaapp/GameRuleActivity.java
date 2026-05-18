package com.sch.scalaapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameRuleActivity extends BaseActivity {

    TextView tvRule, tvBubble;
    ImageView imgScala;
    Button btnStart;
    String gameName, mode, hostIp;

    String[] bubbleTexts = {
            "이 게임 진짜 재밌어! 😆",
            "규칙 잘 읽어봐~ 😊",
            "틀리면 벌칙이야! 🙈",
            "화이팅! 할 수 있어! 💪",
            "나랑 같이 해봐요~ 🎉"
    };

    int bubbleIndex = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rule);

        gameName = getIntent().getStringExtra("gameName");
        mode = getIntent().getStringExtra("mode");
        hostIp = getIntent().getStringExtra("hostIp");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(gameName);
        }

        tvRule = findViewById(R.id.tvRule);
        tvBubble = findViewById(R.id.tvBubble);
        imgScala = findViewById(R.id.imgScala);
        btnStart = findViewById(R.id.btnStart);

        tvRule.setText(getRule(gameName));

        startBounceAnimation();
        startBubbleRotation();

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(this, GamePlayActivity.class);
            intent.putExtra("gameName", gameName);
            intent.putExtra("mode", mode);
            intent.putExtra("hostIp", hostIp);
            startActivity(intent);
        });
    }

    private void startBounceAnimation() {
        Animation bounce = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                float offset = (float) (-20 * Math.sin(Math.PI * interpolatedTime));
                imgScala.setTranslationY(offset);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        bounce.setDuration(800);
        bounce.setRepeatCount(Animation.INFINITE);
        bounce.setRepeatMode(Animation.RESTART);
        imgScala.startAnimation(bounce);
    }

    private void startBubbleRotation() {
        tvBubble.setText(bubbleTexts[bubbleIndex]);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bubbleIndex = (bubbleIndex + 1) % bubbleTexts.length;
                tvBubble.setText(bubbleTexts[bubbleIndex]);
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String getRule(String name) {
        switch (name) {
            case "369 게임":
                return "🎮 369 게임\n\n1부터 순서대로 숫자를 외치되, 3, 6, 9가 포함된 숫자에서는 숫자 대신 박수(짝!)를 칩니다.\n\n예) 1, 2, 짝, 4, 5, 짝, 7, 8, 짝, 10, 11, 짝, 짝, 14...\n\n• 3이 두 개면 박수 두 번!\n• 박자 틀리면 벌칙!";
            case "007 빵":
                return "🔫 007 빵\n\n참가자들이 원으로 앉아 진행합니다.\n\n1. '007 빵!'이라고 말하며 옆 사람 어깨를 치면서 시작\n2. '오' - 한쪽 어깨만 치며 다음 사람에게 넘김\n3. '오' - 반대쪽 어깨를 치며 넘김\n4. '칠' - 양쪽 어깨를 동시에 치며 방향 선택\n5. '빵' - 칠을 받은 옆 사람이 외침\n6. 빵 이후 다시 '오'부터 시작\n\n• 규칙 어기거나 차례 놓치면 탈락!\n• 마지막 남은 사람이 승자!";
            case "끝말잇기":
                return "💬 끝말잇기\n\n앞 사람이 말한 단어의 마지막 글자로 시작하는 단어를 말합니다.\n\n예) 사과 → 과자 → 자동차 → 차표...\n\n• 이미 나온 단어 사용 불가\n• 단어가 생각 안 나면 벌칙!";
            case "딸기당근수박참외메론":
                return "🍓 딸기당근수박참외메론\n\n인트로: 딸기당근수박참외메론게임~ 딸기당근수박참외메론게임~\n\n4박자에 맞춰 과일 이름을 하나씩 추가해가며 외칩니다.\n\n(1박)(2박)(3박)딸기\n(1박)(2박)딸기 당근\n(1박)딸기 당근 수박\n딸기 당근 수박 참외\n딸기 당근 수박 참외 (1박)(2박)(3박)메론\n딸기 당근 수박 참외 (1박)(2박)메론 딸기...\n\n• 과일 이름을 정확히 외우는 게 핵심!\n• 틀리면 벌칙!";
            case "고래고래":
                return "🐋 고래고래\n\n4박자에 맞춰 박수(짝)와 고래를 외치는 게임!\n\n짝/짝/짝/고래\n짝/짝/고래/고래\n짝/고래/고래/고래\n고래/고래/고래/고래\n고래고래/고래/고래/고래\n고래고래/고래고래/고래/고래\n\n• 박자에 맞게 진행\n• 혀가 꼬이거나 틀리면 벌칙!";
            case "가자빵집으로":
                return "🍞 가자빵집으로\n\n(먼 곳을 가리키며) 어? 가자! 빵집으로~\n\n• '호' 외치며 지목 → 지목받은 사람 혼자 두 손 들며 '빵!'\n• '찐' 외치며 지목 → 지목받은 사람 제외 양옆 사람이 두 손 들며 '빵빵!'\n• '대' 외치며 지목 → 지목받은 사람 제외 모두가 두 손 들며 '빵!'\n\n• 반응 틀리거나 늦으면 벌칙!";
            case "지하철 1호선":
                return "🚇 지하철 게임\n\n특정 호선을 정하고, 그 노선의 역 이름을 차례대로 말합니다.\n\n진행 방법:\n1. 발화자가 노선을 정함 (예: 2호선!)\n2. 역 이름 하나를 외치며 시작\n3. 시계/반시계 방향으로 돌아가며 다음 역 이름 말하기\n\n벌칙 조건:\n• 5초 안에 대답 못할 경우\n• 이미 나온 역을 중복으로 말할 경우\n• 해당 노선에 없는 역을 말할 경우";
            case "딸기가 좋아":
                return "🍓 딸기가 좋아\n\n인트로: 딸기가 좋아! 딸기가 좋아! 딸기! 딸기! 딸기! 딸기!\n\n8박자 동안 본인 순서에 맞춰 딸기를 외칩니다.\n\n• 1번째 사람: 1번째 박자에 '딸기' 1번\n• 2번째 사람: 2번째 박자에 '딸기' 2번\n• ...\n• 8번째 사람: 8번째 박자에 '딸기' 8번\n\n벌칙 조건:\n• 박자를 놓치거나\n• 딸기 횟수를 틀리거나\n• 멍 때리는 경우";
            case "홍삼 게임":
                return "🌿 홍삼 게임\n\n시작 구호: 아싸 홍삼~ 에브리바디 홍삼~!\n\n진행 방법:\n1. 첫 술래가 '아싸 너, 너!'를 외치며 2명 지목\n2. 지목받은 2명이 각각 '아싸 너!'로 다른 사람 지목\n3. 두 사람이 동시에 한 사람을 지목하는 순간!\n4. 지목당한 사람이 '아싸 홍삼!'을 외치며 재미있는 동작\n5. 나머지 모두 '에브리바디 홍삼!'을 외치며 동작 따라하기\n\n벌칙 조건:\n• 동작 타이밍 놓친 사람\n• 구호가 꼬인 사람";
            case "더 게임 오브 데스":
                return "💀 더 게임 오브 데스\n\n구호: 신난다! 재미난다! 더 게임 오브 데쓰!\n\n진행 방법:\n1. 모두 주먹을 쥔 채 구호를 외침\n2. 구호 끝나는 동시에 각자 한 명을 검지로 지목\n3. 첫 술래가 5~30 사이 숫자를 외침\n4. 술래가 지목한 사람 머리에 손가락 얹으며 '1' 카운트\n5. 지목받은 사람이 자기가 지목한 사람 머리에 얹으며 '2' 카운트\n6. 술래가 외친 숫자에 도달한 사람이 벌칙!\n\n• 자기 자신을 지목해도 됨";
            case "두부 게임":
                return "🫘 두부 게임\n\n자리마다 두부 모 수가 부여됩니다 (1모, 2모, 3모...)\n\n진행 방법:\n1. 시작자가 '두부 3모!'를 외치며 3모인 사람 지목\n2. 지목받은 사람은 자기 모 수만큼 '두부 두부 두부...' 외침\n3. 이후 '두부 X모!'를 외치며 다음 사람 지목\n\n벌칙 조건:\n• 모 수만큼 두부를 외치지 못한 경우\n• 자기 모 수가 아닌데 반응한 경우\n• 박자를 놓친 경우";
            default:
                return "규칙을 불러오는 중...";
        }
    }
}