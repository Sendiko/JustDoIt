package com.sendiko.justdoit.api

import android.content.Context
import com.sendiko.justdoit.api.response.auth.RefreshResponse
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.ui.container.dataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenRefresher @Inject constructor(
    context : Context,
    private val refreshTokenApi: RefreshTokenApi
) : Authenticator, SafeApiCall {

    private val context1 = context.applicationContext
    private val authPreferences = AuthPreferences.getInstance(context1.dataStore)
    private val endPoint = "https://benjamin4k.my.id/justdoit/api/login"
    override fun authenticate(route: Route?, response: Response): Request? {
        val responseCode = response.code
        val responseRequest = response.request.url.toString()
        return runBlocking {
            when {
                responseCode == 401 && responseRequest != endPoint -> {
                    when(val tokenResponse = refreshToken()){
                        is Resources.Success -> {
                            authPreferences.saveToken(tokenResponse.value.token.toString())
                            response.request.newBuilder()
                                .header("Authorization", "Bearer ${tokenResponse.value.token}")
                                .build()
                        }
                        else -> null
                    }
                }
                else -> null
            }
        }
    }

    private suspend fun refreshToken() : Resources<RefreshResponse> {
        val token = "Bearer " + authPreferences.getToken()
        return safeApiCall {
            refreshTokenApi.refreshToken(token)
        }
    }

}