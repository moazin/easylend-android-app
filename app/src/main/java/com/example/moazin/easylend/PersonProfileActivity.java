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

    public Double mExchangeAmount;
    public RecyclerView mRecyclerView;
    public ProfileAdapter mProfileAdapter;
    public  ProgressBar mProfileLoadingBar;
    public TextView mExchangeView;
    public ImageView mImageView;
    public String mAuthToken;
    public int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String mFirstName;
        String mLastName;
        String mFullName;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);
        Intent intent = getIntent();
        mFirstName = intent.getStringExtra("first_name");
        mLastName = intent.getStringExtra("last_name");
        mFullName = mFirstName + " " + mLastName;
        mId = intent.getIntExtra("id", 0);
        mExchangeAmount = intent.getDoubleExtra("exchange", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(mFullName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mExchangeView = findViewById(R.id.exchange_box);
        mImageView = findViewById(R.id.exchange_arrow);
        mProfileLoadingBar = findViewById(R.id.profile_load_bar);

        mRecyclerView = findViewById(R.id.profile_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProfileAdapter = new ProfileAdapter();
        mRecyclerView.setAdapter(mProfileAdapter);

        IntentFilter intentFilter = new IntentFilter(getString(R.string.exchange_dialog_closed));
        LocalBroadcastManager.getInstance(this).registerReceiver(new DialogBroadcastReceiver(
                this
        ), intentFilter);

        Intent someintent = new Intent(getString(R.string.exchange_dialog_closed));
        someintent.putExtra("exchange_amount", 0.0);
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
            mAuthToken = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("token", "no_token");
            int FromId = getSharedPreferences("user_info", MODE_PRIVATE).getInt("id", 0);
            args.putString("token", mAuthToken);
            args.putInt("to_id", mId);
            args.putInt("from_id", FromId);
            newTransactionDialogFragment.setArguments(args);
            newTransactionDialogFragment.show(getSupportFragmentManager(), "DIALOG:");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
