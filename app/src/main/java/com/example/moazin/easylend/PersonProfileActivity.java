package com.example.moazin.easylend;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PersonProfileActivity extends AppCompatActivity {
    private String first_name;
    private String last_name;
    private int id;
    private double exchange;
    private String full_name;

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

        if(exchange > 0){
            imageView.setImageDrawable(getDrawable(R.drawable.ic_arrow_downward_black_24dp));
        } else if(exchange < 0) {
            imageView.setImageDrawable(getDrawable(R.drawable.up_arrow));
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.face));
        }
        exchangeView.setText(exchange + "");
    }
}
