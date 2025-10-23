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

        // Reset i ustawienia UI
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
        // Sprawdź czy mamy aktualne pytanie
        if (currentQuestion == null) {
            Toast.makeText(getContext(), "Brak pytania.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (index == currentQuestion.getCorrectAnswerIndex()) {
            Toast.makeText(getContext(), "Poprawna odpowiedź!", Toast.LENGTH_SHORT).show();
            // Powiadom kontener (np. aktywność) że zaznaczono poprawną odpowiedź
            if (getActivity() instanceof CorrectAnswerSelectedListener) {
                ((CorrectAnswerSelectedListener) getActivity()).onCorrectAnswerSelected();
            }
        } else {
            Toast.makeText(getContext(), "Błędna odpowiedź!", Toast.LENGTH_SHORT).show();
        }

        // Jeżeli aktywność obsługuje wydarzenia, przekazuje dalej
        if (getActivity() instanceof AnswersViewFragment.OnAnswerSelectedListener) {
            ((AnswersViewFragment.OnAnswerSelectedListener) getActivity()).onAnswerSelected(index);
        }

        // Sprawdza czy zadał już określoną liczbę pytań
        if (QuestionNumber >= TotalQuestions) {
            if (getActivity() instanceof QuizFinishedListener) {
                ((QuizFinishedListener) getActivity()).onQuizFinished(TotalQuestions);
            }
        }

        // Przejdź do następnego pytania
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

            // Przygotuj opcje odpowiedzi (upewnij się, że jest przynajmniej 3 elementy)
            String[] options = currentQuestion.getAnswerOptions();
            if (options == null) {
                options = new String[]{"", "", ""};
            } else if (options.length < 3) {
                String[] tmp = new String[3];
                for (int i = 0; i < 3; i++) tmp[i] = i < options.length ? options[i] : "";
                options = tmp;
            }

            // Wstaw AnswersViewFragment z aktualnymi opcjami
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.answers_fragment_container, AnswersViewFragment.newInstance(options))
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
