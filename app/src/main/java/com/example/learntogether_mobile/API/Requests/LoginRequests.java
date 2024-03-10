package com.example.learntogether_mobile.API.Requests;
import com.google.gson.annotations.SerializedName;

public class LoginRequests {
    public static class Login {
        @SerializedName("username")
        public String username;

        @SerializedName("password")
        public String password;
    }

    public static class Register {
        @SerializedName("token")
        public String token;

        @SerializedName("username")
        public String username;

        @SerializedName("password")
        public String password;

        @SerializedName("contact")
        public String contact;

        @SerializedName("title")
        public String title;
    }

    //
    public class Recovery {
        @SerializedName("recovery_contact")
        public String recoveryContact;
    }
}
