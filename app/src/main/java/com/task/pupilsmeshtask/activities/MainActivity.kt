package com.task.pupilsmeshtask.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.task.pupilsmeshtask.Fragments.AllDataFragment
import com.task.pupilsmeshtask.PupilsMeshTask
import com.task.pupilsmeshtask.R
import com.task.pupilsmeshtask.Type

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var bottomNav:LinearLayout
    private lateinit var homeImg:ImageView
    private lateinit var cameraImg:ImageView
    private lateinit var homeTxt:TextView
    private lateinit var cameraTxt:TextView
    private lateinit var colorList:ColorStateList
    private lateinit var homeHolder:LinearLayout
    private lateinit var cameraHolder:LinearLayout
    private var actionBar:ActionBar?=null
        get() = field
        set(value) {

            field = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeHolder = findViewById(R.id.home_holder)
        cameraHolder = findViewById(R.id.camera_holder)




        colorList = ContextCompat.getColorStateList(this@MainActivity,R.color.icon_color_selector)


        toolbar = findViewById(R.id.toolbar)
        bottomNav = findViewById(R.id.bottom_nav)

        setSupportActionBar(toolbar)

        actionBar = supportActionBar!!

        homeImg = findViewById(R.id.img_home)
        cameraImg = findViewById(R.id.img_camera)

        homeTxt = findViewById(R.id.txt_home)
        cameraTxt = findViewById(R.id.txt_camera)

        configureBottomNavIcons()

        homeHolder.setOnClickListener {

            selectIcon(Type.HOME)

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame,AllDataFragment())
                .addToBackStack(null)
                .commit()

        }

        cameraHolder.setOnClickListener {

            if (requestCameraPerms()) {

                selectIcon(Type.CAMERA)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame,CameraActivity())
                    .addToBackStack(null)
                    .commit()


            }

        }


        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame,AllDataFragment())
            .addToBackStack(null)
            .commit()

        selectIcon(Type.HOME)










//        val openCamera = findViewById<Button>(R.id.open_camera)
//        openCamera.setOnClickListener {
//
//            var intent = Intent(this@MainActivity,CameraActivity::class.java)
//            startActivity(intent)
//        }

    }

    private fun configureBottomNavIcons() {

        homeImg.imageTintList = colorList
        cameraImg.imageTintList = colorList
    }

    public fun selectIcon(type:Type) {

        disableAllIcons()

        if (type == Type.HOME) {

            homeImg.isSelected = true
            homeTxt.isSelected = true
            homeImg.invalidate()

        } else if (type == Type.CAMERA) {

            cameraImg.isSelected = true
            cameraTxt.isSelected = true
            cameraImg.invalidate()

        }


    }

    private fun disableAllIcons() {

        homeImg.isSelected = false
        cameraImg.isSelected = false

        homeImg.invalidate()
        cameraImg.invalidate()

        homeTxt.isSelected = false
        cameraTxt.isSelected = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {

        if (requestCode == 200) {

            if (grantResults.size>0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectIcon(Type.CAMERA)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame,CameraActivity())
                        .addToBackStack(null)
                        .commit()


                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }

    public fun requestCameraPerms():Boolean {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.CAMERA),200)

            return false

        }

        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


}