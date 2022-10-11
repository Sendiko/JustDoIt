package com.sendiko.justdoit.api.request

import com.google.gson.annotations.SerializedName

data class UpdateDeleteTaskRequest(

	@field:SerializedName("task")
	val task: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("isDone")
	val isDone: String? = null
)
