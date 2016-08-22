package com.demo.demotest;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by quchao on 2016/8/15 0015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900047616", true);
    }
}
