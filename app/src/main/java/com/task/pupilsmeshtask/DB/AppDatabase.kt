package com.task.pupilsmeshtask

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.books.myslelf.db.Converters
import com.task.pupilsmeshtask.DB.UsersDao
import com.task.pupilsmeshtask.Models.MangaData
import com.task.pupilsmeshtask.Models.Users

@Database(entities = [MangaData.Data::class,Users::class] , version = 1)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getDataDao() : DataDao
    abstract fun getUsersDao():UsersDao
}