package com.codersact.smsblock.inbox;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import com.codersact.smsblock.adapter.BlackListAdapter;
import com.codersact.smsblock.adapter.MyAdapter;
import activity.masum.com.smsblock.R;
import com.codersact.smsblock.model.SmsData;

public class InboxFragment extends Fragment implements View.OnClickListener, InboxView {
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    ArrayList<SmsData> smsDatas = new ArrayList<>();
    InboxPresenter inboxPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_inbox_sms, container, false);
        initView(rootView);
        inboxPresenter = new InboxPresenter(this, new InboxService());
        MyAdapter mAdapter = new MyAdapter(inboxPresenter.onFetchList(), getActivity());
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                Toast.makeText(getActivity(),"Added to blacklist", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    @Override
    public String getSmsInfo() {
        return null;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
