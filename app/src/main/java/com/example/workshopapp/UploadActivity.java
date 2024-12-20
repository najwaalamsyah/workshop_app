package com.example.workshopapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private EditText editTextWorkshopName, editTextLocation, editTextMaxPerson, editTextPrice, editTextDate;
    private Button btnUpload, btnPrevious;
    private TextView textViewImageUrl;

    // Firebase Firestore instance
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Initialize UI elements
        editTextWorkshopName = findViewById(R.id.editTextWorkshopName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextMaxPerson = findViewById(R.id.editTextMaxPerson);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDate = findViewById(R.id.editTextDate);
        btnUpload = findViewById(R.id.btnUpload);
        btnPrevious = findViewById(R.id.btnPrevious);
        textViewImageUrl = findViewById(R.id.textViewImageUrl);

        // Initialize Firebase Database reference
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Set up DatePickerDialog for the Date field
        editTextDate.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    UploadActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format the selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        // Set the selected date in the EditText
                        editTextDate.setText(formattedDate);
                    },
                    year, month, day);

            datePickerDialog.show();
        });

        // Handle the Upload button click
        btnUpload.setOnClickListener(v -> {
            String name = editTextWorkshopName.getText().toString();
            String location = editTextLocation.getText().toString();
            String maxParticipants = editTextMaxPerson.getText().toString();
            String price = editTextPrice.getText().toString();
            String date = editTextDate.getText().toString();
            String imageUrl = textViewImageUrl.getText().toString(); // Assuming you get the image URL here

            if (name.isEmpty() || location.isEmpty() || maxParticipants.isEmpty() || price.isEmpty() || date.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(UploadActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Function to upload workshop details to Firestore
            uploadWorkshop(name, location, maxParticipants, price, date, imageUrl);
        });

        // Handle the Previous button click (optional)
        btnPrevious.setOnClickListener(v -> {
            finish(); // Close the current activity and go back
        });
    }

    private void uploadWorkshop(String name, String location, String maxParticipants, String price, String date, String imageUrl) {
        // Create a Map to store the workshop details
        Map<String, Object> workshopData = new HashMap<>();
        workshopData.put("workshopName", name);
        workshopData.put("location", location);
        workshopData.put("maxParticipants", maxParticipants);
        workshopData.put("price", price);
        workshopData.put("date", date);
        workshopData.put("imageUrl", imageUrl); // Assuming imageUrl is passed from another activity

        // Save the workshop data to Firestore
        firebaseFirestore.collection("workshops")
                .add(workshopData)
                .addOnSuccessListener(documentReference -> {
                    // Show success message and navigate
                    Toast.makeText(UploadActivity.this, "Workshop uploaded successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Handle the failure scenario
                    Toast.makeText(UploadActivity.this, "Failed to upload workshop!", Toast.LENGTH_SHORT).show();
                });
    }
}
