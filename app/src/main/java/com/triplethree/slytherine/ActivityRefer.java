package com.triplethree.slytherine;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class ActivityRefer {

    private static WeakReference<Activity> mActivityRef;
    public static void updateActivity(Activity activity) {
        mActivityRef = new WeakReference<Activity>(activity);
    }
}
