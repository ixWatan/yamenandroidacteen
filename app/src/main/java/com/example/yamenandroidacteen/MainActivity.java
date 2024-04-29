package com.example.yamenandroidacteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.classes.other.BaseActivity;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.example.yamenandroidacteen.home.organization.OrganizationHomeActivity;
import com.example.yamenandroidacteen.slideshow.MainSlideFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        checkIfFirstTime();

        // Check if the user is already logged in

    }

    private void checkIfFirstTime() {
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if it's the first time opening the app
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            // Show introduction page
            showIntroductionPage();

            // Set flag to indicate that the user has opened the app before
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                autoLogin(currentUser.getUid());
            } else {
                // if user is not logged in and is not first time redirect him to login page like normal
                if (findViewById(R.id.frameLayout) != null) {
                    LoginFragment loginFragment = new LoginFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, loginFragment).commit();
                }
            }
        }
    }

    private void showIntroductionPage() {
        MainSlideFragment mainSlideFragment = new MainSlideFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mainSlideFragment).commit();
    }

    private void autoLogin(String userId) {
        // Query Firestore to get the user's account type
        db.collection("teenActivists").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User is an activist, redirect to activist home page
                        startActivity(new Intent(this, ActivistHomeActivity.class));
                        finish();
                    } else {
                        // User is an organization, redirect to organization home page
                        startActivity(new Intent(this, OrganizationHomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle Firestore query failure
                    Toast.makeText(this, "Error fetching user data!", Toast.LENGTH_SHORT).show();
                    LoginFragment loginFragment = new LoginFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, loginFragment).commit();
                });
    }





}