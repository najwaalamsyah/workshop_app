package com.example.workshopapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketHistory extends AppCompatActivity {

    private ListView listView;
    private TicketActivity ticketAdapter;
    private ArrayList<HashMap<String, String>> ticketList;
    private FirebaseFirestore firebaseFirestore;
    private ImageView btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        // Initialize UI elements
        listView = findViewById(R.id.listView);
        btnHome = findViewById(R.id.btnHome);

        // Set onClickListener for btnHome
        btnHome.setOnClickListener(v -> {
            // Navigate to HomepageActivity
            Intent intent = new Intent(TicketHistory.this, MainActivity.class);
            startActivity(intent);
        });


        // Initialize Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Initialize list and adapter
        ticketList = new ArrayList<>();
        ticketAdapter = new TicketActivity(this, ticketList);
        listView.setAdapter(ticketAdapter);

        // Fetch data from Firestore
        fetchTicketHistory();
    }

    private void fetchTicketHistory() {
        firebaseFirestore.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ticketList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        HashMap<String, String> ticket = new HashMap<>();
                        ticket.put("name", document.getString("name"));
                        ticket.put("phone", document.getString("phone"));
                        ticket.put("totalPerson", document.getString("totalPerson"));
                        ticket.put("amount", document.getString("amount"));
                        ticket.put("paymentMethod", document.getString("paymentMethod"));
                        ticketList.add(ticket);
                    }
                    ticketAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("TickethistoryActivity", "Error fetching data", e);
                    Toast.makeText(TicketHistory.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
           });
    }
}
