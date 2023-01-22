package com.example.usefulapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.model.UserPost;

@Database(entities = {UserPost.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserPostDao userPostDao();
}
