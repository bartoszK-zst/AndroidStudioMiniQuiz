package com.example.miniquiz;

public class Question {
    private final String questionText;
    private final String[] answerOptions;
    private final int correctAnswerIndex;
    public Question(String questionText, String[] answerOptions, int correctAnswerIndex) {
        this.questionText = questionText;
        this.answerOptions = answerOptions;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }
    public String[] getAnswerOptions() {
        return answerOptions;
    }
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
