package com.kuaishan.obtainmsg;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kuaishan.obtainmsg.core.SmsObserver;
import com.kuaishan.obtainmsg.core.T;
import com.kuaishan.obtainmsg.core.Utils;
import com.yanzhenjie.permission.runtime.Permission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static final int MSG_RECEIVED_CODE = 1;
    private SmsObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Utils.requestPermission(this, Permission.SEND_SMS, Permission.READ_SMS,
                Permission.RECEIVE_SMS);
        mObserver = new SmsObserver(MainActivity.this, new MsgHandler());
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    private static class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                T.i("receive code " + code);
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}

