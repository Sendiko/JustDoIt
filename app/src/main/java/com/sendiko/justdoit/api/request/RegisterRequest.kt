package com.sendiko.justdoit.api.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("surename")
	val surename: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: String? = null
)
