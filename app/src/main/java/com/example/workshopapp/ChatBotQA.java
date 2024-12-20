package com.example.workshopapp;

public class ChatBotQA {
    private String question;
    private String answer;

    // Default constructor required for Firebase
    public ChatBotQA() {}

    public ChatBotQA(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
