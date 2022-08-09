package com.sendiko.justdoit.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class Task(

   @PrimaryKey(autoGenerate = true)
   var id : Int?= null,

   @ColumnInfo(name = "task")
   var task : String?= null,

   @ColumnInfo(name = "subject")
   var subject: String?= null,

   @ColumnInfo(name = "isDone")
   var isDone : Boolean?= false

)
