package com.example.moazin.easylend;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.moazin.easylend.R;
import com.example.moazin.easylend.fragments.ExchangeFragment;

public class ExchangeSearchActivity extends AppCompatActivity {

    Fragment exchangeFragment = new ExchangeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(query);
            FragmentManager fragmentManager = getSupportFragmentManager();
            // set the arguments
            Bundle args = new Bundle();
            args.putString("search_query", query);
            exchangeFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container_search, exchangeFragment).commit();
        }
    }
}
