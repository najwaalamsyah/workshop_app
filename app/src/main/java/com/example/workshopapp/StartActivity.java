package com.example.workshopapp;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); // Layout for Start Activity

        // Set a delay before transitioning to the LoginActivity
        new Handler().postDelayed(() -> {
            // Intent redirect it to LoginActivity
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close Start activity
        }, 2000); // Delay of 2 seconds (2000 milliseconds)
    }
}
