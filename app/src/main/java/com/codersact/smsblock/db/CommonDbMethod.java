package com.codersact.smsblock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.codersact.smsblock.model.SmsData;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by masum on 11/08/2015.
 */
public class CommonDbMethod {
    Context context;

    public CommonDbMethod(Context context) {
        this.context = context;
    }

    public void addToSMSBlacklist(String name, String number, String body) {
        if (number.length() == 0) {
            Toast.makeText(context, "Please fill up both the fields", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db;
        db = context.openOrCreateDatabase("/data/data/activity.masum.com.smsblock/databases/BlackListDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        db.setLocale(Locale.getDefault());
        db.setLockingEnabled(true);
        db.execSQL("create table IF NOT EXISTS SMS_BlackList(names varchar(20) UNIQUE, numbers varchar(20) UNIQUE, body varchar(250))");

        // Insert the "PhoneNumbers" into database-table, "SMS_BlackList"
        ContentValues values = new ContentValues();
        values.put("names", name);
        values.put("numbers", number);
        values.put("body", body);

        if (db.insert("SMS_BlackList", null, values) == -1){
            Log.d("addToSMS_BlackList", "3: blockingCodeForSMS ");
            Toast.makeText(context, name + " already exist in database\n Please try a new name!!", Toast.LENGTH_LONG).show();
            db.close();
            return;
        }

        Log.d("addToSMS_BlackList", "4: blockingCodeForSMS ");
        Log.d("addToSMS_BlackList", "5: blockingCodeForSMS ");

        db.close();
        Toast.makeText(context, number +" is added to blacklist", Toast.LENGTH_LONG).show();
        //finish();
    }

    public boolean deleteSms(final String number, final String tableName) {
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/activity.masum.com.smsblock/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);
            db.delete(tableName, "numbers" + " = ?", new String[] {number});
            //return true;

            db.close();

        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
            // return false;
        }

        return false;

    }


}


