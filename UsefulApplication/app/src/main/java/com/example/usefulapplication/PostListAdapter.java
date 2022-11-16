package com.example.usefulapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.fragment.PostFragment;
import com.example.usefulapplication.model.Track;
import com.example.usefulapplication.model.UserPost;
import com.example.usefulapplication.service.DeezerController;
import com.example.usefulapplication.service.TrackRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

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
    private Fragment parentFragment;

    public PostListAdapter(Context context, List<UserPost> posts, LifecycleOwner viewLifecycleOwner, Fragment fragment) {
        this.inflater = LayoutInflater.from(context);
        this.posts = posts;
        this.retrofit = new Retrofit.Builder().baseUrl("https://deezerdevs-deezer.p.rapidapi.com").addConverterFactory(GsonConverterFactory.create()).build();
        this.service = retrofit.create(DeezerController.class);
        this.trackRepository = new TrackRepository(service);
        this.lifecycleOwner = viewLifecycleOwner;
        this.parentFragment = fragment;
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

//        holder.postDateView.setText(post.getDate());
        holder.postLocationView.setText(post.getLocation());
        holder.postCaptionView.setText(post.getCaption());

        holder.postMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("NB", "onClick: Post menu button clicked");
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.post_popup_menu, popupMenu.getMenu());
                popupMenu.getMenu().findItem(R.id.menu_edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.i("NB", "onMenuItemClick: " + menuItem.getTitle());
                        Bundle bundle = new Bundle();
                        bundle.putLong("postId", post.getUid());
                        bundle.putString("trackId", post.getSongId());
                        bundle.putString("caption", post.getCaption());
                        bundle.putString("location", post.getLocation());
                        NavHostFragment.findNavController(parentFragment).navigate(R.id.action_postFragment_to_editPost, bundle);
                        return true;
                    }
                });

                popupMenu.getMenu().findItem(R.id.menu_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AppDatabase appDatabase = DatabaseFactory.getAppDatabase(view.getContext());
                        UserPostDao userPostDao = appDatabase.userPostDao();
                        userPostDao.deletePost(post);
                        posts.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), posts.size());
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        MutableLiveData<Track> track = new MutableLiveData<>();
        requestTrack(post.getSongId(), track);

        final Observer<Track> trackObserver = new Observer<Track>() {
            @Override
            public void onChanged(@Nullable Track track) {
                try{
                    holder.postTitleView.setText(track.getTitle());
                    holder.postArtistView.setText(track.getArtist().getName());
                    Log.i("NB", "onChanged: " + track.getAlbum().getCover_medium());
                    Picasso.get().load(track.getAlbum().getCover_medium()).into(holder.postTrackImageView);
                    holder.postTrackImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        track.observe(lifecycleOwner, trackObserver);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void requestTrack(String trackId, MutableLiveData<Track> track) {
        if(track.getValue() == null){
            Call<Track> trackCall = trackRepository.getTrack(trackId);
            trackCall.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
//                    Log.i(this.getClass().getSimpleName(), response.body().);
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
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public final TextView postDateView;
        public final TextView postLocationView;
        public final TextView postCaptionView;
        public final ImageView postTrackImageView;
        public final TextView postTitleView;
        public final TextView postArtistView;
        public final Button postMenuButton;
        final PostListAdapter adapter;

        public PostViewHolder(@NonNull View itemView, PostListAdapter adapter) {
            super(itemView);
            this.postDateView = itemView.findViewById(R.id.post_date);
            this.postLocationView = itemView.findViewById(R.id.post_location);
            this.postCaptionView = itemView.findViewById(R.id.post_caption);
            this.postTitleView = itemView.findViewById(R.id.post_song_title);
            this.postArtistView = itemView.findViewById(R.id.post_song_artist);
            this.postTrackImageView = itemView.findViewById(R.id.post_song_image);
            this.postMenuButton = itemView.findViewById(R.id.post_menu_button);
            this.adapter = adapter;
        }
    }
}