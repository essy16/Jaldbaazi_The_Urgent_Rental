package com.example.jaldbaazi_theurgentrental;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.IndeterminateDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialButton loginMaterialButton;
    private ProgressBar progressBar;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        loginMaterialButton = findViewById(R.id.login_account_button);
        progressBar = findViewById(R.id.loginProgressBar);
        signup = findViewById(R.id.sign_up_Text);

        loginMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String email = ((TextInputEditText) findViewById(R.id.email_edit_text)).getText().toString();
                String password = ((TextInputEditText) findViewById(R.id.password_edit_text)).getText().toString();

                // Reset errors
                clearErrors();

                // Perform login
                loginWithEmailPassword(email, password);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to SignUpActivity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish(); // Optional: finish LoginActivity so user cannot navigate back
            }
        });
    }

    private void loginWithEmailPassword(String email, String password) {
        TextInputLayout emailInputLayout = findViewById(R.id.email_input_layout);
        TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);

        // Validate input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            setError(emailInputLayout, "All fields are required");
            setError(passwordInputLayout, "All fields are required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(emailInputLayout, "Enter a valid email address");
            return;
        }

        // Show progress bar
        showProgress(true);

        // Perform login with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Hide progress bar
                    showProgress(false);

                    if (task.isSuccessful()) {
                        // Check if the user is verified
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // Login success and user is verified
                            // Add your logic for what to do after a successful login
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // User is not verified, show a message or handle accordingly
                            Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If login fails, display a message to the user.
                        setError(emailInputLayout, "");
                        setError(passwordInputLayout, "Login failed: " + task.getException().getMessage());
                    }
                });
    }

    private void setError(TextInputLayout inputLayout, String error) {
        inputLayout.setError(error);
    }

    private void clearErrors() {
        setError(findViewById(R.id.email_input_layout), "");
        setError(findViewById(R.id.password_input_layout), "");
    }

    private void showProgress(boolean show) {

        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.animate().alpha(1.0f); // Fade in the progress bar
            loginMaterialButton.setText("");// or set to a loading message
            loginMaterialButton.setEnabled(false); // Disable the button while showing progress
        } else {
            progressBar.animate().alpha(0.0f); // Fade out the progress bar
            progressBar.setVisibility(View.GONE);
            loginMaterialButton.setText("Login");
            loginMaterialButton.setEnabled(true); // Enable the button after progress is hidden
        }
    }
}

