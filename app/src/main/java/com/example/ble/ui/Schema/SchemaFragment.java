package com.example.ble.ui.Schema;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ble.R;
import com.example.ble.SendDataToESP;

public class SchemaFragment extends Fragment {


    Button acceptButton, firstButton, secondButton, thirdButton, fourthButton;


    public static SchemaFragment newInstance() {
        return new SchemaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schema, container, false);
        firstButton = root.findViewById(R.id.firstButton);
        secondButton = root.findViewById(R.id.secondButton);
        thirdButton = root.findViewById(R.id.thirdButton);
        fourthButton = root.findViewById(R.id.fourthButton);

        SendDataToESP.SELECED_ACTIVITY = false;

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "7";
                Navigation.findNavController(v).navigate(R.id.action_schemaFragment2_to_nav_gallery);

            }
        });
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "6";
                Navigation.findNavController(v).navigate(R.id.action_schemaFragment2_to_nav_gallery);

            }
        });
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "8";
                Navigation.findNavController(v).navigate(R.id.action_schemaFragment2_to_nav_gallery);

            }
        });
        fourthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToESP.SELECTED_ACTION = "9";
                Navigation.findNavController(v).navigate(R.id.action_schemaFragment2_to_nav_gallery);

            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}