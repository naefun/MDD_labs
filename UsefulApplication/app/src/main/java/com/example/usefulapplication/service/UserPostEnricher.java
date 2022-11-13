package com.example.usefulapplication.service;

import android.util.Log;

import com.example.usefulapplication.model.Track;
import com.example.usefulapplication.model.UserPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPostEnricher {

    private final Retrofit retrofit;
    private final DeezerController service;
    private final TrackRepository trackRepository;

    public UserPostEnricher() {
        this.retrofit = new Retrofit.Builder().baseUrl("https://deezerdevs-deezer.p.rapidapi.com").addConverterFactory(GsonConverterFactory.create()).build();
        this.service = retrofit.create(DeezerController.class);
        this.trackRepository = new TrackRepository(service);
    }

    public void requestTrack(String trackId){
        Call<Track> trackCall = trackRepository.getTrack(trackId);
        trackCall.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    Log.i("NB", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                // show error message to user
                Log.i("NB", "Error: " + t.toString());
            }
        });
    }
}
