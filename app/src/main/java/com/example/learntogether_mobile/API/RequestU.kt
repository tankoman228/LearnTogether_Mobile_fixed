package com.example.learntogether_mobile.API;

import com.google.gson.annotations.SerializedName;

/**
 * Класс универсальный для обработки/отправки запросов. Сгенерирован скриптами на основе кода для бэкенда,
 * содержимое ответа (значения, которые могут быть не null для данного запроса) см. в коде сервера.
 *
 * Ещё раз, класс СГЕНЕРИРОВАН СКРИПТАМИ, не человеком, разбираться в этом болоте не нужно!
 */
public class RequestU {
    @SerializedName("session_token")
    public String session_token;

    @SerializedName("id_object")
    public int id_object;

    @SerializedName("username")
    public String username;

    @SerializedName("token")
    public String token;

    @SerializedName("password")
    public String password;

    @SerializedName("text")
    public String text;

    @SerializedName("attachment")
    public String attachment;

    @SerializedName("ID_InfoBase")
    public int ID_InfoBase;

    @SerializedName("id_group")
    public int id_group;

    @SerializedName("title")
    public String title;

    @SerializedName("tags")
    public String tags;

    @SerializedName("group")
    public int group;

    @SerializedName("NewName")
    public String NewName;

    @SerializedName("NewIcon")
    public String NewIcon;

    @SerializedName("NewDescription")
    public String NewDescription;

    @SerializedName("contents")
    public String contents;

    @SerializedName("type")
    public String type;

    @SerializedName("starts")
    public String starts;

    @SerializedName("place")
    public String place;

    @SerializedName("ID_Group")
    public int ID_Group;

    @SerializedName("ID_Role")
    public int ID_Role;

    @SerializedName("Text")
    public String Text;

    @SerializedName("ID_Account")
    public int ID_Account;

    @SerializedName("Name")
    public String Name;

    @SerializedName("IsAdmin")
    public boolean IsAdmin;

    @SerializedName("AdminLevel")
    public int AdminLevel;

    @SerializedName("Reason")
    public String Reason;

    @SerializedName("images")
    public String images;

    @SerializedName("deadline")
    public String deadline;

    @SerializedName("recovery_contact")
    public String recovery_contact;

    @SerializedName("contact")
    public String contact;
}
