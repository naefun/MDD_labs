package com.example.usefulapplication.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.usefulapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocationMap extends Fragment {
    private String locationLatArgument;
    private String locationLongArgument;

    private OnMapReadyCallback callback = googleMap ->  {
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng location = new LatLng(Double.parseDouble(locationLatArgument), Double.parseDouble(locationLongArgument));
        googleMap.addMarker(new MarkerOptions().position(location).title("Post location"));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        Log.i("view map", "onMapReady: map loaded");
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        locationLatArgument = getArguments().getString("locationLat");
        locationLongArgument = getArguments().getString("locationLong");
        return inflater.inflate(R.layout.fragment_view_location_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Button backButton = view.findViewById(R.id.viewMapLocationBackButton);
        backButton.setOnClickListener(backButtonView -> {
            NavHostFragment.findNavController(this.getParentFragment()).navigate(R.id.action_viewLocationMapFragment_to_postFragment);
        });

        Log.i("location arguments", "onViewCreated: lat = " + locationLatArgument + " long = " + locationLongArgument);
    }
}