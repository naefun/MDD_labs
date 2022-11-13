package com.example.usefulapplication.service;

import com.example.usefulapplication.model.Track;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DeezerController {

    @Headers({
            "X-RapidAPI-Key: 3c9673f07fmshc1062c6d2e39435p1779a5jsn97fdf1fcdf61",
            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
    })
    @GET("/track/{id}")
    Call<Track> getTrack(@Path("id") String trackId);
}
