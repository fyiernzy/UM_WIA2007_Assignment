package com.example.swipablecardtest.informationhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swipablecardtest.R;

public class SectionListAdapter extends ListAdapter<Section, RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public SectionListAdapter() {
        super(new SectionDiff()); // Assuming SectionDiff is correctly implemented
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1; // Add one for the header
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_combined, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
            return new SectionViewHolder(view); // Assuming you have a SectionViewHolder class
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            // Configure your header here if needed
        } else {
            Section section = getItem(position); // Adjust position for header
            ((SectionViewHolder) holder).bind(section); // Assuming your SectionViewHolder has a bind method
        }
    }

    // Override getItem to handle the header offset
    @Override
    public Section getItem(int position) {
        return super.getItem(position - 1); // Adjust for the header
    }

    // HeaderViewHolder Class
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderTitle;
        SearchView searchView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeaderTitle = itemView.findViewById(R.id.tvHeaderTitle); // The title TextView
            searchView = itemView.findViewById(R.id.searchView); // The SearchView
            searchView.setIconified(false);
        }
    }

}

