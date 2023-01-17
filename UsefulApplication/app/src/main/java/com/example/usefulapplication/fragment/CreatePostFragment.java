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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.usefulapplication.MainActivity;
import com.example.usefulapplication.R;
import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.model.UserPost;

import java.security.Permission;


public class CreatePostFragment extends Fragment {
    private String captionArgument;
    private String locationArgument;
    private String dateArgument;
    private String trackIdArgument;
    private EditText captionEditText;
    private EditText locationEditText;
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
        dateArgument = getArguments().getString("date");
        trackIdArgument = getArguments().getString("trackId");

        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        captionEditText = view.findViewById(R.id.editTextCaption);
        locationEditText = view.findViewById(R.id.editTextLocation);
        dateEditText = view.findViewById(R.id.editTextDate);
        trackIdEditText = view.findViewById(R.id.editTextTrackId);
        Button createPostButton = view.findViewById(R.id.createPostButton);
        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);

        if(captionArgument != null) captionEditText.setText(captionArgument);
        if(locationArgument != null) locationEditText.setText(locationArgument);
        if(dateArgument != null) dateEditText.setText(dateArgument);
        if(trackIdArgument != null) trackIdEditText.setText(trackIdArgument);

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

        selectLocationButton.setOnClickListener(view1 -> {
            Log.i("selectLocationButton", "onViewCreated: Select location button pressed");

            if(locationPermissionsSet(this.getContext())){
                navigateToMapFragment();
            }else{
                Log.i("selectLocationButton", "onViewCreated: permissions not set");
                requestPermissions();
            }
        });
    }

    private void navigateToMapFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("caption", captionEditText.getText().toString());
        bundle.putString("location", locationEditText.getText().toString());
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else{
            return true;
        }
    }

    private void requestPermissions(){
        Activity main = (MainActivity) getActivity();
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(main, permissions, 1);
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length == 1){
                Log.i("requesting permissions", "onRequestPermissionsResult: permissions granted");
                navigateToMapFragment();
            }
        }
    }


}