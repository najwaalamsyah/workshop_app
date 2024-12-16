package com.example.workshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Button registerButton;
    private TextView txtDisplayInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toast.makeText(this, "RegisterActivity Loaded", Toast.LENGTH_SHORT).show();

        // Initialize Firebase Authentication
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.email);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        txtDisplayInfo = findViewById(R.id.login); // Reference login TextView

        // Register button click event
        findViewById(R.id.registerButton).setOnClickListener(view -> {
            String strEmail = emailEditText.getText().toString().trim();
            String strUsername = usernameEditText.getText().toString().trim();
            String strPassword = passwordEditText.getText().toString().trim();

            // Validate fields
            if (strEmail.isEmpty() || strUsername.isEmpty() || strPassword.isEmpty()) {
                txtDisplayInfo.setText(getString(R.string.all_fields_required)); // Display message from strings.xml
            } else {
                // Create a user object
                Map<String, Object> user = new HashMap<>();
                user.put("email", strEmail);
                user.put("username", strUsername);
                user.put("password", strPassword);

                // Add user data to Firestore
                firestore.collection("users")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            txtDisplayInfo.setText(getString(R.string.registration_successful)); // Display success message
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                            // Debug log
                            Log.d("RegisterActivity", "User registered with ID: " + documentReference.getId());

                            // Navigate to LoginActivity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Finish the current activity
                        })
                        .addOnFailureListener(e -> {
                            txtDisplayInfo.setText(getString(R.string.registration_failed)); // Display failure message
                            Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("RegisterActivity", "Error adding document", e);
                        });
            }
        });

        // Handle login action using TextView
        txtDisplayInfo.setOnClickListener(view -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
