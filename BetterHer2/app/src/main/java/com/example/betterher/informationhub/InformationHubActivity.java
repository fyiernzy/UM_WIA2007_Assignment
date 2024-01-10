package com.example.betterher.informationhub;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterher.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InformationHubActivity extends AppCompatActivity {

    private SectionListAdapter sectionListAdapter;
    private RecyclerView rvInformationHub;
    private List<Section> allSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_hub);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        rvInformationHub = findViewById(R.id.rvInformationHub);
        rvInformationHub.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        sectionListAdapter = new SectionListAdapter();
        rvInformationHub.setAdapter(sectionListAdapter);
        allSections = new ArrayList<>();

        // Define the fields to load from Firestore
        String[] fields = new String[]{"Workspace", "Family", "Education"};
        String[] titles = new String[]{"Workspace", "Family", "Education"};

        // Load content for the specified fields
        AtomicInteger pendingLoads = new AtomicInteger(fields.length);
        for (int i = 0; i < fields.length; i++) {
            loadContentFromFirestore(db, fields[i], titles[i], pendingLoads);
        }
    }

    private void loadContentFromFirestore(FirebaseFirestore db, String field, String sectionTitle, AtomicInteger pendingLoads) {
        db.collection("Content").whereEqualTo("field", field)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SectionCard> sectionCardsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Content content = document.toObject(Content.class);
                            SectionCard sectionCard = new SectionCard(content);
                            sectionCardsList.add(sectionCard);
                        }

                        synchronized (allSections) {
                            allSections.add(new Section(sectionTitle, sectionCardsList));
                        }

                        if (pendingLoads.decrementAndGet() == 0) {
                            // Sort or order sections if needed
                            // Collections.sort(allSections, yourComparator);
                            sectionListAdapter.submitList(new ArrayList<>(allSections));
                            scrollToTop();
                        }
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void scrollToTop() {
        rvInformationHub.scrollToPosition(0);
    }

}





