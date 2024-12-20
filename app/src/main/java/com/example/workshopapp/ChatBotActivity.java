package com.example.workshopapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ChatBotActivity extends AppCompatActivity {

    private LinearLayout linearLayoutQuestions;
    private TextView tvAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize the views
        linearLayoutQuestions = findViewById(R.id.linearLayoutQuestions);
        tvAnswer = findViewById(R.id.tvAnswer);

        // Fetch questions from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                            // Loop through the documents and create a button for each question
                            for (DocumentSnapshot document : documents) {
                                String question = document.getString("question");
                                String answer = document.getString("answer");

                                // Create a button for each question
                                Button questionButton = new Button(ChatBotActivity.this);
                                questionButton.setText(question);
                                questionButton.setOnClickListener(v -> tvAnswer.setText(answer));

                                // Add the button to the LinearLayout
                                linearLayoutQuestions.addView(questionButton);
                            }
                        }
                    } else {
                        Log.w("Firebase", "Error getting documents.", task.getException());
                    }
                });
    }
}
