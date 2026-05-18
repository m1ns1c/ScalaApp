package com.sch.scalaapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

public class GamePlayActivity extends BaseActivity {

    LinearLayout chatContainer;
    EditText etInput;
    Button btnSend;
    ScrollView scrollView;

    String gameName, mode, hostIp;
    String API_KEY = "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";

    List<JSONObject> chatHistory = new ArrayList<>();

    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter multiOut;
    BufferedReader multiIn;

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        gameName = getIntent().getStringExtra("gameName");
        mode = getIntent().getStringExtra("mode");
        hostIp = getIntent().getStringExtra("hostIp");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(gameName);
        }

        chatContainer = findViewById(R.id.chatContainer);
        etInput = findViewById(R.id.etInput);
        btnSend = findViewById(R.id.btnSend);
        scrollView = findViewById(R.id.scrollView);

        if (mode.equals("ai")) {
            startAiMode();
        } else if (mode.equals("host")) {
            startHostMode();
        } else if (mode.equals("client")) {
            startClientMode();
        }

        btnSend.setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (input.isEmpty()) return;
            etInput.setText("");
            addBubble("나", input, true);

            if (mode.equals("ai")) {
                sendToAi(input);
            } else {
                sendToMulti(input);
            }
        });
    }

    private void addBubble(String sender, String message, boolean isMe) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(isMe ? Gravity.END : Gravity.START);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 0, 0, 16);
        row.setLayoutParams(rowParams);

        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(24, 16, 24, 16);
        bubble.setBackgroundColor(Color.parseColor(isMe ? "#1B3A6B" : "#1A2E45"));

        int maxWidth = (int)(getResources().getDisplayMetrics().widthPixels * 0.75);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMargins(isMe ? 80 : 0, 0, isMe ? 0 : 80, 0);
        bubble.setLayoutParams(bubbleParams);

        TextView tvSender = new TextView(this);
        tvSender.setText(sender);
        tvSender.setTextColor(Color.parseColor(isMe ? "#4A90D9" : "#7B9EC7"));
        tvSender.setTextSize(11);
        tvSender.setPadding(0, 0, 0, 4);
        bubble.addView(tvSender);

        TextView tvMsg = new TextView(this);
        tvMsg.setText(message);
        tvMsg.setTextColor(Color.WHITE);
        tvMsg.setTextSize(14);
        tvMsg.setLineSpacing(4, 1);
        bubble.addView(tvMsg);

        row.addView(bubble);
        chatContainer.addView(row);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void startAiMode() {
        addBubble("스칼라 AI", gameName + " 게임을 시작할게요! 제가 진행할게요 😊\n\n먼저 시작해볼까요?", false);
    }

    private void sendToAi(String userInput) {
        new Thread(() -> {
            HttpsURLConnection conn = null;
            try {
                JSONObject userMsg = new JSONObject();
                userMsg.put("role", "user");
                userMsg.put("content", userInput);
                chatHistory.add(userMsg);

                URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
                conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setDoOutput(true);

                JSONArray messages = new JSONArray();
                JSONObject systemMsg = new JSONObject();
                systemMsg.put("role", "system");
                systemMsg.put("content", getSystemPrompt(gameName));
                messages.put(systemMsg);

                for (JSONObject msg : chatHistory) {
                    messages.put(msg);
                }

                JSONObject body = new JSONObject();
                body.put("model", "llama-3.3-70b-versatile");
                body.put("messages", messages);
                body.put("max_tokens", 500);

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes());
                os.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                JSONObject json = new JSONObject(response.toString());
                String aiReply = json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                JSONObject assistantMsg = new JSONObject();
                assistantMsg.put("role", "assistant");
                assistantMsg.put("content", aiReply);
                chatHistory.add(assistantMsg);

                handler.post(() -> {
                    addBubble("스칼라 AI", aiReply, false);
                    if (aiReply.contains("틀렸") || aiReply.contains("벌칙")) {
                        updateCoins(-1);
                    } else if (aiReply.contains("정답") || aiReply.contains("맞았")) {
                        updateCoins(2);
                    }
                });

            } catch (Exception e) {
                HttpsURLConnection finalConn = conn;
                try {
                    int responseCode = finalConn.getResponseCode();
                    BufferedReader errBr = new BufferedReader(
                            new InputStreamReader(finalConn.getErrorStream()));
                    StringBuilder errResponse = new StringBuilder();
                    String errLine;
                    while ((errLine = errBr.readLine()) != null) {
                        errResponse.append(errLine);
                    }
                    String finalErr = errResponse.toString();
                    handler.post(() -> addBubble("오류", responseCode + ": " + finalErr, false));
                } catch (Exception e2) {
                    handler.post(() -> addBubble("오류", e.toString(), false));
                }
            }
        }).start();
    }

    private String getSystemPrompt(String game) {
        String rule = "";
        switch (game) {
            case "369 게임":
                rule = "1부터 순서대로 숫자를 세되, 3, 6, 9가 포함된 숫자에서는 숫자 대신 '짝!'이라고 말해야 해. 네가 먼저 숫자를 말하고 사용자가 다음 숫자를 말하는 식으로 번갈아가며 진행해. 사용자가 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "007 빵":
                rule = "오(O), 오(O), 칠(7), 빵! 순서로 진행하는 게임이야. 네가 진행자 역할을 하고, 사용자가 참여자야. 사용자가 순서에 맞는 단어를 말해야 해. 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "끝말잇기":
                rule = "앞 사람이 말한 단어의 마지막 글자로 시작하는 단어를 말하는 게임이야. 네가 먼저 단어를 말하고 사용자가 이어받는 식으로 번갈아가며 진행해. 이미 나온 단어나 규칙에 맞지 않는 단어를 말하면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "딸기당근수박참외메론":
                rule = "4박자에 맞춰 과일 이름을 순서대로 외치는 게임이야. 딸기→당근→수박→참외→메론 순서야. 네가 진행자로서 박자를 세어주고 사용자가 따라오게 해. 순서나 박자가 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "고래고래":
                rule = "4박자에 맞춰 짝(박수)과 고래를 외치는 게임이야. 짝/짝/짝/고래 → 짝/짝/고래/고래 → 짝/고래/고래/고래 → 고래/고래/고래/고래 순서로 진행해. 네가 박자를 알려주고 사용자가 따라오게 해. 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "가자빵집으로":
                rule = "'호', '찐', '대' 중 하나를 외치며 지목하는 게임이야. 호→지목받은 사람 혼자 빵!, 찐→양옆 사람이 빵빵!, 대→지목받은 사람 제외 모두 빵!. 네가 진행자로서 호/찐/대를 외치면 사용자가 맞게 반응해야 해. 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "지하철 1호선":
                rule = "1호선 역 이름을 순서대로 말하는 게임이야. 소요산-동두천-보산-동두천중앙-지행-덕정-덕계-양주-녹양-가능-의정부-회룡-망월사-도봉산-도봉-방학-창동-녹천-월계-광운대-석계-신이문-외대앞-회기-청량리-제기동-신설동-동묘앞-동대문-종로5가-종로3가-종각-시청-서울역 순서야. 네가 역 이름을 하나 말하면 사용자가 다음 역을 말하는 식으로 번갈아가며 진행해. 5초 안에 못 말하거나 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "딸기가 좋아":
                rule = "8박자 동안 순서에 맞춰 딸기를 외치는 게임이야. 1번째 박자에 딸기 1번, 2번째 박자에 딸기 2번... 이런 식이야. 네가 박자를 세어주고 사용자가 맞는 횟수만큼 딸기를 외쳐야 해. 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "홍삼 게임":
                rule = "아싸 홍삼~ 에브리바디 홍삼~! 구호로 시작하는 지목 게임이야. 네가 진행자로서 사용자를 지목하고, 두 사람이 동시에 한 사람을 지목하면 그 사람이 동작을 취해야 해. 규칙을 어기면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "더 게임 오브 데스":
                rule = "신난다! 재미난다! 더 게임 오브 데쓰! 구호로 시작해. 모두가 한 명씩 지목하고, 술래가 5~30 사이 숫자를 외치면 손가락이 지목 경로를 따라 이동해서 그 숫자에 해당하는 사람이 벌칙을 받아. 네가 진행자로서 게임을 이끌어줘. 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            case "두부 게임":
                rule = "각자 두부 모 수가 있어 (1모, 2모, 3모...). '두부 X모!'를 외치며 지목하면, 지목받은 사람이 자기 모 수만큼 '두부'를 외친 후 다음 사람을 지목해. 네가 진행자로서 게임을 이끌어줘. 틀리면 '틀렸어요! 벌칙!'이라고 말해.";
                break;
            default:
                rule = "게임 규칙에 따라 진행해줘.";
        }
        return "너는 순천향대학교 마스코트 '스칼라'야. 밝고 재미있는 성격이야. " +
                "지금 '" + game + "' 게임을 진행하고 있어. " +
                "게임 규칙: " + rule + " " +
                "한국어로만 대화하고, 이모지를 적절히 사용해줘. " +
                "게임을 실제로 진행하면서 사용자와 대화해줘. " +
                "절대 뜬금없는 말 하지 말고 게임에 집중해줘.";
    }

    private void startHostMode() {
        appendChat("시스템", "방을 만들고 있어요...");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(9999);
                handler.post(() -> appendChat("시스템", "방 준비 완료! 친구가 입장하길 기다리는 중..."));
                clientSocket = serverSocket.accept();
                multiOut = new PrintWriter(clientSocket.getOutputStream(), true);
                multiIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                handler.post(() -> appendChat("시스템", "친구가 입장했어요! 게임 시작!"));
                listenMulti();
            } catch (Exception e) {
                handler.post(() -> appendChat("시스템", "오류: " + e.getMessage()));
            }
        }).start();
    }

    private void startClientMode() {
        appendChat("시스템", "방에 입장하는 중...");
        new Thread(() -> {
            try {
                clientSocket = new Socket(hostIp, 9999);
                multiOut = new PrintWriter(clientSocket.getOutputStream(), true);
                multiIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                handler.post(() -> appendChat("시스템", "입장 완료! 게임 시작!"));
                listenMulti();
            } catch (Exception e) {
                handler.post(() -> appendChat("시스템", "연결 실패: " + e.getMessage()));
            }
        }).start();
    }

    private void listenMulti() {
        new Thread(() -> {
            try {
                String line;
                while ((line = multiIn.readLine()) != null) {
                    String msg = line;
                    handler.post(() -> addBubble("상대방", msg, false));
                }
            } catch (Exception e) {
                handler.post(() -> appendChat("시스템", "연결이 끊겼어요."));
            }
        }).start();
    }

    private void sendToMulti(String msg) {
        new Thread(() -> {
            if (multiOut != null) {
                multiOut.println(msg);
            }
        }).start();
    }

    private void appendChat(String sender, String message) {
        addBubble(sender, message, false);
    }

    private void updateCoins(int delta) {
        SharedPreferences prefs = getSharedPreferences("ScalaPrefs", MODE_PRIVATE);
        int coins = prefs.getInt("coins", 0);
        coins = Math.max(0, coins + delta);
        prefs.edit().putInt("coins", coins).apply();
        Toast.makeText(this, delta > 0 ? "+" + delta + " 코인!" : delta + " 코인", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}