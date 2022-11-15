package com.example.usefulapplication.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseFactory {

    private static AppDatabase database;

    public static AppDatabase getAppDatabase(Context context){
        if(database == null){
            database = Room.databaseBuilder(context, AppDatabase.class, "app-database").fallbackToDestructiveMigration().build();
        }
        return database;
    }
}
