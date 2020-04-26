package com.kuaishan.obtainmsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.account.LoginActivity;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.SmsObserver;
import com.kuaishan.obtainmsg.core.T;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.bean.Relation;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    public static boolean isForeground = false;
    public static final int MSG_RECEIVED_CODE = 1;
    private SmsObserver mObserver;
    static List datas;

    // jpush
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long timeToke = LoginActivity.getTimeToken(this);
        if ((System.currentTimeMillis() - timeToke) >= Constants.COMMON.TENDAYS) {
            goLoginActivity();
            return;
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Utils.requestPermission(this, Permission.SEND_SMS, Permission.READ_SMS,
                Permission.RECEIVE_SMS, Permission.READ_PHONE_STATE,
                Permission.WRITE_EXTERNAL_STORAGE);
        mObserver = new SmsObserver(MainActivity.this, new MsgHandler(MainActivity.this));
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
        requestLeaders();

        // jpush regiser
        registerMessageReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private void goLoginActivity() {
        LoginActivity.start(this);
    }

    public static class MsgHandler extends Handler {
        private WeakReference<Activity> activity;

        public MsgHandler(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            HashMap map = new HashMap();
            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                T.i("receive code " + code);
                if (datas != null) {
                    for (int i = 0; i < datas.size(); i++) {
                        Relation relation = new Relation();
                        String leaderMobile = relation.getUser_phone();
                        if (!map.containsKey(leaderMobile)) {
                            map.put(leaderMobile, true);
                            if (activity != null && activity.get() != null) {
//                                sendSMSS(leaderMobile, code,activity.get());
                                saveMesage2Server(activity.get(), code);
                            } else {
                                T.i("activity is null, null");
                            }
                        }
                    }
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

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void requestLeaders() {
        final HashMap map = new HashMap();
        map.put("mobile", Utils.getPhone(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.GETRELATION, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        datas = gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                new TypeToken<List<Relation>>() {
                                                }.getType());
                                        // need gson;
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private static void saveMesage2Server(final Activity context, String content) {
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
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Utils.toast(context, str);
                                        // need gson;
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.kuaishan.obtainmsg.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public static class MessageReceiver extends BroadcastReceiver {

        private WeakReference<Activity> context;
        public MessageReceiver(Activity activity){
            context = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setCostomMsg(String toString) {
            if (MainActivity.isForeground) {
                if(context!=null && context.get()!=null){
                    Dialog dialog =
                            new AlertDialog.Builder(context.get()).setMessage(toString).create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);
                }

            }
        }
    }
}

