package com.task.pupilsmeshtask.Api

import com.task.pupilsmeshtask.Constants
import com.task.pupilsmeshtask.Models.MangaData
import org.json.JSONArray
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap

interface Api {

    @GET(Constants.FETCH_DATA_URL)

    suspend fun getAllData(@QueryMap map:Map<String,String>
    ,@HeaderMap headerMap: Map<String,String>):Response<MangaData?>
}