package com.example.moazin.easylend.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
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
    ProfileAdapter mProfileAdapter;
    ProgressBar mProfileLoadingBar;
    RecyclerView mRecyclerView;
    int mPersonId;
    Context mContext;
    RequestQueue mQueue;
    Double mExchangeAmount;
    TextView mExchangeView;
    ImageView mImageView;

    public DialogBroadcastReceiver(PersonProfileActivity activity){
        mProfileAdapter = activity.mProfileAdapter;
        mProfileLoadingBar = activity.mProfileLoadingBar;
        mRecyclerView = activity.mRecyclerView;
        mPersonId = activity.mId;
        mExchangeView = activity.mExchangeView;
        mImageView = activity.mImageView;
        mExchangeAmount = activity.mExchangeAmount;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Bundle extras = intent.getExtras();
        if(extras != null) {
            mExchangeAmount = mExchangeAmount + extras.getDouble("exchange_amount");
            renderBadge(context);
            loadData();
        }
    }


    public void renderBadge(Context context){
        if(mExchangeAmount > 0){
            mImageView.setImageDrawable(context.getDrawable(R.drawable.down_arrow_green));
        } else if(mExchangeAmount < 0) {
            mImageView.setImageDrawable(context.getDrawable(R.drawable.up_arrow_red));
        } else {
            mImageView.setImageDrawable(context.getDrawable(R.drawable.face));
        }
        mExchangeView.setText(String.valueOf(mExchangeAmount));
    }

    public void loadData(){
        String base_url = mContext.getString(R.string.base_url_emulator);
        String port = mContext.getString(R.string.port);
        String protocol = mContext.getString(R.string.protocol);
        String url = protocol + "://" + base_url + ":" + port + "/transactions/mytransactionswithsomeone?id=" + mPersonId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mProfileAdapter.setData(response);
                        mProfileLoadingBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mQueue.stop();
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
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        mQueue = Volley.newRequestQueue(mContext);
        mQueue.add(jsonArrayRequest);
    }
}
