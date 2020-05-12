package com.kuaishan.obtainmsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kuaishan.obtainmsg.account.LoginActivity;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.live.LiveService;
import com.kuaishan.obtainmsg.live.MyJobService;
import com.yanzhenjie.permission.runtime.Permission;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {
    public static boolean isForeground = false;
    // jpush
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long timeToke = LoginActivity.getTimeToken(this);
        if ((System.currentTimeMillis() - timeToke) >= Constants.COMMON.TENDAYS) {
            goLoginActivity();
            return;
        }
        setContentView(R.layout.activity_main);
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
        // jpush regiser
        registerMessageReceiver();


        // 保活1
        LiveService.toLiveService(this);
        // 保活2
//        KeepLiveService.toLiveService(this);
        // 保活3
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MyJobService.startJobService(this);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
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



    @Override
    protected void onStart() {
        super.onStart();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.kuaishan.obtainmsg" +
            ".MESSAGE_RECEIVED_ACTION";
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

        public MessageReceiver(Activity activity) {
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
                if (context != null && context.get() != null) {
                    Dialog dialog =
                            new AlertDialog.Builder(context.get()).setMessage(toString).create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

