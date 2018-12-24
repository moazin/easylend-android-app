package com.example.moazin.easylend.utilities;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moazin.easylend.R;

import org.json.JSONArray;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private JSONArray mNotifications;

    public NotificationAdapter(){
        // empty constructor
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout mConstraintLayout;
        private TextView mPersonName;
        private TextView mTransactionDate;
        private TextView mTransactionExchange;
        private ImageButton mAcceptButton;
        private ImageButton mDeclineButton;
        public MyViewHolder(ConstraintLayout constraintLayout){
            super(constraintLayout);
            mConstraintLayout = constraintLayout;
            mTransactionDate = constraintLayout.findViewById(R.id.transaction_date);
            mTransactionExchange = constraintLayout.findViewById(R.id.transaction_exchange);
            mAcceptButton = constraintLayout.findViewById(R.id.verify_transaction);
            mDeclineButton = constraintLayout.findViewById(R.id.cross_transaction);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
        return new MyViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        // to be filled here
    }

    @Override
    public int getItemCount() {
        if(mNotifications == null) return 0;
        return mNotifications.length();
    }

}
