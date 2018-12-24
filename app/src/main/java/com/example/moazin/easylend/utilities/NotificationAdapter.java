package com.example.moazin.easylend.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            mPersonName = constraintLayout.findViewById(R.id.person_name);
            mTransactionDate = constraintLayout.findViewById(R.id.transaction_date);
            mTransactionExchange = constraintLayout.findViewById(R.id.transaction_exchange);
            mAcceptButton = constraintLayout.findViewById(R.id.verify_transaction);
            mDeclineButton = constraintLayout.findViewById(R.id.cross_transaction);

            // let's set listener for accept button
            mAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context mContext = mAcceptButton.getContext();
                    String protocol = mContext.getString(R.string.protocol);
                    String base_url = mContext.getString(R.string.base_url_emulator);
                    String port = mContext.getString(R.string.port);
                    try {
                        int pos = getAdapterPosition();
                        int transaction_id = mNotifications.getJSONObject(pos).getInt("id");
                        String url = protocol + "://" + base_url + ":" + port + "/transactions/verifytransaction/" + transaction_id + "/";
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("verified", true);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Intent intent = new Intent();
                                        intent.setAction(mContext.getString(R.string.notification_refresh));
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                String token = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("token", "no_token");
                                params.put("Authorization", "Token " + token);
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        queue.add(jsonObjectRequest);
                    } catch (JSONException jsonException){
                        jsonException.printStackTrace();
                    }
                }
            });
            mDeclineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context mContext = mAcceptButton.getContext();
                    String protocol = mContext.getString(R.string.protocol);
                    String base_url = mContext.getString(R.string.base_url_emulator);
                    String port = mContext.getString(R.string.port);
                    try {
                        int pos = getAdapterPosition();
                        int transaction_id = mNotifications.getJSONObject(pos).getInt("id");
                        String url = protocol + "://" + base_url + ":" + port + "/transactions/deletetransaction/" + transaction_id + "/";
                        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Intent intent = new Intent();
                                        intent.setAction(mContext.getString(R.string.notification_refresh));
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(mContext, "Hello error", Toast.LENGTH_LONG).show();
                                    }
                                }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                String token = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("token", "no_token");
                                params.put("Authorization", "Token " + token);
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        queue.add(stringRequest);
                    } catch (JSONException jsonException){
                        jsonException.printStackTrace();
                    }
                }
            });
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
        try {
            JSONObject jsonObject = mNotifications.getJSONObject(i);
            String first_name = jsonObject.getJSONObject("from_user").getString("first_name");
            String last_name = jsonObject.getJSONObject("from_user").getString("last_name");
            myViewHolder.mPersonName.setText(first_name + " " + last_name);
            myViewHolder.mTransactionExchange.setText(String.valueOf(jsonObject.getDouble("amount")));
            myViewHolder.mTransactionDate.setText(jsonObject.getString("date").substring(0, 10));
        } catch(JSONException jsonException){
            jsonException.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(mNotifications == null) return 0;
        return mNotifications.length();
    }

    public void setData(JSONArray data){
        mNotifications = data;
        this.notifyDataSetChanged();
    }

}
