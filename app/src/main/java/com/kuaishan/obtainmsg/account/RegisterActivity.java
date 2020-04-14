package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class RegisterActivity extends Activity {

    private Button buttonReg, buttonVerify;
    private EditText pass1, pass2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_reg);
        buttonReg = findViewById(R.id.btn_reg);
        buttonVerify = findViewById(R.id.btn_very);
        pass1 = findViewById(R.id.et_pass1);
        pass2 = findViewById(R.id.et_pass2);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(RegisterActivity.this);
            }
        });

    }

    private void reg() {
        if (verfyedPhone == null) {
            Utils.toast(RegisterActivity.this, "手机号未通过验证");
            return;
        }
        String pass_1 = pass1.getText().toString();
        String pass_2 = pass2.getText().toString();

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
        map.put("firstName", verfyedPhone);
        map.put("password", pass_1);
        map.put("mobile", verfyedPhone);
        showLoadingDialog("发送数据中..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    String str = NetWorkUtils.sendMessge(Constants.Url.REGISTER, map);
                    if (!TextUtils.isEmpty(str)) {
                        if(str.contains("ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    Utils.toast(RegisterActivity.this,"注册成功，请登录");
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
        if(!this.isFinishing()){
            if(mProgressDialog !=null && mProgressDialog.isShowing()){
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
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }
}
