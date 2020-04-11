package com.kuaishan.obtainmsg.core;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.kuaishan.obtainmsg.MainActivity;

/**
 *
 */

public class SmsObserver extends ContentObserver {

    private Handler mHandler;
    private Context mContext;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String code;
        ////onChange会执行二次,第二次短信才会入库
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String body = c.getString(c.getColumnIndex("body"));//获取短信内容
                mHandler.obtainMessage(MainActivity.MSG_RECEIVED_CODE, body).sendToTarget();
            }
        }
        c.close();
    }
}