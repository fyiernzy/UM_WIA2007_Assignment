package com.example.betterher.informationhub;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterher.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    TextView tvHeaderTitle;
    SearchView searchView;
    PopupWindow suggestionPopup;
    ArrayAdapter<String> suggestionAdapter;
    ListView suggestionListView;

    HeaderViewHolder(View itemView, FirestoreSearchCallback callback) {
        super(itemView);
        tvHeaderTitle = itemView.findViewById(R.id.tvHeaderTitle); // The title TextView
        searchView = itemView.findViewById(R.id.searchView); // The SearchView
        searchView.setIconified(false);

        // Initialize the PopupWindow
        suggestionListView = new ListView(itemView.getContext());
        suggestionAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());

        suggestionListView.setAdapter(suggestionAdapter);

        suggestionPopup = new PopupWindow(itemView.getContext());

        suggestionPopup.setWidth(LayoutParams.WRAP_CONTENT);
        suggestionPopup.setHeight(LayoutParams.WRAP_CONTENT);
        suggestionPopup.setFocusable(true);
        suggestionPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        suggestionPopup.setContentView(suggestionListView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContentByTitle(query, callback);

                // Hide the soft keyboard after search
                InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(itemView.getWindowToken(), 0);

                suggestionPopup.dismiss();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    searchTitles(newText);
                } else {
                    suggestionPopup.dismiss();
                }
                return false; // Return false to let the SearchView handle the default action of showing suggestions
            }

        });

        suggestionListView.setOnItemClickListener((parent, view, position, id) -> {
            String selection = suggestionAdapter.getItem(position);
            searchView.setQuery(selection, true); // Set the query text and submit
            suggestionPopup.dismiss();
        });
    }

    private void searchTitles(String searchText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Contents")
                .orderBy("title")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff") // This is a high Unicode value to include all strings that start with `searchText`
                .limit(10) // Limit the number of results to avoid fetching too many documents
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> titleSuggestions = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title = documentSnapshot.getString("title");
                        titleSuggestions.add(title);
                    }
                    suggestionAdapter.clear();
                    suggestionAdapter.addAll(titleSuggestions);
                    suggestionAdapter.notifyDataSetChanged();
                    if (!suggestionPopup.isShowing() && !titleSuggestions.isEmpty()) {
                        suggestionPopup.showAsDropDown(searchView);
                    }
                    suggestionPopup.update();


                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    private void searchContentByTitle(String title, FirestoreSearchCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Content")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Content> matchedContents = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Content content = documentSnapshot.toObject(Content.class);
                        matchedContents.add(content);
                    }
                    callback.onSearchResult(matchedContents);
                })
                .addOnFailureListener(callback::onError);
    }
}
