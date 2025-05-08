package com.books.myslelf.db

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.pupilsmeshtask.Models.MangaData

class Converters {

//    @TypeConverter
//    fun fromListToString(villainList:List<MangaData.Data>?):String?{
//
//        return Gson().toJson(villainList)
//
//    }

    @TypeConverter
    fun fromListToString(notesList:List<String>):String?{

        return notesList.joinToString(",")

    }

    @TypeConverter
    fun fromStringToList(listString:String?):List<String?>?{

        return listString?.split(",")?:ArrayList<String?>()

    }

//    @TypeConverter
//    fun fromStringToVillainList(villainString:String?):ArrayList<MangaData.Data>?{
//
//        return Gson().fromJson(villainString, object : TypeToken<List<MangaData.Data>>() {
//
//        }.type)
//
//    }


}