package com.example.mdad_useful_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mdad_useful_app.dao.PostDao;
import com.example.mdad_useful_app.database.AppDatabase;
import com.example.mdad_useful_app.entity.Post;
import com.example.mdad_useful_app.model.PostModel;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private LinearLayout postContainer; // this is always known as it is defined in the xml
    PostModel postModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postContainer = findViewById(R.id.post_container);
        postModel = PostModel.getPostModel(this);
        // this should be populated from the data source (database)
        List<LinearLayout> posts = postModel.getPosts();

        displayPosts(posts, postContainer);
    }

    private void displayPosts(List<LinearLayout> posts, LinearLayout postContainer) {
        for (LinearLayout post : posts) {
            postContainer.addView(post);
        }
    }


    public void addView(View view) {
        postContainer.addView(PostComponent.createComponent(postContainer.getContext()));
        Log.i(this.getClass().getSimpleName(), "View added");
    }

    // this should initiate the post creation (bring up the create post screen)
    public void addCard(View view) {

        // the below implementation is currently temporary
        Post post = new Post();
        post.caption = "This is a new post";
        postModel.insertPost(post);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PostModel.getPostModel(this).closeDb();
        Log.i(this.getClass().getSimpleName(), "App instance destroyed");
    }
}