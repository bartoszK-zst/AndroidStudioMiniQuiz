package com.example.miniquiz;

import java.util.Arrays;
import java.util.Random;

public class QuestionsLibrary {
    private final Question[] questions;
    private boolean markUsedQuestions = false;
    private boolean[] usedQuestions;
    private int previosQuestionIndex = -1;

    private static final Question[] DEFAULT_QUESTIONS = new Question[] {
            new Question("Jaki jest stolica Polski?", new String[]{"Warszawa", "Kraków", "Gdańsk"}, 0),
            new Question("Ile wynosi 2 + 2?", new String[]{"3", "4", "5"}, 1),
            new Question("Jaki jest największy ocean na Ziemi?", new String[]{"Atlantycki", "Spokojny", "Indyjski"}, 1),
            new Question("Jaki jest symbol chemiczny wody?", new String[]{"H2O", "CO2", "O2"}, 0)
    };
    public QuestionsLibrary(Question[] questions, boolean markUsedQuestions){
        this.questions = questions.clone();
        this.markUsedQuestions = markUsedQuestions;
        if(markUsedQuestions) {
            usedQuestions = new boolean[questions.length];
            markQuestionsUnused();
        }
    }
    public QuestionsLibrary(Question[] questions){
        this(questions, true);
    }
    public QuestionsLibrary() {
        this(DEFAULT_QUESTIONS);
    }
    public QuestionsLibrary(boolean markUsedQuestions) {
        this(DEFAULT_QUESTIONS, markUsedQuestions);
    }
    public Question getQuestion(int index, boolean markQuestionUsed, boolean acceptSameQuestionInRow){
        if(index < 0 || index >= questions.length) {
            return null;
        }
        if(!acceptSameQuestionInRow && index == previosQuestionIndex) {
            getQuestion(markQuestionUsed);
        }
        else{
            return questions[index];
        }
    }
    public Question getQuestion(int index, boolean markQuestionUsed) {
        if (index >= 0 && index < questions.length) {
            Question q = questions[index];
            if (markUsedQuestions) {
                if (usedQuestions == null) {
                    usedQuestions = new boolean[questions.length];
                    markQuestionsUnused();
                }
                usedQuestions[index] = true;
            }
            if(markQuestionUsed){
                usedQuestions[index] = true;
            }
            previosQuestionIndex = index;
            return q;
        }
        return null;
    }
    public Question getQuestion(int index) {
        return getQuestion(index, markUsedQuestions);
    }
    public Question getQuestion(boolean includeUsed, boolean markQuestionUsed){
        Random Rand = new Random();
        int questionIndex = Rand.nextInt(questions.length);
        if(includeUsed){
            if(markQuestionUsed){
                usedQuestions[questionIndex] = true;
            }
            return questions[questionIndex];
        }
        else{
            int attempts = 0;
            while(usedQuestions[questionIndex] || questionIndex == previosQuestionIndex) {
                attempts++;
                if(questionIndex == questions.length - 1) {
                    questionIndex = 0;
                } else {
                    questionIndex++;
                }

                if(attempts >= questions.length) {
                    markQuestionsUnused();
                    return getQuestion(true, markQuestionUsed);
                }
            }
            if(markQuestionUsed){
                usedQuestions[questionIndex] = true;
            }
            return questions[questionIndex];
        }
    }
    public Question getQuestion(boolean manageUsedQuestions) {
        return getQuestion(manageUsedQuestions, manageUsedQuestions);
    }
    public Question getQuestion(){
        return getQuestion(markUsedQuestions);
    }

    public int getTotalQuestions() {
        return questions.length;
    }

    private void markQuestionsUnused() {
        if (markUsedQuestions) {
            if (usedQuestions == null) usedQuestions = new boolean[questions.length];
            Arrays.fill(usedQuestions, false);
        }
    }
}
