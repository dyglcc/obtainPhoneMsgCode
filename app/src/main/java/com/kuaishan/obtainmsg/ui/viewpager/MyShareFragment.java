package com.kuaishan.obtainmsg.ui.viewpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kuaishan.obtainmsg.R;
import com.shizhefei.fragment.LazyFragment;

public class MyShareFragment extends LazyFragment {
    public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
    public static final String INTENT_INT_POSITION = "intent_int_position";
    private String tabName;
    private int position;
    private TextView textView;
    private ListView list;
    private ProgressBar progressBar;
    private View stubEmpty;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        tabName = getArguments().getString(INTENT_STRING_TABNAME);
        position = getArguments().getInt(INTENT_INT_POSITION);
        setContentView(R.layout.layout_myshare);
        textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
        textView.setText(tabName + " " + position + " 界面加载完毕");
        progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
        stubEmpty = findViewById(R.id.layout_myshare);
//        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
//        handler.removeMessages(1);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                progressBar.setVisibility(View.GONE);
            }
            if(msg.what ==10){
                stubEmpty.setVisibility(View.VISIBLE);
            }
//            textView.setVisibility(View.VISIBLE);
        }
    };
}
