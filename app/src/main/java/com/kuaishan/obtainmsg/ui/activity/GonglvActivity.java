package com.kuaishan.obtainmsg.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kuaishan.obtainmsg.BaseActivity;
import com.kuaishan.obtainmsg.R;
import com.kuaishan.obtainmsg.core.T;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

public class GonglvActivity extends BaseActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_gonglv);

        imageView = findViewById(R.id.gonglv);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                T.i(imageView.getWidth() + " " + imageView.getHeight());
                int width = imageView.getWidth();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(GonglvActivity.this.getResources(),
                        R.mipmap.gonglv_long, options);
//                if (map != null) {
                int outHeight = options.outHeight;
                int outWidth = options.outWidth;
                int height = (outHeight * width) / outWidth;
                LinearLayout.LayoutParams newParams =
                        new LinearLayout.LayoutParams(imageView.getLayoutParams());
//                newParams.height = Utils.pxToDp(GonglvActivity.this,height);
//                newParams.width = Utils.pxToDp(GonglvActivity.this,width);
                newParams.height = height;
                newParams.width = width;
                imageView.setLayoutParams(newParams);
                imageView.requestLayout();
                T.i(height + " " + width);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.i(v.getWidth() + " " + v.getHeight());
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("攻略");
        }
    }
}
