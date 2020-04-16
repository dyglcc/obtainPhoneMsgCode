package com.kuaishan.obtainmsg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.ui.bean.Relation;
import java.util.List;

public class RelationAdapter extends BaseAdapter {
    private List<Relation> data;
    private Context context;

    public RelationAdapter(List<Relation> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<Relation> data){
        this.data = data;
    }
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Relation relation = data.get(position);
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
        holder.tv_name.setText(relation.getName());
        holder.tv_mobile.setText(relation.getUser_phone());
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
