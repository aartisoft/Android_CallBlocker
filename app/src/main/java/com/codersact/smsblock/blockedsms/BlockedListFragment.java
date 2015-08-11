package com.codersact.smsblock.blockedsms;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


import com.codersact.smsblock.adapter.MyAdapter;
import activity.masum.com.smsblock.R;
import com.codersact.smsblock.model.SmsData;

public class BlockedListFragment extends Fragment implements View.OnClickListener {
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    ArrayList<SmsData> smsDatas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_blocked_list, container, false);
        initView(rootView);
        fetchBlackList();
        MyAdapter mAdapter = new MyAdapter(smsDatas, getActivity());
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

    }

    private void fetchBlackList() {
        //Create a cursor for the "SMS_BlackList" table
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/activity.masum.com.smsblock/databases/BlackListDB.db", null, SQLiteDatabase.OPEN_READWRITE);

        //Check, if the "fromAddr" exists in the BlackListDB
        Cursor c = db.query("sms_blocked", null, null, null, null, null, null);
        //Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

        if (c.moveToFirst() && c.getCount() > 0) {
            while (!c.isAfterLast()) {
                SmsData smsData = new SmsData();
                smsData.setSmsNo(c.getString(c.getColumnIndex("names")));
                smsData.setSmsAddress(c.getString(c.getColumnIndex("numbers")));
                smsData.setSmsString(c.getString(c.getColumnIndex("names")));
                smsDatas.add(smsData);
                c.moveToNext();
            }

            c.close();

        }

        db.close();
        return;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFetchSMS:
                /*Intent intent = new Intent(getActivity(), BlackListActivity.class);
                startActivity(intent);*/
                break;

            case R.id.btnBlockedList:
               /* Intent intent1 = new Intent(getActivity(), BlockedListActivity.class);
                startActivity(intent1);*/
                break;
        }
    }
}
