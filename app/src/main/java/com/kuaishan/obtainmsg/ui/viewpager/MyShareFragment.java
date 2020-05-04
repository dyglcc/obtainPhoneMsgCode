package com.kuaishan.obtainmsg.ui.viewpager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.test.GOODS;
import com.kuaishan.obtainmsg.test.HorizonListviewAdapter;
import com.kuaishan.obtainmsg.test.MainActivity;
import com.kuaishan.obtainmsg.ui.activity.ShareAppSettingActivity;
import com.kuaishan.obtainmsg.ui.adapter.UserAppAdapter;
import com.kuaishan.obtainmsg.ui.bean.UserApp;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class MyShareFragment extends LazyFragment {
    public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
    public static final String INTENT_INT_POSITION = "intent_int_position";
    private String tabName;
    private int position;
    private ListView list;
    private ProgressBar progressBar;
    static List<UserApp> datas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void requestMySharedApps() {
        final HashMap map = new HashMap();
        map.put("main_account", Utils.getPhone(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.GROUPS, map);
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
                                        datas =
                                                gson.fromJson(dataObj.optJSONArray("apps").toString(),
                                                        new TypeToken<List<UserApp>>() {
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


    GOODS goods;
    private String[] str = { "小明" ,"小红","小新","小红","小新"};
    private HorizonListviewAdapter mAdapter;
    private MainActivity.MyAdapter adapter;
    private void initTest(){
        List data = new ArrayList<GOODS>();
        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
        data.add(goods);
        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
        data.add(goods);
        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
        data.add(goods);
        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
        data.add(goods);
        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
        data.add(goods);
        mAdapter = new HorizonListviewAdapter(getActivity(), data);
        adapter = new MainActivity.MyAdapter(str,mAdapter,getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_myshare, container, false);
    }

    private void refreshData(List datas) {
        appAdapter.setData(datas);
    }
    private ListView listView;
    private UserAppAdapter appAdapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        tabName = getArguments().getString(INTENT_STRING_TABNAME);
        position = getArguments().getInt(INTENT_INT_POSITION);
        progressBar = findViewById(R.id.fragment_mainTab_item_progressBar);
        listView = findViewById(R.id.list);
        initTest();
//        appAdapter = new UserAppAdapter(null, getActivity());
//        listView.setAdapter(appAdapter);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_user_app,null);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),ShareAppSettingActivity.class),100);
            }
        });
        listView.addFooterView(footer);
        requestMySharedApps();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View view1 = view.findViewById(R.id.hList);

                Utils.toast(getActivity(),"view1" + view1.getHeight() + " " + view1.getWidth());
                Utils.toast(getActivity(),"view2" + view.getHeight() + " " + view.getWidth());
            }
        });
//        HorizontalListView hlist_ = findViewById(R.id.hlist_);
//        hlist_.setAdapter(new SubAccountAdapter(getActivity(),null));
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==100){
            requestMySharedApps();
        }else if(requestCode == 101){
            requestMySharedApps();
        }
    }
}
