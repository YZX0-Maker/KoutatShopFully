package com.shopfully.koutatshopfully

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class showProduct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)


        val showTitle: TextView = findViewById(R.id.textView)
        val showImg: ImageView = findViewById(R.id.imageView2)
        val showRead: TextView = findViewById(R.id.textView2)

        val bundle: Bundle? = intent.extras
        val title = bundle!!.getString("title")
        val id = bundle.getString("id")
        val read = bundle.getBoolean("read")

        showTitle.text = title
        Glide.with(showImg.context)
            .load("https://it-it-media.shopfully.cloud/images/volantini/${id}@3x.jpg").into(showImg)
        showRead.text = read.toString()
    }

    override fun onBackPressed() {

        /*StreamFully Close*/

        val bundle: Bundle? = intent.extras
        val id = bundle!!.getInt("idInt")
        val initDuration = bundle.getLong("initSession")
        val long = System.currentTimeMillis()
        val finalDuration = (long - initDuration)

        var mMap: Map<String, Any> = mapOf()
        mMap = mapOf("flyer_id" to id)
        mMap = mapOf("session_duration" to finalDuration)
        mMap = mapOf("first_read" to true)

        StreamFully(packageName, "2").process(FullyEvent("flyer_session", mMap))

        super.onBackPressed()
    }
}