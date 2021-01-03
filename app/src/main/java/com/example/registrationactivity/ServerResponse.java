package com.example.registrationactivity;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    String getMessage() {
        return message;
    }
    boolean getSuccess() {
        return success;
    }
}
