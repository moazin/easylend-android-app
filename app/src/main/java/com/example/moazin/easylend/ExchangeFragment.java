package com.example.moazin.easylend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {


    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        // setting up data for populating recycler view (at least for testing)
        JSONObject moazin = new JSONObject();
        JSONObject wahab = new JSONObject();
        try {
            moazin.put("first_name", "Moazin");
            moazin.put("last_name",  "Khatri");
            moazin.put("exchange", 50);
            wahab.put("first_name", "Abdul");
            wahab.put("last_name",  "Wahab");
            wahab.put("exchange", -100);
        } catch(JSONException jsonException){
            // TODO: Properly handle this shit
        }
        JSONArray array = new JSONArray();
        array.put(moazin);
        array.put(wahab);
        // setting up the Recycler View
        RecyclerView recyclerView = view.findViewById(R.id.exchange_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ExchangeAdapter exchangeAdapter = new ExchangeAdapter(array);
        recyclerView.setAdapter(exchangeAdapter);
        return view;
    }

}
