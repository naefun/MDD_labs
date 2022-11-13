package com.example.usefulapplication.service;

import com.example.usefulapplication.model.Track;

import retrofit2.Call;

public class TrackRepository {
    private DeezerController deezerController;

    public TrackRepository(DeezerController deezerController) {
        this.deezerController = deezerController;
    }

    public Call<Track> getTrack(String trackId){
        return deezerController.getTrack(trackId);
    }
}
