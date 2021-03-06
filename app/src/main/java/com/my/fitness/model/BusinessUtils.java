package com.my.fitness.model;

import android.net.Uri;

import com.my.fitness.R;

import java.util.concurrent.TimeUnit;

public class BusinessUtils {

    public Uri getVideoUri(String packagename) {
        return Uri.parse("android.resource://" + packagename + "/" + R.raw.test_10003);
    }

    public String getTimeToShow(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

}
