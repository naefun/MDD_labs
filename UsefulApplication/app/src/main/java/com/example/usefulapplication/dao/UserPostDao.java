package com.example.usefulapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.usefulapplication.model.UserPost;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface UserPostDao {

    @Query("SELECT * FROM userpost")
    ListenableFuture<List<UserPost>> getAll();

    @Query("DELETE FROM userpost")
    ListenableFuture<Integer> deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertUserPosts(List<UserPost> userPosts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertUserPost(UserPost userPost);

    @Update
    ListenableFuture<Integer> updateUserPost(UserPost userPost);

    @Delete
    ListenableFuture<Integer> deletePost(UserPost userPost);
}
