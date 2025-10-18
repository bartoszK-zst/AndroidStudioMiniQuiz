package com.example.miniquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        Button[] buttons = new Button[]{b0, b1, b2};

        String[] answers = null;
        if (getArguments() != null) {
            answers = getArguments().getStringArray(ARG_ANSWERS);
        }

        // Przygotuj listę dostępnych indeksów odpowiedzi (maksymalnie 3)
        List<Integer> available = new ArrayList<>();
        if (answers != null) {
            for (int i = 0; i < answers.length && i < 3; i++) {
                available.add(i);
            }
        }

        // Losowa kolejność pozycji przycisków
        List<Integer> positions = new ArrayList<>(Arrays.asList(0,1,2));
        Collections.shuffle(positions, new Random());

        // Ukryj wszystkie przyciski na start i usuń listenery
        for (Button btn : buttons) {
            btn.setVisibility(View.GONE);
            btn.setOnClickListener(null);
        }

        // Rozmieść dostępne odpowiedzi we wylosowanych pozycjach
        for (int j = 0; j < available.size(); j++) {
            int sourceIndex = available.get(j); // indeks w tablicy answers
            int pos = positions.get(j); // miejsce, w którym pokażemy tę odpowiedź
            String text = "";
            if (answers != null && sourceIndex >= 0 && sourceIndex < answers.length) {
                text = answers[sourceIndex];
            }
            Button btn = buttons[pos];
            btn.setText(text != null ? text : "");
            btn.setVisibility(View.VISIBLE);
            final int idx = sourceIndex; // finalna kopia indeksu źródłowego
            btn.setOnClickListener(v -> notifyAnswerSelected(idx));
        }
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
