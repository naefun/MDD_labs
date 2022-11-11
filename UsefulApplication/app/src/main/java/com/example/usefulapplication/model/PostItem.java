package com.example.usefulapplication.model;

import java.time.LocalDate;
import java.util.Date;

public class PostItem {

    private LocalDate date;
    private String location;
    private String caption;
    private String songTitle;
    private String songArtist;

    private int clickCount;

    public PostItem(LocalDate date, String location, String caption, String songTitle, String songArtist) {
        this.date = date;
        this.location = location;
        this.caption = caption;
        this.songTitle = songTitle;
        this.songArtist = songArtist;

        clickCount = 0;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public void incrementClickCount(){
        this.clickCount++;
    }

    public String simpleDisplay() {
        String clickMsg = "";
        if (clickCount > 0) {
            clickMsg = ". Clicked: " + clickCount + " times";
        }
        return songTitle + " " + songArtist + clickMsg;
    }
}
