package com.example.usefulapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;


@Entity
public class UserPost {

    @PrimaryKey(autoGenerate = true)
    public Long uid;

//    @ColumnInfo(name = "date")
//    private LocalDate date;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "caption")
    private String caption;
    @ColumnInfo(name = "song_title")
    private String songTitle;
    @ColumnInfo(name = "song_artist")
    private String songArtist;

//    public UserPost(LocalDate date, String location, String caption, String songTitle, String songArtist) {
//        this.date = date;
//        this.location = location;
//        this.caption = caption;
//        this.songTitle = songTitle;
//        this.songArtist = songArtist;
//
//        clickCount = 0;
//    }
//
    public UserPost(String location, String caption, String songTitle, String songArtist) {
        this.location = location;
        this.caption = caption;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }

//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }

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

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }
}
