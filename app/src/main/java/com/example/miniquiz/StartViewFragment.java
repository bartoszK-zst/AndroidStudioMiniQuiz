package com.example.miniquiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class StartViewFragment extends Fragment {
    private Button startButton;

    public StartViewFragment() {
        super(R.layout.start_view);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        startButton = view.findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            // Sprawdź, czy aktywność implementuje nasz interfejs:
            if (getActivity() instanceof OnStartButtonClickListener) {
                ((OnStartButtonClickListener) getActivity()).onStartButtonClicked();
            }
        });
    }

    public interface OnStartButtonClickListener {
        void onStartButtonClicked();
    }
}
