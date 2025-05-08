package com.task.pupilsmeshtask.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.task.pupilsmeshtask.Models.MangaData
import com.task.pupilsmeshtask.PupilsMeshTask
import com.task.pupilsmeshtask.R
import com.task.pupilsmeshtask.Repo.Repo
import com.task.pupilsmeshtask.Type
import com.task.pupilsmeshtask.ViewModel.DataViewModel
import com.task.pupilsmeshtask.activities.DescriptionActivity
import com.task.pupilsmeshtask.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class AllDataFragment:Fragment() {

    private var allItems:ArrayList<MangaData.Data> = ArrayList()

    private lateinit var mainRecycler:RecyclerView

    private lateinit var dataViewModel: DataViewModel

    private lateinit var allItemsAdapter: AllItemsAdapter

    private var isFetching = false

    private var isOffline = false

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var bottomNav:LinearLayout

    private val TAG = AllDataFragment::class.java.name

    private var type:Type = Type.SERVER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_all_items,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val toolbar: androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"



        var loadingProgress = requireActivity().findViewById<ProgressBar>(R.id.loading_progress)
        loadingProgress.visibility = View.GONE

        Repo.buildDB(requireActivity())

        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)


        mainRecycler = view.findViewById(R.id.main_recycler)
        mainRecycler.layoutManager = LinearLayoutManager(requireActivity())

        allItemsAdapter = AllItemsAdapter(requireActivity())

        mainRecycler.adapter = allItemsAdapter

        (requireActivity().applicationContext as PupilsMeshTask).networkStatus!!.observe(requireActivity()
            , object : Observer<Boolean>{
                override fun onChanged(value: Boolean) {

                    if (!value) {

                        if (!isOffline) {

                            type = Type.LOCAL

                            dataViewModel.page = 0
                            dataViewModel.isFetching = false
                            isOffline = true

                            Snackbar.make(requireActivity().findViewById(android.R.id.content)
                                ,"No Internet"
                                , Snackbar.LENGTH_INDEFINITE)
                                .setAction("Fetch Local",{

                                    allItems.clear()
                                    allItemsAdapter.notifyDataSetChanged()
                                    mainRecycler.scrollToPosition(0)

                                    loadingProgress.visibility = View.VISIBLE

                                    dataViewModel.getAllData(Type.LOCAL)


                                })
                                .show()



                        }




                    } else {

                        if (isOffline) {

                            type = Type.SERVER

                            Snackbar.make(requireActivity().findViewById(android.R.id.content)
                                ,"Connected"
                                , Snackbar.LENGTH_INDEFINITE).setAction("Refresh",{

                                allItems.clear()
                                allItemsAdapter.notifyDataSetChanged()
                                mainRecycler.scrollToPosition(0)

                                loadingProgress.visibility = View.VISIBLE

                                dataViewModel.getAllData(Type.SERVER)

                                isOffline = false


                            }).show()


                        } else {

                            type = Type.SERVER

                            dataViewModel.getAllData(Type.SERVER)
                        }




                    }

                }


            })

        mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy>0 && !dataViewModel.isFetching
                    && (requireActivity().applicationContext as PupilsMeshTask).networkStatus!!.value!!
                    && type == Type.SERVER) {

                    val layoutManager:LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

                    val totalItems = layoutManager.itemCount
                    val visibleItems = layoutManager.childCount

                    val lastItemVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    if (lastItemVisiblePosition+1 == totalItems) {

                        loadingProgress.visibility = View.VISIBLE

                        dataViewModel.getAllData(Type.SERVER)

                    }


                } else {

                    Snackbar.make(requireActivity().findViewById(android.R.id.content)
                        ,"No Internet"
                        , Snackbar.LENGTH_SHORT)


                }


            }


        })

        dataViewModel.allMutableLiveData.observe(requireActivity(), object :
            Observer<ArrayList<MangaData.Data>> {

            override fun onChanged(value: ArrayList<MangaData.Data>) {

                try {

                    requireActivity().runOnUiThread {

                        value.forEach {


                            if (!allItems.contains(it)) {



                                allItems.add(it)
                                allItemsAdapter.notifyItemInserted(allItems.size-1)


                            }
                        }

                        loadingProgress.visibility = View.GONE



                    }

                } catch (ex:Exception) {


                }













//                        try {
//
//                            value.forEach{it->
//
//                                if (allItems.contains(it)) {
//
//                                    if (it._isDelete) {
//
//                                        var index = allItems.indexOf(it)
//                                        allItems.removeAt(index)
//                                        allItemsAdapter.notifyItemRemoved(index)
//
//
//                                    } else {
//
//                                        var index = allItems.indexOf(it)
//                                        allItems.set(index,it)
//                                        allItemsAdapter.notifyItemChanged(index)
//
//                                    }
//
//
//
//
//                                } else {
//
//                                    allItems.add(it)
//                                    allItemsAdapter.notifyItemInserted(allItems.size-1)
//
//                                }
//
//                            }
//
//                        } catch (ex:Exception) {
//
//                            requireActivity().supportFragmentManager
//                                .beginTransaction()
//                                .remove(this@AllDataFragment)
//                                .commit()
//
//                            Toast.makeText(requireActivity(),"Error fetching data", Toast.LENGTH_SHORT).show()
//
//
//
//
//                        }




            }




        })

//        Handler().postDelayed(object :Runnable{
//            override fun run() {
//
//                loadingProgress.visibility = View.GONE
//
//
//
//            }
//
//
//        },3000)





    }

    private inner class AllItemsAdapter(var c: Context) : RecyclerView.Adapter<AllItemsAdapter.AllItemsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemsViewHolder {

            return AllItemsViewHolder(LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.single_item_book
                    ,parent
                    ,false))

        }

        override fun getItemCount(): Int {

            return allItems.size


        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun onBindViewHolder(holder: AllItemsViewHolder, position: Int) {

            var a : MangaData.Data = allItems.get(position)

            holder.name.setText(a.title)
            holder.others.setText(a.subTitle)

            a.thumb?.let {

                Glide.with(requireActivity())
                    .load(it)
                    .placeholder(R.drawable.no_image)
                    .into(holder.image)
            }



            val sb:StringBuilder = StringBuilder()



            holder.itemView.setOnClickListener {

                var intent:Intent = Intent(requireActivity(),DescriptionActivity::class.java)
                intent.putExtra("title",a.title ?: "")
                intent.putExtra("subtitle",a.subTitle?:"")
                intent.putExtra("summary",a.summary?:"")
                intent.putExtra("image",a.thumb?:"")

                requireActivity().startActivity(intent)

            }

        }

        inner class AllItemsViewHolder(val v :View) : RecyclerView.ViewHolder(v) {

            val name: TextView = v.findViewById(R.id.name)
            var others: TextView=v.findViewById(R.id.others)
            var image:ImageView = v.findViewById(R.id.image)

        }


    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).selectIcon(Type.HOME)
    }

}