package com.kuaishan.obtainmsg.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.adapter.AppShareAdapter;
import com.kuaishan.obtainmsg.ui.bean.AppShares;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShareAppSettingActivity extends AppCompatActivity {
    private ListView list;
    private AppShareAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_share_app_setting);
        list = findViewById(R.id.list);
        adapter= new AppShareAdapter(null,this);
        list.setAdapter(adapter);
        requestApps();
    }
    private void requestApps() {
        final HashMap map = new HashMap();
        map.put("main_account", Utils.getPhone(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.APPS, map);
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
                                                gson.fromJson(dataObj.optJSONArray("apps").toString(),
                                                        new TypeToken<List<AppShares>>() {
                                                        }.getType());
                                        refreshData(datas);
                                        // need gson;
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
    private void refreshData(List data){
        adapter.setData(data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(101);
        finish();
    }
}
