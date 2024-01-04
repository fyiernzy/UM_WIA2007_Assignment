package com.example.swipablecardtest.informationhub;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swipablecardtest.R;

public class SectionViewHolder extends RecyclerView.ViewHolder {
    TextView tvSectionTitle;
    RecyclerView sectionRecyclerView;
    SectionCardListAdapter sectionCardListAdapter; // Adapter for the section's RecyclerView

    SectionViewHolder(View itemView) {
        super(itemView);
        this.tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
        this.sectionRecyclerView = itemView.findViewById(R.id.sectionRecyclerView);

        // Initialize the RecyclerView and its adapter here or in the binding method
        sectionRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        sectionCardListAdapter = new SectionCardListAdapter(new SectionCardDiff()); // Assuming SectionCardListAdapter exists and is appropriate for your data
        sectionRecyclerView.setAdapter(sectionCardListAdapter);
    }

    // Bind the Section data to the views and adapter
    public void bind(Section section) {
        tvSectionTitle.setText(section.getSectionTitle()); // Set the section title

        // Update the adapter with the SectionCards for this section
        sectionCardListAdapter.submitList(section.getSectionCards()); // Assuming Section has getSectionCards() method and adapter has submitList() method
    }
}

