package com.example.usefulapplication.model;

import java.io.Serializable;

public class Track implements Serializable {
    private String title;
    private Artist artist;
    private Album album;
    private String preview;

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", artist=" + artist.getName() +
                ", album=" + album.getTitle() +
                ", album cover=" + album.getCover_medium() +
                '}';
    }
}
