package com.codersact.smsblock.blacklist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.codersact.smsblock.model.SmsData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class BlackListService {
    public ArrayList<SmsData> getSmsInfo() {
        return fetchBlackList();
    }

    private ArrayList<SmsData> fetchBlackList() {
        ArrayList<SmsData> smsDatas = new ArrayList<>();

        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/activity.plab.com.smsblock/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("SMS_BlackList", null, null, null, null, null, null);
            //Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                while (!c.isAfterLast()) {
                    SmsData smsData = new SmsData();
                    smsData.setSmsNo(c.getString(c.getColumnIndex("names")));
                    smsData.setSmsAddress(c.getString(c.getColumnIndex("numbers")));
                    smsData.setSmsString("");
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
