package com.kuaishan.obtainmsg.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.Constants;
import com.kuaishan.obtainmsg.test.HorizonListviewAdapter;
import com.kuaishan.obtainmsg.test.HorizontalListView;
import com.kuaishan.obtainmsg.ui.activity.RelationCreateActivity;
import com.kuaishan.obtainmsg.ui.bean.UserApp;

import java.util.List;

public class UserAppAdapter extends BaseAdapter {
    private List<UserApp> data;
    private Activity context;

    public UserAppAdapter(List<UserApp> data, Activity context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<UserApp> data) {
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
        final UserApp usera = data.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_main_listitem,
                    null);
            TextView tv_name = convertView.findViewById(R.id.tv_name);
            ImageView icon = convertView.findViewById(R.id.iv_icon);
            TextView tv_add_sub_account = convertView.findViewById(R.id.tv_add_sub_account);
            HorizontalListView hList = convertView.findViewById(R.id.horizontal_listview);
            holder = new ViewHolder(tv_name, icon, tv_add_sub_account, hList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(usera.getApp().getApp_name());
        if (usera.getRelations() != null && usera.getRelations().size() > 0 && usera.getRelations().get(0).getName() != null) {
            holder.tv_add_sub_account.setVisibility(View.GONE);
            holder.hList.setVisibility(View.VISIBLE);
            holder.hList.setAdapter(new HorizonListviewAdapter(context, usera.getRelations()));
        } else {
            holder.tv_add_sub_account.setVisibility(View.VISIBLE);
            holder.hList.setVisibility(View.GONE);
            holder.tv_add_sub_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(),
                            RelationCreateActivity.class);
                    intent.putExtra("group_id", usera.getApp().getId());
                    context.startActivityForResult(intent, 101);
                }
            });
        }
        Glide.with(context).load(Constants.Url.mHost+usera.getApp().getIcon_url()).placeholder(R.drawable.ic_launcher)
                .into(holder.icon)
                .onLoadFailed(null, context.getResources().getDrawable(R.drawable.ic_launcher));

        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_name;
        private TextView tv_add_sub_account;
        private ImageView icon;
        private HorizontalListView hList;

        public ViewHolder(TextView tv_name, ImageView icon, TextView tv_add_sub_account,
                          HorizontalListView hList) {
            this.tv_name = tv_name;
            this.tv_add_sub_account = tv_add_sub_account;
            this.icon = icon;
            this.hList = hList;
        }
    }

}
