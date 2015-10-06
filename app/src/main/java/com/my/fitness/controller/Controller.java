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

    public Controller() {
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

    Runnable updateTimerThread = new Runnable() {
        long updatedTime = 0L;

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            String timeString = businessUtils.getTimeToShow(updatedTime);
            setValues.setTimerValueToUI(timeString);
            Log.d("Controller","In Controller: time in ms:"+updatedTime+", "+timeString);
            if(updatedTime>0)
            customHandler.postDelayed(this, 0);
        }
    };

}

// 1) show time in hr:min:sec and not in min:sec:ms
// 2) stop the Handler when the time has elapsed and show the alertDialog asking for entering repetion count
// 3) in back button handling only show the alertDialgo if the time has not elapsed as if time is elapsed rep count dialog will be shown by a call from handler.
// 4) On orientation changes handle state of timer.


