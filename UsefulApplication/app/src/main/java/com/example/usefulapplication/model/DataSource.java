package com.example.usefulapplication.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSource {

    private List<PostItem> data;

    private static DataSource dataSource;

    public DataSource() {
        this.data = new ArrayList<>();
        this.data.add(new PostItem(LocalDate.of(2022, 10, 10), "Birmingham", "Some caption", "Zombie", "Jamie T"));
        this.data.add(new PostItem(LocalDate.of(2022, 10, 10), "Birmingham", "Some caption 2", "Zombie 2", "Jamie T 2"));
        this.data.add(new PostItem(LocalDate.of(2022, 10, 10), "Birmingham", "Some caption 3", "Zombie 3", "Jamie T 3"));
        this.data.add(new PostItem(LocalDate.of(2022, 10, 10), "Birmingham", "Some caption 4", "Zombie 4", "Jamie T 4"));
        this.data.add(new PostItem(LocalDate.of(2022, 10, 10), "Birmingham", "Some caption 5", "Zombie 5", "Jamie T 5"));
    }

    public List<PostItem> getData() {
        return data;
    }

    public static DataSource getDataSource(){
        return dataSource == null ? new DataSource() : dataSource;
    }
}
