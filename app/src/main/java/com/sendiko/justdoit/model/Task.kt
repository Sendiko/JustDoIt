package com.sendiko.justdoit.model

data class Task(
   var id : String?= null,
   var task : String?= null,
   val subject: String?= null,
   val isDone : Boolean?= false
)
