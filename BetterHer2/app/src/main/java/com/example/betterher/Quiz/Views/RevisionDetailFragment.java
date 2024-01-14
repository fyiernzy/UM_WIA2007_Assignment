package com.example.betterher.Quiz.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.betterher.Quiz.Model.QuizListModel;
import com.example.betterher.Quiz.ViewModel.QuizListViewModel;
import com.example.betterher.R;

import java.util.List;

public class RevisionDetailFragment extends Fragment {

    private TextView title;
    private Button startRevisionBtn;
    private int position;
    private ProgressBar progressBar;
    private QuizListViewModel viewModel;
    private ImageView topicImage;
    private String quizId;
    private long totalQuesCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revision_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.detailFragmentTitle);
        startRevisionBtn = view.findViewById(R.id.StartRevisionButton);
        progressBar = view.findViewById(R.id.detailProgressBar);
        topicImage = view.findViewById(R.id.detailFragmentImage);

        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                QuizListModel quiz = quizListModels.get(position);
                title.setText(quiz.getTitle());
                Glide.with(view).load(quiz.getImage()).into(topicImage);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 1000);

                totalQuesCount = quiz.getQuestions();
                quizId = quiz.getQuizId();
            }
        });

        startRevisionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RevisionFragment revisionFragment = new RevisionFragment();

                // Pass necessary data to the QuizFragment using Bundle if needed
                Bundle args = new Bundle();
                args.putString("quizId", quizId);
                args.putLong("totalQuesCount", totalQuesCount);
                revisionFragment.setArguments(args);

                // Perform fragment transaction to navigate to QuizFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, revisionFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        });
    }
}