package com.example.miniquiz;

import java.util.Arrays;
import java.util.Random;

public class Question {
    private final String questionText;
    private final String[] answerOptions;
    private final int correctAnswerIndex;
    private boolean[] usedAnswerOptions;
    public Question(String questionText, String[] answerOptions, int correctAnswerIndex) {
        this.questionText = questionText;
        this.answerOptions = answerOptions;
        this.correctAnswerIndex = correctAnswerIndex;
        markAnswerOptionsUnused();
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
    public String getAnswerOption(int index) {
        if (index >= 0 && index < answerOptions.length) {
            usedAnswerOptions[index] = true;
            return answerOptions[index];
        }
        return null;
    }
    public String getAnswerOption(){
        if (usedAnswerOptions == null) {
            markAnswerOptionsUnused();
        }
        Random rand = new Random();
        int answerOptionIndex = rand.nextInt(answerOptions.length);
        while (usedAnswerOptions[answerOptionIndex]) {
            if (answerOptionIndex == answerOptions.length - 1) {
                answerOptionIndex = 0;
            } else {
                answerOptionIndex++;
            }
        }

        return getAnswerOption(answerOptionIndex);
    }

    private void markAnswerOptionsUnused(){
        if(usedAnswerOptions == null) {
            usedAnswerOptions = new boolean[answerOptions.length];
        }
        Arrays.fill(usedAnswerOptions, false);
    }
}
