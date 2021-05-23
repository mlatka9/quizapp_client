package com.example.ble.ui.quiz;


        import android.os.Bundle;
        import android.os.Handler;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.navigation.Navigation;

        import com.example.ble.R;
        import com.example.ble.ui.api.JsonPlaceholderAPI;
        import com.example.ble.ui.api.QuizQuestion;

        import java.util.List;
        import java.util.concurrent.TimeUnit;

        import okhttp3.OkHttpClient;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class QuizFragment extends Fragment {

    private String category;

    private List<QuizQuestion> quizQuestions;
    private Integer currentQuestion;
    private int score;

    private TextView textViewQuestion;
    private TextView textViewAnswer1;
    private TextView textViewAnswer2;
    private TextView textViewAnswer3;
    private TextView textViewAnswer4;

    private TextView textViewScore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            category = getArguments().getString("category").toLowerCase();
        }
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentQuestion = 0;
        score = 0;

        textViewQuestion = view.findViewById(R.id.textViewQuestion);
        textViewAnswer1 = view.findViewById(R.id.textViewAnswer1);
        textViewAnswer2 = view.findViewById(R.id.textViewAnswer2);
        textViewAnswer3 = view.findViewById(R.id.textViewAnswer3);
        textViewAnswer4 = view.findViewById(R.id.textViewAnswer4);
        textViewScore = view.findViewById(R.id.textViewScore);


        getRandomQuizQuestionsByCategory(category, 5);
    }

    private void getRandomQuizQuestionsByCategory(String category, Integer limit) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://quizapp-spring-boot-server.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();



        JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        Call<List<QuizQuestion>> call = jsonPlaceholderAPI.getRandomQuizQuestionsByCategory(category, limit);


        call.enqueue(new Callback<List<QuizQuestion>>() {
            @Override
            public void onResponse(Call<List<QuizQuestion>> call, Response<List<QuizQuestion>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                }
                textViewAnswer1.setOnClickListener(v -> checkAnswer((TextView) v));
                textViewAnswer2.setOnClickListener(v -> checkAnswer((TextView) v));
                textViewAnswer3.setOnClickListener(v -> checkAnswer((TextView) v));
                textViewAnswer4.setOnClickListener(v -> checkAnswer((TextView) v));
                quizQuestions = response.body();
                if(quizQuestions != null && quizQuestions.size()>0){
                    showQuestion();
                } else {
                    Navigation.findNavController(getView()).navigate(R.id.action_quizFragment_to_categoryFragment);
                }

            }

            @Override
            public void onFailure(Call<List<QuizQuestion>> call, Throwable t) {
                System.out.println("onFailure" + t.getMessage());
                Navigation.findNavController(getView()).navigate(R.id.action_quizFragment_to_categoryFragment);
            }
        });
    }

    private void showQuestion() {

        String message = "Pytanie "+ (currentQuestion + 1) + " / "+ quizQuestions.size();
        textViewScore.setText(message);

        textViewQuestion.setText(quizQuestions.get(currentQuestion).getQuestion());
        textViewAnswer1.setText(quizQuestions.get(currentQuestion).getAnswers().get(0));
        textViewAnswer2.setText(quizQuestions.get(currentQuestion).getAnswers().get(1));
        textViewAnswer3.setText(quizQuestions.get(currentQuestion).getAnswers().get(2));
        textViewAnswer4.setText(quizQuestions.get(currentQuestion).getAnswers().get(3));

        textViewAnswer1.setBackgroundResource(R.color.black_dark);
        textViewAnswer2.setBackgroundResource(R.color.black_dark);
        textViewAnswer3.setBackgroundResource(R.color.black_dark);
        textViewAnswer4.setBackgroundResource(R.color.black_dark);

        textViewAnswer1.setClickable(true);
        textViewAnswer2.setClickable(true);
        textViewAnswer3.setClickable(true);
        textViewAnswer4.setClickable(true);

    }

    private void checkAnswer(TextView textView) {

        textViewAnswer1.setClickable(false);
        textViewAnswer2.setClickable(false);
        textViewAnswer3.setClickable(false);
        textViewAnswer4.setClickable(false);


        Integer correctAnswerIndex = quizQuestions.get(currentQuestion).getCorrectAnswer();
        String correctAnswer = quizQuestions.get(currentQuestion).getAnswers().get(correctAnswerIndex);

        if (textView.getText().toString().equals(correctAnswer)) {
            score++;
            textView.setBackgroundResource(R.color.green);
        } else {
            textView.setBackgroundResource(R.color.red);
        }


        if (quizQuestions.size() - 1 == currentQuestion) {

            (new Handler()).postDelayed(this::showQuizResult , 1000);



        } else {
            currentQuestion++;
            (new Handler()).postDelayed(this::showQuestion, 1000);
        }

    }

    private void showQuizResult(){
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        Navigation.findNavController(getView()).navigate(R.id.action_quizFragment_to_quizResultFragment, bundle);
    }

}
