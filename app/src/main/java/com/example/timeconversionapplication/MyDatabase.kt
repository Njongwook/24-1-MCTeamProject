package com.example.timeconversionapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class MyDatabase {
    object MyDBContract {
        object WorkPlace : BaseColumns{
            const val TABLE_NAME = "workplace"
            const val place_name = "place_name"
            const val salary_style = "salary_style"
            const val salary_day = "salary_day"
            const val tax = "tax"
        }
        object WorkTime : BaseColumns{
            const val TABLE_NAME = "worktime"
            const val date = "date"
            const val work_time = "work_time"
            const val break_time = "break_time"
            const val place_name = "place_name"
            const val hourly = "hourly"
            const val wage = "wage"
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
            const val product_name = "product_name"
            const val product_memo = "product_memo"
            const val product_price = "product_price"
            const val place_name = "place_name"
            const val hourly = "hourly"
        }
    }
    class MyDBHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
        val SQL_CREATE_WORK_PLACE_ENTRIES =
            "CREATE TABLE ${MyDBContract.WorkPlace.TABLE_NAME}(" +
                    "${MyDBContract.WorkPlace.place_name} TEXT PRIMARY KEY," +
                    "${MyDBContract.WorkPlace.salary_style} INTEGER," +
                    "${MyDBContract.WorkPlace.salary_day} INTEGER," +
                    "${MyDBContract.WorkPlace.tax} INTEGER);"
        val SQL_CREATE_WORK_TIME_ENTRIES =
            "CREATE TABLE ${MyDBContract.WorkTime.TABLE_NAME}(" +
                    "${MyDBContract.WorkTime.date} TEXT PRIMARY KEY," +
                    "${MyDBContract.WorkTime.work_time} TEXT," +
                    "${MyDBContract.WorkTime.break_time} TEXT," +
                    "${MyDBContract.WorkTime.place_name} TEXT," +
                    "${MyDBContract.WorkTime.hourly} INTEGER," +
                    "${MyDBContract.WorkTime.wage} INTEGER," +
                    "CONSTRAINT PLACE_NAME_TIME_FK FOREIGN KEY (${MyDBContract.WorkTime.place_name})" +
                    "REFERENCES ${MyDBContract.WorkPlace.TABLE_NAME} (${MyDBContract.WorkPlace.place_name})" +
                    "ON DELETE SET NULL);"
        val SQL_CREATE_PRODUCT_ENTRIES =
            "CREATE TABLE ${MyDBContract.Product.TABLE_NAME}(" +
                    "${MyDBContract.Product.product_name} TEXT PRIMARY KEY," +
                    "${MyDBContract.Product.price} INTEGER," +
                    "${MyDBContract.Product.memo} TEXT);"
        val SQL_CREATE_DDAY_ENTRIES =
            "CREATE TABLE ${MyDBContract.Dday.TABLE_NAME}(" +
                    "${MyDBContract.Dday.Dtime} INTEGER," +
                    "${MyDBContract.Dday.Dday} INTEGER," +
                    "${MyDBContract.Dday.product_name} TEXT," +
                    "${MyDBContract.Dday.product_price} INTEGER," +
                    "${MyDBContract.Dday.product_memo} TEXT," +
                    "${MyDBContract.Dday.place_name} TEXT," +
                    "${MyDBContract.Dday.hourly} INTEGER," +
                    "PRIMARY KEY (${MyDBContract.Dday.product_name}, ${MyDBContract.Dday.Dday})," +
                    "CONSTRAINT PRODUCT_NAME_DDAY_FK FOREIGN KEY (${MyDBContract.Dday.product_name})" +
                    "REFERENCES ${MyDBContract.Product.TABLE_NAME} (${MyDBContract.Product.product_name})" +
                    "ON DELETE SET NULL," +
                    "CONSTRAINT PRICE_DDAY_FK FOREIGN KEY (${MyDBContract.Dday.product_price})" +
                    "REFERENCES ${MyDBContract.Product.TABLE_NAME} (${MyDBContract.Product.price})" +
                    "ON DELETE SET NULL," +
                    "CONSTRAINT MEMO_DDAY_FK FOREIGN KEY (${MyDBContract.Dday.product_memo})" +
                    "REFERENCES ${MyDBContract.Product.TABLE_NAME} (${MyDBContract.Product.memo})" +
                    "ON DELETE SET NULL," +
                    "CONSTRAINT PLACE_NAME_DDAY_FK FOREIGN KEY (${MyDBContract.Dday.place_name})" +
                    "REFERENCES ${MyDBContract.WorkPlace.TABLE_NAME} (${MyDBContract.WorkPlace.place_name})" +
                    "ON DELETE SET NULL," +
                    "CONSTRAINT HOURLY_DDAY_FK FOREIGN KEY (${MyDBContract.Dday.hourly})" +
                    "REFERENCES ${MyDBContract.WorkTime.TABLE_NAME} (${MyDBContract.WorkTime.hourly})" +
                    "ON DELETE SET NULL);"

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

        fun <T> selectAll(tableName: String, clazz: Class<T>): MutableList<T> {
            val readList = mutableListOf<T>()
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $tableName;", null)
            Log.d("TAG", "Select All Query: SELECT * FROM $tableName;")
            Log.d("TAG", cursor.toString())
            with(cursor) {
                while (moveToNext()) {
                    when (clazz) {
                        WorkPlace::class.java -> {
                            readList.add(clazz.getConstructor(
                                String::class.java, Int::class.java, Int::class.java, Int::class.java
                            ).newInstance(
                                cursor.getString(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getInt(3),
                            ) as T)
                        }
                        WorkTime::class.java -> {
                            readList.add(clazz.getConstructor(
                                String::class.java, String::class.java, String::class.java, String::class.java, Int::class.java, Int::class.java
                            ).newInstance(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getInt(4),
                                cursor.getInt(5)
                            ) as T)
                        }
                        Product::class.java -> {
                            readList.add(clazz.getConstructor(
                                String::class.java, Int::class.java, String::class.java
                            ).newInstance(
                                cursor.getString(0),
                                cursor.getInt(1),
                                cursor.getString(2)
                            ) as T)
                        }
                        Dday::class.java -> {
                            readList.add(clazz.getConstructor(
                                Int::class.java, Int::class.java, String::class.java, Int::class.java, String::class.java, String::class.java, Int::class.java
                            ).newInstance(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getString(2),
                                cursor.getInt(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getInt(6)
                            ) as T)
                        }
                        else -> throw IllegalArgumentException("Unknown class type")
                    }
                }
            }
            cursor.close()
            db.close()
            return readList
        }

        fun selectByDate(date: String): WorkTime? {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${MyDBContract.WorkTime.TABLE_NAME} WHERE ${MyDBContract.WorkTime.date} = ?", arrayOf(date))
            var workTime: WorkTime? = null
            with(cursor) {
                if (moveToFirst()) {
                    workTime = WorkTime(
                        getString(getColumnIndexOrThrow(MyDBContract.WorkTime.date)),
                        getString(getColumnIndexOrThrow(MyDBContract.WorkTime.work_time)),
                        getString(getColumnIndexOrThrow(MyDBContract.WorkTime.break_time)),
                        getString(getColumnIndexOrThrow(MyDBContract.WorkTime.place_name)),
                        getInt(getColumnIndexOrThrow(MyDBContract.WorkTime.hourly)),
                        getInt(getColumnIndexOrThrow(MyDBContract.WorkTime.wage))
                    )
                }
            }
            cursor.close()
            db.close()
            return workTime
        }
    }
}