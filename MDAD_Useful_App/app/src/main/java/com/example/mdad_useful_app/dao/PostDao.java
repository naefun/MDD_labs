package com.example.mdad_useful_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mdad_useful_app.entity.Post;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;


@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    ListenableFuture<List<Post>>getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertPost(Post post);

    @Delete
    void delete(Post post);

}
