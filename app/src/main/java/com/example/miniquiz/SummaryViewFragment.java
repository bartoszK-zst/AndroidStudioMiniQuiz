package com.example.miniquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SummaryViewFragment extends Fragment {

    private static final String ARG_SCORE = "arg_score";
    private static final String ARG_TOTAL = "arg_total";

    public SummaryViewFragment() {
        super(R.layout.summary_view);
    }

    public static SummaryViewFragment newInstance(int score, int total) {
        SummaryViewFragment fragment = new SummaryViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_TOTAL, total);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        int score = 0;
        int total = 0;
        if (args != null) {
            score = args.getInt(ARG_SCORE, 0);
            total = args.getInt(ARG_TOTAL, 0);
        }

        TextView summaryTextView = view.findViewById(R.id.summaryTextView);
        if (summaryTextView != null) {
            summaryTextView.setText(getString(R.string.summary_format, score, total));
        }

        Button resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> {
            if (getActivity() instanceof QuizViewFragment.QuizResetListener) {
                ((QuizViewFragment.QuizResetListener) getActivity()).onQuizReset();
            }
        });
    }

}
