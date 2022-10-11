package com.sendiko.justdoit.api

import com.sendiko.justdoit.api.response.auth.RefreshResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshTokenApi {

    @POST("refresh")
    suspend fun refreshToken(
        @Header("Authorization") token : String
    ) : RefreshResponse

}