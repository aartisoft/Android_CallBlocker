package com.codersact.smsblock.blockedsms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.codersact.smsblock.model.SmsData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class BlockedListService {

    public ArrayList<SmsData> getSmsInfo() {
        return fetchBlockedList();
    }

    private ArrayList<SmsData> fetchBlockedList() {
        ArrayList<SmsData> smsDatas = new ArrayList<>();

        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/activity.masum.com.smsblock/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("sms_blocked", null, null, null, null, null, null);
            //Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                while (!c.isAfterLast()) {
                    SmsData smsData = new SmsData();
                    smsData.setSmsNo(c.getString(c.getColumnIndex("names")));
                    smsData.setSmsAddress(c.getString(c.getColumnIndex("numbers")));
                    smsData.setSmsString(c.getString(c.getColumnIndex("body")));
                    smsDatas.add(smsData);
                    c.moveToNext();
                }

                c.close();

            }

            db.close();

        } catch (Exception e) {

        }

        return smsDatas;
    }
}
