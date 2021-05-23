package com.example.ble.ui.api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceholderAPI {

    @GET("questions")
    Call<List<QuizQuestion>> getRandomQuizQuestions(@Query("limit") Integer limit);

    @GET("questions/category/{category}")
    Call<List<QuizQuestion>> getRandomQuizQuestionsByCategory(@Path("category") String category,
                                                              @Query("limit") Integer limit);
}