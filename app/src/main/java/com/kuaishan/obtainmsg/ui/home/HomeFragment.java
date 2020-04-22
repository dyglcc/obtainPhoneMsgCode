package com.kuaishan.obtainmsg.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuaishan.obtainmsg.MainActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.account.LoginActivity;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.adapter.RelationAdapter;
import com.kuaishan.obtainmsg.ui.bean.Relation;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EditText editGualian,et_name;
    private Button btnGl,logout;
    private ListView list;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.MsgHandler.sendSMSS("13810580894","都几点了，还不回家？",getActivity());
            }
        });
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        logout = root.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.logOut(getActivity());
            }
        });
        editGualian = root.findViewById(R.id.et_guanlian);
        et_name = root.findViewById(R.id.et_name);
        btnGl = root.findViewById(R.id.btn_guanlian);
        btnGl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRelation();
            }
        });
        list = root.findViewById(R.id.list);
         adapter= new RelationAdapter(null,getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Relation relation = (Relation) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), MessagesActivity.class);
                intent.putExtra("mobile",relation.getRelate_phone());
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestRelations();
    }

    private void requestRelations() {
        final HashMap map = new HashMap();
        map.put("mobile",getPhone(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.GETRELATION, map);
                    if (!TextUtils.isEmpty(str)) {
                        if(str.contains("ok")){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        List datas = gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                new TypeToken<List<Relation>>(){}.getType());
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

    private RelationAdapter adapter;
    private void refreshData(List data){
            adapter.setData(data);
    }
    private void addRelation() {
        String etPhone = editGualian.getText().toString();
        String etName = et_name.getText().toString();

        if (TextUtils.isEmpty(etPhone)) {
            Utils.toast(getActivity(), "关联帐号必须要有哦");
            return;
        }
        final HashMap map = new HashMap();
        map.put("user_phone", getPhone(getActivity()));
        map.put("relate_phone", etPhone);
        map.put("name", etName);
        showLoadingDialog("发送数据中..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.ADDRELATION, map);
                    if (!TextUtils.isEmpty(str)) {
                        if(str.contains("ok")){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    Utils.toast(getActivity(),"添加关联帐号成功");
                                    try{
                                        JSONObject jsonObject = new JSONObject(str);
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        Gson gson = new Gson();
                                        List datas = gson.fromJson(dataObj.optJSONArray("data").toString(),
                                                new TypeToken<List<Relation>>(){}.getType());
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
    public static String getPhone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.COMMON.SHARE_NAME,0);
        return sharedPreferences.getString("mobile","");
    }

    private ProgressDialog mProgressDialog;

    private void dismiss(){
        if (getActivity()!=null && !getActivity().isFinishing()) {
            if(mProgressDialog !=null && mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }
    }
    public void showLoadingDialog(String message) {
        if (getActivity()!=null && !getActivity().isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity());
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }
}
