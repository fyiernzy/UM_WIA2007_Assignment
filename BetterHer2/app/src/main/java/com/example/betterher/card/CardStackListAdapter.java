package com.example.betterher.card;

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
import com.example.betterher.R;
import com.example.betterher.informationhub.Content;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class CardStackListAdapter extends ListAdapter<Content, CardStackListAdapter.CardViewHolder> {
    private static final int VIEW_TYPE_REGULAR = 0;
    private static final int VIEW_TYPE_LAST_CARD = 1;

    public CardStackListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_LAST_CARD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_card_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        }
        return new CardViewHolder(itemView, viewType);
    }


    @Override
    public int getItemViewType(int position) {
        Content content = getItem(position);
        return content.isLast() ? VIEW_TYPE_LAST_CARD : VIEW_TYPE_REGULAR;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Content content = getItem(position);
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LAST_CARD) {
        } else {
            // Regular card handling
            holder.tvTitle.setText(content.getTitle());
            holder.tvAuthor.setText(content.getAuthor());

            Glide.with(holder.itemView.getContext())
                    .load(content.getImageUrl())
                    .placeholder(R.drawable.img_1) // Optional placeholder while image loads
                    .into(holder.ivImage);

            holder.itemView.setOnClickListener(view -> {
                String url = content.getUrl();
                openWebPage(url, view.getContext());
            });
        }
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
        TextView tvAuthor;

        CardViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_REGULAR) {
                ivImage = itemView.findViewById(R.id.ivImage);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvAuthor = itemView.findViewById(R.id.tvAuthor);
            } // No else part needed, as last card has static content
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

