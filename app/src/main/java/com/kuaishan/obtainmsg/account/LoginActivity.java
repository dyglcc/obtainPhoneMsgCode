package com.kuaishan.obtainmsg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.MainActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.SoftKeyboardUtil;
import com.kuaishan.obtainmsg.core.T;
import com.kuaishan.obtainmsg.core.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class LoginActivity extends BaseActivity {
    private Button buttonLogin;
    private EditText et_pass;
    private EditText et_phone;

    public static void start(Activity context) {
        context.startActivity(new Intent(context, LoginActivity.class));
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
        findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        com.kuaishan.obtainmsg.test.MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_add_myshare_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // test
//                Utils.btn_add_myshare_dialog(LoginActivity.this, 1, 101);
            }
        });
        String phone = Utils.getPhone(this);
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardUtil.hideSoftKeyboard(LoginActivity.this);
                login();
            }
        });
        findViewById(R.id.btn_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(LoginActivity.this);
            }
        });
        findViewById(R.id.btn_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotActivity.start(LoginActivity.this);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("登录");
        }
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isFirstOpen(LoginActivity.this)){
                    Utils.showPrivicyDialog(LoginActivity.this,true);
                }
            }
        },1000);
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
                                    setAlias(etPhone);
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
    public static boolean isFirstOpen(Context context) {
        return context.getSharedPreferences(Constants.COMMON.SHARE_NAME, 0).getBoolean(Constants.COMMON.FIRST_OPEN, true);
    }

    public static void saveFirstOpenApp(Activity context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.COMMON.SHARE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.COMMON.FIRST_OPEN,false);
        editor.apply();
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

    public static void logOut(Activity context) {
        if (context == null) {
            return;
        }
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.COMMON.SHARE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Constants.COMMON.TIME_TOKEN, 0);
        editor.putString(Constants.COMMON.ALIAS, "");
        editor.apply();
        start(context);

    }


    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    T.i(logs);
                    saveTimeToken();
                    Utils.toast(LoginActivity.this, "登录成功");
                    goMain();
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    T.i(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                            1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    T.i(logs);
            }
//            Utils.toast(LoginActivity.this,logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    T.i("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    T.i("Unhandled msg - " + msg.what);
            }
        }
    };// 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
}
