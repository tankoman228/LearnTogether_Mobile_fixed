package com.example.learntogether_mobile.API.Requests;
import com.google.gson.annotations.SerializedName;

public class LoginRequests {
    public static class Login {
        @SerializedName("username")
        public String username;

        @SerializedName("password")
        public String password;
    }
}
