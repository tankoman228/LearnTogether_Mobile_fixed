package com.example.learntogether_mobile.API

import com.google.gson.annotations.SerializedName

/**
 * Класс универсальный для обработки/отправки запросов. Сгенерирован скриптами на основе кода для бэкенда,
 * содержимое ответа (значения, которые могут быть не null для данного запроса) см. в коде сервера.
 *
 * Ещё раз, класс СГЕНЕРИРОВАН СКРИПТАМИ, не человеком, разбираться в этом болоте не нужно!
 */
open class RequestU {

    @SerializedName("session_token")
    var session_token: String? = null

    @SerializedName("id_object")
    var id_object = 0

    @SerializedName("search_string")
    var search_string: String? = null

    @JvmField
    @SerializedName("username")
    var username: String? = null

    @JvmField
    @SerializedName("token")
    var token: String? = null

    @JvmField
    @SerializedName("password")
    var password: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("attachment")
    var attachment: String? = null

    @SerializedName("ID_InfoBase")
    var ID_InfoBase = 0

    @SerializedName("id_group")
    var id_group = 0

    @JvmField
    @SerializedName("title")
    var title: String? = null

    @SerializedName("tags")
    var tags: String? = null

    @SerializedName("group")
    var group = 0

    @SerializedName("NewName")
    var NewName: String? = null

    @SerializedName("NewIcon")
    var NewIcon: String? = null

    @SerializedName("NewDescription")
    var NewDescription: String? = null

    @SerializedName("contents")
    var contents: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("starts")
    var starts: String? = null

    @SerializedName("place")
    var place: String? = null

    @SerializedName("ID_Group")
    var ID_Group = 0

    @SerializedName("ID_Role")
    var ID_Role = 0

    @SerializedName("Text")
    var TextH: String? = null

    @SerializedName("ID_Account")
    var ID_Account = 0

    @SerializedName("Name")
    var Name: String? = null

    @SerializedName("IsAdmin")
    var IsAdmin = false

    @SerializedName("AdminLevel")
    var AdminLevel = 0

    @SerializedName("Reason")
    var Reason: String? = null

    @SerializedName("images")
    var images: String? = null

    @SerializedName("deadline")
    var deadline: String? = null

    @SerializedName("recovery_contact")
    var recovery_contact: String? = null

    @JvmField
    @SerializedName("contact")
    var contact: String? = null

    @JvmField
    @SerializedName("Rank")
    var Rank: Int? = null

    @SerializedName("number")
    var number: Int? = null

    @JvmField
    @SerializedName("id")
    var id: Int? = null
}
