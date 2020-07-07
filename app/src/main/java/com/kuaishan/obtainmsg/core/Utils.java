package com.kuaishan.obtainmsg.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.RuntimeRationale;
import com.kuaishan.obtainmsg.ui.activity.RelationCreateActivity;
import com.kuaishan.obtainmsg.ui.bean.SubTicket;
import com.mob.MobSDK;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.PermissionDef;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

public class Utils {
    public static void toast(final Context context, @StringRes final int message) {
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void toast(final Context context, final String message) {
        if(context == null){
            return;
        }
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Request permissions.
     */
    public static void requestPermission(final Context context,
                                         @PermissionDef String... permissions) {
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        T.i(context.getString(R.string.successfully));
                        MobSDK.submitPolicyGrantResult(true, null);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        MobSDK.submitPolicyGrantResult(false, null);
                        Utils.toast(context, R.string.failure);
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            showSettingDialog(context, permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * Display setting dialog.
     */
    public static void showSettingDialog(final Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed,
                TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(context);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * loading dialog
     */
    public static void showGetMSGDialog(final Activity context, String main_account,
                                        String appName) {
        if (context == null) {
            return;
        }
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_get_msg, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ProgressBar progressBar = view.findViewById(R.id.progress_loading);
        TextView tvCount = view.findViewById(R.id.tv_count);
        TextView textMessageContent = view.findViewById(R.id.tv_message);
        TextView tvLoading = view.findViewById(R.id.tv_msg);
        Button button = view.findViewById(R.id.btn_copy);
        requestSingleMessageCode(progressBar, tvCount, tvLoading, textMessageContent, button,
                context,
                main_account, appName,dialog);
    }

    private static void requestSingleMessageCode(final ProgressBar progressBar,
                                                 final TextView tvCount,
                                                 final TextView tvLoading,
                                                 final TextView textMessageContent,
                                                 final Button btnCopy,
                                                 final Activity activity,
                                                 String main_account,
                                                 String appName,
                                                 final Dialog dialog) {
        final HashMap map = new HashMap();
        map.put("main_account", main_account);
        map.put("app_name", appName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.G_SINGLE_MSG, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        if(dataObj.optString("data").equals("null")){
                                            updateDialog(null,null);
                                            return;
                                        }
                                        Gson gson = new Gson();
                                        final SubTicket message =
                                                gson.fromJson(dataObj.optJSONObject("data").toString(),
                                                        new TypeToken<SubTicket>() {
                                                        }.getType());
                                        if (message != null) {
                                            // regular get msg code
                                            Pattern pattern = Pattern.compile("\\b\\d{4,8}\\b");
                                            Matcher matcher = pattern.matcher(str);
                                            String foundOne = null;
                                            if (matcher.find()) {
                                                foundOne = matcher.group(0);
                                            }
                                            updateDialog(foundOne,message);
                                        }
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }

                private void updateDialog(String foundOne,SubTicket message) {
                    progressBar.setVisibility(View.INVISIBLE);
                    tvCount.setVisibility(View.INVISIBLE);
                    tvLoading.setVisibility(View.GONE);
                    if(message == null){
                        textMessageContent.setText("未获取到验证码");
                        btnCopy.setText("确定");
                        btnCopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        textMessageContent.setText(message.getMessage());

                        if (foundOne != null && foundOne.trim().length() > 0) {
                            btnCopy.setText(btnCopy.getText() + "[" + foundOne +
                                    "]");
                        }
                        final String finalFoundOne = foundOne;
                        btnCopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.clipString(activity, "验证码",
                                        finalFoundOne);
                                dialog.dismiss();
                            }
                        });
                    }
                    textMessageContent.setVisibility(View.VISIBLE);
                    btnCopy.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    /**
     * loading dialog
     */
    public static void btn_add_myshare_dialog(final Activity context, final int id, final int i) {
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_myshare_subaccount,
                null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button btnAdd = view.findViewById(R.id.tv_invite);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipShare(context);
                Utils.toast(context, "链接已经复制");
                dialog.dismiss();
            }
        });
        Button btnInvite = view.findViewById(R.id.tv_add);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),
                        RelationCreateActivity.class);
                intent.putExtra("group_id", id);
                context.startActivityForResult(intent, i);
                dialog.dismiss();
            }
        });
    }

    private static final int REQUEST_CODE_SETTING = 1;

    /**
     * Set permissions.
     */
    private static void setPermission(Context context) {
        AndPermission.with(context).runtime().setting().start(REQUEST_CODE_SETTING);
    }


    public static String getPhone(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.COMMON.SHARE_NAME, 0);
        return sharedPreferences.getString("mobile", "");
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    /**
     * 获取当前进程名
     */
    private static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager =
                (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                Log.i("service:", process.processName);
                Log.i("proccess_name:", process.processName);
                return process.processName;
            }
        }
        return null;
    }

    /**
     * 包名判断是否为主进程
     *
     * @param
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return context.getApplicationContext().getPackageName().equals(getCurrentProcessName(context));
    }


    public static void clipShare(Context context) {
        //获取剪贴板管理器：
        clipString(context, "亲密共享号下载链接", "https://sj.qq.com/myapp/detail" +
                ".htm?apkName=com.cleanmaster.mguard_cn");
    }

    public static void clipString(Context context, String label, String text) {
        //获取剪贴板管理器：
        ClipboardManager cm =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText(label, text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    public static  void savePhone(String mobile,Context context) {
        if(context == null || TextUtils.isEmpty(mobile)){
            T.w("参数错误");
            return;
        }
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.COMMON.SHARE_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.apply();
    }

}
