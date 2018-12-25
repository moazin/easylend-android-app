package com.example.moazin.easylend;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.fragments.ExchangeFragment;
import com.example.moazin.easylend.fragments.NotificationFragment;
import com.example.moazin.easylend.fragments.TransactionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // TODO: Make Landscape available in this app

    // setting up these global variables so they can be used at different places
    private Fragment mExchangeFragment;
    private Fragment mTransactionFragment;
    private Fragment mNotificationFragment;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting up the toolbar as the top bar (Basically sets the menu button)
        Toolbar customToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(customToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        // setup shared preferences (sharedPreferences are like key value storage)
        // we check if a token is set (if the user is logged in, it would be set)
        mSharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String token = mSharedPreferences.getString("token", "no_user");
        if((token != null) && (token.equals("no_user"))){
            Intent launchLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(launchLoginActivity);
            finish();
        }

        // we initialize all fragments once, so they get only created once not on every tap on nav drawer
        // rather we can hook into onPause and onResume to do our things
        mExchangeFragment = new ExchangeFragment();
        mTransactionFragment = new TransactionFragment();
        mNotificationFragment = new NotificationFragment();

        // grab the drawer layout so we can close it on triggers
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch(menuItem.getItemId()){
                    case R.id.transaction_button:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mTransactionFragment).commit();
                        break;
                    case R.id.exchange_button:
                        mExchangeFragment = new ExchangeFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mExchangeFragment).commit();
                        break;
                    case R.id.notification_button:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mNotificationFragment).commit();
                        break;
                    case R.id.logout_button:
                        deleteTokens();
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
        String full_name = mSharedPreferences.getString("first_name", "no_first_name") + " " +
                mSharedPreferences.getString("last_name", "no_last_name");
        user_name_display.setText(full_name);

        // starting the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mExchangeFragment).commit();

        // some firebase debugging stuff
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    Log.e("FIREBASE: ", "Unable to get ID");
                } else {
                    String token = task.getResult().getToken();
                    Log.e("FIREBASE: ", token);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_button).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        return true;
    }

    public void deleteTokens(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token_fcm", "no_token");
        String protocol = getString(R.string.protocol);
        String base_url = getString(R.string.base_url_emulator);
        String port = getString(R.string.port);
        String url = protocol + "://" + base_url +":" + port + "/users/deletetokens";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mSharedPreferences.edit().clear().apply();
                        Toast.makeText(MainActivity.this, "Deleted tokens!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = MainActivity.this
                        .getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "no_token");
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}
