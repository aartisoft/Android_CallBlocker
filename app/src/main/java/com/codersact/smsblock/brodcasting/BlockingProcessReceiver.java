package com.codersact.smsblock.brodcasting;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.codersact.smsblock.main.MainActivity;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
                disconnectPhoneItelephony(context);
                return;
            }*/
        }

        /*Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");
        final SmsMessage smsMessage[] = new SmsMessage[messages.length];

        for (int n = 0; n < messages.length; n++) {
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
        }

        msgBody = smsMessage[0].getDisplayMessageBody();

        String fromAddr = smsMessage[0].getOriginatingAddress();
        final Long threadId = getThreadIdFromAddress(context, (smsMessage[0].getOriginatingAddress()).toString());
        Log.d("SMSBlockingProcess", "1 onReceive");
        Log.d("SMSBlockingProcess", "Thread id " + threadId);
        Log.d("SMSBlockingProcess", "Original Address " + fromAddr);

        Toast.makeText(context, "T::: " + threadId + " ADD::: " + fromAddr, Toast.LENGTH_LONG).show();
        //raiseNotification(context, fromAddr, threadId);

        ifBlockedDeleteSMS(fromAddr, threadId, context, msgBody);*/

    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context) {

        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /*
	 *   If the "From Address" is Blacklisted,
	 *      then,  delete the SMS
	 *   else
	 *      raise a notification for the SMS
	 */

    protected void saveIncommingBlockedSMS(Context context, String name, String number, String body) {

        try {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase("/data/data/activity.plab.com.smsblock/databases/BlackListDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            db.setVersion(1);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.execSQL("create table IF NOT EXISTS sms_blocked(names varchar(20), numbers varchar(20), body varchar(250))");

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

    private void ifBlockedDeleteSMS(final String fromAddr, final Long threadId, final Context context, String body) {
        // Creating a schedulable "delete SMS" task.

        try {
            //Create a cursor for the "SMS_BlackList" table
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/activity.plab.com.smsblock/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("SMS_BlackList", null, "numbers=?", new String[] { fromAddr }, null, null, null);
            Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                // Scheduling the "delete SMS" task.
                //new Timer().schedule(timerTask, 1500);

                AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                disconnectPhoneItelephony(context); // call disconnect
                saveIncommingBlockedSMS(context, "Call blocked ", fromAddr, body);
                //raiseNotification(context, fromAddr, threadId);
                pushNotification(fromAddr);

                c.close();
                db.close();
                return;
            }

            /*//Extract the name corresponding to the "Sender" from contacts
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(fromAddr));
            c = context.getContentResolver().query(lookupUri, null, null, null, null);
            if( ! c.moveToFirst()){
                Log.d("If","! c.moveToFirst(): "+ ! c.moveToFirst());
                db.close();
                c.close();
                //raiseNotification(context, fromAddr,threadId);
                return;
            }

            Log.d("ifBlockedDeleteSMS", c.moveToFirst() + "");
            Log.i("ifBlockedDeleteSMS","DisplayName: "+c.getString(c.getColumnIndex("display_name")));
            Log.i("ifBlockedDeleteSMS","Number: "+c.getString(c.getColumnIndex("number")));
            String name = c.getString(c.getColumnIndex("display_name"));
            Log.d("SMSBlockingProcess", "1 ifBlockedDeleteSMS");

            //Check, if the "Contact name" is present in BlackListDB
            db.execSQL("create table IF NOT EXISTS SMS_BlackList(names varchar(20) UNIQUE, numbers varchat(20))");
            c.close();
            c = db.query("SMS_BlackList", null, "names=?", new String[] { name }, null, null, null);

            Log.d("SMSBlockingProcess", "2 ifBlockedDeleteSMS");
            Log.i("SMSBlockingProcess", "c.getCount: "+c.getCount());
            if (c.getCount() <= 0) {
                Log.d("BlockCallsPressed", "ifBlockedDeleteSMS");
                Log.d("If", "c.getCount(): " + c.getCount());
                db.close();
                c.close();
                //pushNotification();
                //raiseNotification(context, name,threadId);
                return;
            }
            db.close();
            c.close();

            // Scheduling the "delete SMS" task.
            //new Timer().schedule(timerTask, 1500);*/

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

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itselcf)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
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


    /*
     * 	 Deleting the Latest SMS present in InBox, using the Thread Id.
     */
    private static void deleteSMSInProgress(Context context, long thread_id) {
        Uri inbox = Uri.parse("content://sms/inbox");
        Cursor c = context.getContentResolver().query(inbox, null, null, null, "date desc");

        Log.i("Timer Task", (c == null) + "  " + c.moveToFirst());
        if (c == null || !c.moveToFirst()){
            c.close();
            return;
        }

        Log.i("Timer Task", "Delete IN PROGRESS");
        String from = c.getString(c.getColumnIndex("address"));
        c.close();

        Uri thread = Uri.parse("content://sms/conversation/" + thread_id);
        context.getContentResolver().delete(thread, null, null);
        Log.i("Timer Task", "Delete Successful");
        Toast.makeText(context,"Delete Successful " + thread_id, Toast.LENGTH_LONG).show();

        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

    }

    public void delete_thread(String thread)
    {
        Cursor c = context.getContentResolver().query(
                Uri.parse("content://sms/"),new String[] {
                        "_id", "thread_id", "address", "person", "date","body" }, null, null, null);

        try {
            while (c.moveToNext())
            {
                int id = c.getInt(0);
                Log.i("ID", "***" + id);
                String address = c.getString(2);
                if (address.equals(thread))
                {
                    Log.i("OK", "***" + address);
                    Log.i("OK ID", "***" + id);
                    context.getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
                }

            }
        } catch (Exception e) {
            Log.e("DELETE EXCEPTION", "" + e.getMessage() );
        }
    }

    /*
     *   Retrieving the SMS Thread Id, using the Phone number
     */
    public static long getThreadIdFromAddress(Context context, String address) {
        if (address == null)
            return 0;

        String THREAD_RECIPIENT_QUERY = "recipient";

        Uri.Builder uriBuilder = Uri.parse("content://mms-sms/threadID").buildUpon();
        uriBuilder.appendQueryParameter(THREAD_RECIPIENT_QUERY, address);

        long threadId = 0;

        Cursor cursor = context.getContentResolver().query(uriBuilder.build(), new String[] { "_id" }, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    threadId = cursor.getLong(0);
                }
            }
            finally {
                cursor.close();
            }
        }
        return threadId;
    }

    public static int deleteAllSMSbyNumber(String number, Context ctx) {

        Uri smsURI = Uri.parse(SMS_INBOX_URI);
        String thread_id= findThreadIdByAddress(number,ctx);

        return ctx.getContentResolver().delete(smsURI, "thread_id=?", new String[] {thread_id});

    }



    public static String findThreadIdByAddress(String number, Context ctx) {

        Uri uri = Uri.parse(SMS_INBOX_URI);
        String thread_id ="";

        Cursor c = null;
        try{
            c = ctx.getContentResolver().query(uri, new String[] {"thread_id"}, "address=?" ,new String[] {number},null);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(c != null && c.moveToFirst()){

            thread_id= c.getString(0);
        }

        return thread_id;
    }

}
