package com.example.usefulapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class UserPost {

    @PrimaryKey(autoGenerate = true)
    public Long uid;

    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "caption")
    private String caption;
    @ColumnInfo(name = "song_id")
    private String songId;

    public UserPost(String location, String caption, String songId, String date) {
        this.location = location;
        this.caption = caption;
        this.songId = songId;
        this.date = date;
    }

    @Ignore
    public UserPost(String location, String caption, String songId, String date, Long uid) {
        this.uid = uid;
        this.location = location;
        this.caption = caption;
        this.songId = songId;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getUid() {
        return uid;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
