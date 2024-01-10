package com.example.betterher.forum;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.betterher.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.title.setText(post.getTitle());
        holder.content.setText(truncateText(post.getContent(), 50)); // Truncate content to 50 words

        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            Glide.with(holder.image.getContext())
                    .load(post.getImageUrl())
                    .transform(new RoundedCorners(16)) // Adjust the radius as needed
                    .into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.title.getLayoutParams();
            layoutParams.topMargin = 20;
            holder.title.setLayoutParams(layoutParams);
        } else {
            holder.image.setVisibility(View.GONE);
        }


        // Inside the RecyclerView adapter's onBindViewHolder method
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected forum post data
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Post selectedPost = postList.get(clickedPosition);

                    // Create an Intent to launch ViewPostActivity
                    Intent intent = new Intent(holder.itemView.getContext(), ViewPostActivity.class);
                    intent.putExtra("postTitle", selectedPost.getTitle());
                    intent.putExtra("postContent", selectedPost.getContent());

                    // Check if there are images
                    if (selectedPost.getImageUrls() != null && !selectedPost.getImageUrls().isEmpty()) {
                        intent.putStringArrayListExtra("postImages", new ArrayList<>(selectedPost.getImageUrls()));
                    }

                    // Start the ViewPostActivity
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

    }

    private String truncateText(String text, int wordLimit) {
        if (text == null || text.trim().isEmpty()) return "";
        String[] words = text.split(" ");
        if (words.length > wordLimit) {
            return String.join(" ", Arrays.copyOfRange(words, 0, wordLimit)) + "...";
        }
        return text;
    }




    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView content;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.post_image);
            content = itemView.findViewById(R.id.post_content);
            title = itemView.findViewById(R.id.post_title);
        }
    }
}
