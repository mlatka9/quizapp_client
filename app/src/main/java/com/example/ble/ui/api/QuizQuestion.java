package com.example.ble.ui.api;

import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {
    private final Long id;
    private final String question;
    private final Integer correctAnswer;
    private final String category;
    private final List<String> answers;

    public QuizQuestion(Long id, String question, Integer correctAnswer, String category, List<String> answers) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.category = category;
        this.answers = answers;
    }


    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getAnswers() {
        return answers;
    }


    @Override
    public String toString() {
        return "QuizQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", category='" + category + '\'' +
                ", answers=" + answers +
                '}';
    }
}