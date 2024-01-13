package com.example.betterher.informationhub;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class SectionDiff extends DiffUtil.ItemCallback<Section> {
    @Override
    public boolean areItemsTheSame(@NonNull Section oldSection, @NonNull Section newSection) {
        // Check if two Sections are the same based on a unique identifier (e.g., section title, ID)
        return oldSection.getSectionTitle().equals(newSection.getSectionTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Section oldSection, @NonNull Section newSection) {
        // Deep comparison of Section contents. This might include checking the equality of all properties
        // and the contents of the SectionCards list.

        // Check if basic properties are the same
        if (!oldSection.getSectionTitle().equals(newSection.getSectionTitle())) {
            return false;
        }

        // Check if the lists of SectionCards are the same size
        if (oldSection.getSectionCards().size() != newSection.getSectionCards().size()) {
            return false;
        }

        // Optionally, do a deeper comparison of each SectionCard (this can be expensive)
        // for (int i = 0; i < oldSection.getSectionCards().size(); i++) {
        //     if (!oldSection.getSectionCards().get(i).equals(newSection.getSectionCards().get(i))) {
        //         return false;
        //     }
        // }

        // If you've reached here, everything checked out
        return true;
    }
}
