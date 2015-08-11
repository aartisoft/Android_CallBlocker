package com.codersact.smsblock.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import activity.masum.com.smsblock.R;
import com.codersact.smsblock.model.SmsData;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<SmsData> mDataset = new ArrayList<>();
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
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
    public MyAdapter(ArrayList<SmsData> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText("Thread ID " + mDataset.get(position).getSmsAddress());
        holder.mTextDesc.setText(mDataset.get(position).getSmsString());
        holder.mTextDesc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addToSMSBlacklist("Thread " + mDataset.get(position).getSmsNo(), mDataset.get(position).getSmsAddress());

            }
        });

    }

    protected void addToSMSBlacklist(String name, String number) {
        if (name.length() == 0 || number.length() == 0) {
            Toast.makeText(context, "Please fill up both the fields", Toast.LENGTH_LONG);
            return;
        }

        SQLiteDatabase db;
        db = context.openOrCreateDatabase("/data/data/activity.masum.com.smsblock/databases/BlackListDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        db.setLocale(Locale.getDefault());
        db.setLockingEnabled(true);
        db.execSQL("create table IF NOT EXISTS SMS_BlackList(names varchar(20) UNIQUE, numbers varchar(20))");

        // Insert the "PhoneNumbers" into database-table, "SMS_BlackList"
        ContentValues values = new ContentValues();
        values.put("names", name);
        values.put("numbers", number);
        if (db.insert("SMS_BlackList", null, values) == -1){
            Log.d("addToSMS_BlackList", "3: blockingCodeForSMS ");
            Toast.makeText(context, name + " already exist in database\n Please try a new name!!", Toast.LENGTH_LONG).show();
            db.close();
            return;
        }

        Log.d("addToSMS_BlackList", "4: blockingCodeForSMS ");
        Log.d("addToSMS_BlackList", "5: blockingCodeForSMS ");

        db.close();
        Toast.makeText(context, name+" added to SMS blacklist", Toast.LENGTH_LONG).show();
        //finish();
    }


    public void deleteItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}