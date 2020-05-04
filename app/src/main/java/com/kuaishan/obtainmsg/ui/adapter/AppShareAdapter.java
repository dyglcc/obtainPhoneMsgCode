package com.kuaishan.obtainmsg.ui.adapter;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.AdhocExecutorService;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.NetWorkUtils;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.bean.AppShares;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.SwitchCompat;

public class AppShareAdapter extends BaseAdapter {
    private List<AppShares> data;
    private Activity context;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");

    public AppShareAdapter(List<AppShares> data, Activity context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<AppShares> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppShares app = data.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_app_setting, null);
            TextView tv_mobile = convertView.findViewById(R.id.tv_app_name);
            ImageView iv_icon = convertView.findViewById(R.id.iv_icon);
            SwitchCompat switchCompat = convertView.findViewById(R.id.switch_selected);
            holder = new ViewHolder(tv_mobile, iv_icon, switchCompat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_app_name.setText(app.getApp_name());
        Glide.with(context).load(app.getIcon_url()).placeholder(R.drawable.ic_launcher).into(holder.iv_icon).onLoadFailed(null, context.getResources().getDrawable(R.drawable.ic_launcher));
        holder.switchCompat.setChecked(app.getStatus() == 1);
        holder.switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clickstatus = holder.switchCompat.isChecked();
                requestNetAddGroup(context,clickstatus,app);
            }
        });
//        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
        return convertView;
    }

    private void requestNetAddGroup(final Activity context, boolean add,AppShares app) {
        final HashMap map = new HashMap();
        map.put("main_account", Utils.getPhone(context));
        map.put("app_id",app.getId()+"");
        if(app.getGroup_id()!=0){
            map.put("group_id",app.getGroup_id()+"");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AdhocExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final String str = NetWorkUtils.sendMessge(Constants.Url.ADDGROUP, map);
                    if (!TextUtils.isEmpty(str)) {
                        if (str.contains("ok")) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.toast(context, "授权app成功");
                                    try {
//                                        JSONObject jsonObject = new JSONObject(str);
//                                        JSONObject dataObj = jsonObject.optJSONObject("data");
//                                        Gson gson = new Gson();
//                                        List datas =
//                                                gson.fromJson(dataObj.optJSONArray("data").toString(),
//                                                        new TypeToken<List<Relation>>() {
//                                                        }.getType());
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
    private static class ViewHolder {
        private TextView tv_app_name;
        private ImageView iv_icon;
        private SwitchCompat switchCompat;

        public ViewHolder(TextView tv_app_name, ImageView iv_icon, SwitchCompat switchCompat) {
            this.tv_app_name = tv_app_name;
            this.iv_icon = iv_icon;
            this.switchCompat = switchCompat;
        }
    }

}
