package com.example.learntogether_mobile.API

import com.google.gson.annotations.SerializedName

/**
 * Класс универсальный для обработки полученных списков после запроса. Сгенерирован скриптами на основе кода для бэкенда,
 * содержимое ответа (значения, которые не могут быть null для данного запроса) см. в коде сервера.
 *
 * Ещё раз, класс СГЕНЕРИРОВАН СКРИПТАМИ, не человеком, разбираться в этом болоте не нужно!
 */
class ListU {
    @SerializedName("NeedHelp")
    var NeedHelp = false

    @SerializedName("AccountTitle")
    var AccountTitle: String? = null

    @SerializedName("Author")
    var Author: String? = null

    @SerializedName("About")
    var About: String? = null

    @SerializedName("Rating")
    var Rating = 0f

    @SerializedName("Role")
    var Role: String? = null

    @SerializedName("Start")
    var Start: Int? = null

    @SerializedName("Sender")
    var Sender: String? = null

    @SerializedName("Username")
    var Username: String? = null

    @SerializedName("ID_Vote")
    var ID_Vote = 0

    @SerializedName("ID_Group")
    var ID_Group = 0

    @SerializedName("ID_Role")
    var ID_Role = 0

    @SerializedName("Account")
    var Account: String? = null

    @SerializedName("Description")
    var Description: String? = null

    @SerializedName("PeopleJoined")
    var PeopleJoined = 0

    @SerializedName("Items")
    var Items: List<String>? = null

    @SerializedName("WhenAdd")
    var WhenAdd: String? = null

    @SerializedName("LastSeen")
    var LastSeen: String? = null

    @SerializedName("StartsAt")
    var StartsAt: String? = null

    @SerializedName("ID_InfoBase")
    var ID_InfoBase = 0

    @SerializedName("Finished")
    var Finished = false

    @SerializedName("ID_Task")
    var ID_Task = 0

    @SerializedName("Reason")
    var Reason: String? = null

    @SerializedName("ID_News")
    var ID_News = 0

    @SerializedName("Item")
    var Item: String? = null

    @SerializedName("Text")
    var Text: String? = null

    @SerializedName("Deadline")
    var Deadline: String? = null

    @SerializedName("TaskTitle")
    var TaskTitle: String? = null

    @SerializedName("Surety")
    var Surety = 0f

    @SerializedName("Type")
    var Type: String? = null

    @SerializedName("Progress")
    var Progress = 0f

    @SerializedName("IsAdmin")
    var IsAdmin = false

    @SerializedName("Permissions")
    var Permissions: List<String>? = null

    @SerializedName("Anonymous")
    var Anonymous = false

    @SerializedName("Title")
    var Title: String? = null

    @SerializedName("Moderated")
    var Moderated = false

    @SerializedName("ID_Complaint")
    var ID_Complaint = 0

    @SerializedName("Suspected")
    var Suspected: String? = null

    @JvmField
    @SerializedName("Rate")
    var Rate = 0f

    @SerializedName("Priority")
    var Priority = 0f

    @SerializedName("SuspectedID")
    var SuspectedID = 0

    @SerializedName("End")
    var End: Int? = null

    @SerializedName("Name")
    var Name: String? = null

    @SerializedName("ID_Meeting")
    var ID_Meeting = 0

    @SerializedName("Images")
    var Images: String? = null

    @SerializedName("CommentsFound")
    var CommentsFound: String? = null

    @SerializedName("Count")
    var Count: String? = null

    @SerializedName("ID_Comment")
    var ID_Comment = 0

    @SerializedName("Place")
    var Place: String? = null

    @SerializedName("ID_Author")
    var ID_Author = 0

    @SerializedName("Avatar")
    var Avatar: String? = null

    @SerializedName("ID_Account")
    var ID_Account = 0

    @SerializedName("AuthorTitle")
    var AuthorTitle: String? = null

    @SerializedName("Attachment")
    var Attachment: String? = null

    @SerializedName("ID_Information")
    var ID_Information = 0

    @SerializedName("SenderID")
    var SenderID = 0

    @SerializedName("DateTime")
    var DateTime: String? = null

    @SerializedName("Icon")
    var Icon: String? = null

    @SerializedName("AdminLevel")
    var AdminLevel = 0

    @SerializedName("ID_ForumAsk")
    var ID_ForumAsk = 0

    @SerializedName("Solved")
    var Solved = false

    @JvmField
    var type_ = ""

    fun initByType(type: String): ListU {
        this.type_ = type;
        return this
    }
}
