package com.example.learntogether_mobile.API.Responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponses {
    public class Login {
        @SerializedName("Result")
        public String result;

        @SerializedName("Token")
        public String token;
    }
}
