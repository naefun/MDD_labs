package com.example.myapplication;

import android.content.Context;
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

import com.example.myapplication.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- CreateView");

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MainActivity.TAG + this.getClass().getName(), "Alert button clicked");
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_alertFragment);
            }
        });

        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- ViewCreated");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Create");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Resume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Destroy");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Attach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- Detach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i(MainActivity.TAG + this.getClass().getName(), "on -- DestroyView");
    }

}