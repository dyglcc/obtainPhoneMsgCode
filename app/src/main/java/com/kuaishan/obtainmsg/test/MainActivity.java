package com.kuaishan.obtainmsg.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private HorizontalListView mListView;
    private String[] str = { "小明" ,"小红","小新","小红","小新"};
    private HorizonListviewAdapter mAdapter;
    private List<GOODS> data;
    private GOODS goods;
    private MyListView mListView2;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initView();
        initView2();
    }

    private void initView2() {

//        mListView2 = (MyListView) findViewById(R.id.mylist);
//        data = new ArrayList<GOODS>();
//        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
//        data.add(goods);
//        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
//        data.add(goods);
//        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
//        data.add(goods);
//        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
//        data.add(goods);
//        goods = new GOODS("5月22日   星期一", "魅族mx4", "￥1200", R.mipmap.ic_launcher);
//        data.add(goods);
//        mAdapter = new HorizonListviewAdapter(this, data);
//        adapter = new MyAdapter(str,mAdapter);
//        mListView2.setAdapter(adapter);


    }

    private void initView() {
        //	mListView = (HorizontalVariableListView) findViewById(R.id.mylist);
        data = new ArrayList<GOODS>();
        goods = new GOODS("2015-09-29-星期一", "手机", "1000", R.mipmap.ic_launcher);
        data.add(goods);
        goods = new GOODS("2015-09-29-星期一", "手机", "1000", R.mipmap.ic_launcher);
        data.add(goods);
        goods = new GOODS("2015-09-29-星期一", "手机", "1000", R.mipmap.ic_launcher);
        data.add(goods);
        mAdapter = new HorizonListviewAdapter(this, data);
        mListView.setAdapter(mAdapter);

    }

    public static class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;
        private String [] data;
        private HorizonListviewAdapter adapter;
        private Context context;
        public MyAdapter(String [] data, HorizonListviewAdapter adapter, Context context) {
            this.adapter =adapter;
            this.data = data;
            this.context = context;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_main_listitem, null);
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            name.setText(data[position]);
            HorizontalListView mListView = (HorizontalListView) convertView.findViewById(R.id.horizontal_listview);
            mListView.setAdapter(adapter);
            return convertView;
        }

    }

}
