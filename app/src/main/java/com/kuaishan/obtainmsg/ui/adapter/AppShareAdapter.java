package com.kuaishan.obtainmsg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.ui.bean.SubTicket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppShareAdapter extends BaseAdapter {
    private List<SubTicket> data;
    private Context context;
//    ("yyyy-MM-dd/HH:mm:ss")
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
//    private DateFormat format = SimpleDateFormat.getDateTimeInstance();

    public AppShareAdapter(List<SubTicket> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<SubTicket> data){
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
        SubTicket subticket = data.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_relation, null);
            TextView tv_mobile = convertView.findViewById(R.id.tv_mobile);
            TextView tv_name = convertView.findViewById(R.id.tv_name);
            holder = new ViewHolder(tv_mobile,tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(format.format(new Date(subticket.getCreated_at())));
        holder.tv_mobile.setText(subticket.getMessage());
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_mobile, tv_name;

        public ViewHolder(TextView tv_mobile, TextView tv_name) {
            this.tv_mobile = tv_mobile;
            this.tv_name = tv_name;
        }
    }

}
