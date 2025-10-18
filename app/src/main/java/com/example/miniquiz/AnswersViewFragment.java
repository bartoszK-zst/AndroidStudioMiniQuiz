package com.example.miniquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnswersViewFragment extends Fragment {
    private static final String ARG_ANSWERS = "arg_answers";

    public AnswersViewFragment(){
        super(R.layout.answers_view);
    }

    // Factory method to create fragment with answers array
    public static AnswersViewFragment newInstance(String[] answers) {
        AnswersViewFragment fragment = new AnswersViewFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_ANSWERS, answers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button b0 = view.findViewById(R.id.answerButton0);
        Button b1 = view.findViewById(R.id.answerButton1);
        Button b2 = view.findViewById(R.id.answerButton2);

        String[] answers = null;
        if (getArguments() != null) {
            answers = getArguments().getStringArray(ARG_ANSWERS);
        }

        if (answers != null && answers.length > 2) {
            if (answers[0] != null) b0.setText(answers[0]);
            if (answers[1] != null) b1.setText(answers[1]);
            if (answers[2] != null) b2.setText(answers[2]);
        }

        View.OnClickListener listener0 = v -> notifyAnswerSelected(0);
        View.OnClickListener listener1 = v -> notifyAnswerSelected(1);
        View.OnClickListener listener2 = v -> notifyAnswerSelected(2);

        b0.setOnClickListener(listener0);
        b1.setOnClickListener(listener1);
        b2.setOnClickListener(listener2);
    }

    private void notifyAnswerSelected(int index) {
        // Najpierw sprawdź rodzica (fragment), potem aktywność
        if (getParentFragment() instanceof OnAnswerSelectedListener) {
            ((OnAnswerSelectedListener) getParentFragment()).onAnswerSelected(index);
        } else if (getActivity() instanceof OnAnswerSelectedListener) {
            ((OnAnswerSelectedListener) getActivity()).onAnswerSelected(index);
        }
    }

    // Interfejs callbacku dla kontenera
    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int index);
    }
}
