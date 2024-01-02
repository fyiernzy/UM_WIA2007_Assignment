package com.example.betterher.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.betterher.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class AuthViewModel extends AndroidViewModel {

    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseUser currentUser;
    private AuthRepository repository;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public AuthViewModel(@NotNull Application application) {
        super(application);

        repository = new AuthRepository(application);
        currentUser = repository.getCurrentUser();
        firebaseUserMutableLiveData = repository.getFirebaseUserMutableLiveData();
    }

    public void signUp(String email, String pass) {
        repository.signUp(email, pass);
    }
    public void signIn(String email, String pass) {
        repository.signIn(email, pass);
    }

    public void signOut() {
        repository.signOut();
    }
}
