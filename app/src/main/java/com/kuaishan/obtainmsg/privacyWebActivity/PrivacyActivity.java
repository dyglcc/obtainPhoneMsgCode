package com.kuaishan.obtainmsg.privacyWebActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

public class PrivacyActivity extends BaseActivity {
    public static final int yinsitiaokuan = 1;
    public static final int yonghuxieyi = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_privacy);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        TextView tv = findViewById(R.id.webview);
        int type = intent.getIntExtra("type",0);
        if (actionBar != null) {
            if(type == yonghuxieyi){
                actionBar.setTitle("用户协议");
                tv.setText(Html.fromHtml(getResources().getString(R.string.user_agreement)));
            }else{
                actionBar.setTitle("隐私政策");
                tv.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy)));
            }
        }


        tv.setMovementMethod(new ScrollingMovementMethod());
    }


}
