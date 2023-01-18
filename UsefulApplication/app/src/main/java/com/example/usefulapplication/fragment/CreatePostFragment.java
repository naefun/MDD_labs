package com.example.usefulapplication.fragment;

import static com.example.usefulapplication.helper.InputValidation.dateIsValid;
import static com.example.usefulapplication.helper.InputValidation.inputIsEmpty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.usefulapplication.MainActivity;
import com.example.usefulapplication.R;
import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.model.UserPost;


public class CreatePostFragment extends Fragment {
    private String captionArgument;
    private String locationArgument;
    private String locationLatArgument;
    private String locationLongArgument;
    private String dateArgument;
    private String trackIdArgument;

    private EditText captionEditText;
    private TextView locationTextView;
    private TextView locationLatTextView;
    private TextView locationLongTextView;
    private EditText dateEditText;
    private EditText trackIdEditText;

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
        Log.i("test bundle", "onViewCreated: location = " + getArguments().getString("location"));
        captionArgument = getArguments().getString("caption");
        locationArgument = getArguments().getString("location");
        locationLatArgument = getArguments().getString("locationLat");
        locationLongArgument = getArguments().getString("locationLong");
        dateArgument = getArguments().getString("date");
        trackIdArgument = getArguments().getString("trackId");

        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        captionEditText = view.findViewById(R.id.editTextCaption);
        locationTextView = view.findViewById(R.id.textViewLocation);
        locationLatTextView = view.findViewById(R.id.textViewLocationLat);
        locationLongTextView = view.findViewById(R.id.textViewLocationLong);
        dateEditText = view.findViewById(R.id.editTextDate);
        trackIdEditText = view.findViewById(R.id.editTextTrackId);
        Button createPostButton = view.findViewById(R.id.createPostButton);
        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);

        if(captionArgument != null) captionEditText.setText(captionArgument);
        if(locationArgument != null) locationTextView.setText(locationArgument);
        if(locationLatArgument != null) locationLatTextView.setText(locationLatArgument);
        if(locationLongArgument != null) locationLongTextView.setText(locationLongArgument);
        if(dateArgument != null) dateEditText.setText(dateArgument);
        if(trackIdArgument != null) trackIdEditText.setText(trackIdArgument);

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationText = locationTextView.getText().toString();
                String locationLatText = locationLatTextView.getText().toString();
                String locationLongText = locationLongTextView.getText().toString();
                String captionText = captionEditText.getText().toString();
                String dateText = dateEditText.getText().toString();
                String trackIdText = trackIdEditText.getText().toString();

                String toastMessage = "";
                if(inputIsEmpty(locationText, locationLatText, locationLongText, captionText, trackIdText, dateText)){
                    toastMessage += "Please make sure all fields are filled in.";
                }
                if(toastMessage.length() == 0 && !dateIsValid(dateText)){
                    toastMessage += "Please make sure the date is in the correct format. (dd/mm/yyyy)";
                }
                if(toastMessage.length() > 0){
                    Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO
                // add lat and long to user post
                UserPost post = new UserPost(locationText, captionText, trackIdText, dateText, locationLatText, locationLongText);
                createPost(view.getContext(), post);
                Log.i("NB", "onClick: post created!"
                        + ", " + captionText
                        + ", " + locationText
                        + ", " + locationLatText
                        + ", " + locationLongText
                        + ", " + dateText
                        + ", " + trackIdText
                );
                Toast.makeText(view.getContext(), "post created", Toast.LENGTH_SHORT).show();
            }
        });

        selectLocationButton.setOnClickListener(view1 -> {
            Log.i("selectLocationButton", "onViewCreated: Select location button pressed");

            if(locationPermissionsSet(this.getContext())){
                navigateToSelectMapLocationFragment();
            }else{
                Log.i("selectLocationButton", "onViewCreated: permissions not set");
                requestPermissions();
            }
        });
    }

    private void navigateToSelectMapLocationFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("caption", captionEditText.getText().toString());
        bundle.putString("location", locationTextView.getText().toString());
        bundle.putString("locationLat", locationLatTextView.getText().toString());
        bundle.putString("locationLong", locationLongTextView.getText().toString());
        bundle.putString("date", dateEditText.getText().toString());
        bundle.putString("trackId", trackIdEditText.getText().toString());
        NavHostFragment.findNavController(CreatePostFragment.this).navigate(R.id.action_createPostFragment_to_selectLocationMapFragment, bundle);
    }

    private void createPost(Context context, UserPost post){
        AppDatabase appDatabase = DatabaseFactory.getAppDatabase(context);
        UserPostDao userPostDao = appDatabase.userPostDao();

        userPostDao.insertUserPost(post);
    }

    private boolean locationPermissionsSet(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        Activity main = (MainActivity) getActivity();
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(main, permissions, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length == 1){
                Log.i("requesting permissions", "onRequestPermissionsResult: permissions granted");
                navigateToSelectMapLocationFragment();
            }
        }
    }


}