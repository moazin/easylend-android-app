package com.example.moazin.easylend.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.R;
import com.example.moazin.easylend.utilities.ExchangeAdapter;
import com.example.moazin.easylend.utilities.ExchangeBroadcastReceiver;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {

    public ExchangeAdapter mExchangeAdapter;
    public ConstraintLayout mProgressBarConstraintLayout;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public String mQueryString;
    public Context mContext;

    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();

        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        // get arguments if they exist
        Bundle args = getArguments();
        if(args != null) {
            mQueryString = args.getString("search_query");
        } else {
            mQueryString = "";
        }

        mExchangeAdapter = new ExchangeAdapter();
        mProgressBarConstraintLayout = view.findViewById(R.id.progress_exchange_constraintlayout);
        mSwipeRefreshLayout = view.findViewById(R.id.exchange_swiperefresh_layout);

        // register the broadcast receiver
        IntentFilter intentFilter = new IntentFilter(getString(R.string.exchange_data_ready));
        LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(new ExchangeBroadcastReceiver
                        (this), intentFilter);

        // setup loading methods
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.exchange_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mExchangeAdapter);
        recyclerView.setHasFixedSize(true);
        Intent do_once = new Intent();
        do_once.setAction(getString(R.string.exchange_data_ready));
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(do_once);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshData();
    }

    public void RefreshData(){
        String base_url = getString(R.string.base_url_emulator);
        String port = getString(R.string.port);
        String protocol = getString(R.string.protocol);
        String url = protocol + "://" + base_url +  port + "/transactions/myexchangewitheveryone";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        File directory = mContext.getCacheDir();
                        File file = new File(directory, "exchange_data.json");
                        try {
                            FileWriter fileWriter = new FileWriter(file);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(response.toString());
                            bufferedWriter.close();
                            fileWriter.close();
                            Intent intent = new Intent();
                            intent.setAction(getString(R.string.exchange_data_ready));
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } catch (IOException ioe) {
                            // TODO: Proper error handling
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "no_token");
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue queue;
        queue = Volley.newRequestQueue(mContext);
        queue.add(jsonArrayRequest);
    }

}
