package com.example.moazin.easylend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExchangeBroadcastReceiver extends BroadcastReceiver {
    private ExchangeAdapter exchangeAdapter;
    private ConstraintLayout exchange_progressbar_constraint;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ExchangeBroadcastReceiver(ExchangeAdapter adapter, ConstraintLayout constraintLayout, SwipeRefreshLayout swipeLayout){
        exchangeAdapter = adapter;
        exchange_progressbar_constraint = constraintLayout;
        swipeRefreshLayout = swipeLayout;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RECEIVE BROADCAST: ", "Received a broadcast");
        FileInputStream file = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            file = new FileInputStream(new File(context.getCacheDir(), "exchange_data.json"));
            inputStreamReader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }
            String ret = stringBuilder.toString();
            JSONArray array = new JSONArray(ret);
            exchangeAdapter.setData(array);
            if(exchange_progressbar_constraint.getVisibility() == View.VISIBLE){
                exchange_progressbar_constraint.setVisibility(View.GONE);
            }
            if(swipeRefreshLayout.getVisibility() == View.GONE){
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
            swipeRefreshLayout.setRefreshing(false);
        } catch (IOException|JSONException ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(file != null) file.close();
            } catch (IOException ioe){
                Toast.makeText(context, ioe.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
