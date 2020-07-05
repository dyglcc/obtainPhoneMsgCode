package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.app.ProgressDialog;
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

import org.json.JSONObject;

import java.util.HashMap;

import androidx.annotation.Nullable;

public class RegisterActivity extends Activity {

    private Button buttonReg;
    private EditText pass1, pass2, et_phone, et_name;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_reg);
        buttonReg = findViewById(R.id.btn_reg);
        mobile = Utils.getPhone(this);
        et_phone = findViewById(R.id.et_phone);
        et_name = findViewById(R.id.et_name);
        et_phone.setText(mobile);
        pass1 = findViewById(R.id.et_pass1);
        pass2 = findViewById(R.id.et_pass2);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });

    }
    private void reg() {
        String pass_1 = pass1.getText().toString();
        String pass_2 = pass2.getText().toString();
        String name = et_name.getText().toString();

        if (TextUtils.isEmpty(pass_1)) {
            Utils.toast(RegisterActivity.this, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(pass_2)) {
            Utils.toast(RegisterActivity.this, "请输入确认密码");
            return;
        }
        if (!pass_1.equals(pass_2)) {
            Utils.toast(RegisterActivity.this, "两次输入密码不一致");
            return;
        }
//        String firstName, String email, String lastName, String password, String mobile
        final HashMap map = new HashMap();
        map.put("firstName", name);
        map.put("password", pass_1);
        map.put("mobile", mobile);
        showLoadingDialog("发送数据中..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.REGISTER, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    Utils.toast(RegisterActivity.this, "注册成功，请登录");
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    try {
                                        JSONObject object = new JSONObject(str);
                                        String msg = object.optString("message");
                                        Utils.toast(RegisterActivity.this, msg);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                        Utils.toast(RegisterActivity.this, "未知错误");
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

    }

    private ProgressDialog mProgressDialog;

    private void dismiss() {
        if (!this.isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void showLoadingDialog(String message) {
        if (!this.isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

}
