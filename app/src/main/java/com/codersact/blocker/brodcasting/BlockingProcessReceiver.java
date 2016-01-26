package com.codersact.blocker.brodcasting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.codersact.blocker.main.MainActivity;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import activity.masum.com.smsblock.R;
import com.android.internal.telephony.ITelephony;

/**
 * Created by masum on 30/07/2015.
 */
public class BlockingProcessReceiver extends BroadcastReceiver {
    Integer notificationId = 1207, requestId=1208;
    String msgBody;
    Context context;
    public static final String SMS_INBOX_URI="content://sms/inbox";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        // If, the received action is not a type of "Phone_State", ignore it
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

            // Else, try to do some action
        else {
            // Fetch the number of incoming call
           String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
           SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss");
           Calendar c = Calendar.getInstance();
           String formattedDate = df.format(c.getTime());
           ifBlockedDeleteSMS(number , null, context, formattedDate);
           Log.i("income number", "" + number);

            // Check, whether this is a member of "Black listed" phone numbers stored in the database
            /*if(MainActivity.blockList.contains(new Blacklist(number)))
            {
                // If yes, invoke the method
                disconnectphoneiTelephony(context);
                return;
            }*/
        }

    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectphoneiTelephony(Context context) {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if(telephony.getCallState() == telephony.CALL_STATE_RINGING) {

        }

        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    protected void saveIncomingBlockedNumber(Context context, String name, String number, String body) {
        try {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase("/data/data/com.codersact.blocker/databases/BlackListDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            db.setVersion(1);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.execSQL("create table IF NOT EXISTS sms_blocked(id integer primary key autoincrement, names varchar(20), numbers varchar(20), body varchar(250))");

            // Insert the "PhoneNumbers" into database-table, "SMS_BlackList"
            ContentValues values = new ContentValues();
            values.put("names", number);
            values.put("numbers", number);
            values.put("body", body);
            db.insert("sms_blocked", null, values);
            db.close();

        } catch (Exception e) {
            Log.d("addToSMS_BlackList", "4: blockingCodeForSMS ");
            Log.d("addToSMS_BlackList", "5: blockingCodeForSMS ");
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private void ifBlockedDeleteSMS(final String fromAddr, final Long threadId, final Context context, String createdDate) {
        // Creating a schedulable "delete SMS" task.

        try {
            //Create a cursor for the "SMS_BlackList" table
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.codersact.blocker/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("SMS_BlackList", null, "numbers=?", new String[] { fromAddr }, null, null, null);
            Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                disconnectphoneiTelephony(context); // call disconnect
                saveIncomingBlockedNumber(context, "Call blocked ", fromAddr, createdDate);
                //raiseNotification(context, fromAddr, threadId);
                pushNotification(fromAddr);

                //AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                c.close();
                db.close();
                return;
            }
            Log.d("SMSBlockingProcess", " ifBlockedDeleteSMS Ended");
        } catch (Exception e){
            Log.e("SMSBlocking excep", " " + e.getMessage());
        }


    }

    private void pushNotification(String fromAddress){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("Call Blocked")
                        .setContentText(fromAddress);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }
    /*
     * Notify the user, about the SMS
     */
    private void raiseNotification(Context context, String from, Long threadId) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification mNotification = new Notification(R.drawable.about, "Message from: " + from, System.currentTimeMillis());

        //Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setData(Uri.parse("content://mms-sms/conversations/" + threadId));

        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.putExtra("NotificationMessage", intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent mPendingIntent = PendingIntent.getActivity(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setLatestEventInfo(context, from, msgBody, mPendingIntent);

        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(notificationId, mNotification);
    }

}
