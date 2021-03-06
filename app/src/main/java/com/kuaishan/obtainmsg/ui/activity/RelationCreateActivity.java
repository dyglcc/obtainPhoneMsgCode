package com.kuaishan.obtainmsg.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.adapter.RelationAdapter;
import com.kuaishan.obtainmsg.ui.bean.Relation;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.graphics.ColorUtils;


public class RelationCreateActivity extends BaseActivity {
    private Button btnGl;
    private EditText editGualian, et_name;
    private ListView list;
    int group_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        group_id = intent.getIntExtra("group_id", 0);

        setContentView(R.layout.main_relation);
        btnGl = findViewById(R.id.btn_guanlian);
        btnGl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRelation();
            }
        });
        editGualian = findViewById(R.id.et_guanlian);
        et_name = findViewById(R.id.et_name);
        list = findViewById(R.id.list);
        adapter = new RelationAdapter(null, this);
        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Relation relation = (Relation) adapter.getItem(position);
//                Intent intent = new Intent(RelationCreateActivity.this, MessagesActivity.class);
//                intent.putExtra("mobile", relation.getMain_account());
//                startActivity(intent);
//            }
//        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                if(adapter == null){
                    Utils.toast(RelationCreateActivity.this,"ops!,error");
                    return false;
                }
                final Relation relation = (Relation) adapter.getItem(position);
                Dialog dialog = new AlertDialog.Builder(RelationCreateActivity.this).setTitle(
                        "删除").setMessage("确定删除选中共享帐号").setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDeleteSubAccount(relation);
                    }
                }).show();
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("添加帐号");
        }


        findViewById(R.id.btn_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clipShare(RelationCreateActivity.this);
                Utils.toast(RelationCreateActivity.this, "链接已经复制");
            }
        });


    }
    private void requestDeleteSubAccount(Relation relation) {
        final HashMap map = new HashMap();
        map.put("main_account", Utils.getPhone(this));
        map.put("group_id",group_id+"");
        map.put("sub_account",relation.getSub_account());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.D_SUB_ACCOUNT, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        List datas =
                                                gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                        new TypeToken<List<Relation>>() {
                                                        }.getType());
                                        // need gson;
                                        refreshData(datas);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private RelationAdapter adapter;

    private void refreshData(List data) {
        adapter.setData(data);
    }

    private void requestRelations() {
        final HashMap map = new HashMap();
        map.put("main_account", Utils.getPhone(this));
        map.put("group_id",group_id+"");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.GETRELATION, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        List datas =
                                                gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                        new TypeToken<List<Relation>>() {
                                                        }.getType());
                                        // need gson;
                                        refreshData(datas);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestRelations();
    }

    private void addRelation() {
        String etPhone = editGualian.getText().toString();
        String etName = et_name.getText().toString();

        if (TextUtils.isEmpty(etPhone)) {
            Utils.toast(RelationCreateActivity.this, "关联帐号必须要有哦");
            return;
        }
        final HashMap map = new HashMap();
        map.put("main_account", Utils.getPhone(this));
        map.put("sub_account", etPhone);
        map.put("name", etName);
        map.put("group_id", group_id + "");
        showLoadingDialog("发送数据中..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.ADDRELATION, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    Utils.toast(RelationCreateActivity.this, "添加关联帐号成功");
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        List datas =
                                                gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                        new TypeToken<List<Relation>>() {
                                                        }.getType());
                                        // need gson;
                                        refreshData(datas);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
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
        if (isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void showLoadingDialog(String message) {
        if (isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(100);
        finish();
    }



    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    /**
     * 获取StatusBar颜色，默认白色
     *
     * @return
     */
    protected @ColorInt int getStatusBarColor() {
        return Color.WHITE;
    }

}
