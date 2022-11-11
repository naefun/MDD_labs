package com.example.usefulapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.usefulapplication.databinding.ActivityMainBinding;
import com.example.usefulapplication.fragment.PostFragment;
import com.example.usefulapplication.model.PostItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}