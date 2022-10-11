package com.sendiko.justdoit.api

import com.sendiko.justdoit.api.request.LoginRequest
import com.sendiko.justdoit.api.request.RegisterRequest
import com.sendiko.justdoit.api.request.TaskRequest
import com.sendiko.justdoit.api.request.UpdateDeleteTaskRequest
import com.sendiko.justdoit.api.response.auth.LoginResponse
import com.sendiko.justdoit.api.response.auth.LogoutResponse
import com.sendiko.justdoit.api.response.auth.RegisterResponse
import com.sendiko.justdoit.api.response.task.GetTaskResponse
import com.sendiko.justdoit.api.response.task.PostTaskResponse
import com.sendiko.justdoit.api.response.task.UpdateTaskResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun postLogin(
        @Body loginRequest: LoginRequest
    ) : Call<LoginResponse>

    @POST("register")
    fun postRegister(
        @Body registerRequest: RegisterRequest
    ) : Call<RegisterResponse>

    @POST("logout")
    fun postLogout(
        @Header("Authorization") token : String
    ) : Call<LogoutResponse>

    @GET("task")
    fun getTask(
        @Header("Authorization") token: String
    ) : Call<GetTaskResponse>

    @POST("task")
    fun postTask(
        @Header("Authorization") token : String,
        @Body taskRequest : TaskRequest
    ) : Call<PostTaskResponse>

    @PATCH("task")
    fun patchTask(
        @Header("Authorization") token : String,
        @Body updateTask : UpdateDeleteTaskRequest
    ) : Call<UpdateTaskResponse>

    @DELETE("task")
    fun deleteTask(
        @Header("Authorization") token : String,
        @Body updateTask : UpdateDeleteTaskRequest
    )

}