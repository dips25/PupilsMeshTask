package com.task.pupilsmeshtask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.task.pupilsmeshtask.Models.MangaData

@Dao

interface DataDao {

    @Insert
    suspend fun insertData(books: MangaData.Data);

    @Query("select * from Data")
    suspend fun getAllData():List<MangaData.Data>

    @Query("Delete from Data where id=:bookId")
    suspend fun deleteData(bookId:Int)

    @Update
    suspend fun updateData(b: MangaData.Data)

    @Delete
    suspend fun deleteAllData(dBookList:List<MangaData.Data>)

    @Query("select * from Data where LOWER(Title) like LOWER(:s || '%')")
    suspend fun searchData(s:String):List<MangaData.Data>



}