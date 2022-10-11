package com.sendiko.justdoit.api.response.task

import com.google.gson.annotations.SerializedName

data class GetTaskResponse(

	@field:SerializedName("metadata")
	val metadata: MetadataGetTask? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("meta")
	val meta: MetaGetTask? = null,

	@field:SerializedName("links")
	val links: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class User(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

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

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class DataItem(

	@field:SerializedName("task")
	val task: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("relationship")
	val relationship: Relationship? = null,

	@field:SerializedName("isDone")
	val isDone: String? = null
)

data class MetaGetTask(

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
	val content: ContentGetTask? = null
)

data class MetadataGetTask(

	@field:SerializedName("total_data")
	val totalData: Int? = null,

	@field:SerializedName("count_data")
	val countData: Int? = null
)

data class ContentGetTask(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("accept")
	val accept: String? = null
)

data class Relationship(

	@field:SerializedName("user")
	val user: User? = null
)
