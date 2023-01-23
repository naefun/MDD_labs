package com.example.usefulapplication.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.usefulapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.List;

public class CreatePostSelectMapLocationFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private String captionArgument;
    private String locationArgument;
    private String locationLatArgument;
    private String locationLongArgument;
    private String newLocation;
    private String newLocationLat;
    private String newLocationLong;
    private String dateArgument;
    private String trackIdArgument;
    private String imageUriArgument;
    private GoogleMap googleMap;

    private boolean useNewLocationValues = false;
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    private OnMapReadyCallback callback = googleMap -> {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (!locationLatArgument.isEmpty() && !locationLongArgument.isEmpty()) {
            LatLng location = new LatLng(Double.parseDouble(locationLatArgument), Double.parseDouble(locationLongArgument));
            googleMap.addMarker(new MarkerOptions().position(location).title("Image location"));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            Log.i("location", ": " + locationLatArgument + " " + locationLongArgument);
        }else{
            FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
            locationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnSuccessListener(deviceLocation -> {
                LatLng location = new LatLng(Double.parseDouble(String.valueOf(deviceLocation.getLatitude())), Double.parseDouble(String.valueOf(deviceLocation.getLongitude())));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        captionArgument = getArguments().getString("caption");
        locationArgument = getArguments().getString("location");
        locationLatArgument = getArguments().getString("locationLat");
        locationLongArgument = getArguments().getString("locationLong");
        dateArgument = getArguments().getString("date");
        trackIdArgument = getArguments().getString("trackId");
        imageUriArgument = getArguments().getString("imageUri");

        return inflater.inflate(R.layout.fragment_create_post_select_map_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        Log.i("test bundle", "onViewCreated: location = " + getArguments().getString("location"));


        Button cancelButton = view.findViewById(R.id.createMapLocationCancelButton);
        cancelButton.setOnClickListener(cancelButtonView -> {
            Log.i("cancelButtonView", "onViewCreated: cancel button pressed");
            Bundle bundle = new Bundle();
            bundle.putString("caption", captionArgument);
            bundle.putString("location", locationArgument);
            bundle.putString("locationLat", locationLatArgument);
            bundle.putString("locationLong", locationLongArgument);
            bundle.putString("date", dateArgument);
            bundle.putString("trackId", trackIdArgument);
            bundle.putString("imageUri", imageUriArgument);
            NavHostFragment.findNavController(CreatePostSelectMapLocationFragment.this).navigate(R.id.action_selectLocationMapFragment_to_createPostFragment, bundle);
        });

        Button saveButton = view.findViewById(R.id.createMapLocationSaveButton);
        saveButton.setOnClickListener(saveButtonView -> {
            Log.i("cancelButtonView", "onViewCreated: cancel button pressed");

            if(!useNewLocationValues && locationArgument.isEmpty() && locationLongArgument.isEmpty() && locationLatArgument.isEmpty()){
                Log.i("new location", "onViewCreated: location has not been selected");
                Toast.makeText(view.getContext(), "Please select a location", Toast.LENGTH_SHORT).show();
                return;
            }else if(!useNewLocationValues){
                newLocation = locationArgument;
                newLocationLat = locationLatArgument;
                newLocationLong = locationLongArgument;
            }

            Bundle bundle = new Bundle();
            bundle.putString("caption", captionArgument);
            bundle.putString("location", newLocation);
            bundle.putString("locationLat", newLocationLat);
            bundle.putString("locationLong", newLocationLong);
            bundle.putString("date", dateArgument);
            bundle.putString("trackId", trackIdArgument);
            bundle.putString("imageUri", imageUriArgument);
            NavHostFragment.findNavController(CreatePostSelectMapLocationFragment.this).navigate(R.id.action_selectLocationMapFragment_to_createPostFragment, bundle);

        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.i("location click", "onMyLocationClick: latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.i("map clicked", "onMapClick: lat = " + latLng.latitude + " long = " + latLng.longitude);
        useNewLocationValues = true;
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Image location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        newLocationLat = String.valueOf(latLng.latitude);
        newLocationLong = String.valueOf(latLng.longitude);

        Geocoder geocoder = new Geocoder(this.getContext());
        geocoder.getFromLocation(Double.parseDouble(newLocationLat), Double.parseDouble(newLocationLong), 1, new Geocoder.GeocodeListener(){

            @Override
            public void onGeocode(@NonNull List<Address> addresses) {
                Log.i("location", "onGeocode: " + addresses.get(0).getCountryName());
                newLocation = addresses.get(0).getCountryName();
            }

            @Override
            public void onError(@Nullable String errorMessage) {
                Geocoder.GeocodeListener.super.onError(errorMessage);
            }
        });

    }
}