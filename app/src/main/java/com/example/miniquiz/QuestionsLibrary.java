package com.example.miniquiz;

import java.util.Arrays;
import java.util.Random;

public class QuestionsLibrary {
    private final Question[] questions;
    private boolean markUsedQuestions = false;
    private boolean[] usedQuestions;
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
        this(new Question[] {
                new Question("Jaki jest stolica Polski?", new String[]{"Warszawa", "Kraków", "Gdańsk"}, 0),
                new Question("Ile wynosi 2 + 2?", new String[]{"3", "4", "5"}, 1),
                new Question("Jaki jest największy ocean na Ziemi?", new String[]{"Atlantycki", "Spokojny", "Indyjski"}, 1),
                new Question("Jaki jest symbol chemiczny wody?", new String[]{"H2O", "CO2", "O2"}, 0)
        });
    }
    public QuestionsLibrary(boolean markUsedQuestions) {
        this();
        this.markUsedQuestions = markUsedQuestions;
        if(markUsedQuestions) {
            usedQuestions = new boolean[questions.length];
            markQuestionsUnused();
        }
    }
    public Question getQuestion(int index) {
        if (index >= 0 && index < questions.length) {
            if(markUsedQuestions) {
                usedQuestions[index] = true;
            }
            return questions[index];
        }
        return null;
    }
    public Question getQuestion(){
        if(markUsedQuestions) {
            Random rand = new Random();
            int questionIndex = rand.nextInt(questions.length);
            int attempts = 0;
            while(usedQuestions[questionIndex]) {
                attempts++;
                if(questionIndex == questions.length - 1) {
                    questionIndex = 0;
                } else {
                    questionIndex++;
                }

                //Jeśli wszystkie pytania zostały użyte, resetuje stan użycia pytań
                if(attempts >= questions.length) {
                    markQuestionsUnused();
                    return getQuestion();
                }
            }

            //Jeśli znaleziono nieużyte pytanie, zwraca je i oznacza jako użyte
            return getQuestion(questionIndex);
        }
        return getQuestion(0);
    }
    public int getTotalQuestions() {
        return questions.length;
    }

    private void markQuestionsUnused() {
        if(markUsedQuestions) {
            Arrays.fill(usedQuestions, false);
        }
    }
}
