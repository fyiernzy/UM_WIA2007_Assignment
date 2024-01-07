package com.example.signuploginfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateFullName, editTextUpdateUsername;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    //private String textFullName, textUsername, textGender;
    private FirebaseAuth auth;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private ListenerRegistration userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        backButton = findViewById(R.id.back_button);
        editTextUpdateFullName = findViewById(R.id.editText_update_profile_fullName);
        editTextUpdateUsername = findViewById(R.id.editText_update_profile_username);
        progressBar = findViewById(R.id.progressBar);

        radioGroupUpdateGender = findViewById(R.id.radioGroup_update_gender);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        //Show Profile Data
        showProfile(firebaseUser);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    // Fetch data from Firebase and display
    private void showProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Fetch user data from Firestore
        userListener = firestore.collection("Users").document(userID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error fetching user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Populate UI with user data
                        String fullName = documentSnapshot.getString("fullName");
                        String username = documentSnapshot.getString("username");
                        String gender = documentSnapshot.getString("gender");

                        editTextUpdateFullName.setText(fullName);
                        editTextUpdateUsername.setText(username);

                        // Select the gender radio button
                        if (gender != null && !gender.isEmpty()) {
                            radioButtonUpdateGenderSelected = findViewById(getGenderRadioButtonId(gender));
                            if (radioButtonUpdateGenderSelected != null) {
                                radioButtonUpdateGenderSelected.setChecked(true);
                            }
                        }
                    }
                });
    }

    // Get the radio button ID based on gender string
    private int getGenderRadioButtonId(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            return R.id.radio_male;
        } else if (gender.equalsIgnoreCase("Female")) {
            return R.id.radio_female;
        } else {
            return -1;
        }
    }

    // Update profile button click listener
    public void updateProfile(View view) {
        Log.d("UpdateProfile", "Update Profile button clicked");
        String newFullName = editTextUpdateFullName.getText().toString().trim();
        String newUsername = editTextUpdateUsername.getText().toString().trim();
        String newGender = getSelectedGender();

        // Validate input
        if (newFullName.isEmpty() || newUsername.isEmpty() || newGender.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the new username is unique
        checkUsernameAvailability(newUsername, newFullName, newGender);
    }

    // Get the selected gender from radio group
    private String getSelectedGender() {
        int selectedId = radioGroupUpdateGender.getCheckedRadioButtonId();

        if (selectedId == R.id.radio_male) {
            return "Male";
        } else if (selectedId == R.id.radio_female) {
            return "Female";
        } else {
            return "";
        }
    }

    // Check if the new username is available
    private void checkUsernameAvailability(final String newUsername, final String newFullName, final String newGender) {
        firestore.collection("Users")
                .whereEqualTo("username", newUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // Username is not unique
                            editTextUpdateUsername.setError("Invalid username. Username is already in use by another user.");
                        } else {
                            // Username is unique, proceed with the update
                            updateUserData(newFullName, newUsername, newGender);
                        }
                    } else {
                        // Display a general error message
                        editTextUpdateUsername.setError("Error checking username availability");
                    }
                });
    }


    // Update user data in Firestore
    private void updateUserData(String newFullName, String newUsername, String newGender) {
        Log.d("UpdateProfile", "Updating user data...");
        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("Users").document(firebaseUser.getUid())
                .update("fullName", newFullName, "username", newUsername, "gender", newGender)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity after successful update
                    } else {
                        Toast.makeText(this, "Error updating profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove Firestore listener to prevent memory leaks
        if (userListener != null) {
            userListener.remove();
        }
    }
}