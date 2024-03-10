package com.example.learntogether_mobile.API.Requests;

import com.google.gson.annotations.SerializedName;

public class Comments {
    public static class GetCommentsRequest {
        @SerializedName("session_token")
        public String sessionToken;

        @SerializedName("id_object")
        public int objectId;
    }

    public static class AddCommentRequest {
        @SerializedName("session_token")
        public String sessionToken;

        @SerializedName("id_object")
        public int objectId;

        @SerializedName("text")
        public String text;

        @SerializedName("attachment")
        public String attachment;
    }
}
