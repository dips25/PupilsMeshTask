package com.task.pupilsmeshtask.Repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.task.pupilsmeshtask.Api.Api
import com.task.pupilsmeshtask.AppDatabase
import com.task.pupilsmeshtask.Constants
import com.task.pupilsmeshtask.Models.MangaData
import com.task.pupilsmeshtask.Models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date


object Repo {

    private val TAG = Repo::class.simpleName

    var allData:MutableLiveData<ArrayList<MangaData.Data>> = MutableLiveData()
    var allDataList:ArrayList<MangaData.Data> = ArrayList()

    val keySet:HashSet<String?> = HashSet()
    var newList:ArrayList<MangaData.Data>? = null
    var oldList: ArrayList<MangaData.Data>?=null


    lateinit var r: Retrofit

    lateinit var db: AppDatabase

    fun buildDB(c: Context) {

        synchronized(this) {

            if (!Repo::r.isInitialized) {

                r = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                if (!Repo::db.isInitialized) {
                    db = Room.databaseBuilder(
                        c,
                        AppDatabase::class.java,
                        "my-data-db"
                    ).build()

                }
            }

        }
    }


    suspend fun getAllData(page:Int) {

        if (page == 1) {

            allDataList.clear()
            allData.postValue(allDataList)
        }

        val map:MutableMap<String,String> = mutableMapOf()
        map.put("page",page.toString())
        map.put("nsfw",true.toString())
        map.put("type","all")

        val headerMap:MutableMap<String,String> = mutableMapOf()
        headerMap.put("x-rapidapi-host","mangaverse-api.p.rapidapi.com")
        headerMap.put("x-rapidapi-key","09608ac339msh6ac735cbf151285p1f0d22jsn915f3640f093")



        r?.apply {

            val response:Response<MangaData?> = r.create(Api::class.java)
                .getAllData(map,headerMap)

            response?.let {

                if (response.isSuccessful) {

                    var mangaData:MangaData? = response.body()

                    mangaData?.let {

                        newList = ArrayList(mangaData.data)

                        oldList = ArrayList(db.getDataDao().getAllData())

                        withContext(Dispatchers.Main) {


                            newList!!.forEach {

                                if (!oldList!!.contains(it)) {

                                    db.getDataDao().insertData(it)
                                    addItem(it)


                                } else {

                                    addItem(it)


                                }

                            }

                            oldList!!.clear()
                            newList!!.clear()
                            oldList = null
                        }



                    }





                }


            }

        }


    }

    suspend fun getAllLocalData() {

        withContext(Dispatchers.Main){

            allDataList.clear()
            allData.postValue(allDataList)
            newList?.clear()

            var localList = db.getDataDao().getAllData()
            allDataList.addAll(localList)
            allData.postValue(allDataList)


        }


    }




    private suspend fun addItem(item:MangaData.Data) {




            allDataList.add(item)
            allData.value = allDataList

    }

    public suspend fun insertUsers(user: Users) {

        db.getUsersDao().insertUser(user)
    }

    public suspend fun searchUser(user: Users):Users? {

       return db.getUsersDao().searchSingleUser(user.email,user.password)
    }




}



