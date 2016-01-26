package com.codersact.blocker.blacklist;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codersact.blocker.adapter.BlackListAdapter;
import com.codersact.blocker.adapter.InboxNumberDialogAdapter;
import com.codersact.blocker.db.CommonDbMethod;
import com.codersact.blocker.inbox.InboxService;
import com.codersact.blocker.model.NumberData;
import com.codersact.blocker.model.SmsData;
import com.codersact.blocker.utility.UtilityMethod;

import java.util.ArrayList;
import java.util.Date;

import activity.masum.com.smsblock.R;

public class BlackListFragment extends Fragment implements View.OnClickListener, BlacklistView {
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    BlackListPresenter blackListPresenter;
    RelativeLayout relative_help;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_black_list, container, false);
        initView(rootView);
        blackListPresenter = new BlackListPresenter(this, new BlackListService());

        BlackListAdapter mAdapter = new BlackListAdapter(blackListPresenter.onSaveClick(), getActivity());
        recyclerView.setAdapter(mAdapter);

        setMessage(blackListPresenter.onSaveClick().size());

        mAdapter.setOnDataChangeListener(new BlackListAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                setMessage(blackListPresenter.onSaveClick().size());
            }
        });

        return rootView;
    }

    public void setMessage(int size) {
        if (size > 0) {
            relative_help.setVisibility(View.INVISIBLE);
            //textView.setVisibility(View.VISIBLE);
        } else {
            relative_help.setVisibility(View.VISIBLE);
            //textView.setVisibility(View.INVISIBLE);
        }
    }

    private void initView(View rootView) {
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        textView = (TextView) rootView.findViewById(R.id.textView);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        relative_help = (RelativeLayout) rootView.findViewById(R.id.relative_help);

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
            case R.id.floatingActionButton:
                openActionDialog();
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




    private void openActionDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom_Destructive);
        //builderSingle.setIcon(R.drawable.about);
        builderSingle.setTitle("Add From Sender");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item);
        arrayAdapter.add("From Inbox");
        arrayAdapter.add("From Log");
        arrayAdapter.add("Manual Entry");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            openDialogInbox("Cancel");

                        } else if(which == 1) {
                            openDialogLog("Cancel");

                        } else if(which == 2) {
                            openManualEntryDilaog("Number", "Add", "Cancel");
                        }

                    }
                });

        builderSingle.show();
    }

    private void openManualEntryDilaog(String message, String okButton, String cancelButton) {
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogCustom_Destructive);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_retry);
        dialog.setCanceledOnTouchOutside(false);

        TextView txtViewPopupMessage = (TextView) dialog.findViewById(R.id.txtViewPopupMessage);
        ImageButton imgBtnClose = (ImageButton) dialog.findViewById(R.id.imgBtnClose);
        final EditText editText = (EditText) dialog.findViewById(R.id.editText);

        Button btnAccept = (Button) dialog.findViewById(R.id.btnAdd);
        btnAccept.setText(okButton);
        txtViewPopupMessage.setText(message);

        // if button is clicked, close the custom dialog
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonDbMethod(getActivity()).addToSMSBlacklist("", "", editText.getText().toString().trim(), "");
                UtilityMethod.blackListFragment(getActivity());
                getActivity().setTitle("Black List");
                dialog.dismiss();
                //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hello");
            }

        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText(cancelButton);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }

    private void openDialogInbox(String cancelButton) {
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogCustom_Destructive);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_inbox);
        dialog.setCanceledOnTouchOutside(false);

        ListView listView = (ListView) dialog.findViewById(R.id.listViewInbox);
        final ArrayList<SmsData> smsDatas = new InboxService().fetchInboxSms(getActivity());

        final ArrayList<NumberData> numberDatas = new ArrayList<>();
        for (int i = 0; i < smsDatas.size(); i++) {
            NumberData numberData = new NumberData();
            numberData.setSenderNumber(smsDatas.get(i).getSmsAddress());
            numberDatas.add(numberData);
        }

        InboxNumberDialogAdapter inboxNumberAdapter = new InboxNumberDialogAdapter(getActivity(), numberDatas);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setText(cancelButton);
        listView.setAdapter(inboxNumberAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                new CommonDbMethod(getActivity()).addToSMSBlacklist("", smsDatas.get(position).getSmsThreadNo(), numberDatas.get(position).getSenderNumber(), "");
                UtilityMethod.blackListFragment(getActivity());
                getActivity().setTitle("Black List");
                dialog.dismiss();
                //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hello");
                //Toast.makeText(getActivity(), "Position" + numberDatas.get(position).getSenderNumber(), Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }


    private void openDialogLog(String cancelButton) {
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogCustom_Destructive);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_inbox);
        dialog.setCanceledOnTouchOutside(false);

        TextView view = (TextView) dialog.findViewById(R.id.view);
        ListView listView = (ListView) dialog.findViewById(R.id.listViewInbox);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        InboxNumberDialogAdapter inboxNumberAdapter = new InboxNumberDialogAdapter(getActivity(), getCallDetails());
        btnCancel.setText(cancelButton);
        listView.setAdapter(inboxNumberAdapter);
        view.setText("Log");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                new CommonDbMethod(getActivity()).addToSMSBlacklist("", getCallDetails().get(position).getSenderNumber(), getCallDetails().get(position).getSenderNumber(), "");
                UtilityMethod.blackListFragment(getActivity());
                getActivity().setTitle("Black List");
                dialog.dismiss();
                //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hello");
                //Toast.makeText(getActivity(), "Position" + numberDatas.get(position).getSenderNumber(), Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }

    private ArrayList<NumberData> getCallDetails() {

        final ArrayList<NumberData> numberDatas = new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while ( managedCursor.moveToNext() ) {
            String callType = managedCursor.getString(type);
            if (Integer.parseInt(callType) == CallLog.Calls.INCOMING_TYPE || Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE) {
                String phNumber = managedCursor.getString(number);

                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt( callType );
                switch( dircode ) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                NumberData numberData = new NumberData();
                numberData.setSenderNumber(phNumber);
                numberDatas.add(numberData);

                sb.append( "\n Phone Number:--- "+phNumber +" \n Call Type:--- "+dir+" \n Call Date:--- "+callDayTime+" \n Call duration in sec :--- "+callDuration );
                sb.append("\n----------------------------------");
            }


        }

        managedCursor.close();

        return numberDatas;

    }


}
