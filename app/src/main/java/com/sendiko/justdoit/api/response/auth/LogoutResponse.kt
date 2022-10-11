package com.sendiko.justdoit.api.response.auth

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: DataLogout? = null,

	@field:SerializedName("meta")
	val meta: MetaLogout? = null,

	@field:SerializedName("links")
	val links: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataLogout(

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

data class MetaLogout(

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
	val content: ContentLogout? = null
)

data class MetadataLogout(

	@field:SerializedName("total_data")
	val totalData: Int? = null,

	@field:SerializedName("count_data")
	val countData: Int? = null
)

data class ContentLogout(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("accept")
	val accept: String? = null
)
