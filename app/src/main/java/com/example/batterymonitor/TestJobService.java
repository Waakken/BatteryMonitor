package com.example.batterymonitor;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class TestJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
//        Intent service = new Intent(getApplicationContext(), LocalWordService.class);
//        getApplicationContext().startService(service);
        //Context context = getApplicationContext();
        //Util.scheduleJob(getApplicationContext()); // reschedule the job
        Log.d("", "TestJobService->onStartJob");

        TextView tv = new TextView(getApplicationContext());
        tv.setTextSize(60);
        tv.setText("HELLO HELLO");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("", "TestJobService->onStopJob");
        return true;
    }
}
