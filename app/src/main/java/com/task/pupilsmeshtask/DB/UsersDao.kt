package com.task.pupilsmeshtask.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.task.pupilsmeshtask.Models.MangaData
import com.task.pupilsmeshtask.Models.Users

@Dao

interface UsersDao {

    @Insert
    suspend fun insertUser(user: Users);

    @Query("select * from Users where email=:email limit 1")
    suspend fun searchUser(email:String):Users?

    suspend fun searchSingleUser(email:String,password:String):Users? {

        val user:Users? = searchUser(email)

        return user

    }
}