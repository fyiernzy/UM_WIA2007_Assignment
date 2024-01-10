package com.example.betterher.informationhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterher.R;

public class SectionCardViewHolder extends RecyclerView.ViewHolder {
    private TextView tvTitle;
    private TextView tvAuthor;
    private ImageView ivImage;
    private ImageView ivLike;
    private ImageView ivStar;

    SectionCardViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvAuthor = itemView.findViewById(R.id.tvAuthor);
        ivImage = itemView.findViewById(R.id.ivImage);
        ivLike = itemView.findViewById(R.id.ivLike);
        ivStar = itemView.findViewById(R.id.ivStar);
    }

    static SectionCardViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_card_item, parent, false);
        return new SectionCardViewHolder(view);
    }

    public void bind(SectionCard sectionCard) {
        Content content = sectionCard.getContent(); // Assuming SectionCard has getContent() method.

        tvTitle.setText(content.getTitle()); // Assuming Content has getTitle() method.
        tvAuthor.setText(content.getAuthor()); // Assuming Content has getAuthor() method.

        // Use Glide or a similar library to load images from a URL or resource ID.
        Glide.with(itemView.getContext())
                .load(content.getImageUrl()) // Assuming Content has getImageUrl() method.
                .placeholder(R.drawable.img_1) // Optional placeholder.
                .into(ivImage);

        // You might want to add logic to determine which resource to use based on some conditions.
        ivLike.setImageResource(sectionCard.getLikeImageId());
        ivStar.setImageResource(sectionCard.getStarImageId());
    }
}
