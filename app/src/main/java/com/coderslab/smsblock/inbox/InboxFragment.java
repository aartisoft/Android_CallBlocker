package com.coderslab.smsblock.inbox;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import com.coderslab.smsblock.adapter.MyAdapter;
import activity.masum.com.smsblock.R;
import activity.masum.com.smsblock.SmsData;

public class InboxFragment extends Fragment implements View.OnClickListener {
    Button btnFetchSMS;
    Button btnBlockedList;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    ArrayList<SmsData> smsDatas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_inbox_sms, container, false);
        initView(rootView);
        fetchInboxSms();
        MyAdapter mAdapter = new MyAdapter(smsDatas, getActivity());
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void initView(View rootView) {
        btnFetchSMS = (Button) rootView.findViewById(R.id.btnFetchSMS);
        btnBlockedList = (Button) rootView.findViewById(R.id.btnBlockedList);

        btnFetchSMS.setOnClickListener(this);
        btnBlockedList.setOnClickListener(this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

    }

    public void fetchInboxSms() {
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[]{"_id", "address", "body", "thread_id"};

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getActivity().getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);


        if (c.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                SmsData smsData = new SmsData();

                for(int idx=0;idx<c.getColumnCount();idx++)
                {
                    msgData += "****" + c.getColumnName(idx) + ":" + c.getString(idx);
                    Log.i("***mm", "*** " + msgData);

                    if (idx == 1) {
                        smsData.setSmsAddress(c.getString(idx));
                    }

                    if (idx == 3) {
                        smsData.setSmsNo(c.getString(idx));
                    }

                }

                smsData.setSmsString(msgData);
                smsDatas.add(smsData);

                // use msgData
            } while (c.moveToNext());
        } else {
            // empty box, no SMS
        }
       /* for (int i = 0; i < c.getCount(); i++) {
            Log.i("Value ", "id:" + c.address);
        }*/
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
                /*Intent intent1 = new Intent(getActivity(), BlockedListActivity.class);
                startActivity(intent1);*/
                break;
        }
    }
}
