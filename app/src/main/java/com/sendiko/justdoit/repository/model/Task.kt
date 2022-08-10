package com.sendiko.justdoit.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
class Task(

   @ColumnInfo(name = "task")
   var task : String?= null,

   @ColumnInfo(name = "subject")
   var subject: String?= null,

   @ColumnInfo(name = "isDone")
   var isDone : String?= null

) {
   @PrimaryKey(autoGenerate = true)
   var id = 0
}
