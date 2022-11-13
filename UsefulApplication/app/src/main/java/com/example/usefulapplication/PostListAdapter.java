package com.example.usefulapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usefulapplication.model.Track;
import com.example.usefulapplication.model.UserPost;
import com.example.usefulapplication.service.DeezerController;
import com.example.usefulapplication.service.TrackRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {
    private List<UserPost> posts;
    private LayoutInflater inflater;

    Retrofit retrofit;
    DeezerController service;
    TrackRepository trackRepository;
    private LifecycleOwner lifecycleOwner;

    public PostListAdapter(Context context, List<UserPost> posts, LifecycleOwner viewLifecycleOwner) {
        this.inflater = LayoutInflater.from(context);
        this.posts = posts;
        this.retrofit = new Retrofit.Builder().baseUrl("https://deezerdevs-deezer.p.rapidapi.com").addConverterFactory(GsonConverterFactory.create()).build();
        this.service = retrofit.create(DeezerController.class);
        this.trackRepository = new TrackRepository(service);
        this.lifecycleOwner = viewLifecycleOwner;
    }

    @NonNull
    @Override
    public PostListAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.postlist_item, parent, false);
        return new PostViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.PostViewHolder holder, int position) {
        UserPost post = posts.get(position);
        MutableLiveData<Track> track = new MutableLiveData<>();
        requestTrack(post.getSongId(), track);

        final Observer<Track> trackObserver = new Observer<Track>() {
            @Override
            public void onChanged(@Nullable Track track) {
                holder.postTitleView.setText(track.getTitle());
                holder.postArtistView.setText(track.getArtist().getName());
            }
        };
        track.observe(lifecycleOwner, trackObserver);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void requestTrack(String trackId, MutableLiveData<Track> track) {
        Call<Track> trackCall = trackRepository.getTrack(trackId);
        trackCall.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    Log.i(this.getClass().getSimpleName(), response.body().toString());
                    track.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                // show error message to user
                Log.i(this.getClass().getSimpleName(), "Error: " + t.toString());
            }
        });

    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public final TextView postTitleView;
        public final TextView postArtistView;
        final PostListAdapter adapter;

        public PostViewHolder(@NonNull View itemView, PostListAdapter adapter) {
            super(itemView);
            this.postTitleView = itemView.findViewById(R.id.post_song_title);
            this.postArtistView = itemView.findViewById(R.id.post_song_artist);
            this.adapter = adapter;
        }
    }
}