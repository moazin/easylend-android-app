package com.example.moazin.easylend.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.R;
import com.example.moazin.easylend.fragments.NotificationFragment;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public NotificationAdapter mNotificationAdapter;
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public ConstraintLayout mNotificationProgressConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public NotificationBroadcastReceiver(NotificationFragment fragment){
        mNotificationAdapter = fragment.mNotificationAdapter;
        mRecyclerView = fragment.mRecyclerView;
        mSwipeRefreshLayout = fragment.mSwipeRefreshLayout;
        mNotificationProgressConstraintLayout = fragment.mNotificationProgressConstraintLayout;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        loadData();
    }

    public void loadData(){
        String protocol = mContext.getString(R.string.protocol);
        String base_url = mContext.getString(R.string.base_url_emulator);
        String port = mContext.getString(R.string.port);
        String url = protocol + "://" + base_url + port + "/transactions/unverifiedtransactions";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mNotificationAdapter.setData(response);
                        if(mNotificationProgressConstraintLayout.getVisibility() == View.VISIBLE){
                            mNotificationProgressConstraintLayout.setVisibility(View.GONE);
                        }
                        if(mSwipeRefreshLayout.getVisibility() == View.GONE){
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
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
                String token = mSharedPreferences.getString("token", "no_token");
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(jsonObjectRequest);
    }
}
