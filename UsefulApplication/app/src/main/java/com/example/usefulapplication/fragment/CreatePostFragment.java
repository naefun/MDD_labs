package com.example.usefulapplication.fragment;

import static androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import static androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
import static androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import static com.example.usefulapplication.helper.InputValidation.dateIsValid;
import static com.example.usefulapplication.helper.InputValidation.inputIsEmpty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType;
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

import java.util.Date;


public class CreatePostFragment extends Fragment {
    private String captionArgument;
    private String locationArgument;
    private String locationLatArgument;
    private String locationLongArgument;
    private String dateArgument;
    private String trackIdArgument;
    private String imageUriArgument;

    private EditText captionEditText;
    private TextView locationTextView;
    private TextView locationLatTextView;
    private TextView locationLongTextView;
    private EditText dateEditText;
    private EditText trackIdEditText;
    private ImageView postImageView;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Uri imageUri;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia =
                registerForActivityResult(new PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
                        postImageView.setImageURI(imageUri);
                        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContext().getContentResolver().takePersistableUriPermission(imageUri, flag);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        requestPermissionLauncher = registerForActivityResult(new RequestPermission(), isGranted -> {
            if(isGranted){
                pickImage();
                Log.i("permission", "onCreate: permissions");
            }
        });
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
        imageUriArgument = getArguments().getString("imageUri");

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
        postImageView = view.findViewById(R.id.createPostImage);
        Button createPostButton = view.findViewById(R.id.createPostButton);
        Button cancelPostButton = view.findViewById(R.id.cancelPostButton);
        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);

        if(captionArgument != null) captionEditText.setText(captionArgument);
        if(locationArgument != null) locationTextView.setText(locationArgument);
        if(locationLatArgument != null) locationLatTextView.setText(locationLatArgument);
        if(locationLongArgument != null) locationLongTextView.setText(locationLongArgument);
        if(dateArgument != null) dateEditText.setText(dateArgument);
        if(trackIdArgument != null) trackIdEditText.setText(trackIdArgument);
        if(imageUriArgument != null) {
            imageUri = Uri.parse(imageUriArgument);
            postImageView.setImageURI(imageUri);
        }

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationText = locationTextView.getText().toString();
                String locationLatText = locationLatTextView.getText().toString();
                String locationLongText = locationLongTextView.getText().toString();
                String captionText = captionEditText.getText().toString();
                String dateText = dateEditText.getText().toString();
                String trackIdText = trackIdEditText.getText().toString();
                String imageUriString = imageUri != null ? imageUri.toString() : "";

                String toastMessage = "";
                if(inputIsEmpty(locationText, locationLatText, locationLongText, captionText, trackIdText, dateText, imageUriString)){
                    toastMessage += "Please make sure all fields are filled in.";
                }
                if(toastMessage.length() == 0 && !dateIsValid(dateText)){
                    toastMessage += "Please make sure the date is in the correct format. (dd/mm/yyyy)";
                }
                if(toastMessage.length() > 0){
                    Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                Date date = new Date();
                String postCreationTimeMillis = String.valueOf(date.getTime());

                UserPost post = new UserPost(locationText, captionText, trackIdText, dateText, locationLatText, locationLongText, imageUriString, postCreationTimeMillis);
                createPost(view.getContext(), post);
                Log.i("NB", "onClick: post created!"
                        + ", " + captionText
                        + ", " + locationText
                        + ", " + locationLatText
                        + ", " + locationLongText
                        + ", " + dateText
                        + ", " + trackIdText
                        + ", " + imageUriString
                        + ", " + postCreationTimeMillis
                );
                Toast.makeText(view.getContext(), "post created", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(CreatePostFragment.this).navigate(R.id.action_createPostFragment_to_postFragment);

            }
        });

        cancelPostButton.setOnClickListener(cancelPostView -> {
            NavHostFragment.findNavController(CreatePostFragment.this).navigate(R.id.action_createPostFragment_to_postFragment);
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

        selectImageButton.setOnClickListener(selectImageView -> {
            Log.i("select image", "onViewCreated: select image button pressed. Photo picker is available: " + PickVisualMedia.isPhotoPickerAvailable());
            if(readMediaPermissionsSet(this.getContext())){
                Log.i("select image", "onViewCreated: external storage permissions are set");
                pickImage();
            }else{
                Log.i("select image", "onViewCreated: external storage permissions are not set");
                requestReadMediaPermissions();
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

        String imageUriString = imageUri != null ? imageUri.toString() : null;
        bundle.putString("imageUri", imageUriString);

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

    private boolean readMediaPermissionsSet(Context context){
        Boolean b = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        Log.i("external storage permission", "pickMedia: " + b);
        return b;
    }

    private void requestPermissions(){
        Activity main = (MainActivity) getActivity();
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(main, permissions, 1);
    }

    private void requestReadMediaPermissions(){
        Log.i("request permissions", "requesting permissions");
        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length == 1){
                Log.i("requesting permissions", "onRequestPermissionsResult: permissions granted");
                navigateToSelectMapLocationFragment();
            }
        } else{
            Log.i("grant results length", "onRequestPermissionsResult: " + grantResults.length);
        }
    }

    private void pickImage(){
        // Registers a photo picker activity launcher in single-select mode.


// Include only one of the following calls to launch(), depending on the types
// of media that you want to allow the user to choose from.

// Launch the photo picker and allow the user to choose images and videos.
        Log.i("pick media", "pickMedia: picking media");
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType((VisualMediaType) INSTANCE)
                .build());

    }


}