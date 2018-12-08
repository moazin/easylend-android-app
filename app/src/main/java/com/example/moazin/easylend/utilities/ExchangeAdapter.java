package com.example.moazin.easylend.utilities;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moazin.easylend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.MyViewHolder> {
    private JSONArray mDataset;

    // constructor for this adapter
    public ExchangeAdapter(){
        // empty
    }

    public void setData(JSONArray dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }

    // a count returning function
    @Override
    public int getItemCount() {
        if(mDataset == null) return 0;
        return mDataset.length();
    }

    // create a view holder object for our list items, each item is a constraint layout object
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName;
        public TextView personExchange;
        public MyViewHolder(ConstraintLayout constraintLayout){
            super(constraintLayout);
            personName = constraintLayout.findViewById(R.id.person_name);
            personExchange = constraintLayout.findViewById(R.id.person_exchange);
        }
    }

    // a view holder creator that inflates layouts
    @NonNull
    @Override
    public ExchangeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.exchange_recycler_layout, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(constraintLayout);
        return vh;
    }

    // a binder that takes the view holder and inserts data in it
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            JSONObject obj = mDataset.getJSONObject(i);
            String personFirstName = obj.getString("first_name");
            String personLastName = obj.getString("last_name");
            Double personExchange = obj.getDouble("exchange");
            myViewHolder.personName.setText(personFirstName + " " + personLastName);
            myViewHolder.personExchange.setText(personExchange.toString());
            int green = ContextCompat.getColor(myViewHolder.personName.getContext(), R.color.colorPrimaryDark);
            int pink = ContextCompat.getColor(myViewHolder.personName.getContext(), R.color.colorAccent);
            if(personExchange >= 0){
                myViewHolder.personExchange.setTextColor(green);
            } else {
                myViewHolder.personExchange.setTextColor(pink);
            }
        } catch (JSONException jsonException){
            Toast.makeText(myViewHolder.personExchange.getContext(), jsonException.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
