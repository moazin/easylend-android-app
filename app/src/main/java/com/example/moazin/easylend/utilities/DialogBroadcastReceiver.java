package com.example.moazin.easylend.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.PersonProfileActivity;
import com.example.moazin.easylend.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class DialogBroadcastReceiver extends BroadcastReceiver {
    ProfileAdapter profileAdapter;
    ProgressBar profileLoadingBar;
    RecyclerView recyclerView;
    int someones_id;
    Context mContext;
    RequestQueue queue;
    Double exchange;
    TextView exchangeView;
    ImageView imageView;

    public DialogBroadcastReceiver(PersonProfileActivity activity){
        profileAdapter = activity.profileAdapter;
        profileLoadingBar = activity.profileLoadingBar;
        recyclerView = activity.recyclerView;
        someones_id = activity.my_id;
        exchangeView = activity.exchangeView;
        imageView = activity.imageView;
        exchange = activity.exchange;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        exchange = exchange + intent.getExtras().getDouble("exchange_amount");
        renderBadge(context);
        loadData();
    }


    public void renderBadge(Context context){
        if(exchange > 0){
            imageView.setImageDrawable(context.getDrawable(R.drawable.down_arrow_green));
        } else if(exchange < 0) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.up_arrow_red));
        } else {
            imageView.setImageDrawable(context.getDrawable(R.drawable.face));
        }
        exchangeView.setText(exchange + "");
    }

    public void loadData(){
        String base_url = mContext.getString(R.string.base_url_emulator);
        String url = "https://" + base_url + "/transactions/mytransactionswithsomeone?id=" + someones_id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        profileAdapter.setData(response);
                        profileLoadingBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        queue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext
                        .getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "no_token");
                int my_id = sharedPreferences.getInt("id", 0);
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        queue = Volley.newRequestQueue(mContext);
        queue.add(jsonArrayRequest);
    }
}
