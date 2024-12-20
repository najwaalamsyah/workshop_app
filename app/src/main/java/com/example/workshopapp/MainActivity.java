package com.example.workshopapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkshopAdapter adapter;
    private List<Map<String, String>> workshopList;
    private FirebaseFirestore firebaseFirestore;
    private ImageView btnTicket, btnAdd, btnChat, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Firestore and Views
        firebaseFirestore = FirebaseFirestore.getInstance();
        initializeViews();

        // Set up RecyclerView
        setupRecyclerView();

        // Load workshops data from Firestore
        loadWorkshops();

        // Set up buttons
        setupButtons();

        // Load introductory video
        loadIntroductoryVideo();
    }

    // Method to initialize views
    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewWorkshops);
        btnTicket = findViewById(R.id.btnTicket);
        btnAdd = findViewById(R.id.btnAdd);
        btnChat = findViewById(R.id.btnChat);
    }

    // Method to set up RecyclerView and its adapter
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workshopList = new ArrayList<>();
        adapter = new WorkshopAdapter(workshopList, this, position -> onWorkshopItemClick(position));
        recyclerView.setAdapter(adapter);
    }

    // Method to load workshops data from Firestore
    private void loadWorkshops() {
        firebaseFirestore.collection("workshops")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Failed to load workshops!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        updateWorkshopList(value);
                    }
                });
    }

    // Method to update the workshop list with the fetched data
    private void updateWorkshopList(QuerySnapshot value) {
        workshopList.clear();
        for (QueryDocumentSnapshot doc : value) {
            Map<String, Object> data = doc.getData();
            Map<String, String> workshop = new HashMap<>();
            workshop.put("workshopName", (String) data.get("workshopName"));
            workshop.put("date", (String) data.get("date"));
            workshop.put("location", (String) data.get("location"));
            workshop.put("imageUrl", (String) data.get("imageUrl"));
            workshop.put("price", (String) data.get("price"));
            workshop.put("maxParticipants", (String) data.get("maxParticipants"));
            workshopList.add(workshop);
        }
        adapter.notifyDataSetChanged();
    }

    // Method to handle workshop item click
    private void onWorkshopItemClick(int position) {
        Map<String, String> selectedWorkshop = workshopList.get(position);
        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
        intent.putExtra("workshopName", selectedWorkshop.get("workshopName"));
        intent.putExtra("price", selectedWorkshop.get("price"));
        intent.putExtra("location", selectedWorkshop.get("location"));
        intent.putExtra("date", selectedWorkshop.get("date"));
        intent.putExtra("maxParticipants", selectedWorkshop.get("maxParticipants"));
        startActivity(intent);
    }

    // Method to set up the buttons
    private void setupButtons() {
        btnTicket.setOnClickListener(v -> navigateToTicketHistory());
        btnChat.setOnClickListener(v -> navigateToChatBot());
        btnAdd.setOnClickListener(v -> navigateToUploadActivity());
    }

    // Method to navigate to TicketHistory
    private void navigateToTicketHistory() {
        Intent intent = new Intent(MainActivity.this, TicketHistory.class);
        startActivity(intent);
    }

    // Method to navigate to ChatBotActivity
    private void navigateToChatBot() {
        Intent intent = new Intent(MainActivity.this, ChatBotActivity.class);
        startActivity(intent);
    }

    // Method to navigate to UploadActivity
    private void navigateToUploadActivity() {
        Intent intent = new Intent(MainActivity.this, UploadActivity.class);
        startActivity(intent);
    }

    // Method to load introductory video
    private void loadIntroductoryVideo() {
        VideoView videoView = findViewById(R.id.videoView1);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pizza));
        videoView.start();
    }
}
