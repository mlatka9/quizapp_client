package com.example.ble.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ble.R;
import com.example.ble.SendDataToESP;


public class QuizResultFragment extends Fragment{

    private int score;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
               Navigation.findNavController(getView()).navigate(R.id.action_quizResultFragment_to_categoryFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz_result, container, false);
        Button showRes = root.findViewById(R.id.buttonBack2);

        if (getArguments() != null) {
            score = getArguments().getInt("score");
        }
        showRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_quizResultFragment_to_nav_gallery);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonBack).setOnClickListener(v->{
            //Navigation.findNavController(view).navigate(R.id.action_quizResultFragment_to_categoryFragment);
            Navigation.findNavController(view).navigate(R.id.action_quizResultFragment_to_categoryFragment);
        });

        TextView resultScore = (TextView)view.findViewById(R.id.textViewResultScore);
        resultScore.setText("Twój wynik: " + score);

        if(score <=2)
            SendDataToESP.SELECTED_ACTION = "1";
        else if(score > 2 && score < 4)
            SendDataToESP.SELECTED_ACTION = "3";
        else
            SendDataToESP.SELECTED_ACTION = "5";
    }
}