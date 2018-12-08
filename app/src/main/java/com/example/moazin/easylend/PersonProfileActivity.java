package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.example.moazin.easylend.utilities.ProfileAdapter;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class PersonProfileActivity extends AppCompatActivity {
    private String first_name;
    private String last_name;
    private int id;
    private double exchange;
    private String full_name;
    private RequestQueue queue;
    RecyclerView recyclerView;
    ProfileAdapter profileAdapter;
    private ProgressBar profileLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        first_name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");
        exchange = intent.getDoubleExtra("exchange", 0);
        full_name = first_name + " " + last_name;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(full_name);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView exchangeView = findViewById(R.id.exchange_box);
        ImageView imageView = findViewById(R.id.exchange_arrow);
        profileLoadingBar = findViewById(R.id.profile_load_bar);

        if(exchange > 0){
            imageView.setImageDrawable(getDrawable(R.drawable.down_arrow_green));
        } else if(exchange < 0) {
            imageView.setImageDrawable(getDrawable(R.drawable.up_arrow_red));
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.face));
        }
        exchangeView.setText(exchange + "");

        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileAdapter = new ProfileAdapter();
        recyclerView.setAdapter(profileAdapter);

        String base_url = getString(R.string.base_url_emulator);
        String url = "http://" + base_url + ":8000/transactions/mytransactionswithsomeone?id=" + id;
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
                    Toast.makeText(PersonProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = PersonProfileActivity.this
                        .getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "no_token");
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }
}
