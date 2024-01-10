package com.example.betterher.informationhub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class SectionCardListAdapter extends ListAdapter<SectionCard, SectionCardViewHolder> {

    public SectionCardListAdapter(@NonNull DiffUtil.ItemCallback<SectionCard> diffCallback) {
        super(diffCallback); // Provide the DiffUtil.ItemCallback for SectionCard here
    }

    @NonNull
    @Override
    public SectionCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SectionCardViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionCardViewHolder holder, int position) {
        SectionCard currentSectionCard = getItem(position); // Correctly retrieves the item from the internal list

        // Assuming the bind method in SectionCardViewHolder now takes a SectionCard object
        holder.bind(currentSectionCard);

        holder.itemView.setOnClickListener(view -> {
            String url = currentSectionCard.getUrl();
            Log.d("Hello", url);
            openWebPage(url, view.getContext());
        });
    }

    private void openWebPage(String url, Context context) {
        if (url != null && !url.isEmpty()) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(intent);
        }
    }
}
