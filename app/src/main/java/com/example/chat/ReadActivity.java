package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_read);
    }
}