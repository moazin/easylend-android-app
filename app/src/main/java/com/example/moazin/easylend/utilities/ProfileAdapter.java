package com.example.moazin.easylend.utilities;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moazin.easylend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private JSONArray mDataset;

    public ProfileAdapter(){
        // empty constructor
    }

    @Override
    public int getItemCount() {
        if(mDataset == null) return 0;
        return mDataset.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public  ImageView up_down_badge;
        public TextView dateTextView;
        public TextView exchangeTextView;
        public MyViewHolder(View view){
            super(view);
            up_down_badge = view.findViewById(R.id.exchange_arrow_badge);
            dateTextView = view.findViewById(R.id.exchange_date_profile);
            exchangeTextView = view.findViewById(R.id.exchange_amount_profile);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.profile_recycler_layout, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(constraintLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            JSONObject transaction = mDataset.getJSONObject(i);
            // decide whether the persion viewing is the from or the two
            int owner_id = myViewHolder.up_down_badge.getContext()
                    .getSharedPreferences("user_info", Context.MODE_PRIVATE)
                    .getInt("id", 0);
            boolean from_is_owner = owner_id == transaction.getJSONObject("from_user").getInt("id");
            String datetimeString = transaction.getString("date").substring(0, 10);
            myViewHolder.exchangeTextView.setText(transaction.getDouble("amount") + "");
            myViewHolder.dateTextView.setText(datetimeString);
            if(from_is_owner){
                myViewHolder.exchangeTextView
                        .setTextColor(myViewHolder.exchangeTextView.getContext().getResources()
                        .getColor(R.color.colorPrimary));
                myViewHolder.up_down_badge.setImageDrawable
                        (myViewHolder.up_down_badge.getContext()
                        .getDrawable(R.drawable.up_arrow_green));
            } else {
                myViewHolder.exchangeTextView
                        .setTextColor(myViewHolder.exchangeTextView.getContext().getResources()
                                .getColor(R.color.colorAccent));
                myViewHolder.up_down_badge.setImageDrawable
                        (myViewHolder.up_down_badge.getContext()
                                .getDrawable(R.drawable.down_arrow_red));
            }
        } catch (JSONException json){
            json.printStackTrace();
        }

    }

    public void setData(JSONArray array){
        mDataset = array;
        notifyDataSetChanged();
    }
}
