package com.example.learntogether_mobile.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;

/**
 * Для обработки/отправки запросов ЧЕРЕЗ RETROFIT. Сгенерирован скриптами на основе кода для бэкенда,
 * содержимое ответа (значения, которые могут быть не null для данного запроса) см. в коде сервера.
 *
 * Ещё раз, интерфейс СГЕНЕРИРОВАН СКРИПТАМИ, не человеком, разбираться в этом болоте не нужно!
 */
public interface ApiServiceInterface {

    @POST("/comments/get_comments")
    Call<ResponseU> get_comments(@Body RequestU request);

    @POST("/notifications/check_notifications")
    Call<ResponseU> check_notifications(@Body RequestU request);

    @POST("/comments/add_comment")
    Call<ResponseU> add_comment(@Body RequestU request);

    @DELETE("/comments/delete_comment")
    Call<ResponseU> delete_comment(@Body RequestU request);

    @POST("/comments/rate")
    Call<ResponseU> rate(@Body RequestU request);

    @POST("/forum/get_asks")
    Call<ResponseU> get_asks(@Body RequestU request);

    @POST("/forum/add_forum_ask")
    Call<ResponseU> add_forum_ask(@Body RequestU request);

    @PUT("/forum/mark_solved")
    Call<ResponseU> mark_solved(@Body RequestU request);

    @DELETE("/forum/delete_ask")
    Call<ResponseU> delete_ask(@Body RequestU request);

    @POST("/groups/get_groups")
    Call<ResponseU> get_groups(@Body RequestU request);

    @POST("/groups/get_users")
    Call<ResponseU> get_users(@Body RequestU request);

    @POST("/groups/join_group")
    Call<ResponseU> join_group(@Body RequestU request);

    @POST("/groups/edit_group")
    Call<ResponseU> edit_group(@Body RequestU request);

    @POST("/info/get_infos")
    Call<ResponseU> get_infos(@Body RequestU request);

    @POST("/info/download")
    Call<ResponseU> download(@Body RequestU request);

    @POST("/info/add_info")
    Call<ResponseU> add_info(@Body RequestU request);

    @POST("/meetings/get_meetings")
    Call<ResponseU> get_meetings(@Body RequestU request);

    @POST("/meetings/get_meeting")
    Call<ResponseU> get_meeting(@Body RequestU request);

    @POST("/meetings/join_meeting")
    Call<ResponseU> join_meeting(@Body RequestU request);

    @POST("/meetings/add_meeting")
    Call<ResponseU> add_meeting(@Body RequestU request);

    @POST("/moderator/get_roles")
    Call<ResponseU> get_roles(@Body RequestU request);

    @POST("/moderator/create_token")
    Call<ResponseU> create_token(@Body RequestU request);

    @POST("/moderator/delete_ib")
    Call<ResponseU> delete_ib(@Body RequestU request);

    @POST("/moderator/change_user_role")
    Call<ResponseU> change_user_role(@Body RequestU request);

    @POST("/moderator/new_role")
    Call<ResponseU> new_role(@Body RequestU request);

    @POST("/moderator/delete_role")
    Call<ResponseU> delete_role(@Body RequestU request);

    @POST("/moderator/block_account")
    Call<ResponseU> block_account(@Body RequestU request);

    @POST("/moderator/complaint")
    Call<ResponseU> complaint(@Body RequestU request);

    @POST("/moderator/get_complaints")
    Call<ResponseU> get_complaints(@Body RequestU request);

    @POST("/moderator/get_news")
    Call<ResponseU> get_news(@Body RequestU request);

    @POST("/news/accept_news")
    Call<ResponseU> accept_news(@Body RequestU request);

    @POST("/news/add_news")
    Call<ResponseU> add_news(@Body RequestU request);

    @POST("/news/add_task")
    Call<ResponseU> add_task(@Body RequestU request);

    @POST("/news/get_tasks_statuses")
    Call<ResponseU> get_tasks_statuses(@Body RequestU request);

    @POST("/news/update_task_status")
    Call<ResponseU> update_task_status(@Body RequestU request);

    @POST("/news/get_vote_info")
    Call<ResponseU> get_vote_info(@Body RequestU request);

    @POST("/news/add_vote")
    Call<ResponseU> add_vote(@Body RequestU request);

    @POST("/news/vote")
    Call<ResponseU> vote(@Body RequestU request);

    @POST("/login/login")
    Call<ResponseU> login(@Body RequestU request);

    @POST("/login/register")
    Call<ResponseU> register(@Body RequestU request);

    @POST("/login/request_recovery")
    Call<ResponseU> request_recovery(@Body RequestU request);

    @POST("/test/Test")
    Call<ResponseU> Test(@Body RequestU request);

}