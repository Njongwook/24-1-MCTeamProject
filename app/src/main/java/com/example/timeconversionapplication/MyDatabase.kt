package com.example.timeconversionapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MyDatabase {
    object MyDBContract {
        object WorkPlace : BaseColumns{
            const val TABLE_NAME = "workplace"
            const val place_name = "place_name"
            const val salary_style = "salary_style"
            const val salary_day = "salary_day"
            const val tax = "tax"
            const val insurance = "insurance"
        }
        object WorkTime : BaseColumns{
            const val TABLE_NAME = "worktime"
            const val date = "date"
            const val work_time = "work_time"
            const val break_time = "break_time"
        }
        object Product : BaseColumns{
            const val TABLE_NAME = "product"
            const val product_name = "product_name"
            const val price = "price"
            const val memo = "memo"
        }
        object Dday : BaseColumns{
            const val TABLE_NAME = "Dday"
            const val Dtime = "Dtime"
            const val Dday = "Dday"

        }
    }
    class MyDBHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
        val SQL_CREATE_WORK_PLACE_ENTRIES =
            "CREATE TABLE ${MyDBContract.WorkPlace.TABLE_NAME}(" +
                    "${MyDBContract.WorkPlace.place_name} TEXT PRIMARY KEY," +
                    "${MyDBContract.WorkPlace.salary_style} INTEGER," +
                    "${MyDBContract.WorkPlace.salary_day} INTEGER," +
                    "${MyDBContract.WorkPlace.tax} INTEGER," +
                    "${MyDBContract.WorkPlace.insurance} INTEGER);"
        val SQL_CREATE_WORK_TIME_ENTRIES =
            "CREATE TABLE ${MyDBContract.WorkTime.TABLE_NAME}(" +
                    "${MyDBContract.WorkTime.date} DATE PRIMARY KEY," +
                    "${MyDBContract.WorkTime.work_time} TIME," +
                    "${MyDBContract.WorkTime.break_time} INTEGER);"
        val SQL_CREATE_PRODUCT_ENTRIES =
            "CREATE TABLE ${MyDBContract.Product.TABLE_NAME}(" +
                    "${MyDBContract.Product.product_name} TEXT PRIMARY KEY," +
                    "${MyDBContract.Product.price} INTEGER," +
                    "${MyDBContract.Product.memo} TEXT);"
        val SQL_CREATE_DDAY_ENTRIES =
            "CREATE TABLE ${MyDBContract.Dday.TABLE_NAME}(" +
                    "${MyDBContract.Dday.Dtime} INTEGER," +
                    "${MyDBContract.Dday.Dday} INTEGER);"

        val SQL_DELETE_WORK_PLACE_ENTRIES =
            "DROP TABLE IF EXISTS ${MyDBContract.WorkPlace.TABLE_NAME}"
        val SQL_DELETE_WORK_TIME_ENTRIES =
            "DROP TABLE IF EXISTS ${MyDBContract.WorkTime.TABLE_NAME}; "
        val SQL_DELETE_PRODUCT_ENTRIES =
            "DROP TABLE IF EXISTS ${MyDBContract.Product.TABLE_NAME}; "
        val SQL_DELETE_DDAY_ENTRIES =
            "DROP TABLE IF EXISTS ${MyDBContract.Dday.TABLE_NAME}"
        override fun onCreate(db: SQLiteDatabase){
            db.execSQL(SQL_CREATE_WORK_PLACE_ENTRIES)
            db.execSQL(SQL_CREATE_WORK_TIME_ENTRIES)
            db.execSQL(SQL_CREATE_PRODUCT_ENTRIES)
            db.execSQL(SQL_CREATE_DDAY_ENTRIES)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
            db.execSQL(SQL_DELETE_WORK_PLACE_ENTRIES)
            db.execSQL(SQL_DELETE_WORK_TIME_ENTRIES)
            db.execSQL(SQL_DELETE_PRODUCT_ENTRIES)
            db.execSQL(SQL_DELETE_DDAY_ENTRIES)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
            onUpgrade(db, oldVersion, newVersion)
        }
        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "myDBfile.db"
        }
    }
}