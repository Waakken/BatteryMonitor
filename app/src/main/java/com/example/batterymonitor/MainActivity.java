package com.example.batterymonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.text.method.BaseMovementMethod;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String extra_msg = "hello";
    private int prevCharge = 0;
    private int events = 0;
    private boolean scheduled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "initializing");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(batteryStatusReceiver, ifilter);
        Log.d("","Scheduling a job");
        scheduleJob(getApplicationContext());
        TextView messageField = findViewById(R.id.messageField);
        messageField.setMovementMethod(new ScrollingMovementMethod());

    }

    private static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
        Log.d("", "Util->scheduleJob");
    }

    private BroadcastReceiver batteryStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // this is where we deal with the data sent from the battery.
            int charge = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            //if (charge != prevCharge) {
            Log.d("batteryStatusReceiver", "Battery level: " + charge);
            handleBatteryStatus(intent);
            //}
            prevCharge = charge;
            events++;
            TextView statusView = findViewById(R.id.textViewStatus);
            statusView.setText("Events: " + events);

//            if (!scheduled) {
//                Util.scheduleJob(context);
//            }
        }
    };

    public void clearMessages(View view) {
        TextView messageField = findViewById(R.id.messageField);
        messageField.setText("");
    }

    public void sendMessage(View view) {
        writeBatteryCharge(prevCharge, "(button)");
    }

    public class MyThread extends Thread {
        public void run() {
            Log.d("batteryStatusReceiver", "Thread is running");
            int max = 250000;
            int prime = calculatePrimeUntil(max);
            TextView messageField = findViewById(R.id.messageField);
            messageField.append("Prime: " + prime + " max was: " + max + "\n");
        }
    }

    public void runCpu(View view) {
        int threadCount = 8;
        Log.d("batteryStatusReceiver", "Calculating prime");
        TextView messageField = findViewById(R.id.messageField);
        messageField.append("Calculation started on " + threadCount + " threads\n");
        //writeBatteryCharge(prevCharge, "(button)");
        for (int i = 0; i < threadCount; i++) {
            MyThread myThread = new MyThread();
            myThread.start();
        }
    }

    private boolean isPrime(int number) {
        if (number < 3)
            return false;
        for (int i = 2; i < number; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    private int calculatePrimeUntil(int max) {
        int biggestPrime = 0;
        for (int i = 3; i < max; i++) {
            if (isPrime(i))
                biggestPrime = i;
        }
        return biggestPrime;
    }

    private void handleBatteryStatus(Intent batteryStatus) {
        int charge = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        writeBatteryCharge(charge, "(event)");
    }

    public void writeBatteryCharge(int charge, String msg) {
        String curDate = getCurrentTime();

        TextView messageField = findViewById(R.id.messageField);
        messageField.append(curDate + " Battery level: " + charge + " " + msg + "\n");
    }

    private String getCurrentTime() {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.get(Calendar.DAY_OF_WEEK);

        String minuteStr = getZeroPaddedNumber(curCalendar.get(Calendar.MINUTE));
        String hourStr = getZeroPaddedNumber(curCalendar.get(Calendar.HOUR_OF_DAY));
        String secondStr = getZeroPaddedNumber(curCalendar.get(Calendar.SECOND));

        return hourStr + ":" +
                minuteStr + ":" +
                secondStr;
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
