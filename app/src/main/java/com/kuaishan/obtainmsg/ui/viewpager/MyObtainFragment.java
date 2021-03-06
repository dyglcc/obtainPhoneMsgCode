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

    private ListView listView;
    private UserObtainAdapter obtainAdapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.layout_myshare);
        tabName = getArguments().getString(INTENT_STRING_TABNAME);
        position = getArguments().getInt(INTENT_INT_POSITION);
        progressBar = findViewById(R.id.fragment_mainTab_item_progressBar);
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
                    if (getActivity() == null) {
                        return;
                    }
                    final String str = NetWorkUtils.sendMessge(Constants.Url.FINDSUBACCOUNT, map);
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok") && getActivity() != null && !getActivity().isFinishing()) {
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
        if (datas == null) {
            addFooter();
        } else {
            int count = listView.getFooterViewsCount();
            if (count == 0 && datas.size() == 0) {
                addFooter();
            }
            obtainAdapter.setData(datas);
        }
    }

    private void addFooter() {
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_obtain, null);
        View.OnClickListener clickListenter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clipShare(getActivity());
                Utils.toast(getActivity(), "已复制链接,马上分享微信好友～");
            }
        };
        footer.setOnClickListener(clickListenter);
        footer.findViewById(R.id.btn_share_weixin).setOnClickListener(clickListenter);
        listView.addFooterView(footer);
    }
}
