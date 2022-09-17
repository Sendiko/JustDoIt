package com.sendiko.justdoit.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sendiko.justdoit.repository.Constant

@Entity(tableName = "taskTable")
data class Task(

   @PrimaryKey(autoGenerate = true)
   val id: Int,

   @ColumnInfo(name = "task")
   val task : String?= null,

   @ColumnInfo(name = "subject")
   val subject: String?= null,

   @ColumnInfo(name = "priority", defaultValue = "low")
   val priority : String?= null,

   @ColumnInfo(name = "isDone")
   val isDone : String?= null

)
