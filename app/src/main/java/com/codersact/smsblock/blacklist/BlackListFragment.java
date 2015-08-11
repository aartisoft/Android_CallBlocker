package com.codersact.smsblock.blacklist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codersact.smsblock.adapter.MyAdapter;
import activity.masum.com.smsblock.R;

public class BlackListFragment extends Fragment implements View.OnClickListener, BlacklistView {
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    BlackListPresenter blackListPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_black_list, container, false);
        initView(rootView);
        blackListPresenter = new BlackListPresenter(this, new BlackListService());
        MyAdapter mAdapter = new MyAdapter(blackListPresenter.onSaveClick(), getActivity());
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

    }

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

    @Override
    public void getSmsInfo() {

    }

    @Override
    public String getSmsName() {
        return null;
    }

    @Override
    public String getSmsNumber() {
        return null;
    }
}
