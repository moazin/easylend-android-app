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
}
