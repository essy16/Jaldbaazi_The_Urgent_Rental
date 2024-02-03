package com.example.jaldbaazi_theurgentrental;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialButton createAccountButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        createAccountButton = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.ProgressBar);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String email = ((TextInputEditText) findViewById(R.id.Email_edit_text)).getText().toString();
                String password = ((TextInputEditText) findViewById(R.id.password_edit_text)).getText().toString();
                String confirmPassword = ((TextInputEditText) findViewById(R.id.confirm_password_edit_text)).getText().toString();

                // Reset errors
                clearErrors();

                // Perform sign-up
                signUpWithEmailPassword(email, password, confirmPassword);
            }
        });
        // Inside SignUpActivity, after initializing the TextView
        TextView loginTextView = findViewById(R.id.login_textview);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
//                finish(); // Optional: finish SignUpActivity so user cannot navigate back
            }
        });


    }

    private void signUpWithEmailPassword(String email, String password, String confirmPassword) {
        TextInputLayout emailInputLayout = findViewById(R.id.email_input_layout);
        TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);
        TextInputLayout confirmPwdInputLayout = findViewById(R.id.confirm_password_input_layout);

        // Validate input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            setError(emailInputLayout, "All fields are required");
            setError(passwordInputLayout, "All fields are required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(emailInputLayout, "Enter a valid email address");
            return;
        }

        if (password.length() < 8) {
            setError(passwordInputLayout, "Password must be at least 8 characters long");
            return;
        }

        if (!containsUppercase(password) || !containsSpecialCharacter(password)) {
            setError(passwordInputLayout, "Password must contain at least one uppercase letter and one special character");
            return;
        }

        if (!password.equals(confirmPassword)) {
            setError(confirmPwdInputLayout, "Passwords do not match");
            return;
        }

        // Show progress bar
        showProgress(true);

        // Perform sign-up with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Hide progress bar
                    showProgress(false);

                    if (task.isSuccessful()) {
                        // Sign-up success, send verification email
                        sendVerificationEmail();
                    } else {
                        // If sign-up fails, display a message to the user.
                        setError(emailInputLayout, "");
                        setError(passwordInputLayout, "");
                        setError(confirmPwdInputLayout, "Sign-up failed: " + task.getException().getMessage());
                    }
                });
    }

    private void sendVerificationEmail() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        // Verification email sent
                        if (task.isSuccessful()) {
                            // Inform the user to check their email
                            showVerificationDialog();
                        } else {
                            // If sending email verification fails, display a message to the user.
                            // You may want to handle this case differently based on your app's logic.
                            setError(findViewById(R.id.email_input_layout), "Failed to send verification email: " + task.getException().getLocalizedMessage());
                        }
                    });
        }
    }

    private void showVerificationDialog() {
        // Implement your logic for showing a dialog or message to inform the user about the verification email.
        // For example, display a Toast or show a dialog with a message.
        Toast.makeText(SignUpActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
    }

    private void setError(TextInputLayout inputLayout, String error) {
        inputLayout.setError(error);
    }

    private void clearErrors() {
        setError(findViewById(R.id.email_input_layout), "");
        setError(findViewById(R.id.password_input_layout), "");
        setError(findViewById(R.id.confirm_password_input_layout), "");
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.animate().alpha(1.0f); // Fade in the progress bar
            createAccountButton.setText(""); // or set to a loading message
            createAccountButton.setEnabled(false); // Disable the button while showing progress
        } else {
            progressBar.animate().alpha(0.0f); // Fade out the progress bar
            progressBar.setVisibility(View.GONE);
            createAccountButton.setText("Login");
            createAccountButton.setEnabled(true); // Enable the button after progress is hidden
        }
    }


    private boolean containsUppercase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialCharacter(String str) {
        return !str.matches("[A-Za-z0-9 ]*");
    }
}

