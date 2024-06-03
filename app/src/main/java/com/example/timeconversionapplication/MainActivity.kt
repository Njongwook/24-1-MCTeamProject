package com.example.timeconversionapplication

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.timeconversionapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val dbHelper = MyDatabase.MyDBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commitNow()
        }
//
//        var db = dbHelper.writableDatabase
//        Log.d("TAG", "START")
//        val productArr = mutableListOf(
//            Product("아이폰",1500000, "도전! 사과농장주")
//        )
//        val workplaceArr = mutableListOf(
//            WorkPlace("버거킹",3,15,10000,1,1)
//        )
//        for (entry in productArr){
//            val myentry = MyDatabase.MyDBContract.Product
//            val products = ContentValues().apply{
//                put(myentry.product_name, entry.product_name)
//                put(myentry.price, entry.price)
//                put(myentry.memo, entry.memo)
//            }
//            Log.d("TAG", "Products:$products")
//
//            try{
//                val newRowId=db?.insertOrThrow(myentry.TABLE_NAME, null, products)
//                Log.d("TAG", newRowId.toString())
//            }
//            catch(e: SQLiteConstraintException) {
//                db?.update(myentry.TABLE_NAME, products, "${myentry.product_name} LIKE ?", arrayOf(entry.product_name))
//            }
//        }
    }
}