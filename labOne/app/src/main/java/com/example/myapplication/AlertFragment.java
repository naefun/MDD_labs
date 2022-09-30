package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentAlertBinding;


public class AlertFragment extends Fragment {
    private FragmentAlertBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(MainActivity.TAG + this.getClass().getName(), "Alert fragment oncreateview");
        binding = FragmentAlertBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.alertFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MainActivity.TAG + this.getClass().getName(), "AlertFragment first button clicked");
                displayToast("first button pressed");
                NavHostFragment.findNavController(AlertFragment.this)
                        .navigate(R.id.action_alertFragment_to_FirstFragment);
            }
        });

        binding.alertSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MainActivity.TAG + this.getClass().getName(), "AlertFragment second button clicked");
                displayToast("second button pressed");
                NavHostFragment.findNavController(AlertFragment.this)
                        .navigate(R.id.action_alertFragment_to_SecondFragment);
            }
        });
    }

    private void displayToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}