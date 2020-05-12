package com.kuaishan.obtainmsg.ui.viewpager;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.adapter.UserObtainAdapter;
import com.kuaishan.obtainmsg.ui.bean.Relation;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyObtainFragment extends LazyFragment {
    public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
    public static final String INTENT_INT_POSITION = "intent_int_position";
    private String tabName;
    private int position;
    private TextView textView;
    private ProgressBar progressBar;

//    @Override
//    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
//        return inflater.inflate(R.layout.layout_myshare, container, false);
//    }

    private ListView listView;
    private UserObtainAdapter obtainAdapter;
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.layout_myshare);
        tabName = getArguments().getString(INTENT_STRING_TABNAME);
        position = getArguments().getInt(INTENT_INT_POSITION);
//        textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
//        textView.setText(tabName + " " + position + " 界面加载完毕");
        progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
        listView = findViewById(R.id.list);
        obtainAdapter = new UserObtainAdapter(null, getActivity());
        listView.setAdapter(obtainAdapter);
        requestMyObtainApps();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
    }
    private void requestMyObtainApps() {
        final HashMap map = new HashMap();
        map.put("sub_account", Utils.getPhone(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.FINDSUBACCOUNT, map);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            getActivity().runOnUiThread(new Runnable() {
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

    private void refreshData(List datas) {
        int count = listView.getFooterViewsCount();
        if(count==0){
            View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_obtain,null);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"aaa",Toast.LENGTH_LONG).show();
                }
            });
            footer.findViewById(R.id.btn_share_weixin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"aaa",Toast.LENGTH_LONG).show();
                }
            });
            listView.addFooterView(footer);
        }
        obtainAdapter.setData(datas);
    }
}
