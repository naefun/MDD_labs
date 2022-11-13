package com.example.usefulapplication.model;

import java.io.Serializable;

public class Artist implements Serializable {
    private String name;
    private String pictureMedium;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureMedium() {
        return pictureMedium;
    }

    public void setPictureMedium(String pictureMedium) {
        this.pictureMedium = pictureMedium;
    }
}
