package com.example.betterher.forum;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.betterher.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class PostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MAX_IMAGE_COUNT = 10;
    private EditText titleInput, contentInput;
    private LinearLayout imageContainer;
    private ArrayList<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        imageContainer = findViewById(R.id.imageContainer);
        Button uploadImagesButton = findViewById(R.id.uploadImagesButton);
        Button submitButton = findViewById(R.id.submitButton);

        uploadImagesButton.setOnClickListener(v -> pickImages());
        submitButton.setOnClickListener(v -> submitPost());

        // Set the animated cursor drawable
        setAnimatedCursor(titleInput);
        setAnimatedCursor(contentInput);

    }

    private void setAnimatedCursor(EditText editText) {
        // Set the cursor resource to our animated drawable
        editText.setCursorVisible(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextCursorDrawable(R.drawable.animated_cursor);
        }
        // Start the cursor animation
        Drawable cursorDrawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cursorDrawable = editText.getTextCursorDrawable();
        }
        if (cursorDrawable instanceof AnimationDrawable) {
            ((AnimationDrawable) cursorDrawable).start();
        }
    }

    private void pickImages() {
        if (imageUris.size() >= MAX_IMAGE_COUNT) {
            Toast.makeText(this, "You can upload up to 10 images only.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addImageToView(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                addImageToView(imageUri);
            }
        }
    }

    private void addImageToView(Uri imageUri) {
        if (imageUris.size() < MAX_IMAGE_COUNT) {
            imageUris.add(imageUri);
            ImageView imageView = new ImageView(this);

            // Convert 80dp and 100dp to pixels.
            float density = getResources().getDisplayMetrics().density;
            int widthPixels = (int) (80 * density);
            int heightPixels = (int) (100 * density);

            imageView.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, heightPixels));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Load the image using Glide with rounded corners
            Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (10 * density)))) // 10dp corner radius
                    .into(imageView);

            // Optionally set padding to ensure the image doesn't cover the entire background
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
            imageView.setPadding(padding, padding, padding, padding);

            imageContainer.addView(imageView);
        } else {
            Toast.makeText(this, "You can upload up to 10 images only.", Toast.LENGTH_SHORT).show();
        }
    }


    private void submitPost() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUris.isEmpty()) {
            submitPostToFirestore(title, content, new ArrayList<>());
        } else {
            uploadImages(imageUris, uploadedImageUrls -> submitPostToFirestore(title, content, uploadedImageUrls));
        }


    }

    private void submitPostToFirestore(String title, String content, List<String> imageUrls) {
        Map<String, Object> post = new HashMap<>();
        post.put("title", title);
        post.put("content", content);
        post.put("images", imageUrls);
        post.put("createdAt", FieldValue.serverTimestamp());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Post").add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PostActivity.this, "Post successfully added to database!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(PostActivity.this, "Post addition failed!", Toast.LENGTH_SHORT).show());
    }

    private void uploadImages(List<Uri> imageUris, final OnImagesUploadedListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        ConcurrentHashMap<String, Uri> uploadedImageUrls = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(imageUris.size());

        for (Uri uri : imageUris) {
            StorageReference imageRef = storage.getReference().child("images/" + UUID.randomUUID().toString());
            imageRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                            .addOnSuccessListener(downloadUri -> {
                                uploadedImageUrls.put(downloadUri.toString(), uri);
                                latch.countDown();
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(PostActivity.this, "Upload failed for one of the images!", Toast.LENGTH_SHORT).show();
                        latch.countDown(); // Ensure the count is decremented even on failure to avoid deadlock
                    });
        }

        // Wait for all uploads to complete
        new Thread(() -> {
            try {
                latch.await();
                listener.onUploaded(new ArrayList<>(uploadedImageUrls.keySet()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Toast.makeText(PostActivity.this, "Upload was interrupted!", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }


    interface OnImagesUploadedListener {
        void onUploaded(List<String> imageUrls);
    }


}
