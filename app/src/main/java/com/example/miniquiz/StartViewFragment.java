package com.example.miniquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartViewFragment extends Fragment {

    public StartViewFragment() {
        super(R.layout.start_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button startButton = view.findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            if (getActivity() instanceof OnStartButtonClickListener) {
                ((OnStartButtonClickListener) getActivity()).onStartButtonClicked();
            }
        });
    }

    public interface OnStartButtonClickListener {
        void onStartButtonClicked();
    }
}
