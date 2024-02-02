package com.example.jaldbaazi_theurgentrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is already logged in
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    // User is logged in, redirect to MainActivity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    // User is not logged in, redirect to OnboardingActivity
                    onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                    boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                    if (isFirstTime) {
                        SharedPreferences.Editor editor = onBoardingScreen.edit();
                        editor.putBoolean("firstTime", false);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }

                finish();
            }
        }, 1500);
    }
}
