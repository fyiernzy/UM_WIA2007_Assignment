package com.example.signuploginfirebase.Quiz.Views;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.signuploginfirebase.Quiz.Model.QuestionModel;
import com.example.signuploginfirebase.Quiz.ViewModel.QuestionViewModel;
import com.example.signuploginfirebase.R;

import java.util.List;

public class RevisionFragment extends Fragment implements View.OnClickListener {
    private QuestionViewModel viewModel;
    private ImageView revisionCard;
    private ProgressBar progressBar;
    private Button downloadBtn, nextCardBtn, previousCardBtn;
    private TextView revisionTitle;
    private int currentCardNo = 1;
    private long totalCards;
    private List<QuestionModel> revisionCardList;
    private String quizId;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revision, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        revisionCard = view.findViewById(R.id.RevisionCard);
        progressBar = view.findViewById(R.id.detailProgressBar);
        downloadBtn = view.findViewById(R.id.downloadBtn);
        nextCardBtn = view.findViewById(R.id.nextCardBtn);
        previousCardBtn = view.findViewById(R.id.previousCardBtn);
        revisionTitle = view.findViewById(R.id.revisionTitle);

        quizId = getArguments().getString("quizId");
        viewModel.setQuizId(quizId);
        viewModel.getQuestions();

        downloadBtn.setOnClickListener(this);
        nextCardBtn.setOnClickListener(this);
        previousCardBtn.setOnClickListener(this);

        totalCards = getArguments().getLong("totalQuesCount");

        updateUI();
        loadRevisionCardImage(currentCardNo);
    }

    private void updateUI() {
        revisionTitle.setText("Revision Card " + currentCardNo + "/" + totalCards);
        previousCardBtn.setVisibility(currentCardNo > 1 ? View.VISIBLE : View.GONE);
        loadRevisionCardImage(currentCardNo);
        nextCardBtn.setText(currentCardNo == totalCards ? "Finish" : "Next");
    }

    private void loadRevisionCardImage(int i) {
        currentCardNo = i;
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                String revisionCardImageUrl = questionModels.get(i-1).getRevision_card();
                Glide.with(requireContext()).load(revisionCardImageUrl).into(revisionCard);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.downloadBtn) {
            downloadRevisionCardImage();
        } else if (viewId == R.id.nextCardBtn) {
            if (currentCardNo == totalCards) {
                // Perform a fragment transaction to navigate to RevisionListFragment
                RevisionListFragment revisionListFragment = new RevisionListFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, revisionListFragment); // Replace with the appropriate layout container ID
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            } else {
                currentCardNo++;
                updateUI();
                loadRevisionCardImage(currentCardNo);
            }
        } else if (viewId == R.id.previousCardBtn) {
            if (currentCardNo > 1) {
                currentCardNo--;
                updateUI();
                loadRevisionCardImage(currentCardNo);
            }
        }
    }

    private void downloadRevisionCardImage() {
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                String revisionCardImageUrl = questionModels.get(currentCardNo - 1).getRevision_card();

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(revisionCardImageUrl));
                request.setTitle("Revision Card Download");
                request.setDescription("Downloading revision card image...");

                // Save the downloaded file to the "Pictures" directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "revision_card_image.jpg");

                // Enqueue the download and get download ID
                DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadId = downloadManager.enqueue(request);

                // Listen for download completion and handle errors
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                        if (downloadId == id) {
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(id);

                            Cursor cursor = downloadManager.query(query);

                            if (cursor.moveToFirst()) {
                                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int status = cursor.getInt(statusIndex);

                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    // Download completed successfully
                                    Toast.makeText(requireContext(), "Image downloaded successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle download failure
                                    Toast.makeText(requireContext(), "Failed to download image", Toast.LENGTH_SHORT).show();
                                }
                            }

                            cursor.close();
                        }

                        // Unregister the BroadcastReceiver
                        requireContext().unregisterReceiver(this);
                    }
                };

                // Register the BroadcastReceiver to listen for download completion
                requireContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        });
    }
}