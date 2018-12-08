package com.example.moazin.easylend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExchangeBroadcastReceiver extends BroadcastReceiver {
    private ExchangeAdapter exchangeAdapter;

    public ExchangeBroadcastReceiver(ExchangeAdapter adapter){
        exchangeAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Hey dude just received a signal", Toast.LENGTH_LONG).show();
        FileInputStream file = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            // TODO: DO proper handling here
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
            // setting up the Recycler View
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
