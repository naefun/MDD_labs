package com.example.usefulapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usefulapplication.PostListAdapter;
import com.example.usefulapplication.R;
import com.example.usefulapplication.databinding.FragmentPostBinding;
import com.example.usefulapplication.model.DataSource;
import com.example.usefulapplication.model.UserPost;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private List<UserPost> posts;
    FragmentPostBinding binding;
    private RecyclerView recyclerView;
    private PostListAdapter adapter;

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
        posts = DataSource.getDataSource(container.getContext()).getData();
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new PostListAdapter(getContext(), posts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}