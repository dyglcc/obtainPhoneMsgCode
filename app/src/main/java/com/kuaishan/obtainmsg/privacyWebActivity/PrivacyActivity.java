package com.kuaishan.obtainmsg.privacyWebActivity;

import android.os.Bundle;

import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

public class PrivacyActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_privacy);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("用户须知");
        }
    }


}
