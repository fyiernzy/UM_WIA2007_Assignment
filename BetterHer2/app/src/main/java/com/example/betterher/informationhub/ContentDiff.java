package com.example.swipablecardtest.informationhub;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ContentDiff extends DiffUtil.ItemCallback<Content> {
    @Override
    public boolean areItemsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
        // Implement your logic to check if items are the same (usually ID comparison)
        return oldItem.getUrl().equals(newItem.getUrl());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
        // Implement your logic to check if the contents of the items are the same
        return oldItem.equals(newItem);
    }
}
