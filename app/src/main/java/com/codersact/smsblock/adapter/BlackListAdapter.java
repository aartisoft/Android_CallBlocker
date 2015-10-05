package com.codersact.smsblock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codersact.smsblock.blacklist.BlacklistView;
import com.codersact.smsblock.db.CommonDbMethod;
import com.codersact.smsblock.model.SmsData;

import java.util.ArrayList;

import activity.masum.com.smsblock.R;

public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.ViewHolder>{
    private ArrayList<SmsData> mDataset = new ArrayList<>();
    private Context context;
    private BlacklistView blacklistView;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextDesc;
        public Button btnDelete;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.person_name);
            btnDelete = (Button) v.findViewById(R.id.btnDelete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BlackListAdapter(ArrayList<SmsData> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BlackListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_black_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.mTextView.setText("" + mDataset.get(position).getSmsAddress());
        holder.btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Toast.makeText(context, mDataset.get(position).getSmsAddress()  +  " Position " + position, Toast.LENGTH_SHORT).show();
                deleteItem(position);
                doButtonOneClickActions();
            }
        });

    }

    public void deleteItem(int position) {
        CommonDbMethod commonDbMethod = new CommonDbMethod(context);
        commonDbMethod.deleteSms(mDataset.get(position).getSmsAddress(), "SMS_BlackList");
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void doButtonOneClickActions() {
        if(mOnDataChangeListener != null){
            mOnDataChangeListener.onDataChanged(mDataset.size());
        }
    }

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener{
        void onDataChanged(int size);
    }

}