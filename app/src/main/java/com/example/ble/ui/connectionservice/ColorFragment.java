package com.example.ble.ui.connectionservice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.ble.R;
import com.example.ble.SendDataToESP;

public class ColorFragment extends Fragment {

    private ColorViewModel slideshowViewModel;

    Button  redButton, pinkButton, greenButton, violetButton, whiteButton, orangeButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(ColorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_color, container, false);
        redButton = root.findViewById(R.id.redButton);
        pinkButton = root.findViewById(R.id.pinkButton);
        greenButton = root.findViewById(R.id.greenButton);
        violetButton = root.findViewById(R.id.violetButton);
        whiteButton = root.findViewById(R.id.whiteButton);
        orangeButton = root.findViewById(R.id.orangeButton);

        SendDataToESP.SELECED_ACTIVITY = true;

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "r";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_nav_gallery);
            }
        });
        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "p";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_nav_gallery);
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "g";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_nav_gallery);

            }
        });
        violetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "v";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_nav_gallery);

            }
        });
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "w";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_nav_gallery);

            }
        });
        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "o";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_nav_gallery);
            }
        });
        return root;
    }
}
