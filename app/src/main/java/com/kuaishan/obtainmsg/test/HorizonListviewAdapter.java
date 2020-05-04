package com.kuaishan.obtainmsg.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 2016/11/14.
 */
public class HorizonListviewAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<GOODS> goods = new ArrayList<GOODS>();

    public HorizonListviewAdapter(Context context, List<GOODS> goods) {
        this.inflater = LayoutInflater.from(context);
        this.goods = goods;
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
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
            holder.date = (TextView) convertView.findViewById(R.id.tv_history_bill_date);
            holder.goods = (TextView) convertView.findViewById(R.id.tv_history_bill_goods);
            holder.price = (TextView) convertView.findViewById(R.id.tv_history_bill_price_quantity);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_history_bill);
            holder.item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(goods.get(position).getDate());
        holder.goods.setText(goods.get(position).getGoods());
        holder.price.setText(goods.get(position).getPrice());
        holder.image.setImageResource(goods.get(position).getImageId());
//	    holder.item.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent ev) {
//				// TODO Auto-generated method stub
//				    int lastX = 0,lastY = 0;
//				    int dx = 0,dy = 0;
//			        int action = ev.getAction();
//			        switch (action) {
//			        case MotionEvent.ACTION_DOWN:
//	                    lastX=(int) ev.getRawX();
//	                    lastY=(int) ev.getRawY();
//	                    break;
//			        case MotionEvent.ACTION_MOVE:
//			        	dx =(int)ev.getRawX() - lastX;
//	                     dy =(int)ev.getRawY() - lastY;
//			        	break;
//			        case MotionEvent.ACTION_UP:
//			        	if (dy<200|| dy > -200) {
//							return true;
//						}
//			        	break;
//					default:
//						break;
//					}
//			    return false;
//			}
//
//		});

        return convertView;
    }

    class ViewHolder {
        LinearLayout item;
        TextView date;
        TextView goods;
        TextView price;
        ImageView image;
    }

}
