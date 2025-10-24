package com.example.miniquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuizViewFragment extends Fragment implements AnswersViewFragment.OnAnswerSelectedListener {
    private int QuestionNumber = 0;
    private int TotalQuestions = 5;
    private final QuestionsLibrary questionsLibrary = new QuestionsLibrary(true);
    private Question currentQuestion;

    public QuizViewFragment() {
        super(R.layout.quiz_view);
    }

    // Overloaded factory: create QuizViewFragment with custom answers array
    public static QuizViewFragment newInstance() {
        QuizViewFragment fragment = new QuizViewFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetQuiz();

        Button resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> {
            if (getParentFragment() instanceof QuizResetListener) {
                ((QuizResetListener) getParentFragment()).onQuizReset();
            } else if (getActivity() instanceof QuizResetListener) {
                ((QuizResetListener) getActivity()).onQuizReset();
            }
        });


        loadNextQuestion();
    }

    @Override
    public void onAnswerSelected(int index) {
        if (index == currentQuestion.getCorrectAnswerIndex()) {
            if (getActivity() instanceof CorrectAnswerSelectedListener) {
                ((CorrectAnswerSelectedListener) getActivity()).onCorrectAnswerSelected();
            }
        }

        if (getActivity() instanceof AnswersViewFragment.OnAnswerSelectedListener) {
            ((AnswersViewFragment.OnAnswerSelectedListener) getActivity()).onAnswerSelected(index);
        }

        // Sprawdza czy zadał już określoną liczbę pytań
        if (QuestionNumber >= TotalQuestions) {
            if (getActivity() instanceof QuizFinishedListener) {
                ((QuizFinishedListener) getActivity()).onQuizFinished(TotalQuestions);
            }
            return;
        }

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        // Pobierz pytanie z biblioteki (getQuestion() obsługuje losowanie i oznaczanie użytych)
        currentQuestion = questionsLibrary.getQuestion();
        if (currentQuestion == null) {
            Toast.makeText(getContext(), "Nie udało się pobrać pytania.", Toast.LENGTH_SHORT).show();
            return;
        }

        View root = getView();
        if (root != null) {
            TextView questionText = root.findViewById(R.id.questionText);
            questionText.setText(currentQuestion.getQuestionText());

            // Przekazujemy pełną tablicę odpowiedzi do AnswersViewFragment
            String[] options = currentQuestion.getAnswerOptions();
            int correctIdx = currentQuestion.getCorrectAnswerIndex();

            // Wstaw AnswersViewFragment z aktualnymi opcjami i indeksem poprawnej odpowiedzi
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.answers_fragment_container, AnswersViewFragment.newInstance(options, correctIdx))
                    .commit();
        }

        // (opcjonalnie możemy śledzić liczbę pokazanych pytań)
        QuestionNumber++;
    }

    private void resetQuiz() {
        QuestionNumber = 0;
        currentQuestion = null;
    }


    public interface CorrectAnswerSelectedListener {
        void onCorrectAnswerSelected();
    }

    public interface QuizResetListener {
        void onQuizReset();
    }

    public interface QuizFinishedListener {
        void onQuizFinished(int totalQuestions);
    }
}
