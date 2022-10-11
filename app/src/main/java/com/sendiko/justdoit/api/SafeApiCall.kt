package com.sendiko.justdoit.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ) : Resources<T>{
        return withContext(Dispatchers.IO){
            try{
                Resources.Success(apiCall.invoke())
            } catch (t : Throwable) {
                when(t){
                    is HttpException ->
                        Resources.Failure(
                            false,
                            t.code(),
                            t.response()?.errorBody()
                        )
                    else -> Resources.Failure(true, null, null)
                }
            }
        }
    }

}