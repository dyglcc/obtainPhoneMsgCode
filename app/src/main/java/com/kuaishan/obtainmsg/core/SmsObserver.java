package com.kuaishan.obtainmsg.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

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
        if (uri != null && uriString.equals(uri.toString())) {
            ContentResolver cr = mContext.getContentResolver();
            String[] projection = new String[]{"message_body,_id"};//"_id", "address", "person",,
            Cursor cur = cr.query(SMS_INBOX, projection, null, null, "_id desc");
            if (null == cur)
                return;
            if (cur.moveToNext()) {
                String body = cur.getString(cur.getColumnIndex("message_body"));
                mHandler.obtainMessage(App.MSG_RECEIVED_CODE, body).sendToTarget();
            }
        }

    }


    public static String uriString = "content://sms/raw";
    public static Uri SMS_INBOX = Uri.parse(uriString);


}