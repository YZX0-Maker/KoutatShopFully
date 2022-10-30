package com.shopfully.koutatshopfully

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.shopfully.koutatshopfully.databinding.ActivityMainBinding
import com.treetanium.koutatshopfullytest.MySingleton
import java.text.FieldPosition

class MainActivity() : AppCompatActivity(), ItemClicked {

    private var dataList: MutableList<Product> = mutableListOf()

    private lateinit var adapterX: ListAdapterProduct
    private lateinit var binding: ActivityMainBinding
    private lateinit var sqlite: SQLiteGestor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* INIT CONSUME API */
        sqlite = SQLiteGestor(this)
        chargeData()
        val allProduct = (sqlite.getAllProduct())

        /* INIT OF TOOLBAR */
        val mToolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Koutat ShopFully Test"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        /* INIT OF RECYCLERVIEW */
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapterX = ListAdapterProduct(allProduct) {
            onItemClicked(it)
        }
        binding.recyclerView.adapter = adapterX

        adapterX.notifyDataSetChanged()

        /* SWITCH FILTER */
        val swichs = findViewById<Switch>(R.id.switch1)
        swichs.setOnCheckedChangeListener { _, onSwitch ->
            if (onSwitch) {
                val allProducts = (sqlite.getAllProduct())
                val newData: List<Product> = allProducts.filter { it.read == 1 }
                adapterX.updateActivity(newData)
            } else {
                val allProducts = (sqlite.getAllProduct())
                adapterX.updateActivity(allProducts)
            }
        }
    }

    private fun chargeData() {
        /* CONSUME THE API */
        val url = "https://run.mocky.io/v3/94da1ce3-3d3f-414c-8857-da813df3bb05"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val arrayJson = it.getJSONArray("data")
                for (i in 0 until arrayJson.length()) {
                    val jsonObject = arrayJson.getJSONObject(i)
                    val products = Product(
                        jsonObject.getString("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("retailer_id"),
                        0
                    )
                    sqlite.addProduct(products)
                    //println(products)
                }

            },
            { error ->
                println(error)
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onItemClicked(item: Product) {
        println("clicked this $item")
        val intent = Intent(this, showProduct::class.java)
        intent.putExtra("title", item.title)
        intent.putExtra("id", item.id)

        var position = dataList.binarySearch(
            Product(item.title, item.id, item.retailer_id, 0),
            compareBy<Product> { it.id }.thenBy { it.retailer_id })

        /*StreamFully LOGS*/
        var mMap: Map<String, Any> = mapOf()
        mMap = mapOf("retailer_id" to item.retailer_id)
        mMap = mapOf("flyer_id" to item.id)
        mMap = mapOf("title" to item.title)
        mMap = mapOf("position" to position)
        mMap = mapOf("first_read" to true)

        StreamFully(packageName, "2").process(FullyEvent("flyer_open", mMap))

        sqlite.updateProduct(item.id)

        val long = System.currentTimeMillis()
        val idI:Int = item.id.toInt()
        intent.putExtra("initSession", long)
        intent.putExtra("read", item.read)
        intent.putExtra("idInt", idI)

        val allProduct = (sqlite.getAllProduct())
        adapterX.updateActivity(allProduct)
        startActivity(intent)
    }

}

