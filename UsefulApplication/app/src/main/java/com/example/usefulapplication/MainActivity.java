package com.example.usefulapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_home_24);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        PostListAdapter.stopSong();
        Log.i("paused", "onPause: app paused");
    }

    // 1. add post image to bundle arguments when moving between select location map
    // 2. clicking save when a location is already chosen and not choosing a new location causes the location to be lost - fix
    // 3. Add a cancel button to create a post page
    // 4. Make create a post page go to main page after post has been created
    // 5. Add filter based on post locations
    //TODO
    // 6. add the ability to select a song from somewhere (list of pre selected songs or create search functionality)
    // 7. change position of buttons on select a map page OR move navigation button
}