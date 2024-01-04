package com.example.swipablecardtest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.swipablecardtest.informationhub.Content;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS = "prefs";
    private static final String SWIPE_COUNT_KEY = "swipeCount";
    private static final String SWIPE_DATE_KEY = "swipeDate";
    private static final int MAX_SWIPES_PER_DAY = 3;

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
                    Toast.makeText(MainActivity.this, "You've reached the limit of swipes for today.", Toast.LENGTH_SHORT).show();
                    cardStackView.rewind();
                    return;
                }

                // Paginating
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
        fetchContentFromFirestore();
    }

    private void fetchContentFromFirestore() {
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
                        // Randomize the list if needed
                        Collections.shuffle(newContentList);
                        // Update the adapter with new data
                        updateCardStack(newContentList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void updateCardStack(List<Content> newContentList) {
        // Use submitList to update the list
        adapter.submitList(newContentList);
    }

    private void paginate() {
        // Fetch more content from Firestore or elsewhere and update the adapter
        fetchContentFromFirestore();
    }
}

