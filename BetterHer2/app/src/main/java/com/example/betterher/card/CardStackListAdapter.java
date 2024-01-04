package com.example.swipablecardtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swipablecardtest.informationhub.Content;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class CardStackListAdapter extends ListAdapter<Content, CardStackListAdapter.CardViewHolder> {

    public CardStackListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Content content = getItem(position);
        holder.tvTitle.setText(content.getTitle());
        holder.tvAuthor.setText(content.getAuthor());

        // Use a library like Glide or Picasso to load images from URLs
        // Example using Glide (make sure you've added Glide to your project):
        Glide.with(holder.itemView.getContext())
                .load(content.getImageUrl())
                .placeholder(R.drawable.img_1) // Optional placeholder while image loads
                .into(holder.ivImage);

        // Set an onClickListener to open the URL
        holder.itemView.setOnClickListener(view -> {
            String url = content.getUrl();
            openWebPage(url, view.getContext());
        });
    }

    private void openWebPage(String url, Context context) {
        if (url != null && !url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                context.startActivity(intent);
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvAuthor; // Add this

        CardViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAuthor = itemView.findViewById(R.id.tvAuthor); // Initialize this
        }
    }

    private static final DiffUtil.ItemCallback<Content> DIFF_CALLBACK = new DiffUtil.ItemCallback<Content>() {
        @Override
        public boolean areItemsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
            // Implement this method depending on your unique identifier
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
            // Implement this method to check whether two items have the same data
            return oldItem.equals(newItem);
        }
    };
}

