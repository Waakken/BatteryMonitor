package com.example.batterymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String msg = intent.getStringExtra(MainActivity.extra_msg);

        TextView textView = findViewById(R.id.textView);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int charge = batteryStatus.getIntExtra(String.valueOf(BatteryManager.EXTRA_LEVEL), -1);
        msg = msg + " OK, battery charge: " + charge;
        textView.setText(msg);
    }
}