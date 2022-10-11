package com.sendiko.justdoit.api.response.auth

import com.google.gson.annotations.SerializedName

data class RefreshResponse(

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: DataRefresh? = null,

	@field:SerializedName("meta")
	val meta: MetaRefresh? = null,

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

data class ContentRefresh(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("accept")
	val accept: String? = null
)

data class MetaRefresh(

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
	val content: ContentRefresh? = null
)

data class MetadataRefresh(

	@field:SerializedName("total_data")
	val totalData: Int? = null,

	@field:SerializedName("count_data")
	val countData: Int? = null
)

data class DataRefresh(

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
