package com.example.learntogether_mobile.API

import com.google.gson.annotations.SerializedName

/**
 * Класс универсальный для обработки/отправки запросов. Сгенерирован скриптами на основе кода для бэкенда,
 * содержимое ответа (значения, которые могут быть не null для данного запроса) см. в коде сервера.
 *
 * Ещё раз, класс СГЕНЕРИРОВАН СКРИПТАМИ, не человеком, разбираться в этом болоте не нужно!
 */
class ResponseU {
    @JvmField
    @SerializedName("Error")
    var Error: String? = null

    @SerializedName("Success")
    var Success: Boolean? = false

    @SerializedName("Comments")
    var Comments: List<ListU>? = null

    @SerializedName("Asks")
    var Asks: List<ListU>? = null

    @SerializedName("Infos")
    var Infos: List<ListU>? = null

    @SerializedName("news")
    var news: List<ListU>? = null

    @SerializedName("tasks")
    var tasks: List<ListU>? = null

    @SerializedName("votes")
    var votes: List<ListU>? = null

    @SerializedName("Contents")
    var Contents: String? = null

    @SerializedName("Meetings")
    var Meetings: List<ListU>? = null

    @SerializedName("Responds")
    var Responds: List<ListU>? = null

    @JvmField
    @SerializedName("Result")
    var Result: String? = null

    @SerializedName("Roles")
    var Roles: List<ListU>? = null

    @SerializedName("Complaints")
    var Complaints: List<ListU>? = null

    @JvmField
    @SerializedName("Token")
    var Token: String? = null

    @SerializedName("List")
    var stringList: List<String>? = null

    @SerializedName("NotificationPort")
    var NotificationPort: Int? = 0

    @JvmField
    @SerializedName("users")
    var users: List<ListU>? = null
}
