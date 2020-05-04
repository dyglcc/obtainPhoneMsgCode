package com.kuaishan.obtainmsg.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;

public class TestAdapter extends BaseAdapter {

    private Activity activity;
    public TestAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 10;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(activity).inflate(R.layout.item_sub_account_only_image,
                            null);
            TextView tv = convertView.findViewById(R.id.tv_sub_account_name);
            holder = new ViewHolder(tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private final class ViewHolder {
        TextView tv_sub_account_name;

        ViewHolder(TextView tv_sub_account_name) {
            this.tv_sub_account_name = tv_sub_account_name;
        }
    }
}
