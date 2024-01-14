package com.example.betterher.informationhub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterher.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InformationHubFragment extends Fragment {
    private static final String TAG = "InformationHubActivity";

    private SectionListAdapter sectionListAdapter;
    private RecyclerView rvInformationHub;
    private List<Section> allSections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_hub, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        rvInformationHub = view.findViewById(R.id.rvInformationHub);
        rvInformationHub.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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

        return view;
    }


    private void loadContentFromFirestore(FirebaseFirestore db, String field, String sectionTitle, AtomicInteger pendingLoads) {
        db.collection("Content").whereEqualTo("field", field)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SectionCard> sectionCardsList = new ArrayList<>();
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        Collections.shuffle(documents);

                        for (DocumentSnapshot document : documents) {
                            Content content = document.toObject(Content.class);
                            SectionCard sectionCard = new SectionCard(content);
                            sectionCardsList.add(sectionCard);
                            if(sectionCardsList.size() >= 4) break;
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
                        return;
                    }
                });
    }

    private void scrollToTop() {
        rvInformationHub.scrollToPosition(0);
    }

}





