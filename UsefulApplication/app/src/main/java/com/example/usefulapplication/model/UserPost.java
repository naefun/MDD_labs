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
    @ColumnInfo(name = "location_latitude")
    private String locationLatitude;
    @ColumnInfo(name = "location_longitude")
    private String locationLongitude;
    @ColumnInfo(name = "caption")
    private String caption;
    @ColumnInfo(name = "song_id")
    private String songId;
    @ColumnInfo(name = "image_uri")
    private String imageUri;

    public UserPost(String location, String caption, String songId, String date, String locationLatitude, String locationLongitude, String imageUri) {
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.caption = caption;
        this.songId = songId;
        this.date = date;
        this.imageUri = imageUri;
    }

    @Ignore
    public UserPost(String location, String caption, String songId, String date, Long uid, String locationLatitude, String locationLongitude, String imageUri) {
        this.uid = uid;
        this.location = location;
        this.caption = caption;
        this.songId = songId;
        this.date = date;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
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
