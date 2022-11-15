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
import android.widget.TextView;

import com.example.usefulapplication.R;
import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.model.UserPost;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPost extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST_ID = "postId";
    private static final String ARG_TRACK_ID = "trackId";
    private static final String ARG_CAPTION = "caption";
    private static final String ARG_LOCATION = "location";

    // TODO: Rename and change types of parameters
    private Long postId;
    private String trackId;
    private String caption;
    private String location;

    public EditPost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPost.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPost newInstance(String param1, String param2) {
        EditPost fragment = new EditPost();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, param1);
        args.putString(ARG_TRACK_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getLong(ARG_POST_ID);
            trackId = getArguments().getString(ARG_TRACK_ID);
            caption = getArguments().getString(ARG_CAPTION);
            location = getArguments().getString(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("NB", "onViewCreated: " + postId + ", " + trackId + ", " + caption + ", " + location);
        // set text for editText views

        TextView captionTextView = view.findViewById(R.id.edit_editTextCaption);
        TextView locationTextView = view.findViewById(R.id.edit_editTextLocation);
        TextView dateTextView = view.findViewById(R.id.edit_editTextDate);
        Button updatePostButton = view.findViewById(R.id.update_post_button);

        captionTextView.setText(caption);
        locationTextView.setText(location);
        dateTextView.setText("01/01/2001");

        updatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String captionText = captionTextView.getText().toString();
                String locationText = locationTextView.getText().toString();
                String dateText = dateTextView.getText().toString();
                UserPost post = new UserPost(locationText, captionText, trackId, postId);
                updatePost(view.getContext(), post);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void updatePost(Context context, UserPost post){
        AppDatabase appDatabase = DatabaseFactory.getAppDatabase(context);
        UserPostDao userPostDao = appDatabase.userPostDao();

        userPostDao.updateUserPost(post);
    }
}