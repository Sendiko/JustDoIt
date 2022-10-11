package com.sendiko.justdoit.api

import okhttp3.ResponseBody
import retrofit2.Response

open class Resources<out T> {
    data class Success<out T>(val value : T) : Resources<T>()
    data class Failure(
        val isNetworkError : Boolean,
        val errorCode : Int?,
        val errorBody : ResponseBody?
    ) : Resources<Nothing>()
}