package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;

import java.util.HashMap;

import androidx.annotation.Nullable;

import static com.kuaishan.obtainmsg.ui.home.HomeFragment.getPhone;

public class LoginActivity extends Activity {
    private Button buttonLogin;
    private EditText et_phone,et_pass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);
        buttonLogin = findViewById(R.id.btn_login);
        et_phone = findViewById(R.id.et_phone);
        et_pass = findViewById(R.id.et_pass);
        String phone = getPhone(this);
        if(!TextUtils.isEmpty(phone)){
            et_phone.setText(phone);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                login();
            }
        });
        findViewById(R.id.btn_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


    }

    private void login() {
        final String etPhone = et_phone.getText().toString();
        String etPass = et_pass.getText().toString();

        if (TextUtils.isEmpty(etPhone)) {
            Utils.toast(this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(etPass)) {
            Utils.toast(this, "请输入密码");
            return;
        }
        final HashMap map = new HashMap();
        map.put("mobile", etPhone);
        map.put("password", etPass);
        showLoadingDialog("发送数据中..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.LOGIN, map);
                    if (!TextUtils.isEmpty(str)) {
                        if(str.contains("ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    savePhone(etPhone);
                                }
                            });
                        }
                    }
                }
            });
        }

    }
    private ProgressDialog mProgressDialog;

    private void dismiss(){
        if (!isFinishing()) {
            if(mProgressDialog !=null && mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }
    }
    public void showLoadingDialog(String message) {
        if (!isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    private void savePhone(String mobile){
        SharedPreferences sharedPreferences = getSharedPreferences("kuaishan",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile",mobile);
        editor.apply();
    }
}
