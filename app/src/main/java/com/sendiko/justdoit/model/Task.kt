package com.sendiko.justdoit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sendiko.justdoit.database.Constants

@Entity(tableName = Constants.tableName)
data class Task(
    @PrimaryKey
    val id : Int?,

    @ColumnInfo(name = "task")
    var task : String,

    @ColumnInfo(name = "subject")
    val subject: String?,

    @ColumnInfo(name = "isDone")
    val isDone : Boolean
)
