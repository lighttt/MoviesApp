package com.example.trymoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
    }
}
