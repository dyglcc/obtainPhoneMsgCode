package com.kuaishan.obtainmsg.core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.RuntimeRationale;
import com.mob.MobSDK;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.PermissionDef;

import java.util.List;

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
     * Display setting dialog.
     */
    public static void showGetMSGDialog(final Context context) {
        String message = context.getString(R.string.tryingObtain);

        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_dialog_obtain_msg)
                .setMessage(message)
                .setCancelable(true)
                .show();
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

    public static int pxToDp(Context context,int px) {
        DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
