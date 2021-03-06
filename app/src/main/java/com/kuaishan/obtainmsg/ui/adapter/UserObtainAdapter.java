package com.kuaishan.obtainmsg.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.core.Utils;
import com.kuaishan.obtainmsg.ui.bean.Relation;

import java.util.List;

public class UserObtainAdapter extends BaseAdapter {
    private List<Relation> data;
    private Activity context;

    public UserObtainAdapter(List<Relation> data, Activity context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<Relation> data) {
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
        final Relation relation = data.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_obtain,
                    null);
            TextView tv_name = convertView.findViewById(R.id.tv_name);
            TextView tv_host_account = convertView.findViewById(R.id.tv_host_account);
//            TextView tv_relation_name = convertView.findViewById(R.id.tv_relation_name);

            ImageView icon = convertView.findViewById(R.id.iv_icon);
            TextView btn_obtain_msg = convertView.findViewById(R.id.btn_obtain_msg);
            holder = new ViewHolder(tv_name, tv_host_account, icon, btn_obtain_msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(relation.getApp_name());
        holder.tv_host_account.setText(context.getResources().getString(R.string.main_account,
                relation.getMain_account()));
        holder.btn_obtain_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showGetMSGDialog(context, relation.getMain_account(), relation.getApp_name());
            }
        });
        Glide.with(context).load(Constants.Url.mHost + relation.getIcon_url())
                .into(holder.icon);
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_name;
        private TextView tv_host_account;
        private ImageView icon;
        private TextView btn_obtain_msg;

        public ViewHolder(TextView tv_name, TextView tv_host_account, ImageView icon,
                          TextView btn_obtain_msg
        ) {
            this.tv_name = tv_name;
            this.btn_obtain_msg = btn_obtain_msg;
            this.icon = icon;
            this.tv_host_account = tv_host_account;
        }
    }

}
