package com.dewansoft.app.chatapp;

public class Message {
    private String  message,mode;

    public Message() {
    }

    public Message(String message, String mode) {
        this.message = message;
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
