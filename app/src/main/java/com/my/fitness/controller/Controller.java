package com.my.fitness.controller;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.my.fitness.activity.SetValues;
import com.my.fitness.model.BusinessUtils;

public class Controller {
    BusinessUtils businessUtils = new BusinessUtils();
    private Handler customHandler = new Handler();

    private SetValues setValues;

    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;

    long excersiseDurationInMs;

    private String TAG="Fitness";

    public Controller(long excerciseDuration) {
        excersiseDurationInMs = excerciseDuration * 1000;
    }

    public void setSetValues(SetValues mSetValues) {
        setValues = mSetValues;
    }

    public Uri getVideoUri(String packagename) {
        return businessUtils.getVideoUri(packagename);
    }

    public void handleStart() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void handlePause() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    public void stopTimerThread() {
        customHandler.removeCallbacks(updateTimerThread);
    }

    Runnable updateTimerThread = new Runnable() {
        long updatedTime = 0L;

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            long timeRemaining = excersiseDurationInMs - updatedTime;
            String timeString = businessUtils.getTimeToShow(timeRemaining);
            setValues.setTimerValueToUI(timeString);
            Log.i(TAG, "Time remaining :"+ timeString);
            if (updatedTime > 0)
                customHandler.postDelayed(this, 0);
        }
    };

}