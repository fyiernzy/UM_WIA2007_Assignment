package com.example.betterher.forum;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betterher.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity {
    private TextView title, content, likes, saves;
    private SliderView imageSlider;
    private ImageView iconLikes, iconSaves; // Add ImageView for likes and saves
    private boolean isLiked = false, isSaved = false; // Trackers for like and save status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        title = findViewById(R.id.post_title);
        content = findViewById(R.id.post_content);
        imageSlider = findViewById(R.id.imageSlider); // Replace with your SliderView ID
        iconLikes = findViewById(R.id.icon_likes); // Initialize like ImageView
        iconSaves = findViewById(R.id.icon_saves); // Initialize save ImageView
        likes = findViewById(R.id.likes_count); // Initialize likes TextView
        saves = findViewById(R.id.saves_count); // Initialize saves TextView


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String postTitle = extras.getString("postTitle");
            String postContent = extras.getString("postContent");
            ArrayList<String> imageUrls = extras.getStringArrayList("postImages");

            title.setText(postTitle);
            content.setText(postContent);

            if (imageUrls != null && !imageUrls.isEmpty()) {
                imageSlider.setVisibility(View.VISIBLE);
                SliderAdapter adapter = new SliderAdapter(this);
                imageSlider.setAutoCycle(false);
                for (String imageUrl : imageUrls) {
                    adapter.addItem(new SliderItem(imageUrl));
                }
                imageSlider.setSliderAdapter(adapter);
            } else {
                imageSlider.setVisibility(View.GONE);
            }
        }

        // Setting up the click listener for Likes ImageView
        iconLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = !isLiked; // Toggle the like status
                if (isLiked) {
                    iconLikes.setImageResource(R.drawable.love_filled); // Change to filled icon
                    int value = Integer.parseInt(likes.getText().toString()) + 1; // Increment the like count
                    likes.setText(String.valueOf(value));
                } else {
                    iconLikes.setImageResource(R.drawable.love_unfilled); // Change to unfilled icon
                    int value = Integer.parseInt(likes.getText().toString()) - 1; // Decrement the like count
                    likes.setText(String.valueOf(value));
                }
            }
        });

        // Setting up the click listener for Saves ImageView
        iconSaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = !isSaved; // Toggle the save status
                if (isSaved) {
                    iconSaves.setImageResource(R.drawable.star_filled); // Change to filled icon
                    int value = Integer.parseInt(saves.getText().toString()) + 1; // Increment the like count
                    saves.setText(String.valueOf(value));
                } else {
                    iconSaves.setImageResource(R.drawable.star_unfilled); // Change to unfilled icon
                    int value = Integer.parseInt(saves.getText().toString()) + 1; // Increment the like count
                    saves.setText(String.valueOf(value));
                }
            }
        });
    }
}
