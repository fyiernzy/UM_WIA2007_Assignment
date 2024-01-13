package com.example.betterher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadProfilePicActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfileDp;
    private Button choosePictureButton, uploadPicButton;
    private ProgressBar progressBar;
    private TextView textViewUploadPic;
    private ImageButton backButton;
    private Uri imageUri;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        imageViewProfileDp = findViewById(R.id.imageView_profile_dp);
        choosePictureButton = findViewById(R.id.upload_pic_choose_button);
        uploadPicButton = findViewById(R.id.upload_pic_button);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.back_button);
        textViewUploadPic = findViewById(R.id.textView_upload_pic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        choosePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        uploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadFile() {
        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            textViewUploadPic.setVisibility(View.GONE);

            String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            StorageReference fileReference = storageReference.child("profile_pics/" + userId + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressBar.setVisibility(View.GONE);
                        textViewUploadPic.setVisibility(View.VISIBLE);

                        // Handle successful upload
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Log the download URL
                            Log.d("UploadProfilePic", "Download URL: " + uri.toString());
                            Toast.makeText(UploadProfilePicActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                            // You can now store this URL and the actual picture in Firestore
                            String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            DocumentReference userRef = firestore.collection("Users").document(userId);

                            // Create a User object with the actual pic URL
                            Map<String, Object> userUpdate = new HashMap<>();
                            userUpdate.put("profile_pic_url", uri.toString());

                            // Update the user document in Firestore
                            userRef.update(userUpdate)
                                    .addOnSuccessListener(aVoid -> Log.d("UploadProfilePic", "Profile details updated in Firestore"))
                                    .addOnFailureListener(e -> Log.e("UploadProfilePic", "Failed to update profile details in Firestore: " + e.getMessage()));
                        });

                        // Finish the activity and return to UserProfileFragment
                        setResult(Activity.RESULT_OK);
                        finish();
                        // Finish the activity and return to UserProfileFragment
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        textViewUploadPic.setVisibility(View.VISIBLE);

                        // Handle unsuccessful upload
                        Log.e("UploadProfilePic", "Upload failed: " + e.getMessage());
                        Toast.makeText(UploadProfilePicActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Log or display a message indicating that no image is selected
            Log.d("UploadProfilePic", "No image selected");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewProfileDp.setImageURI(imageUri);
        }
    }
}
