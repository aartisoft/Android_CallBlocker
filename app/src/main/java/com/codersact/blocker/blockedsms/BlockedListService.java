package com.codersact.blocker.blockedsms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.codersact.blocker.model.SmsData;

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
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.codersact.blocker/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("sms_blocked", null, null, null, null, null, " id DESC");
            //Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                while (!c.isAfterLast()) {
                    SmsData smsData = new SmsData();
                    smsData.setSmsThreadId(c.getString(c.getColumnIndex("names")));
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
