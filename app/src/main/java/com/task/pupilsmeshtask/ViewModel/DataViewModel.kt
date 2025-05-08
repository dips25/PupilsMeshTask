package com.task.pupilsmeshtask.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.pupilsmeshtask.Models.MangaData
import com.task.pupilsmeshtask.NetworkUtils.NetworkConnectionLiveData
import com.task.pupilsmeshtask.Repo.Repo
import com.task.pupilsmeshtask.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.ArrayList

class DataViewModel : ViewModel() {

    val allMutableLiveData : LiveData<ArrayList<MangaData.Data>> = Repo.allData
    var page = 0;
    var isFetching:Boolean = false
        get() = field
        set(value) {

            field = value
        }


    //val searchMutableData : LiveData<HashMap<String,ArrayList<Books>>> = Repo.searchMutableData



     fun getAllData(type:Type) {

         try {

             if (type == Type.SERVER) {

                 page+=1
                 isFetching = true

                 viewModelScope.launch(Dispatchers.IO) {

                     Repo.getAllData(page)
                     isFetching = false


                 }


             } else {

                 viewModelScope.launch(Dispatchers.IO) {

                     Repo.getAllLocalData()
                     isFetching = false


                 }
             }

         } catch (ex:Exception) {


         }

    }

//    fun getAllSearch(s:String) {
//
//        viewModelScope.launch(Dispatchers.IO) {
//
//            Repo.filterMap.clear()
//            Repo.searchBook(s)
//
//        }
//
//    }


}