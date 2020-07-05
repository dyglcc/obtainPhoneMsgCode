package com.kuaishan.obtainmsg.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                Date date = new Date(msg.getTimestampMillis());//时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String receiveTime = format.format(date);
                String body = msg.getDisplayMessageBody();
                T.i("number:" + msg.getOriginatingAddress()
                        + "   body:" + body + "  time:"
                        + receiveTime);
                if (Build.VERSION.SDK_INT <= 23) {
                    Handler mHandler = new App.MsgHandler(context);
                    if (!TextUtils.isEmpty(body)) {
                        mHandler.obtainMessage(App.MSG_RECEIVED_CODE,
                                msg.getDisplayMessageBody()).sendToTarget();
                    }
                }
            }
        }

    }
}
