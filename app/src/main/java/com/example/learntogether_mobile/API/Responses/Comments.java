package com.example.learntogether_mobile.API.Responses;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comments {
    public static class Comment {
        @SerializedName("ID_Comment")
        public int commentId;

        @SerializedName("ID_Author")
        public int authorId;

        @SerializedName("Text")
        public String text;

        @SerializedName("Author")
        public String author;

        @SerializedName("DateTime")
        public String dateTime;

        @SerializedName("Avatar")
        public String avatar;

        @SerializedName("Attachment")
        public List<String> attachments;
    }

    public static class GetCommentsResponse {
        @SerializedName("comments")
        public List<Comment> comments;
    }

    public class AddCommentResponse {
        @SerializedName("Success")
        public String success;

        @SerializedName("Error")
        public String error;
    }
}
