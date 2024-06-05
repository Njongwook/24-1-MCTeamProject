package com.example.timeconversionapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.timeconversionapplication.databinding.PartTimeJobRegisBinding
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding

class JobRegisActivity : AppCompatActivity() {
    private val dbHelper = MyDatabase.MyDBHelper(this)
    private lateinit var binding: PartTimeJobRegisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PartTimeJobRegisBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)


        // WorkPlace 타입의 데이터를 조회하여 MutableList<Any>로 변환
        val getList: MutableList<Any> = dbHelper.selectAll("workplace", WorkPlace::class.java).toMutableList()

        // 가져온 데이터를 로그로 출력합니다.
        for (e in getList) {
            Log.d("TAG", e.toString())
        }

        // 어댑터에 데이터를 설정합니다.
        val adapter = MyAdapter(getList)


        // 버튼 누르는 것에 따라 일급, 주급, 월급 구분
        var salaryStyle = 0
        binding.daySalary.setOnClickListener{
            salaryStyle = 1
            Log.d("TAG", salaryStyle.toString())
        }
        binding.weekSalary.setOnClickListener{
            salaryStyle = 2
            Log.d("TAG", salaryStyle.toString())
        }
        binding.monthSalary.setOnClickListener{
            salaryStyle = 3
            Log.d("TAG", salaryStyle.toString())
        }

        //다음 버튼 누르면 해당 화면에 있는 근무지 정보 db에 저장하기
        binding.next.setOnClickListener {
            val placeName = binding.partTimeJobName.text.toString()
            val salaryDayText = binding.days.text.toString()

            if (placeName.isBlank() || salaryStyle == 0){
                Log.d("TAG", "All fields must be filled out")
                Log.d("TAG", placeName)
                Log.d("TAG", salaryStyle.toString())
                return@setOnClickListener
            }
            val salaryDay: Int
            try {
                salaryDay = salaryDayText.toInt()

            } catch (e: NumberFormatException) {
                Log.d("TAG", "Salary day must be a number  Provided: '$salaryDayText'")
                return@setOnClickListener
            }

            val newElement = WorkPlace(placeName, salaryStyle, salaryDay, 0)

            // 데이터베이스에 새로운 WorkPlace 객체 삽입
            dbHelper.use {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("place_name", placeName)
                    put("salary_style", salaryStyle)
                    put("salary_day", salaryDay)
                    put("tax", 0)
                }
                db.insert("workplace", null, values)
            }

            adapter.addItem(newElement)

            Log.d("TAG", newElement.toString())

            // 다음 화면으로 넘어가기
            val intent = Intent(this, JobRegisDetailActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //키보드 내리기
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}