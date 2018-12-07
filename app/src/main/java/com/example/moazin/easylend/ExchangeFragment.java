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


    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        // Read from cache dir
        try {
            FileInputStream file = new FileInputStream(new File(getContext().getCacheDir(), "exchange_data.json"));
            InputStreamReader inputStreamReader = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }
            bufferedReader.close();
            String ret = stringBuilder.toString();
            JSONArray array = new JSONArray(ret);
            // setting up the Recycler View
            RecyclerView recyclerView = view.findViewById(R.id.exchange_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ExchangeAdapter exchangeAdapter = new ExchangeAdapter(array);
            recyclerView.setAdapter(exchangeAdapter);
        } catch (FileNotFoundException fnfe){
            // TODO: Proper Error Handling
        } catch (IOException ioe) {
            // TODO: Proper Error Handling
        } catch (JSONException jsone) {
            // TODO: Proper Error Handling
        }
        return view;
    }

}
