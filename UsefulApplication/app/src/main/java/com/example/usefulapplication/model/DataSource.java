package com.example.usefulapplication.model;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataSource {

    private List<UserPost> data;

    AppDatabase appDatabase;
    UserPostDao userPostDao;

    private static DataSource dataSource;

    private DataSource(Context context) {
        this.appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-database").fallbackToDestructiveMigration().build();
        this.userPostDao = appDatabase.userPostDao();
        this.data = new ArrayList<>();

        //prepopulateDatabase();

        try {
            this.data.addAll(userPostDao.getAll().get());
            Log.i(this.getClass().getSimpleName(), "DataSource: "+ "data added");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(this.getClass().getSimpleName(), "DataSource: "+ data.size());
    }

    public List<UserPost> getData() {
        return data;
    }

    public static DataSource getDataSource(Context context){
        return dataSource == null ? new DataSource(context) : dataSource;
    }

    private void prepopulateDatabase(){
        try {
            userPostDao.deleteAll().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<UserPost> posts = new ArrayList<>();

        posts.add(new UserPost("Birmingham", "This is a caption", "85417912"));
        posts.add(new UserPost("Birmingham", "This is a caption", "668913922"));

        try{
            userPostDao.insertUserPosts(posts);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
