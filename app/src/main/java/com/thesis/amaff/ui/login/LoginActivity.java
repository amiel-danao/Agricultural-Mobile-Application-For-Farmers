package com.thesis.amaff.ui.login;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesis.amaff.MainActivity;
import com.thesis.amaff.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.thesis.amaff.ui.models.UserProfile;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private Button createAccountButton;
    private View registrationForm;
    private Button loginFormButton;
    private TextView registerLabel;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            // User is logged in and email is verified, redirect to MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Finish the LoginActivity to prevent going back to it
        }


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        registrationForm = findViewById(R.id.registrationForm);
        loginFormButton = findViewById(R.id.loginFormButton);
        registerLabel = findViewById(R.id.registerLabel);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationForm.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.GONE);
                registerLabel.setVisibility(View.GONE);
            }
        });

        loginFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationForm.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                registerLabel.setVisibility(View.VISIBLE);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    if(!confirmPassword.equals(password)){
                        Toast.makeText(LoginActivity.this, "Password doesn't not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        registerUser(email, password);
                    }
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // User is logged in and email is verified
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                // Check if the user has a profile in Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference profilesCollectionRef = db.collection("Profiles").document(user.getUid());
                                profilesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            UserProfile userProfile = task.getResult().toObject(UserProfile.class);
                                            if (userProfile != null) {
                                                // User has an existing profile
                                                // Add your logic for handling the existing profile
                                            } else {
                                                // User does not have a profile, create one
                                                createProfile(user);
                                            }
                                        } else {
                                            // Error getting profiles collection
                                            Toast.makeText(LoginActivity.this, "Failed to get profiles. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                // Add your logic for handling the successful login, such as navigating to another activity
                            } else {
                                // Email is not verified
                                Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
                                // Check if the email needs to be resent
                                if (user != null) {
                                    if (!user.isEmailVerified()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("Email not verified. Resend verification email?")
                                                .setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(LoginActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(LoginActivity.this, "Failed to resend verification email.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();
                                    }
                                }
                            }
                        } else {
                            // Login failed
                            Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void createProfile(FirebaseUser user) {
        // Get the necessary user information
        String email = user.getEmail();
        String uid = user.getUid();
        // You can prompt the user to enter their first name and last name if needed

        // Create a new UserProfile instance
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(email);
        userProfile.setUid(uid);
        userProfile.setDateCreated(Timestamp.now()); // Set the current timestamp

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference profilesCollectionRef = db.collection("Profiles");

        // Set the document with the user's UID as the document key
        profilesCollectionRef.document(uid)
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Profile creation success
//                        Toast.makeText(LoginActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // Finish the LoginActivity to prevent going back to it
                        // Add your logic for handling the profile creation, such as navigating to another activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Profile creation failed
                        Toast.makeText(LoginActivity.this, "Failed to create profile. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            // Send verification email
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Email sent successfully
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setMessage("Verification email sent. Please check your email.").show();
                                        } else {
                                            // Email sending failed
                                            Toast.makeText(LoginActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            // Add your logic for handling the successful registration, such as navigating to another activity
                        } else {
                            // Registration failed
                            Toast.makeText(LoginActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
