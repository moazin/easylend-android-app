package com.example.moazin.easylend.fragments;


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


    ExchangeAdapter exchangeAdapter;
    Thread synchronizer_thread;
    ConstraintLayout progress_bar_constraint;
    SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue queue;
    String queryString;

    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        // get arguments if they exist
        try {
            queryString = getArguments().getString("search_query");
        } catch(NullPointerException npe){
            queryString = "";
        }
        exchangeAdapter = new ExchangeAdapter();
        progress_bar_constraint = view.findViewById(R.id.progress_exchange_constraintlayout);
        swipeRefreshLayout = view.findViewById(R.id.exchange_swiperefresh_layout);

        // register the broadcast receiver
        IntentFilter intentFilter = new IntentFilter(getString(R.string.exchange_data_ready));
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(new ExchangeBroadcastReceiver
                        (exchangeAdapter,
                         progress_bar_constraint, swipeRefreshLayout, queryString), intentFilter);


        // setup loading methods
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.exchange_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(exchangeAdapter);
        recyclerView.setHasFixedSize(true);
        Intent do_once = new Intent();
        do_once.setAction(getString(R.string.exchange_data_ready));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(do_once);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshData();
    }

    public void RefreshData(){
        String base_url = getString(R.string.base_url_emulator);
        String url = "http://" + base_url + ":8000/transactions/myexchangewitheveryone";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        File directory = getContext().getCacheDir();
                        File file = new File(directory, "exchange_data.json");
                        try {
                            FileWriter fileWriter = new FileWriter(file);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(response.toString());
                            bufferedWriter.close();
                            fileWriter.close();
                            Intent intent = new Intent();
                            intent.setAction(getString(R.string.exchange_data_ready));
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                        } catch (IOException ioe) {
                            // TODO: Proper error handling
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
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
        queue = Volley.newRequestQueue(getContext());
        queue.add(jsonArrayRequest);
    }

}
