package com.example.betterher.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.betterher.Model.QuestionModel;
import com.example.betterher.R;
import com.example.betterher.viewmodel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private QuestionViewModel viewModel;
    private NavController navController;
    private ProgressBar progressBar;
    private Button option1Btn, option2Btn, option3Btn, nextQuesBtn;
    private TextView questionTv, ansFeedBackTv, questionNumberTv, timerCountTv;
    private ImageView closeQuizBtn;
    private String quizId;
    private long totalQuestions, timer;
    private int currentQuesNo = 0;
    private boolean canAnswer = false;
    private CountDownTimer countDownTimer;
    private int notAnswered = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        closeQuizBtn = view.findViewById(R.id.imageView3);
        option1Btn = view.findViewById(R.id.option1Btn);
        option2Btn = view.findViewById(R.id.option2Btn);
        option3Btn = view.findViewById(R.id.option3Btn);
        nextQuesBtn = view.findViewById(R.id.nextQuesBtn);
        ansFeedBackTv = view.findViewById(R.id.ansFeedbackTv);
        questionTv = view.findViewById(R.id.quizQuestionTv);
        timerCountTv = view.findViewById(R.id.countTimeQuiz);
        questionNumberTv = view.findViewById(R.id.quizQuestionsCount);
        progressBar = view.findViewById(R.id.progressBar);

        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuesCount();
        viewModel.setQuizId(quizId);
        viewModel.getQuestions();

        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        nextQuesBtn.setOnClickListener(this);

        closeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_quizFragment_to_listFragment);
            }
        });

        loadData();
    }

    private void loadData() {
        enableOptions();
        loadQuestions(1);
    }

    private void enableOptions() {
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);

        // enable buttons, hide feedback tv, hide nextQuiz btn
        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);

        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQuesBtn.setVisibility(View.INVISIBLE);
    }

    private void loadQuestions(int i) {
        currentQuesNo = i;
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                questionTv.setText(String.valueOf(currentQuesNo) + ") " + questionModels.get(i -1).getQuestion());
                option1Btn.setText(questionModels.get(i-1).getOption_a());
                option2Btn.setText(questionModels.get(i-1).getOption_b());
                option3Btn.setText(questionModels.get(i-1).getOption_c());
                timer = questionModels.get(i-1).getTimer();
                answer = questionModels.get(i-1).getAnswer();

                questionNumberTv.setText(String.valueOf(currentQuesNo));
                startTimer();

            }
        });

        canAnswer = true;
    }

    private void startTimer() {
        timerCountTv.setText(String.valueOf(timer));
        progressBar.setVisibility(View.VISIBLE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timer * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // update time
                timerCountTv.setText(millisUntilFinished / 1000 + "");

                Long percent = millisUntilFinished/(timer * 10);
                progressBar.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedBackTv.setText("Times up!! No answer selected");
                notAnswered++;
                showNextBtn();
            }
        }.start();
    }

    private void showNextBtn() {
        if (currentQuesNo == totalQuestions) {
            nextQuesBtn.setText("Submit");
            nextQuesBtn.setEnabled(true);
            nextQuesBtn.setVisibility(View.VISIBLE);
        } else {
            nextQuesBtn.setVisibility(View.VISIBLE);
            nextQuesBtn.setEnabled(true);
            ansFeedBackTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.option1Btn) {
            verifyAnswer(option1Btn);
        } else if (viewId == R.id.option2Btn) {
            verifyAnswer(option2Btn);
        } else if (viewId == R.id.option3Btn) {
            verifyAnswer(option3Btn);
        } else if (viewId == R.id.nextQuesBtn) {
            if (currentQuesNo == totalQuestions) {
                submitResults();
            } else {
                currentQuesNo++;
                loadQuestions(currentQuesNo);
                resetOptions();
            }
        }
    }

    private void resetOptions() {
        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQuesBtn.setVisibility(View.INVISIBLE);
        nextQuesBtn.setEnabled(false);
        option1Btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.pink));
        option2Btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.pink));
        option3Btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.pink));
    }

    private void submitResults() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswer);
        resultMap.put("wrong", wrongAnswer);
        resultMap.put("notAnswered", notAnswered);

        viewModel.addResults(resultMap);
        QuizFragmentDirections.ActionQuizFragmentToResultFragment action =
                QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setQuizId(quizId);
        navController.navigate(action);
    }

    private void verifyAnswer(Button selectedButton) {
        if (canAnswer) {
            countDownTimer.cancel();
            String selectedOption = selectedButton.getText().toString();
            String correctOption = answer;

            if (selectedOption.equals(correctOption)) {
                // Correct answer
                selectedButton.setBackground(ContextCompat.getDrawable(getContext(), R.color.green));
                correctAnswer++;
            } else {
                // Wrong answer
                selectedButton.setBackground(ContextCompat.getDrawable(getContext(), R.color.red));
                showCorrectOption(correctOption);
                wrongAnswer++;
            }

            // Display the explanation
            ansFeedBackTv.setText(viewModel.getQuestionMutableLiveData().getValue().get(currentQuesNo - 1).getExplanation());

            showNextBtn();
        }
        canAnswer = false;
    }

    private void showCorrectOption(String correctOption) {
        // Determine which option contains the correct answer and turn it green
        if (correctOption.equals(option1Btn.getText().toString())) {
            option1Btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.green));
        } else if (correctOption.equals(option2Btn.getText().toString())) {
            option2Btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.green));
        } else if (correctOption.equals(option3Btn.getText().toString())) {
            option3Btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.green));
        }
    }
}