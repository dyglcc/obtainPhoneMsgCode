package com.kuaishan.obtainmsg.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmsUtils {

    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public void getSmsFromPhone(Context context) {

        if(context == null){
            return ;
        }
        ArrayList<Map> list = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] {"_id", "address", "person","body", "date", "type" };
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.i("ooc","************cur == null");
            return;
        }
        while(cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("num",number);
            map.put("mess",body);
            list.add(map);
        }
    }
}
