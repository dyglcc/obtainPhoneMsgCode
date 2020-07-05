package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.T;
import com.kuaishan.obtainmsg.core.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


public class ForgotStep2Activity extends BaseActivity {
    EditText et_pass,et_pass_confirm;
    Button btn_confirm;

    public static void start(Activity context) {
        context.startActivity(new Intent(context, ForgotStep2Activity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_forget_step2);
        btn_confirm = findViewById(R.id.btn_save_pass);
        et_pass = findViewById(R.id.et_pass);
        et_pass_confirm = findViewById(R.id.et_pass_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePass();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("忘记密码");
        }
    }
    private void savePass() {
        final String etPhone  = Utils.getPhone(this);
        String etPass = et_pass.getText().toString();
        String etPassConfirm = et_pass_confirm.getText().toString();

        if (TextUtils.isEmpty(etPhone)) {
            T.i("手机号莫名奇妙找不到了");
            return;
        }
        if (TextUtils.isEmpty(etPass)) {
            Utils.toast(this, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(etPassConfirm)) {
            Utils.toast(this, "请输入密码");
            return;
        }
        if(!etPass.equals(etPassConfirm)){
            Utils.toast(this, "密码不一致");
            return;
        }
        final HashMap map = new HashMap();
        map.put("mobile", etPhone);
        map.put("newPass", etPass);
        showLoadingDialog("发送数据中..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.FORGOT_PASS, map);
                    dismiss();
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.savePhone(etPhone,ForgotStep2Activity.this);
                                    Utils.toast(ForgotStep2Activity.this,"设置密码成功");
                                    LoginActivity.start(ForgotStep2Activity.this);
                                    finish();
                                }
                            });
                        } else {
                            try {
                                JSONObject object = new JSONObject(str);
                                String msg = object.optString("message");
                                Utils.toast(ForgotStep2Activity.this, "貌似有问题出现:" + msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

    }
    private ProgressDialog mProgressDialog;

    private void dismiss() {
        if (!isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
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

}
