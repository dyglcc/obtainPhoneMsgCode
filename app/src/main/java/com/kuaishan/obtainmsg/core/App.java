package com.kuaishan.obtainmsg.core;

import android.app.Application;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        FeedbackAPI.initAnnoy(this,"5eb8a281895ccaeb3c00010d");
    }
}
