package com.shopfully.koutatshopfully

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class SQLiteGestor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "product.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, ide TEXT, title TEXT, retailer_id TEXT, read BOOLEAN)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    fun addProduct(dataProduct: Product): Long {

        val data = getProduct(dataProduct.id)
        if (data.size === 0) {
            val productValue = ContentValues()
            productValue.put("ide", dataProduct.id)
            productValue.put("title", dataProduct.title)
            productValue.put("retailer_id", dataProduct.retailer_id)
            productValue.put("read", 0)


            val db = this.writableDatabase
            val success = db.insert("products", null, productValue)
            db.close()
            return success
        }
        return 0
    }

        @SuppressLint("Range")
        fun getAllProduct(): ArrayList<Product> {
            val productList: ArrayList<Product> = ArrayList()
            val selectQuery = "SELECT * FROM products"
            val db = this.readableDatabase

            val cursor: Cursor?

            try {
                cursor = db.rawQuery(selectQuery, null)

            } catch (e: Exception) {
                e.printStackTrace()
                db.execSQL(selectQuery)
                return ArrayList()
            }

            var ide: String
            var title: String
            var retailer: String
            var read: Int

            if (cursor.moveToFirst()) {
                do {
                    ide = cursor.getString(cursor.getColumnIndex("ide"))
                    title = cursor.getString(cursor.getColumnIndex("title"))
                    retailer = cursor.getString(cursor.getColumnIndex("retailer_id"))
                    read = cursor.getInt(cursor.getColumnIndex("read"))
                    val product =
                        Product(id = ide, title = title, retailer_id = retailer, read = read)
                    productList.add(product)
                } while (cursor.moveToNext())
            }

            return productList
        }

        @SuppressLint("Range")
        fun getProduct(iden: String): ArrayList<Product> {
            val productList: ArrayList<Product> = ArrayList()
            val selectQuery = "SELECT * FROM products WHERE ide=$iden"
            val db = this.readableDatabase

            val cursor: Cursor?

            try {
                cursor = db.rawQuery(selectQuery, null)

            } catch (e: Exception) {
                e.printStackTrace()
                db.execSQL(selectQuery)
                return ArrayList()
            }

            var ide: String
            var title: String
            var retailer: String


            if (cursor.moveToFirst()) {
                do {

                    ide = cursor.getString(cursor.getColumnIndex("ide"))
                    title = cursor.getString(cursor.getColumnIndex("title"))
                    retailer = cursor.getString(cursor.getColumnIndex("retailer_id"))

                    val product =
                        Product(id = ide, title = title, retailer_id = retailer, read = 0)
                    productList.add(product)
                } while (cursor.moveToNext())
            }

            return productList
        }

        fun updateProduct(value : String) : Unit {
            val db = this.readableDatabase
            val result = db!!.execSQL("UPDATE products SET read=1 WHERE ide=$value")
            // db.update("products", productValue, "ide=$value", null )
            db.close()
            return result
        }
    }