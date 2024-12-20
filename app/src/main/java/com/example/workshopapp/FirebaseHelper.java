package com.example.workshopapp;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {
    private DatabaseReference databaseReference;

    // Constructor to initialize Firebase reference
    public FirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatBotQA");
    }

    // Add a new question-answer pair
    public void addQuestionAnswer(String question, String answer) {
        String id = databaseReference.push().getKey();
        ChatBotQA qa = new ChatBotQA(question, answer);
        databaseReference.child(id).setValue(qa)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Data added successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add data", e));
    }

    // Fetch all question-answer pairs
    public void fetchAllQuestionsAnswers(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    // Update an existing answer
    public void updateAnswer(String id, String updatedAnswer) {
        databaseReference.child(id).child("answer").setValue(updatedAnswer)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Data updated successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to update data", e));
    }

    // Delete a question-answer pair by ID
    public void deleteQuestionAnswer(String id) {
        databaseReference.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Data deleted successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to delete data", e));
    }
}
