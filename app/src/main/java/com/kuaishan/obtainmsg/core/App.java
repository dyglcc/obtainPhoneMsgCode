package com.kuaishan.obtainmsg.core;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
