package com.example.timeconversionapplication

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding
import java.time.LocalDateTime
import java.util.Calendar

class JobRegisDetailActivity : AppCompatActivity() {
    private val dbHelper = MyDatabase.MyDBHelper(this)
    private lateinit var binding: PartTimeJobRegisDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = PartTimeJobRegisDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        // WorkTime 타입의 데이터를 조회하여 MutableList<Any>로 변환
        val getList: MutableList<Any> = dbHelper.selectAll("worktime", WorkTime::class.java).toMutableList()

        // 가져온 데이터를 로그로 출력합니다.
        for (e in getList) {
            Log.d("TAG", e.toString())
        }

        // 어댑터에 데이터를 설정합니다.
        val adapter = MyAdapter(getList)


        // 시간
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        var workTimeInMillis = ""
        binding.startTime.setOnTimeChangedListener { timePicker, hourOfDay, minute ->
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startTime.set(Calendar.MINUTE, minute)
        }

        binding.endTime.setOnTimeChangedListener{ timePicker, hourOfDay, minute ->
            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endTime.set(Calendar.MINUTE, minute)

            // endTime이 startTime보다 이후인지 체크하는 부분
            if (endTime.timeInMillis < startTime.timeInMillis) {
                // 예외 처리 혹은 기본값 설정
                // 여기서는 endTime을 startTime과 동일하게 설정
                endTime.timeInMillis = startTime.timeInMillis
            }

            // 작업 시간 계산 (milliseconds 단위)
            workTimeInMillis = (endTime.timeInMillis - startTime.timeInMillis).toString()

            // 작업 시간을 시간 단위로 변환
//            val hours = workTimeInMillis / (1000 * 60 * 60) // milliseconds -> hours
//            val minutes = (workTimeInMillis % (1000 * 60 * 60)) / (1000 * 60) // milliseconds -> minutes
//
//            // 작업 시간 출력 예시
//            Log.d("WorkTime", "Work time: $hours hours $minutes minutes")
        }

        var breakTime = 0
        var hourly = 0

        binding.save.setOnClickListener {
            // val date = binding.date.text.toString()
            val hourlyText = binding.salary.text.toString()
            val breakTimeText = binding.breakTime.text.toString()
            try {
                breakTime = breakTimeText.toInt()

            } catch (e: NumberFormatException) {
                Log.d("TAG", "Break time must be a number  Provided: '$breakTimeText'")
                return@setOnClickListener
            }
            try {
                hourly = breakTimeText.toInt()

            } catch (e: NumberFormatException) {
                Log.d("TAG", "hourly must be a number  Provided: '$hourlyText'")
                return@setOnClickListener
            }

            val date = "date"
            //date 입력 부분 구현 해야 함 > 직접 입력하게 할지, 캘린더 같은 부분에서 선택하게 할지....

            val newElement = WorkTime(date, workTimeInMillis, breakTime.toString(), "placename", hourly)

            // 데이터베이스에 새로운 WorkTime 객체 삽입
            dbHelper.use {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("date", date)
                    put("work_time", workTimeInMillis)
                    put("break_time", breakTime.toString())
                    put("place_name", place_name)
                    put("hourly", hourly)
                }
                db.insert("worktime", null, values)
            }

            adapter.addItem(newElement)

            Log.d("TAG", newElement.toString())


            // 홈 화면으로 돌아가기
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}