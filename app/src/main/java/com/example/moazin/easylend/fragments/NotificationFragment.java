package com.example.moazin.easylend.fragments;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.R;
import com.example.moazin.easylend.utilities.NotificationAdapter;
import com.example.moazin.easylend.utilities.NotificationBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    public NotificationAdapter mNotificationAdapter;
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public ConstraintLayout mNotificationProgressConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        mContext = getContext();
        // grabbing those items
        mNotificationAdapter = new NotificationAdapter();
        mSwipeRefreshLayout = view.findViewById(R.id.notification_swiperefresh_layout);
        mRecyclerView = view.findViewById(R.id.notification_recycler_view);
        mNotificationProgressConstraintLayout = view.findViewById(R.id.progress_notification_constraintlayout);
        mSharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        // setting up the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mNotificationAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // registering the broadcast receiver
        IntentFilter intentFilter = new IntentFilter(getString(R.string.notification_refresh));
        LocalBroadcastManager.getInstance(mContext).registerReceiver(new NotificationBroadcastReceiver(this), intentFilter);

        // setting on refresh listener
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();
        return view;
    }

    public void loadData(){
        Intent intent = new Intent();
        intent.setAction(mContext.getString(R.string.notification_refresh));
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


}
