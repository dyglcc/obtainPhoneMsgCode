package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuaishan.obtainmsg.MainActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.annotation.Nullable;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.kuaishan.obtainmsg.ui.home.HomeFragment.getPhone;

public class LoginActivity extends Activity {
    private Button buttonLogin;
    private EditText et_phone, et_pass;

    public static void start(Activity context) {
        context.startActivity(new Intent(context,LoginActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);
        buttonLogin = findViewById(R.id.btn_login);
        et_phone = findViewById(R.id.et_phone);
        et_pass = findViewById(R.id.et_pass);

        long timeToke = getTimeToken(this);
        if ((System.currentTimeMillis() - timeToke) < Constants.COMMON.TENDAYS) {
            goMain();
            return;
        }

        String phone = getPhone(this);
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btn_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(LoginActivity.this);
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
                    dismiss();
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    savePhone(etPhone);
                                    JPushInterface.setAlias(LoginActivity.this,0,etPhone);
                                    saveTimeToken();
                                    Utils.toast(LoginActivity.this, "登录成功");
                                    goMain();
                                }
                            });
                        } else {
                            try {
                                JSONObject object = new JSONObject(str);
                                String msg = object.optString("message");
                                Utils.toast(LoginActivity.this, "貌似有问题出现:" + msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        }

    }

    private void goMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public static long getTimeToken(Context context) {
        return context.getSharedPreferences(Constants.COMMON.SHARE_NAME, 0).getLong(Constants.COMMON.TIME_TOKEN, 0);
    }

    private void saveTimeToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.COMMON.SHARE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Constants.COMMON.TIME_TOKEN, System.currentTimeMillis());
        editor.apply();
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

    private void savePhone(String mobile) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.COMMON.SHARE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.apply();
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
                    savePhone(verfyedPhone);
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }

    public static void logOut(Context context){
        if(context == null){
            return;
        }
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.COMMON.SHARE_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Constants.COMMON.TIME_TOKEN,0);
        editor.putString(Constants.COMMON.ALIAS,"");
        editor.apply();
    }
}
