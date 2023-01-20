package com.example.usefulapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 1. add post image to bundle arguments when moving between select location map
    //TODO
    // 2. clicking save when a location is already chosen and not choosing a new location causes the location to be lost - fix
    // 3. Add a cancel button to create a post page
    // 4. Make create a post page go to main page after post has been created
    // 5. Add filter based on post locations
    // 6. change position of buttons on select a map page OR move navigation button
}