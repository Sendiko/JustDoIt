package com.sendiko.justdoit.api.response.task

import com.google.gson.annotations.SerializedName

data class PostTaskResponse(

	@field:SerializedName("metadata")
	val metadata: MetadataPostTask? = null,

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("meta")
	val meta: MetaPostTask? = null,

	@field:SerializedName("links")
	val links: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ContentPostTask(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("accept")
	val accept: String? = null
)

data class MetadataPostTask(

	@field:SerializedName("total_data")
	val totalData: Int? = null,

	@field:SerializedName("count_data")
	val countData: Int? = null
)

data class MetaPostTask(

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
	val content: ContentPostTask? = null
)
