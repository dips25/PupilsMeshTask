package com.task.pupilsmeshtask.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Users")
data class Users(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id:Int=0,

    @ColumnInfo("email")
    val email:String,

    @ColumnInfo("password")
    val password:String)
