package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExchangeSynchronizer implements Runnable {
    Context mContext;
    private RequestQueue queue;
    public ExchangeSynchronizer(Context context){
        mContext = context;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Log.e("THREAD:", "From thread, KiDDO!");
        // Send a request to server asking for exchange data
        for(int i=0; i < 10; i++){
            String base_url = mContext.getString(R.string.base_url_emulator);
            String url = "http://" + base_url + ":8000/transactions/myexchangewitheveryone";
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
                                intent.setAction("moazin.khatri");
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
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
            queue = Volley.newRequestQueue(mContext);
            queue.add(jsonArrayRequest);
            SystemClock.sleep(10000);
        }
    }
}

