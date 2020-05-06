package com.kuaishan.obtainmsg.test;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.ui.bean.Relation;

import java.util.List;

/**
 * Created by hong on 2016/11/14.
 */
public class HorizonListviewAdapter extends BaseAdapter {

    String[] colors = new String[]{"#B1CEFF", "#FFC8C7", "#FCD9AC"};

    private LayoutInflater inflater = null;
    private List<Relation> relations = null;

    public HorizonListviewAdapter(Context context, List<Relation> relations) {
        this.inflater = LayoutInflater.from(context);
        this.relations = relations;
    }

    @Override
    public int getCount() {
        return relations == null ? 0 : relations.size();
    }

    @Override
    public Object getItem(int position) {
        return relations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.horizontal_listview_listitem, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Relation relation = relations.get(position);
        String character = "#";
        if (!TextUtils.isEmpty(relation.getName())) {
            character = relation.getName().substring(0,1);
        }
        holder.name.setText(character);

        holder.name.setBackgroundColor(Color.parseColor(colors[position % 3]));

        return convertView;
    }

    class ViewHolder {
        TextView name;
    }

}
