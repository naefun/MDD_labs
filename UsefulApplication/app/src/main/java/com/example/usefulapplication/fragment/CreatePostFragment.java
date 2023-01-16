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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        EditText trackIdEditText = view.findViewById(R.id.editTextTrackId);

        Button createPostButton = view.findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationText = locationEditText.getText().toString();
                String captionText = captionEditText.getText().toString();
                String dateText = dateEditText.getText().toString();
                String trackIdText = trackIdEditText.getText().toString();

                String toastMessage = "";
                if(inputIsEmpty(locationText, captionText, trackIdText, dateText)){
                    toastMessage += "Please make sure all fields are filled in.";
                }
                if(toastMessage.length() == 0 && !dateIsValid(dateText)){
                    toastMessage += "Please make sure the date is in the correct format. (dd/mm/yyyy)";
                }
                if(toastMessage.length() > 0){
                    Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                UserPost post = new UserPost(locationText, captionText, trackIdText, dateText);
                createPost(view.getContext(), post);
                Log.i("NB", "onClick: post created!"
                        + ", " + captionText
                        + ", " + locationText
                        + ", " + dateText
                        + ", " + trackIdText
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


}