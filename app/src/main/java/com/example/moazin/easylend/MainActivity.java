package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Fragment exchangeFragment;
    private DrawerLayout drawerLayout;
    private SharedPreferences sharedPreferences;
    RequestQueue queue;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting up the toolbar as the top bar
        Toolbar customToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(customToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // setup shared preferences
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "no_user");
        if(token.equals("no_user")){
            Intent launchLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(launchLoginActivity);
            finish();
        }

        // TODO: Make Landscape available in this app

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch(menuItem.getItemId()){
                    case R.id.transaction_button:
                        Fragment transactionFragment = new TransactionFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, transactionFragment).commit();
                        break;
                    case R.id.exchange_button:
                        exchangeFragment = new ExchangeFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, exchangeFragment).commit();
                        break;
                    case R.id.logout_button:
                        sharedPreferences.edit().clear().commit();
                        Intent redirect_login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(redirect_login);
                        finish();
                        break;

                }
                return true;
            }
        });

        // writing the user's name in the nav drawer header
        View headerView = navigationView.getHeaderView(0);
        TextView user_name_display = ((View) headerView).findViewById(R.id.user_name_display);
        String full_name = sharedPreferences.getString("first_name", "no_first_name") + " " + sharedPreferences.getString("last_name", "no_last_name");
        user_name_display.setText(full_name);

        // starting the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        exchangeFragment = new ExchangeFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, exchangeFragment).commit();

        Thread ab = new Thread(new ExchangeSynchronizer(this));
        ab.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
