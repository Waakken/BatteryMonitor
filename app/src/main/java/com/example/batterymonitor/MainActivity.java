package com.example.batterymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String extra_msg = "hello";
    private static final String TAG = MainActivity.class.getSimpleName();
    private int prevCharge = 0;
    private int events = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "initializing");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(batteryStatusReceiver, ifilter);
    }

    private BroadcastReceiver batteryStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // this is where we deal with the data sent from the battery.
            int charge = intent.getIntExtra(String.valueOf(BatteryManager.EXTRA_LEVEL), -1);
            //if (charge != prevCharge) {
                Log.d("batteryStatusReceiver", "Battery level: " + charge);
                handleBatteryStatus(intent);
            //}
            prevCharge = charge;
            events++;
            TextView statusView = findViewById(R.id.textViewStatus);
            statusView.setText("Events: " + events);

        }
    };

    public void clearMessages(View view) {
        //Log.d("clearMessages", "hello");
        TextView messageField = findViewById(R.id.messageField);
        messageField.setText("");
    }

    public void sendMessage(View view) {
        writeBatteryCharge(prevCharge, "(button)");
        //writeBatteryLevel(batteryStatus);
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editTextTextPersonName2);
        String msg = editText.getText().toString();
        intent.putExtra(extra_msg, msg);
        startActivity(intent);
         */
    }

    private void handleBatteryStatus(Intent batteryStatus) {
        int charge = batteryStatus.getIntExtra(String.valueOf(BatteryManager.EXTRA_LEVEL), -1);
        writeBatteryCharge(charge, "(event)");
    }

    private void writeBatteryCharge(int charge, String msg) {
        //Log.d(TAG, "Battery level: " + charge);
        String curDate = getCurrentTime();
        //Log.d(TAG, "Date " + curDate);

        TextView messageField = findViewById(R.id.messageField);
        messageField.append(curDate + " Battery level: " + charge + " " + msg + "\n");
    }

    private String getCurrentTime() {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.get(Calendar.DAY_OF_WEEK);
        int seconds = curCalendar.get(Calendar.SECOND);
        Date curDate = curCalendar.getTime();

        String minuteStr = getZeroPaddedNumber(curCalendar.get(Calendar.MINUTE));
        String hourStr = getZeroPaddedNumber(curCalendar.get(Calendar.HOUR_OF_DAY));
        String secondStr = getZeroPaddedNumber(curCalendar.get(Calendar.SECOND));

        String retVal = hourStr + ":" +
                minuteStr + ":" +
                secondStr;
        return retVal;
    }

    private String getZeroPaddedNumber(int number) {
        String numberStr;
        if (number < 10) {
            numberStr = "0" + number;
        } else {
            numberStr = "" + number;
        }
        return numberStr;
    }
}
