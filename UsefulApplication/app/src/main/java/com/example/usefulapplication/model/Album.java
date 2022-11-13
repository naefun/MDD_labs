package com.example.usefulapplication.model;

import java.io.Serializable;

public class Album implements Serializable {
    private String title;
    private String cover_medium;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_medium() {
        return cover_medium;
    }

    public void setCover_medium(String cover_medium) {
        this.cover_medium = cover_medium;
    }
}
