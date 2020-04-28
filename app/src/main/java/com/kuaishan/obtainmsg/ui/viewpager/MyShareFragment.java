package com.kuaishan.obtainmsg.ui.viewpager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.bean.AppShares;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyShareFragment extends LazyFragment {
    public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
    public static final String INTENT_INT_POSITION = "intent_int_position";
    private String tabName;
    private int position;
    private TextView textView;
    private ListView list;
    private ProgressBar progressBar;
    private View stubEmpty;
    static List datas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestMySharedApps();
    }
    private void requestMySharedApps() {
        final HashMap map = new HashMap();
        map.put("mobile", Utils.getPhone(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.APPS, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        datas =
                                                gson.fromJson(dataObj.optJSONArray("apps").toString(),
                                                new TypeToken<List<AppShares>>() {
                                                }.getType());
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

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        tabName = getArguments().getString(INTENT_STRING_TABNAME);
        position = getArguments().getInt(INTENT_INT_POSITION);
        setContentView(R.layout.layout_myshare);
        textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
        textView.setText(tabName + " " + position + " 界面加载完毕");
        progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
        stubEmpty = findViewById(R.id.stub_layout_myshare);
//        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeMessages(1);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                progressBar.setVisibility(View.GONE);
            }
            if(msg.what ==10){
                stubEmpty.setVisibility(View.VISIBLE);
            }
//            textView.setVisibility(View.VISIBLE);
        }
    };
}
