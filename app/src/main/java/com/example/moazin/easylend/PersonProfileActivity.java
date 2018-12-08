package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.moazin.easylend.fragments.NewTransactionDialogFragment;
import com.example.moazin.easylend.utilities.DialogBroadcastReceiver;
import com.example.moazin.easylend.utilities.ProfileAdapter;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class PersonProfileActivity extends AppCompatActivity {
    private String first_name;
    private String last_name;
    private int id;
    private Double exchange;
    private String full_name;
    private RequestQueue queue;
    RecyclerView recyclerView;
    ProfileAdapter profileAdapter;
    private ProgressBar profileLoadingBar;
    TextView exchangeView;
    ImageView imageView;
    String token;
    int my_id;

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

        exchangeView = findViewById(R.id.exchange_box);
        imageView = findViewById(R.id.exchange_arrow);
        profileLoadingBar = findViewById(R.id.profile_load_bar);

        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileAdapter = new ProfileAdapter();
        recyclerView.setAdapter(profileAdapter);

        IntentFilter intentFilter = new IntentFilter(getString(R.string.exchange_dialog_closed));
        LocalBroadcastManager.getInstance(this).registerReceiver(new DialogBroadcastReceiver(
                profileAdapter,
                profileLoadingBar,
                recyclerView,
                id,
                exchangeView,
                imageView,
                exchange
        ), intentFilter);

        Intent someintent = new Intent(getString(R.string.exchange_dialog_closed));
        someintent.putExtra("exchange_amount", (double) 0.0);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(someintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.new_transaction_button){
            NewTransactionDialogFragment newTransactionDialogFragment = new NewTransactionDialogFragment();
            Bundle args = new Bundle();
            token = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("token", "no_token");
            int my_id = getSharedPreferences("user_info", MODE_PRIVATE).getInt("id", 0);
            args.putString("token", token);
            args.putInt("to_id", id);
            args.putInt("from_id", my_id);
            newTransactionDialogFragment.setArguments(args);
            newTransactionDialogFragment.show(getSupportFragmentManager(), "DIALOG:");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
