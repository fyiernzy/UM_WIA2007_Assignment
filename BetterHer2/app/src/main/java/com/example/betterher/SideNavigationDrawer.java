package com.example.betterher;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.betterher.Authentication.LoginActivity;
import com.example.betterher.Quiz.Views.QuizHomeFragment;
import com.example.betterher.forum.ForumFragment;
import com.example.betterher.informationhub.InformationHubFragment;
import com.example.betterher.safetyhome.SafetyHomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class SideNavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private TextView textUsername, textEmail, textUserId;
    private ImageView profileImageView;
    private ListenerRegistration userListener;
    private Map<Integer, Class<? extends Fragment>> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_nav_header);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        // Find header views
        View headerView = navigationView.getHeaderView(0);
        textUsername = headerView.findViewById(R.id.text_username);
        textEmail = headerView.findViewById(R.id.text_email);
        textUserId = headerView.findViewById(R.id.text_user_id);
        profileImageView = headerView.findViewById(R.id.profilePic_nav_header);

        if (currentUser != null) {
            textEmail.setText(currentUser.getEmail());
            textUserId.setText(currentUser.getUid());

            // Fetch user data from Firestore
            fetchUserDataFromFirestore(currentUser.getUid());
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuizHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_quiz);

            // Set the title dynamically
            setTitle("BetterHer");
        }
    }

    private void fetchUserDataFromFirestore(String userId) {
        userListener = firestore.collection("Users").document(userId)
                .addSnapshotListener(MetadataChanges.INCLUDE, (documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle errors
                        Toast.makeText(this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Fetch and set username
                        String username = documentSnapshot.getString("username");
                        textUsername.setText(username);

                        // Fetch and set profile picture
                        String profilePicUrl = documentSnapshot.getString("profile_pic_url");
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            loadProfilePicture(profilePicUrl);
                        } else {
                            // If no profile picture is available, show the default profile picture
                            profileImageView.setImageResource(R.drawable.baseline_person_24);
                            // Set the profileImageView visibility to VISIBLE
                            profileImageView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@Nonnull MenuItem item) {
        int itemId = item.getItemId();

        if(fragmentMap == null) initiateFragmentMap();

        if (fragmentMap.containsKey(itemId)) {
            switchFragment(fragmentMap.get(itemId));
        } else if (itemId == R.id.logout_button) {
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SideNavigationDrawer.this, LoginActivity.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initiateFragmentMap() {
        if(fragmentMap == null) {
            fragmentMap = new HashMap<>();
            fragmentMap.put(R.id.nav_info_hub, InformationHubFragment.class);
            fragmentMap.put(R.id.nav_profile, UserProfileFragment.class);
            fragmentMap.put(R.id.nav_quiz, QuizHomeFragment.class);
            fragmentMap.put(R.id.nav_safety_support, SafetyHomeFragment.class);
            fragmentMap.put(R.id.nav_forum, ForumFragment.class);
            fragmentMap.put(R.id.nav_settings, SettingsFragment.class);
            fragmentMap.put(R.id.nav_about, AboutFragment.class);
        }
    }

    public void switchFragment(Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove Firestore listener to prevent memory leaks
        if (userListener != null) {
            userListener.remove();
        }
    }

    // Replace Fragment method
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, fragment, null)
                .addToBackStack("name")
                .commit();
    }

    private void loadProfilePicture(String profilePicUrl) {
        Glide.with(this)
                .load(profilePicUrl)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Set the profileImageView visibility to VISIBLE
                        profileImageView.setVisibility(View.VISIBLE);
                        // Set the profileImageView image
                        profileImageView.setImageDrawable(resource);
                        // Hide the default drawable
                        profileImageView.setBackgroundResource(0);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle case where the placeholder is cleared.
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        // Handle Glide load failure
                        Toast.makeText(SideNavigationDrawer.this, "Error loading profile picture", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
