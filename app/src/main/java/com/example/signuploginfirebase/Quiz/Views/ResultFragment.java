package com.example.signuploginfirebase.Quiz.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.signuploginfirebase.Quiz.ViewModel.QuestionViewModel;
import com.example.signuploginfirebase.R;

import java.util.HashMap;

public class ResultFragment extends Fragment {
    private QuestionViewModel viewModel;
    private TextView correctAnswer , wrongAnswer , notAnswered;
    private TextView percentTv;
    private ProgressBar scoreProgressbar;
    private String quizId;
    private Button homeBtn;

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
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        correctAnswer = view.findViewById(R.id.correctAnswerTv);
        wrongAnswer = view.findViewById(R.id.wrongAnswersTv);
        notAnswered = view.findViewById(R.id.notAnsweredTv);
        percentTv = view.findViewById(R.id.resultPercentageTv);
        scoreProgressbar = view.findViewById(R.id.resultCountProgressBar);
        homeBtn = view.findViewById(R.id.home_btn);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToQuizHomeFragment();
            }
        });

        Bundle args = getArguments();
        if (args != null && args.containsKey("quizId")) {
            quizId = args.getString("quizId");
        } else {
            // Handle the scenario when quizId is not available in the arguments
            // You might want to add appropriate error handling or a default behavior here
        }

        viewModel.setQuizId(quizId);
        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {

                Long correct = stringLongHashMap.get("correct");
                Long wrong = stringLongHashMap.get("wrong");
                Long noAnswer = stringLongHashMap.get("notAnswered");

                correctAnswer.setText(correct.toString());
                wrongAnswer.setText(wrong.toString());
                notAnswered.setText(noAnswer.toString());

                Long total = correct + wrong + noAnswer;
                Long percent = (correct*100)/total;

                percentTv.setText(String.valueOf(percent) + "%");
                scoreProgressbar.setProgress(percent.intValue());
            }
        });

    }

    private void navigateToQuizHomeFragment() {
        // Replace "QuizHomeFragment" with the actual name of your QuizHomeFragment class
        Fragment quizHomeFragment = new QuizHomeFragment();

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.fragment_container, quizHomeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}