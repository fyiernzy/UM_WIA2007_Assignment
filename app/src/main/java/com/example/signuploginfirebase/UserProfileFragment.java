package com.example.signuploginfirebase;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class UserProfileFragment extends Fragment {

    private TextView textFullName, textEmail, textUsername, textGender, textWelcome;
    private ImageButton backButton;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private ListenerRegistration userListener;
    private ProgressBar progressBar;
    private ImageView profileImageView;
    private ImageButton editProfileIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        // The ids of TextViews and ImageButton in the fragment
        textFullName = view.findViewById(R.id.textview_show_full_name);
        textEmail = view.findViewById(R.id.textview_show_email);
        textUsername = view.findViewById(R.id.textview_show_username);
        textGender = view.findViewById(R.id.textview_show_gender);
        textWelcome = view.findViewById(R.id.textView_show_welcome_username);

        progressBar = view.findViewById(R.id.progressBar);

        // Set OnClickListener on ImageView to Open com.example.signuploginfirebase.UploadProfilePicActivity
        profileImageView = view.findViewById(R.id.profilePic_profilepage);

        // Set default profile picture visibility to GONE when initializing the fragment
        profileImageView.setImageDrawable(null); // Clear the previous image, if any
        profileImageView.setVisibility(View.GONE);

        editProfileIcon = view.findViewById(R.id.edit_profile_image);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        editProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the UpdateProfileActivity when the edit icon is clicked
                Intent intent = new Intent(requireContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });


        // Fetch and display user details
        fetchUserDetails(currentUser.getUid());

        return view;
    }

    private void fetchUserDetails(String userId) {
        // Show ProgressBar when starting to fetch details
        progressBar.setVisibility(View.VISIBLE);

        userListener = firestore.collection("Users").document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    // Hide ProgressBar when details are fetched
                    progressBar.setVisibility(View.GONE);

                    if (e != null) {
                        Toast.makeText(requireContext(), "Error fetching user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Fetch and display user details
                        String fullName = documentSnapshot.getString("fullName");
                        String email = documentSnapshot.getString("email");
                        String username = documentSnapshot.getString("username");
                        String gender = documentSnapshot.getString("gender");

                        textFullName.setText(fullName);
                        textEmail.setText(email);
                        textUsername.setText(username);
                        textGender.setText(gender);
                        textWelcome.setText(username);

                        // Load profile picture using Glide
                        String profilePicUrl = documentSnapshot.getString("profile_pic_url");
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {

                            Glide.with(requireContext())
                                    .load(profilePicUrl)
                                    .into(new CustomTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            // Set the profileImageView visibility to VISIBLE
                                            profileImageView.setVisibility(View.VISIBLE);
                                            // Set the profileImageView image
                                            profileImageView.setImageDrawable(resource);
                                            // Hide the default drawable
                                            profileImageView.setBackground(null);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Handle case where the placeholder is cleared.
                                        }
                                    });
                        } else {
                            // If no profile picture is available, show the default profile picture
                            profileImageView.setImageResource(R.drawable.baseline_person_24);
                            // Set the profileImageView visibility to VISIBLE
                            profileImageView.setVisibility(View.VISIBLE);
                            // Show the default drawable
                            profileImageView.setBackgroundResource(R.drawable.baseline_person_24);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove Firestore listener to prevent memory leaks
        if (userListener != null) {
            userListener.remove();
        }
    }
}
