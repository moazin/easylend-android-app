package com.example.moazin.easylend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent launchLoginActivitiy = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(launchLoginActivitiy);
    }

    @Override
    protected void onResume() {
        // TODO: if login is not done, redirect to login activity
        Intent go_back_to_login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(go_back_to_login);
        super.onResume();
    }
}
