package com.kuaishan.obtainmsg.ui.home;

import android.app.Activity;
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
import com.kuaishan.obtainmsg.ui.adapter.MessageAdapter;
import com.kuaishan.obtainmsg.ui.bean.SubTicket;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class MessagesActivity extends Activity {
    private ListView list;
    private MessageAdapter adapter;
    private String mobile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_messages);
        mobile = getIntent().getStringExtra("mobile");
        list = findViewById(R.id.list);
        adapter= new MessageAdapter(null,this);
        list.setAdapter(adapter);
        requestMesages();
    }


    private void requestMesages() {
        final HashMap map = new HashMap();
        map.put("mobile",mobile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.MESSAGES, map);
                    if (!TextUtils.isEmpty(str)) {
                        if(str.contains("ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        List datas = gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                new TypeToken<List<SubTicket>>(){}.getType());
                                        // need gson;
                                        refreshData(datas);
                                    }catch (Throwable throwable){
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
}
