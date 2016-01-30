package com.codersact.blocker.inbox;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.codersact.blocker.model.SmsData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class InboxService {

    public ArrayList<SmsData> fetchInboxSms(Context context) {
        ArrayList<SmsData> smsDatas = new ArrayList<>();

        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[]{"_id", "address", "body", "thread_id"};

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = context.getContentResolver();
       /* Cursor c = cr.query(inboxURI, new String[]{"_id", "DISTINCT address", "body", "thread_id"}, //DISTINCT
                "address IS NOT NULL) GROUP BY (address", null, null);*/

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);


        if (c.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                SmsData smsData = new SmsData();

                for(int idx=0;idx < c.getColumnCount();idx++)
                {
                    msgData += "****" + c.getColumnName(idx) + ":" + c.getString(idx);
                    Log.i("***mm", "*** " + msgData);

                    if (idx == 1) {
                        smsData.setMobileNumber(c.getString(idx));
                    }

                    if (idx == 2) {
                        smsData.setOtherString(c.getString(idx));
                    }

                    if (idx == 3) {
                        smsData.setCallerName(c.getString(idx));
                    }
                    if (idx == 4) {
                        smsData.setSmsId(c.getString(idx));
                    }

                }

                smsDatas.add(smsData);


            } while (c.moveToNext());

        } else {
            // empty box, no SMS
        }

        return  smsDatas;
    }

}

