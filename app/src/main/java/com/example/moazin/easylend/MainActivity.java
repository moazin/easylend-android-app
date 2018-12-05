package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private SharedPreferences sharedPreferences;

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
        String token = sharedPreferences.getString("token", "no_user");
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
                switch(menuItem.getItemId()){
                    case R.id.logout_button:
                        sharedPreferences.edit().clear().commit();
                        Intent redirect_login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(redirect_login);
                        finish();

                }
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView user_name_display = ((View) headerView).findViewById(R.id.user_name_display);
        String full_name = sharedPreferences.getString("first_name", "no_first_name") + " " + sharedPreferences.getString("last_name", "no_last_name");
        user_name_display.setText(full_name);
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
