package com.example.betterher.Quiz.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.betterher.Quiz.ViewModel.QuestionViewModel;
import com.example.betterher.R;

public class QuizHomeFragment extends Fragment {
    private QuestionViewModel viewModel;
    private TextView completedQuiz, averageScore;
    private Button startQuizBtn, revisionBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startQuizBtn = view.findViewById(R.id.BtnStartQuiz);
        revisionBtn = view.findViewById(R.id.BtnRevision);

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment listFragment = new ListFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, listFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        });

        revisionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment revisionFragment = new RevisionListFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, revisionFragment); // Replace with the appropriate revision fragment
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        });
    }
}