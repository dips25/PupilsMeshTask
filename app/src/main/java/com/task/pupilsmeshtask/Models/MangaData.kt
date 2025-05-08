package com.task.pupilsmeshtask.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MangaData {

    @SerializedName("code")
    @Expose
    var code:Int=0
        get() = field
        set(value){

            field = value
        }


    @SerializedName("data")
    @Expose
    var data:List<Data>?=null
        get() = field
        set(value){

            field = value
        }

    @Entity(tableName = "Data")
    data class Data(

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        @SerializedName("id")
        @Expose
        val id: String = "",

        @ColumnInfo(name = "title")
        @SerializedName("title")
        @Expose
        val title: String? = null,

        @ColumnInfo(name = "sub_title")
        @SerializedName("sub_title")
        @Expose
        val subTitle: String? = null,

        @ColumnInfo(name = "status")
        @SerializedName("status")
        @Expose
        val status: String? = null,

        @ColumnInfo(name = "thumb")
        @SerializedName("thumb")
        @Expose
        val thumb: String? = null,

        @ColumnInfo(name = "summary")
        @SerializedName("summary")
        @Expose
        val summary: String? = null,

        @ColumnInfo(name = "authors")
        @SerializedName("authors")
        @Expose
        val authors: List<String>? = null,

        @ColumnInfo(name = "genres")
        @SerializedName("genres")
        @Expose
        val genres: List<String>? = null,

        @ColumnInfo(name = "nsfw")
        @SerializedName("nsfw")
        @Expose
        val nsfw: Boolean? = null,

        @ColumnInfo(name = "type")
        @SerializedName("type")
        @Expose
        val type: String? = null,

        @ColumnInfo(name = "total_chapter")
        @SerializedName("total_chapter")
        @Expose
        val totalChapter: Int? = null,

        @ColumnInfo(name = "create_at")
        @SerializedName("create_at")
        @Expose
        val createAt: Long? = null,

        @ColumnInfo(name = "update_at")
        @SerializedName("update_at")
        @Expose
        val updateAt: Long? = null


    ) {

        override fun equals(other: Any?): Boolean {

          return this.id.equals((other as MangaData.Data).id)
        }
    }





}
