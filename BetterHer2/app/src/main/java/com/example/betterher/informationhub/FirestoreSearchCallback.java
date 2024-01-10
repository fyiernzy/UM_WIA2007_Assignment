package com.example.betterher.informationhub;

import java.util.List;

public interface FirestoreSearchCallback {
    void onSearchResult(List<Content> results);
    void onError(Exception e);
}

