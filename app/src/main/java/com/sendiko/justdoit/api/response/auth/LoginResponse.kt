package com.sendiko.justdoit.api.response.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("metadata")
	val metadata: MetadataLogin? = null,

	@field:SerializedName("data")
	val data: DataLogin? = null,

	@field:SerializedName("meta")
	val meta: MetaLogin? = null,

	@field:SerializedName("links")
	val links: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class DataLogin(

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("surename")
	val surename: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class ContentLogin(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("accept")
	val accept: String? = null
)

data class MetadataLogin(

	@field:SerializedName("total_data")
	val totalData: Int? = null,

	@field:SerializedName("count_data")
	val countData: Int? = null
)

data class MetaLogin(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("host")
	val host: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("version")
	val version: String? = null,

	@field:SerializedName("content")
	val content: ContentLogin? = null
)
