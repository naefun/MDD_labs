package com.example.usefulapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DatabaseFactory.createAppDatabase(this.getApplicationContext());
        setContentView(R.layout.activity_main);
    }
}