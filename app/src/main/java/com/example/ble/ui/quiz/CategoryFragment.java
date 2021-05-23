package com.example.ble.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ble.R;


public class CategoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button button1 = view.findViewById(R.id.buttonCategory1);
        Button button2 = view.findViewById(R.id.buttonCategory2);
        Button button3 = view.findViewById(R.id.buttonCategory3);

        button1.setOnClickListener(this::startQuizActivity);

        button2.setOnClickListener(v -> {
            startQuizActivity(button2);
        });

        button3.setOnClickListener(this::startQuizActivity);
    }

    private void startQuizActivity(View view) {

        String category = ((Button)view).getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);


        Navigation.findNavController(view).navigate(R.id.action_categoryFragment_to_quizFragment, bundle);
    }
}
