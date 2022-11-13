package com.example.usefulapplication.model;

import java.io.Serializable;

public class Album implements Serializable {
    private String title;
    private String coverMedium;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverMedium() {
        return coverMedium;
    }

    public void setCoverMedium(String coverMedium) {
        this.coverMedium = coverMedium;
    }
}
