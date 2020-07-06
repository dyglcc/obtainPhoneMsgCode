package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.T;
import com.kuaishan.obtainmsg.core.Utils;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class ForgotActivity extends BaseActivity {
    private Button btn_verify;
    private EditText et_phone;

    public static void start(Activity context) {
        context.startActivity(new Intent(context, ForgotActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_forget);
        btn_verify = findViewById(R.id.btn_verify);
        et_phone = findViewById(R.id.et_phone);
        et_phone.setVisibility(View.GONE);
        String phone = Utils.getPhone(this);
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone);
        }
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(ForgotActivity.this);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("忘记密码");
        }
    }


//    private void savePhone(String mobile) {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.COMMON.SHARE_NAME, 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("mobile", mobile);
//        editor.apply();
//    }
    private String verfyedPhone = null;

    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    // 国家代码，如“86”
                    String country = (String) phoneMap.get("country");
                    // 手机号码，如“13800138000”
                    verfyedPhone = (String) phoneMap.get("phone");
                    Utils.savePhone(verfyedPhone,ForgotActivity.this);
                    ForgotStep2Activity.start(ForgotActivity.this);
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        T.i("Main onDestory forgot1");
    }
}
