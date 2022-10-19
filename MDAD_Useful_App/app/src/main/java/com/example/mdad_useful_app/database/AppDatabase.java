package com.example.mdad_useful_app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mdad_useful_app.dao.PostDao;
import com.example.mdad_useful_app.entity.Post;

@Database(entities = {Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao postDao();
}
