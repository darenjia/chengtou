package com.bokun.bkjcb.chengtou;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by DengShuai on 2017/7/27.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return context;
    }
}
