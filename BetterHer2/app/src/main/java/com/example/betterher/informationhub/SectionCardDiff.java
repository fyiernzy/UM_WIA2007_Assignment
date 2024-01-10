package com.example.betterher.informationhub;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class SectionCardDiff extends DiffUtil.ItemCallback<SectionCard> {
    @Override
    public boolean areItemsTheSame(@NonNull SectionCard oldItem, @NonNull SectionCard newItem) {
        // Implement your logic to check if two SectionCards are the same
        return oldItem.getContent().equals(newItem.getContent()); // Replace getId() with the actual method to get the identifier of the SectionCard
    }

    @Override
    public boolean areContentsTheSame(@NonNull SectionCard oldItem, @NonNull SectionCard newItem) {
        // Implement your logic to check if the contents of two SectionCards are the same
        return oldItem.equals(newItem); // This assumes a properly overridden .equals() method in your SectionCard class
    }
}
