package com.example.mdad_useful_app.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "caption")
    public String caption;
}
