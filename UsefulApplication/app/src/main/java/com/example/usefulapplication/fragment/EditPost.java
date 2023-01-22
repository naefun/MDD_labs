package com.example.usefulapplication.fragment;

import static com.example.usefulapplication.helper.InputValidation.dateIsValid;
import static com.example.usefulapplication.helper.InputValidation.inputIsEmpty;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.usefulapplication.R;
import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.model.UserPost;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditPost extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST_ID = "postId";
    private static final String ARG_TRACK_ID = "trackId";
    private static final String ARG_CAPTION = "caption";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_LOCATION_LAT = "locationLat";
    private static final String ARG_LOCATION_LONG = "locationLong";
    private static final String ARG_DATE = "date";
    private static final String ARG_IMAGE_URI = "imageUri";
    private static final String ARG_POST_CREATION_TIME_MILLIS = "postCreationTimeMillis";


    private Long postId;
    private String trackId;
    private String caption;
    private String location;
    private String date;
    private String locationLat;
    private String locationLong;
    private String imageUri;
    private String postCreationTimeMillis;

    public EditPost() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getLong(ARG_POST_ID);
            trackId = getArguments().getString(ARG_TRACK_ID);
            caption = getArguments().getString(ARG_CAPTION);
            location = getArguments().getString(ARG_LOCATION);
            locationLat = getArguments().getString(ARG_LOCATION_LAT);
            locationLong = getArguments().getString(ARG_LOCATION_LONG);
            date = getArguments().getString(ARG_DATE);
            imageUri = getArguments().getString(ARG_IMAGE_URI);
            postCreationTimeMillis = getArguments().getString(ARG_POST_CREATION_TIME_MILLIS);
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
        Log.i("NB", "onViewCreated: " + postId + ", " + trackId + ", " + caption + ", " + location + ", " + date);
        // set text for editText views

        TextView captionTextView = view.findViewById(R.id.edit_editTextCaption);
        TextView dateTextView = view.findViewById(R.id.edit_editTextDate);
        TextView trackIdTextView = view.findViewById(R.id.edit_editTrackId);
        Button updatePostButton = view.findViewById(R.id.update_post_button);

        captionTextView.setText(caption);
        trackIdTextView.setText(trackId);
        dateTextView.setText(date);

        updatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String captionText = captionTextView.getText().toString();
                String trackText = trackIdTextView.getText().toString();
                String dateText = dateTextView.getText().toString();

                String toastMessage = "";
                if(inputIsEmpty(captionText, trackText, dateText)){
                    toastMessage += "Please make sure all fields are filled in.";
                }
                if(toastMessage.length() == 0 && !dateIsValid(dateText)){
                    toastMessage += "Please make sure the date is in the correct format. (dd/mm/yyyy)";
                }
                if(toastMessage.length() > 0){
                    Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                UserPost post = new UserPost(location, captionText, trackText, dateText,  postId, locationLat, locationLong, imageUri, postCreationTimeMillis);
                updatePost(view.getContext(), post);
                Toast.makeText(view.getContext(), "Post successfully updated", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(EditPost.this).navigate(R.id.action_editPost_to_postFragment);
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