package com.example.betterher.card;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.betterher.R;
import com.example.betterher.informationhub.Content;
import com.example.betterher.informationhub.InformationHubFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecommendationActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS = "prefs";
    private static final String SWIPE_COUNT_KEY = "swipeCount";
    private static final String SWIPE_DATE_KEY = "swipeDate";
    private static final int MAX_SWIPES_PER_DAY = 3;
    private boolean isLimitCardDisplayed = false;

    private CardStackListAdapter adapter;
    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        this.cardStackView = findViewById(R.id.cardStackView);
        this.manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) { }

            @Override
            public void onCardSwiped(Direction direction) {
                if (isLimitCardDisplayed) {
                    // Check if the swiped card is the limit card
                    int topPosition = manager.getTopPosition();
                    Content currentCard = adapter.getCurrentList().get(topPosition - 1);
                    if (currentCard.isLast()) {
                        redirectToInformationHub();
                    }
                    return;
                }

                int swipeCount = prefs.getInt(SWIPE_COUNT_KEY, 0);
                String lastSwipeDate = prefs.getString(SWIPE_DATE_KEY, "");
                String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                if (!todayDate.equals(lastSwipeDate)) {
                    // If it's a new day, reset the swipe count and set the new date
                    prefs.edit()
                            .putInt(SWIPE_COUNT_KEY, 1)
                            .putString(SWIPE_DATE_KEY, todayDate)
                            .apply();
                } else if (swipeCount < MAX_SWIPES_PER_DAY) {
                    // Increment the swipe count for the day
                    prefs.edit().putInt(SWIPE_COUNT_KEY, swipeCount + 1).apply();
                } else {
                    // Max swipes reached for the day
                    showLimitCard();
                    return;
                }

                // Paginating - only proceed here if the limit card is not shown
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }
            }


            @Override
            public void onCardRewound() { }

            @Override
            public void onCardCanceled() { }

            @Override
            public void onCardAppeared(View view, int position) { }

            @Override
            public void onCardDisappeared(View view, int position) { }
        });

        cardStackView.setLayoutManager(manager);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        this.adapter = new CardStackListAdapter();
        cardStackView.setAdapter(adapter);

        // Fetch data from Firestore and set up the adapter
        paginate();
    }

    private void showLimitCard() {
        isLimitCardDisplayed = true;
        Content limitContent = new Content(); // Create a limit content
        limitContent.setIsLast(true);
        // Assuming you have a method to get the current list of items
        List<Content> currentList = adapter.getCurrentList();

        // Add the limit content to the list
        List<Content> updatedList = new ArrayList<>(currentList);
        updatedList.add(limitContent);

        // Submit the updated list to the adapter
        adapter.submitList(updatedList);
    }


    private void redirectToInformationHub() {
        Intent intent = new Intent(this, InformationHubFragment.class);
        startActivity(intent);
    }

    private void updateCardStack(List<Content> newContentList) {
        // Use submitList to update the list
        adapter.submitList(newContentList);
    }

    private void paginate() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Content")
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Content> newContentList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Content content = document.toObject(Content.class);
                            newContentList.add(content);
                        }
                        // Check if there are no more items to add
                        if (newContentList.isEmpty()) {
                            addEndCard();
                        } else {
                            Collections.shuffle(newContentList); // Randomize if needed
                            updateCardStack(newContentList); // Update the adapter
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void addEndCard() {
        Content endContent = new Content();
        // Set the flag or type in Content to mark it as the 'End' card
        endContent.setIsLast(true); // Assuming you have a setEndCard method

        List<Content> currentList = adapter.getCurrentList();
        List<Content> updatedList = new ArrayList<>(currentList);
        updatedList.add(endContent);
        adapter.submitList(updatedList);
    }

}

