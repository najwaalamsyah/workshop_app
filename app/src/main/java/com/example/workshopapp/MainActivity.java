package com.example.workshopapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnPainting, btnPottery, btnFlower, btnPizza;
    private VideoView videoView;
    private ImageView btnTicket; // Declare the ImageView for btnTicket

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnPainting = findViewById(R.id.paintingbutton);
        btnPottery = findViewById(R.id.potterybutton);
        btnFlower = findViewById(R.id.flowerbutton);
        btnPizza = findViewById(R.id.pizzabtn);

        // Initialize VideoView
        videoView = findViewById(R.id.videoView1);

        // Set video URI (pizza.mp4 in res/raw folder)
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pizza);
        videoView.setVideoURI(videoUri);

        // Start video playback automatically
        videoView.start();

        // Set OnClickListeners for buttons with descriptions
        btnPainting.setOnClickListener(v -> startPaymentActivity(80000, "Painting Class"));
        btnPottery.setOnClickListener(v -> startPaymentActivity(150000, "Pottery Class"));
        btnFlower.setOnClickListener(v -> startPaymentActivity(70000, "Flower Class"));
        btnPizza.setOnClickListener(v -> startPaymentActivity(130000, "Pizza Workshop"));

        // Initialize btnTicket and set OnClickListener
        btnTicket = findViewById(R.id.btnTicket); // Link to the btnTicket ImageView in XML
        btnTicket.setOnClickListener(v -> {
            // Navigate to TickethistoryActivity
                Intent intent = new Intent(MainActivity .this, TicketHistory.class);
            startActivity(intent);
        });
    }

    private void startPaymentActivity(int amount, String description) {
        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
        intent.putExtra("selectedAmount", amount);
        intent.putExtra("selectedDescription", description);
        startActivity(intent);
    }
}
