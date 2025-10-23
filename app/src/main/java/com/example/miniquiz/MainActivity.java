package com.example.miniquiz;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity
        implements StartViewFragment.OnStartButtonClickListener,
            QuizViewFragment.QuizResetListener,
            QuizViewFragment.CorrectAnswerSelectedListener,
            QuizViewFragment.QuizFinishedListener{

    private int Score = 0;
    private final int TotalQuestions = 5;
    private TextView scoreTextView;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        scoreTextView = findViewById(R.id.resultTextView);
        // ustaw początkowy tekst wyniku
        if (scoreTextView != null) {
            scoreTextView.setText(getString(R.string.result_format, Score, TotalQuestions));
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, StartViewFragment.class, null)
                    .commit();
        }

    }

    private void setScore(int score) {
        this.Score = score;
        if (scoreTextView != null) {
            scoreTextView.setText(getString(R.string.result_format, Score, TotalQuestions));
        }
    }

    private void incrementScore() {
        setScore(this.Score + 1);
    }

    @Override
    public void onStartButtonClicked() {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container_view, com.example.miniquiz.QuizViewFragment.newInstance())
                .setReorderingAllowed(true)
                .addToBackStack(null) // dzięki temu po back wrócisz do StartViewFragment
                .commit();
    }

    @Override
    public void onCorrectAnswerSelected() {
        incrementScore();
        if (scoreTextView != null) {
            scoreTextView.setText(getString(R.string.result_format, Score, TotalQuestions));
        }
    }

    @Override
    public void onQuizReset() {
        // Najpierw zresetuj wynik
        setScore(0);
        // Zastąp aktualny fragment ekranem startowym (zamiast add, używamy replace)
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, StartViewFragment.class, null)
                .commit();
    }

    @Override
    public void onQuizFinished(int totalQuestions) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, SummaryViewFragment.newInstance(Score, totalQuestions))
                .commit();
    }
}