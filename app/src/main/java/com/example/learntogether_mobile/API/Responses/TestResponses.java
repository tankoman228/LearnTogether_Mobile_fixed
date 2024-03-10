package com.example.learntogether_mobile.API.Responses;

import com.google.gson.annotations.SerializedName;

public class TestResponses {
    public static class Test {
        @SerializedName("Success")
        public boolean success;

        @SerializedName("NotificationPort")
        public int notificationPort;
    }
}
