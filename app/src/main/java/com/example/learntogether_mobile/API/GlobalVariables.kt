package com.example.learntogether_mobile.API

import android.content.Context
import android.graphics.Bitmap
import com.example.learntogether_mobile.API.Loaders.GroupsAndUsersLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Глобальные переменные о данных сессии, пользователе и т.д., нужны для отправки запросов через Retrofit
 * и отображения некоторых данных.
 * Выполняет сохранение и загрузку из shared prefs
 */
object GlobalVariables {

    @JvmField
    var SessionToken: String? = null
    @JvmField
    var username: String? = null
    @JvmField
    var password: String? = null
    @JvmField
    var server: String? = "http://80.89.196.150:8000/"
    @JvmField
    var current_id_group = -1
    @JvmField
    var Title: String? = null
    @JvmField
    var AboutMe: String? = null
    @JvmField
    var myIcon: Bitmap? = null
    @JvmField
    var myPermissions: List<String>? = null

    @JvmStatic
    fun saveValuesToSharedPrefs(context: Context) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("SessionToken", SessionToken)
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putString("server", server)
        editor.putInt("current_id_group", current_id_group)
        editor.apply()
    }

    @JvmStatic
    fun loadValuesFromSharedPrefs(context: Context) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        SessionToken = sharedPref.getString("SessionToken", null)
        username = sharedPref.getString("username", null)
        password = sharedPref.getString("password", null)
        server = sharedPref.getString("server", "http://80.89.196.150:8000/")
        current_id_group = sharedPref.getInt("current_id_group", -1)
    }

    /**
     * Спрашивает у сервера о том, какие разрешения есть у пользователя в текущей группе
     */
    @JvmStatic
    fun requireMyAccountInfo(context: Context) {
        val requestU = RequestU()
        requestU.session_token = SessionToken
        if (current_id_group == -1) {
            GroupsAndUsersLoader.UpdateCacheGroups {
                current_id_group = GroupsAndUsersLoader.Groups[0].ID_Group
                saveValuesToSharedPrefs(context)
                requestU.id_group = current_id_group
                RetrofitRequest().apiService.my_account_info(requestU)
                    .enqueue(object : Callback<ListU> {
                        override fun onResponse(call: Call<ListU>, response: Response<ListU>) {
                            Title = response.body()!!.Title
                            AboutMe = response.body()!!.Text
                            myIcon = ImageConverter.decodeImage(response.body()!!.Icon)
                            myPermissions = response.body()!!.Items
                        }

                        override fun onFailure(call: Call<ListU>, t: Throwable) {}
                    })
            }
        } else {
            requestU.id_group = current_id_group
            RetrofitRequest().apiService.my_account_info(requestU)
                .enqueue(object : Callback<ListU> {
                    override fun onResponse(call: Call<ListU>, response: Response<ListU>) {
                        Title = response.body()!!.Title
                        AboutMe = response.body()!!.Text
                        myIcon = ImageConverter.decodeImage(response.body()!!.Icon)
                        myPermissions = response.body()!!.Items
                    }

                    override fun onFailure(call: Call<ListU>, t: Throwable) {}
                })
        }
    }

    @JvmStatic
    fun IsAllowed(permission: String): Boolean {
        for (s in myPermissions!!) {
            if (permission == s) {
                return true
            }
        }
        return false
    }
}