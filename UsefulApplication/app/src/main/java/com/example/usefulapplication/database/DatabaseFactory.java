package com.example.usefulapplication.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseFactory {

    private static AppDatabase database;

    public static void createAppDatabase(Context context){
        if(database == null){
            database = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
        }
    }

    public static AppDatabase getAppDatabase() throws NullPointerException{
        if(database == null){
            throw new NullPointerException("Database has not been initialised");
        }else{
            return database;
        }
    }
}
