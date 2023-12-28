package com.example.signuploginfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupUsername, signupEmail, signupPassword, signupFullName;
    private Button signupButton;
    private TextView loginRedirectText;
    private ImageButton backButton;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signupUsername = findViewById(R.id.signup_username);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupFullName = findViewById(R.id.signup_full_name);

        signupButton = findViewById(R.id.signup_button);

        loginRedirectText = findViewById(R.id.loginRedirectText);
        backButton = findViewById(R.id.back_button);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signupUsername.getText().toString().trim();
                String userEmail = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String fullName = signupFullName.getText().toString().trim();

                if(username.isEmpty()){
                    signupUsername.setError("Username cannot be empty.");
                }
                if(userEmail.isEmpty()){
                    signupEmail.setError("Email cannot be empty.");
                }

                if(fullName.isEmpty()){
                    signupFullName.setError("Full name cannot be empty.");
                }

                if(pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty.");
                }

                if (!isValidPassword(pass)) {
                    signupPassword.setError("Passwords must be at least 8 characters long and include at least 1 uppercase letter, 1 lowercase letter, and 1 digit.");
                } else {
                    // Check if the username is already in use
                    checkUsernameAvailability(username, new OnUsernameCheckListener() {
                        @Override
                        public void onUsernameCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                // Username is unique, proceed with user registration
                                auth.createUserWithEmailAndPassword(userEmail, pass)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Get the user ID from the authenticated user
                                                    FirebaseUser user = auth.getCurrentUser();
                                                    if (user != null) {
                                                        userID = user.getUid();
                                                        // Store user details in FireStore
                                                        storeUserDetailsInFirestore(username, userEmail, fullName);
                                                    }

                                                    Toast.makeText(SignUpActivity.this, "Sign Up Successful. ", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Sign Up Failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Username is already in use, show an error message
                                signupUsername.setError("Username is already taken. Please choose another.");
                            }
                        }
                    });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void storeUserDetailsInFirestore(String username, String email, String fullName) {
        // Get selected gender
        String gender = getSelectedGender();

        // Create a map to store user details
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("fullName", fullName);
        user.put("gender", gender);

        // Add a new document with the user ID as the document ID
        db.collection("Users").document(userID).set(user);
    }

    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();

        if (selectedId == -1) {
            // No gender selected
            return null;
        } else {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
    }

    private void checkUsernameAvailability(String username, OnUsernameCheckListener listener) {
        // Query FireStore to check if the username exists
        db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Check if any documents match the query
                            boolean isAvailable = task.getResult().isEmpty();
                            listener.onUsernameCheckComplete(isAvailable);
                        } else {
                            // Handle the error
                            listener.onUsernameCheckComplete(false);
                        }
                    }
                });
    }

    interface OnUsernameCheckListener {
        void onUsernameCheckComplete(boolean isAvailable);
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long
        // It must contain at least 1 uppercase letter, 1 lowercase letter, and 1 digit (number)
        return password.length() >= 8 && containsUppercase(password) && containsLowercase(password) && containsDigit(password);
    }

    // Check if the password contains at least 1 uppercase letter
    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    // Check if the password contains at least 1 lowercase letter
    private boolean containsLowercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    // Check if the password contains at least 1 digit
    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}