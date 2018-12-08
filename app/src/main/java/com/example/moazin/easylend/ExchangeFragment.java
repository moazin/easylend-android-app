package com.example.moazin.easylend;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {
    ExchangeAdapter exchangeAdapter;
    Thread synchronizer_thread;
    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        // register the broadcast receiver
        IntentFilter intentFilter = new IntentFilter("moazin.khatri");
        // Inflate the layout for this fragment
        exchangeAdapter = new ExchangeAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.exchange_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(exchangeAdapter);
        recyclerView.setHasFixedSize(true);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(new ExchangeBroadcastReceiver(exchangeAdapter), intentFilter);
        Intent do_once=  new Intent();
        do_once.setAction("moazin.khatri");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(do_once);

        synchronizer_thread = new Thread(new ExchangeSynchronizer(getContext()));
        synchronizer_thread.start();
        return view;
    }

}
