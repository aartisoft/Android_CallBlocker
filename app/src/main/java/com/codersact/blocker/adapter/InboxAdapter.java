package com.codersact.blocker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import activity.masum.com.smsblock.R;

import com.codersact.blocker.model.SmsData;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder>{
    private ArrayList<SmsData> mDataset = new ArrayList<>();
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextDesc;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.person_name);
            mTextDesc = (TextView) v.findViewById(R.id.person_age);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InboxAdapter(ArrayList<SmsData> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.mTextView.setText(mDataset.get(position).getSmsAddress());
        holder.mTextDesc.setText(mDataset.get(position).getSmsString());
        holder.mTextDesc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new CommonDbMethod(context).addToSMSBlacklist(mDataset.get(position).getSmsId() , mDataset.get(position).getSmsThreadNo(), mDataset.get(position).getSmsAddress(), mDataset.get(position).getSmsString().trim());

                delete_thread(mDataset.get(position).getSmsAddress());
                //deleteSMSInProgress(context, mDataset.get(position).getSmsThreadNo());
            }

        });

    }

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

        Uri thread = Uri.parse("content://sms/" + thread_id);
        context.getContentResolver().delete(thread, null, null);
        Log.i("Timer Task", "Delete Successful");

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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}