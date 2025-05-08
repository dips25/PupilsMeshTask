package com.task.pupilsmeshtask.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.task.pupilsmeshtask.R

class DescriptionActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_description)

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(v.left, 0, v.right, systemBars.bottom)
            insets
        }

        var intent:Intent = getIntent()

        var coverImage:ImageView = findViewById(R.id.img_thumb)

        var title:TextView = findViewById(R.id.tv_title)
        var subTitle:TextView = findViewById(R.id.tv_subtitle)
        var summary:TextView = findViewById(R.id.tv_summary)

        title.setText(intent.getStringExtra("title"))
        subTitle.setText(intent.getStringExtra("subtitle"))
        summary.setText(intent.getStringExtra("summary"))

        Glide.with(this@DescriptionActivity)
            .load(intent.getStringExtra("image"))
            .placeholder(R.drawable.no_image)
            .into(coverImage)


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}