package com.example.betterher.informationhub;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentRepository {
    private FirebaseFirestore db;
    private MutableLiveData<List<Content>> allContents;

    ContentRepository(Application application) {
        this.db = FirebaseFirestore.getInstance();
        this.allContents = new MutableLiveData<>();
        fetchAllContentsAndDetermineTopFields();
    }

    LiveData<List<Content>> getAllContents() {
        return this.allContents;
    }

    private void fetchAllContentsAndDetermineTopFields() {
        db.collection("Content")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, Integer> fieldCount = tallyFields(queryDocumentSnapshots);
                    List<String> topFields = determineTopFields(fieldCount, 3);
                    fetchContentsForTopFields(topFields);
                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                });
    }

    private Map<String, Integer> tallyFields(QuerySnapshot queryDocumentSnapshots) {
        Map<String, Integer> fieldCount = new HashMap<>();
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
            String field = documentSnapshot.getString("field");
            if (field != null) {
                fieldCount.put(field, fieldCount.getOrDefault(field, 0) + 1);
            }
        }
        return fieldCount;
    }

    private List<String> determineTopFields(Map<String, Integer> fieldCount, int limit) {
        return fieldCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void fetchContentsForTopFields(List<String> topFields) {
        for (String field : topFields) {
            db.collection("Content")
                    .whereEqualTo("field", field)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Content> contents = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Content content = documentSnapshot.toObject(Content.class);
                            contents.add(content);
                        }
                        updateLiveData(contents);
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors here
                    });
        }
    }

    private synchronized void updateLiveData(List<Content> newContents) {
        List<Content> currentContents = allContents.getValue();
        if (currentContents == null) currentContents = new ArrayList<>();
        currentContents.addAll(newContents);
        allContents.postValue(currentContents);
    }
}


