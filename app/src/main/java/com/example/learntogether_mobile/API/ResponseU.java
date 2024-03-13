package com.example.learntogether_mobile.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Класс универсальный для обработки/отправки запросов. Сгенерирован скриптами на основе кода для бэкенда,
 * содержимое ответа (значения, которые могут быть не null для данного запроса) см. в коде сервера.
 *
 * Ещё раз, класс СГЕНЕРИРОВАН СКРИПТАМИ, не человеком, разбираться в этом болоте не нужно!
 */
public class ResponseU {
    @SerializedName("Error")
    public String Error;

    @SerializedName("Success")
    public boolean Success;

    @SerializedName("Comments")
    public List<ListU> Comments;

    @SerializedName("Asks")
    public List<ListU> Asks;

    @SerializedName("Infos")
    public List<ListU> Infos;

    @SerializedName("Contents")
    public String Contents;

    @SerializedName("Meetings")
    public List<ListU> Meetings;

    @SerializedName("Responds")
    public List<ListU> Responds;

    @SerializedName("Result")
    public String Result;

    @SerializedName("Roles")
    public List<ListU> Roles;

    @SerializedName("Complaints")
    public List<ListU> Complaints;

    @SerializedName("Token")
    public String Token;

    @SerializedName("List")
    public List<String> stringList;

    @SerializedName("NotificationPort")
    public int NotificationPort;
}
