package com.example.mdad_useful_app.model;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.room.Room;

import com.example.mdad_useful_app.PostComponent;
import com.example.mdad_useful_app.dao.PostDao;
import com.example.mdad_useful_app.database.AppDatabase;
import com.example.mdad_useful_app.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class PostModel {
    private AppDatabase db;
    private PostDao postDao;

    private static PostModel postModel;

    private List<LinearLayout> posts;

    private PostModel(Context context) {
        posts = new ArrayList<>();

        db = Room.databaseBuilder(context, AppDatabase.class, "useful-app-db").build();
        postDao = db.postDao();

        populatePosts(context);
    }

    // get posts from database or some shit
    private void populatePosts(Context context){
        try{
            List<Post> postEntities = postDao.getAll().get();
            for (Post post: postEntities) {
                posts.add(PostComponent.createComponent(context, post)); // create PostComponent using data from Post entity
            }
        }catch(Exception e){
            Log.i(this.getClass().getSimpleName(), "Could not get posts");
        }


        // for each Post entity in the database
            // use the PostComponent class to create a new post LinearLayout
            // add the post to the posts variable

        posts.add(PostComponent.createComponent(context));
        posts.add(PostComponent.createComponent(context));
        posts.add(PostComponent.createComponent(context));
        posts.add(PostComponent.createComponent(context));
    }

    public void insertPost(Post post){
        // add post to database
        try{
            postDao.insertPost(post);
            Log.i(this.getClass().getSimpleName(), "Post inserted: ");
        }catch(Exception e){
            Log.i(this.getClass().getSimpleName(), "Could not insert post");
            Log.i(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    public List<LinearLayout> getPosts() {
        return posts;
    }

    public static PostModel getPostModel(Context context){
        if(postModel == null){
            postModel = new PostModel(context);
        }

        return postModel;
    }

    public void closeDb(){
        db.close();
    }
}
