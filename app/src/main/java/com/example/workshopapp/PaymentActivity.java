package com.example.workshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;

    private ImageView imgKTP;
    private EditText etName, etPhone, etAmount, etDescription;
    private Spinner spinnerPaymentMethod, spinnerPerson;
    private Button btnTakePhoto, btnConfirm, btnPrevious;
    private Bitmap capturedImage;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        imgKTP = findViewById(R.id.imgKTP);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        spinnerPerson = findViewById(R.id.spinnerPerson);
        etAmount = findViewById(R.id.etAmount);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnPrevious = findViewById(R.id.btnPrevious);

        // Retrieve the amount and description passed from MainActivity
        Intent intent = getIntent();
        int selectedAmount = intent.getIntExtra("selectedAmount", 0); // Default value 0 if not passed
        String description = intent.getStringExtra("selectedDescription"); // Retrieve the description

        // Set the amount to the EditText if it's passed
        if (selectedAmount != 0) {
            etAmount.setText(String.valueOf(selectedAmount)); // Display the selected amount
        } else {
            Toast.makeText(this, "No amount selected", Toast.LENGTH_SHORT).show();
        }

        // Set the description to the EditText if it's passed
        if (description != null && !description.isEmpty()) {
            etDescription.setText(description);
        } else {
            Toast.makeText(this, "No description provided", Toast.LENGTH_SHORT).show();
        }

        // Set up the Spinner for Number of People
        ArrayAdapter<CharSequence> adapterPerson = ArrayAdapter.createFromResource(this,
                R.array.number_of_persons, android.R.layout.simple_spinner_item);
        adapterPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerson.setAdapter(adapterPerson);

        // Set up the Spinner for Payment Methods
        ArrayAdapter<CharSequence> adapterPaymentMethod = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapterPaymentMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(adapterPaymentMethod);

        // Update the amount based on the selected number of persons
        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected number of persons
                int numOfPersons = Integer.parseInt(spinnerPerson.getSelectedItem().toString());
                // Calculate the total amount based on the number of persons and price per person
                int totalAmount = numOfPersons * selectedAmount;
                // Set the total amount into the EditText field
                etAmount.setText(String.valueOf(totalAmount));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when no selection is made
            }
        });

        // Handle Take Photo button click
        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });

        // Handle Confirm button click
        btnConfirm.setOnClickListener(v -> saveDataToFirebase());

        // Handle Previous button click
        btnPrevious.setOnClickListener(v -> goBackToHomepage());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            capturedImage = (Bitmap) data.getExtras().get("data");
            imgKTP.setImageBitmap(capturedImage);
        } else {
            Toast.makeText(this, "Failed to capture image!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataToFirebase() {
        if (capturedImage == null) {
            Toast.makeText(this, "Please take a photo of your KTP!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String totalPerson = spinnerPerson.getSelectedItem().toString();
        String amount = etAmount.getText().toString().trim();
        String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert Bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("phone", phone);
        userData.put("description", description);
        userData.put("totalPerson", totalPerson);
        userData.put("amount", amount);
        userData.put("paymentMethod", paymentMethod);

        // Save to Firestore
        firebaseFirestore.collection("bookings")
                .add(userData)
                .addOnSuccessListener(documentReference -> {
                    showSuccessPopup();
                })
                .addOnFailureListener(e -> Toast.makeText(PaymentActivity.this, "Failed to save data!", Toast.LENGTH_SHORT).show());
    }

    private void showSuccessPopup() {
        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View customView = getLayoutInflater().inflate(R.layout.activity_popup, null);
        builder.setView(customView);

        // Find button in custom view
        Button btnShowTicket = customView.findViewById(R.id.btnShowTicket);

        AlertDialog dialog = builder.create();

        btnShowTicket.setOnClickListener(v -> {
            // Navigate to the next page
            Intent intent = new Intent(PaymentActivity.this, TicketHistory.class);
            startActivity(intent);
            dialog.dismiss(); // Close the popup
        });

        dialog.show();
    }

    private void goBackToHomepage() {
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        startActivity(intent);
    }
}