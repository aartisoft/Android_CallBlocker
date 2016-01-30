package com.codersact.blocker.blacklist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.codersact.blocker.model.MobileData;

import java.util.ArrayList;

/**
 * Created by masum on 11/08/2015.
 */
public class BlackListService {
    public ArrayList<MobileData> getSmsInfo() {
        return fetchBlackList();
    }

    private ArrayList<MobileData> fetchBlackList() {
        ArrayList<MobileData> mobileDatas = new ArrayList<>();

        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.codersact.blocker/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("SMS_BlackList", null, null, null, null, null, null);
            //Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                while (!c.isAfterLast()) {
                    MobileData mobileData = new MobileData();
                    mobileData.setCallerName(c.getString(c.getColumnIndex("names")));
                    mobileData.setMobileNumber(c.getString(c.getColumnIndex("numbers")));
                    mobileData.setOtherString(c.getString(c.getColumnIndex("sms_id")));
                    mobileDatas.add(mobileData);
                    c.moveToNext();
                }

                c.close();

            }

            db.close();

        } catch (Exception e) {

        }

        return mobileDatas;
    }
}
