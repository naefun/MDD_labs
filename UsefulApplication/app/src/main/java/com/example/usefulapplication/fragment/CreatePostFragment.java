package com.example.usefulapplication.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usefulapplication.R;
import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.model.UserPost;


public class CreatePostFragment extends Fragment {

    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText captionEditText = view.findViewById(R.id.editTextCaption);
        EditText locationEditText = view.findViewById(R.id.editTextLocation);
        EditText dateEditText = view.findViewById(R.id.editTextDate);

        Button createPostButton = view.findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationText = locationEditText.getText().toString();
                String captionText = captionEditText.getText().toString();
                String songIdText = "1109737";

                if(inputIsEmpty(locationText, captionText, songIdText)){
                    Toast.makeText(view.getContext(), "Please make sure all fields are filled in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserPost post = new UserPost(locationText, captionText, songIdText);
                createPost(view.getContext(), post);
                Log.i("NB", "onClick: post created!"
                        + ", " + captionEditText.getText().toString()
                        + ", " + locationEditText.getText().toString()
                        + ", " + dateEditText.getText().toString()
                );
                Toast.makeText(view.getContext(), "post created", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createPost(Context context, UserPost post){
        AppDatabase appDatabase = DatabaseFactory.getAppDatabase(context);
        UserPostDao userPostDao = appDatabase.userPostDao();

        userPostDao.insertUserPost(post);
    }

    private boolean inputIsEmpty(String...input){
        for (String text: input) {
            if (text.trim().length() == 0){
                return true;
            }
        }

        return false;
    }
}