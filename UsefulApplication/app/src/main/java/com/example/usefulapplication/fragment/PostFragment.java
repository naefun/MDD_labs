package com.example.usefulapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usefulapplication.PostListAdapter;
import com.example.usefulapplication.R;
import com.example.usefulapplication.databinding.FragmentPostBinding;
import com.example.usefulapplication.model.DataSource;
import com.example.usefulapplication.model.UserPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private final String SORT_NEWEST_FIRST = "Date: newest first";
    private final String SORT_OLDEST_FIRST = "Date: oldest first";
    private Boolean sortDateNewestFirst = true;
    private List<UserPost> posts;
    FragmentPostBinding binding;
    private RecyclerView recyclerView;
    private PostListAdapter adapter;
    ViewGroup viewGroupContainer;
    List<String> filterLocations;

    public PostFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filterLocations = new ArrayList<String>();
        viewGroupContainer = container;
        //posts = DataSource.getDataSource(container.getContext()).getData();
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        displayAllPosts(viewGroupContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.i("PostFragment", "onViewCreated: Loaded post fragment");

        FloatingActionButton fab = view.findViewById(R.id.addPostFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostListAdapter.stopSong();
                NavHostFragment.findNavController(PostFragment.this).navigate(R.id.action_postFragment_to_createPostFragment);
            }
        });

        String[] locations = getPostLocations(viewGroupContainer);
        Spinner dropdown = view.findViewById(R.id.locationFilterDropdown);
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, locations);
        dropdown.setAdapter(dropdownAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PostListAdapter.stopSong();
                String location = parent.getItemAtPosition(position).toString();
                Log.i("Spinner selected", "onItemSelected: " + location);
                filterPostsByLocation(viewGroupContainer, location);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for(String location : locations){
            Log.i("locations", "onViewCreated: " + location);
        }

        String[] sortOptions = {SORT_NEWEST_FIRST, SORT_OLDEST_FIRST};
        Spinner sortDropdown = view.findViewById(R.id.sortByDateDropdown);
        ArrayAdapter<String> sortDropdownAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortDropdown.setAdapter(sortDropdownAdapter);
        sortDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PostListAdapter.stopSong();
                String sortChoice = parent.getItemAtPosition(position).toString();
                sortDateNewestFirst = sortChoice.equalsIgnoreCase(SORT_NEWEST_FIRST);
                sortPostsByDate(sortDateNewestFirst);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void filterPostsByLocation(ViewGroup container, String location){

        if(location.isEmpty()){
            displayAllPosts(container);
            return;
        }

        posts = DataSource.getDataSource(container.getContext()).getData().stream().filter(post -> {
            if(post.getLocation().equalsIgnoreCase(location)){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());
        sortPostsByDate(sortDateNewestFirst);
    }

    private void sortPostsByDate(Boolean descending){
        if(descending){
            posts.sort((p1, p2) -> (int) (Long.parseLong(p2.getPostCreationTimeMillis()) - Long.parseLong(p1.getPostCreationTimeMillis())));
        }else{
            posts.sort((p1, p2) -> (int) (Long.parseLong(p1.getPostCreationTimeMillis()) - Long.parseLong(p2.getPostCreationTimeMillis())));
        }
        adapter = new PostListAdapter(getContext(), posts, this.getViewLifecycleOwner(), this);
        recyclerView.setAdapter(adapter);
    }

    private void displayAllPosts(ViewGroup container){
        posts = DataSource.getDataSource(container.getContext()).getData();
        sortPostsByDate(sortDateNewestFirst);
    }

    private String[] getPostLocations(ViewGroup container){
        Set<String> locations = new HashSet<String>();
        posts = DataSource.getDataSource(container.getContext()).getData();

        locations.add("");

        for (UserPost post: posts) {
            locations.add(post.getLocation());
        }

        return locations.toArray(new String[locations.size()]);
    }

    private class PermissionRequestHandler extends Fragment{
        private ActivityResultLauncher<String> requestPermissionLauncher;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    Log.i("permission", "onCreate: permissions");
                }
            });
        }

        public void handle(){
            if(!readMediaPermissionsSet(getContext())) requestReadMediaPermissions();
        }

        private boolean readMediaPermissionsSet(Context context){
            Boolean b = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            Log.i("external storage permission", "pickMedia: " + b);
            return b;
        }

        private void requestReadMediaPermissions(){
            Log.i("request permissions", "requesting permissions");
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    }
}