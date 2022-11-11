package com.example.usefulapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usefulapplication.model.PostItem;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {
    private List<PostItem> posts;
    private LayoutInflater inflater;

    public PostListAdapter(Context context, List<PostItem> posts) {
        this.inflater = LayoutInflater.from(context);
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostListAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.postlist_item, parent, false);
        return new PostViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.PostViewHolder holder, int position) {
        PostItem post = posts.get(position);
        holder.postTitleView.setText(post.getSongTitle());
        holder.postArtistView.setText(post.getSongArtist());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
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