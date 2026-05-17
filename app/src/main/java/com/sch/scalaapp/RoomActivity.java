package com.sch.scalaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    Button btnCreate, btnJoin;
    EditText etRoomIp;
    TextView tvMyIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("멀티 모드");
        }

        btnCreate = findViewById(R.id.btnCreate);
        btnJoin = findViewById(R.id.btnJoin);
        etRoomIp = findViewById(R.id.etRoomIp);
        tvMyIp = findViewById(R.id.tvMyIp);

        tvMyIp.setText("내 IP: " + getLocalIpAddress());

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameListActivity.class);
            intent.putExtra("mode", "host");
            startActivity(intent);
        });

        btnJoin.setOnClickListener(v -> {
            String ip = etRoomIp.getText().toString().trim();
            if (ip.isEmpty()) {
                Toast.makeText(this, "방장 IP를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, GameListActivity.class);
            intent.putExtra("mode", "client");
            intent.putExtra("hostIp", ip);
            startActivity(intent);
        });
    }

    private String getLocalIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress() && addr.getHostAddress().contains(".")) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "알 수 없음";
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}