package com.example.betterher.TrackCases;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.betterher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TrackCasesFragment extends Fragment {
    private LinearLayout layoutIndicators;
    private FirebaseFirestore firestore;
    private CollectionReference casesCollection;
    private CollectionReference progressesCollection;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_cases, container, false);
        ViewPager2 viewPager2 = view.findViewById(R.id.vp_cases);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        layoutIndicators = view.findViewById(R.id.ll_indicators);
        firestore = FirebaseFirestore.getInstance();
        casesCollection = firestore.collection("Users").document("bEBpClWNtU4U0BnnB8jX").collection("Cases");

        parentItemList(new FirestoreCallback() {
            @Override
            public void onCallback(List<TrackCasesParentItem> parentItemList) {
                TrackCasesParentAdapter parentAdapter = new TrackCasesParentAdapter(parentItemList);
                viewPager2.setAdapter(parentAdapter);
                setIndicators(parentAdapter.getItemCount());
                progressBar.setVisibility(View.GONE);
            }
        });

        setCurrentIndicator(0);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        return view;
    }

    public interface FirestoreCallback {
        void onCallback(List<TrackCasesParentItem> parentItemList);
    }

    public void parentItemList(FirestoreCallback callback) {
        List<TrackCasesParentItem> parentItemList = new ArrayList<>();

        casesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int caseCount = task.getResult().size();
                    AtomicInteger counter = new AtomicInteger(0);

                    for (DocumentSnapshot caseDocument : task.getResult()) {
                        // Get data from Case document (parent)
                        String caseID = caseDocument.getString("caseId");
                        String incidentType = caseDocument.getString("incidentType");
                        String incidentDate = caseDocument.getString("incidentDate");
                        String incidentTime = caseDocument.getString("incidentTime");
                        String incidentLocation = caseDocument.getString("incidentLocation");
                        String caseStatus = caseDocument.getString("caseStatus");
                        TrackCasesParentItem trackCasesParentItem = new TrackCasesParentItem(caseID, incidentType, incidentDate, incidentTime, incidentLocation, caseStatus);

                        progressesCollection = caseDocument.getReference().collection("Progress");
                        progressesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<TrackCasesChildItem> progressList = new ArrayList<>();
                                    for (DocumentSnapshot progressDocument : task.getResult()) {
                                        // Get data from Progress document (child)
                                        String progressDate = progressDocument.getString("progressDate");
                                        String progressTime = progressDocument.getString("progressTime");
                                        String progressTitle = progressDocument.getString("progressTitle");
                                        String progressDesc = progressDocument.getString("progressDesc");

                                        TrackCasesChildItem progress = new TrackCasesChildItem(progressDate, progressTime, progressTitle, progressDesc, caseStatus);
                                        progressList.add(progress);
                                    }
                                    trackCasesParentItem.setTrackCasesChildItems(progressList);
                                    parentItemList.add(trackCasesParentItem);

                                    if (counter.incrementAndGet() == caseCount) {
                                        callback.onCallback(parentItemList);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void setIndicators(int itemCount) {
        ImageView[] indicators = new ImageView[itemCount];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(requireContext().getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    requireContext().getApplicationContext(),
                    R.drawable.track_cases_inactive_indicator
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext().getApplicationContext(), R.drawable.track_cases_active_indicator)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext().getApplicationContext(), R.drawable.track_cases_inactive_indicator)
                );
            }
        }
    }
}