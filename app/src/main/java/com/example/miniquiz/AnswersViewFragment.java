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
    private static final String ARG_CORRECT = "arg_correct";

    public AnswersViewFragment(){
        super(R.layout.answers_view);
    }

    // metoda fabryczna do tworzenia fragmentu z podanymi odpowiedziami i indeksem poprawnej
    public static AnswersViewFragment newInstance(String[] answers, int correctIndex) {
        AnswersViewFragment fragment = new AnswersViewFragment();
        Bundle args = new Bundle();
        // tablica odpowiedzi i indeks poprawnej zostają przekazane jako argumenty
        args.putStringArray(ARG_ANSWERS, answers);
        args.putInt(ARG_CORRECT, correctIndex);
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
        int correctIndex = -1;
        if (getArguments() != null) {
            answers = getArguments().getStringArray(ARG_ANSWERS);
            correctIndex = getArguments().getInt(ARG_CORRECT, -1);
        }

        // Zbiera wszystkie indeksy, które mają tekst
        List<Integer> available = new ArrayList<>();
        if (answers != null) {
            for (int i = 0; i < answers.length; i++) {
                if (answers[i] != null) available.add(i);
            }
        }

        // Wybiera maksymalnie 3 unikalne indeksy z available
        List<Integer> chosen = new ArrayList<>();
        if (!available.isEmpty()) {
            Collections.shuffle(available, new Random());
            int take = Math.min(3, available.size());

            // Jeśli poprawny indeks jest dostępny i jest dla niego miejsce, zostaje dodany pierwszy
            if (correctIndex >= 0 && available.contains(correctIndex)) {
                chosen.add(correctIndex);
            }

            // Dodaje pozostałe losowe indeksy, pomijając już dodane
            for (int i = 0; i < available.size() && chosen.size() < take; i++) {
                int val = available.get(i);
                if (!chosen.contains(val)) {
                    chosen.add(val);
                }
            }
        }

        // Losowa kolejność przycisków (pozycji), aby teksty rozmieszczały się losowo
        List<Integer> positions = new ArrayList<>(Arrays.asList(0,1,2));
        Collections.shuffle(positions, new Random());

        // Na czas ustawiania przyciski zostają ukryte i odczyszczone
        for (Button btn : buttons) {
            btn.setVisibility(View.GONE);
            btn.setOnClickListener(null);
            btn.setText("");
        }

        // Rozmieszcza wybrane odpowiedzi na przyciskach w losowej kolejności
        for (int j = 0; j < chosen.size(); j++) {
            int originalIndex = chosen.get(j); // indeks w przekazanej tablicy answers
            int pos = positions.get(j); // miejsce na przycisku
            String text = "";
            if (answers != null && originalIndex >= 0 && originalIndex < answers.length) {
                text = answers[originalIndex];
            }
            Button btn = buttons[pos];
            btn.setText(text != null ? text : "");
            btn.setVisibility(View.VISIBLE);
            final int idx = originalIndex; // przekażemy oryginalny indeks
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
