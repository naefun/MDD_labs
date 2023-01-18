package com.example.usefulapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.model.UserPost;

@Database(entities = {UserPost.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserPostDao userPostDao();
}

//TODO
// 1. add post image to bundle arguments when moving between select location map
// 2. clicking save when a location is already chosen and not choosing a new location causes the location to be lost - fix
// 3. Add a cancel button to create a post page
// 4. Make create a post page go to main page after post has been created
// 5. Add filter based on post locations
// 6. change position of buttons on select a map page OR move navigation button
