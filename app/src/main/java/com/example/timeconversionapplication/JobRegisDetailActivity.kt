package com.example.timeconversionapplication

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.time.times

class JobRegisDetailActivity : AppCompatActivity() {
    private val dbHelper = MyDatabase.MyDBHelper(this)
    private lateinit var binding: PartTimeJobRegisDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // 신유빈 코드 시작--------------------------------------------------------------------------------------------
        binding = PartTimeJobRegisDetailsBinding.inflate(layoutInflater)
        // super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        // WorkTime 타입의 데이터를 조회하여 MutableList<Any>로 변환 - db 연결에 필요
        val getList: MutableList<Any> = dbHelper.selectAll("worktime", WorkTime::class.java).toMutableList()

        // 가져온 데이터를 로그로 출력합니다. - 없어도 됨. 디버깅 용
        for (e in getList) {
            Log.d("TAG", e.toString())
        }

        // 어댑터에 데이터를 설정합니다. - db 연결에 필요
        val adapter = MyAdapter(getList)


        // 주석처리 한 부분 -> 시간 계산 코드 - 시작 시간, 종료 시간 받아와서 (종료시간 - 시작 시간)으로 일한 시간 구하기, string 값으로 변경해서 넣기
        // 구현 하신 것 같아서 통채로 지워도 상관 없을 것 같습니다.

//        val startTime = Calendar.getInstance()
//        val endTime = Calendar.getInstance()
//        var workTimeInMillis = ""
//        binding.startTime.setOnTimeChangedListener { timePicker, hourOfDay, minute ->
//            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
//            startTime.set(Calendar.MINUTE, minute)
//        }
//
//        binding.endTime.setOnTimeChangedListener{ timePicker, hourOfDay, minute ->
//            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
//            endTime.set(Calendar.MINUTE, minute)
//
//            // endTime이 startTime보다 이후인지 체크하는 부분
//            if (endTime.timeInMillis < startTime.timeInMillis) {
//                // 예외 처리 혹은 기본값 설정
//                // 여기서는 endTime을 startTime과 동일하게 설정
//                endTime.timeInMillis = startTime.timeInMillis
//            }
//
//            // 작업 시간 계산 (milliseconds 단위)
//            workTimeInMillis = (endTime.timeInMillis - startTime.timeInMillis).toString()
//
//            // 작업 시간을 시간 단위로 변환
////            val hours = workTimeInMillis / (1000 * 60 * 60) // milliseconds -> hours
////            val minutes = (workTimeInMillis % (1000 * 60 * 60)) / (1000 * 60) // milliseconds -> minutes
////
////            // 작업 시간 출력 예시
////            Log.d("WorkTime", "Work time: $hours hours $minutes minutes")
//        }

        var breakTime = 0
        var hourly = 0

        // 저장 버튼 클릭시
        binding.save.setOnClickListener {
            // input 창에서 시급 받아오기
            val hourlyText = binding.salary.text.toString()
            // input 창에서 쉬는 시간  받아오기
            val breakTimeText = binding.breakTime.text.toString()

            // 쉬는 시간이 숫자인지 검사
            try {
                breakTime = breakTimeText.toInt()

            } catch (e: NumberFormatException) {
                Log.d("TAG", "Break time must be a number  Provided: '$breakTimeText'")
                return@setOnClickListener
            }
            // 시급이 숫자인지 검사
            try {
                hourly = breakTimeText.toInt()

            } catch (e: NumberFormatException) {
                Log.d("TAG", "hourly must be a number  Provided: '$hourlyText'")
                return@setOnClickListener
            }

            // date, 장소 이름으로 샘플 값으로 스트링 넣었습니다.
            val date = "date"
            val place_name = "placename" // 장소 이름 받아서 넣는 것 구현 하는 중이었습니다.

            // 화면에서 받은 값 들로 새 WorkTime 값 생성 - db 연결에 필요
            // 순서 대로 날짜, 일한 시간, 쉬는 시간, 근무지명, 시급입니다.
            // 위에 일한 시간 구하는 부분 주석 처리 해두어서 빨갛게 뜹니다.
            val newElement = WorkTime(date, workTimeInMillis, breakTime.toString(), place_name, hourly)

            // 데이터베이스에 새로운 WorkTime 객체 삽입 - db 연결에 필요
            dbHelper.use {
                // db 불러오기
                val db = dbHelper.writableDatabase
                // 값 넣기
                val values = ContentValues().apply {
                    put("date", date)
                    put("work_time", workTimeInMillis)
                    put("break_time", breakTime.toString())
                    put("place_name", place_name)
                    put("hourly", hourly)
                }
                //삽입
                db.insert("worktime", null, values)
            }

            // 아답터에 해당 값 추가 - db 연결에 필요
            adapter.addItem(newElement)

            // 디버깅 용 - 없어도 됨
            Log.d("TAG", newElement.toString())


            // 홈 화면으로 돌아가기
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // 신유빈 코드 종료--------------------------------------------------------------------------------------------

        val binding = PartTimeJobRegisDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)
        var tax = intent.getIntExtra("tax",0)
        var startDate: String = ""
        var endDate: String = ""
        var mon = 0
        var tus = 0
        var wed = 0
        var thu = 0
        var fri = 0
        var sat = 0
        var sun = 0

        binding.searchDate.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("근무 기간을 선택해주세요")
                .build()

            dateRangePicker.show(supportFragmentManager, "date_picker")
            dateRangePicker.addOnPositiveButtonClickListener { selection: Pair<Long, Long>? ->
                val calendar = Calendar.getInstance()

                calendar.timeInMillis = selection?.first ?: 0
                startDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
                Log.d("start", startDate)

                calendar.timeInMillis = selection?.second ?: 0
                endDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
                Log.d("end", endDate)

                binding.searchDate.text = dateRangePicker.headerText

                // Calculate the number of days between startDate and endDate
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val start = dateFormat.parse(startDate)
                val end = dateFormat.parse(endDate)

                val diffInMillis = end.time - start.time
                val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt() + 1

                Log.d("difference", diffInDays.toString())

                binding.daysDifference.text = "총 근무 일수: $diffInDays 일"
            }
        }

        fun calculateDayCount(start: Date, end: Date, dayOfWeek: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.time = start
            var count = 0

            while (calendar.time <= end) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                    count++
                }
                calendar.add(Calendar.DATE, 1)
            }

            return count
        }

        fun setButtonState(button: View, flag: Int, colorTrue: Int, colorFalse: Int): Int {
            val newState = if (flag == 0) {
                button.setBackgroundColor(ContextCompat.getColor(button.context, colorTrue))
                1
            } else {
                button.setBackgroundColor(ContextCompat.getColor(button.context, colorFalse))
                0
            }
            return newState
        }
        data class DayCounts(val mon: Int, val tus: Int, val wed: Int, val thu: Int, val fri: Int, val sat: Int, val sun: Int)
        fun updateDayDifference(startDate: String, endDate: String, mon: Int, tus: Int, wed: Int, thu: Int, fri: Int, sat: Int, sun: Int, textView: TextView): DayCounts {
            var monCount = 0
            var tusCount = 0
            var wedCount = 0
            var thuCount = 0
            var friCount = 0
            var satCount = 0
            var sunCount = 0

            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val start = dateFormat.parse(startDate)
                val end = dateFormat.parse(endDate)

                monCount = if (mon == 1) calculateDayCount(start, end, Calendar.MONDAY) else 0
                tusCount = if (tus == 1) calculateDayCount(start, end, Calendar.TUESDAY) else 0
                wedCount = if (wed == 1) calculateDayCount(start, end, Calendar.WEDNESDAY) else 0
                thuCount = if (thu == 1) calculateDayCount(start, end, Calendar.THURSDAY) else 0
                friCount = if (fri == 1) calculateDayCount(start, end, Calendar.FRIDAY) else 0
                satCount = if (sat == 1) calculateDayCount(start, end, Calendar.SATURDAY) else 0
                sunCount = if (sun == 1) calculateDayCount(start, end, Calendar.SUNDAY) else 0

                val totalCount = monCount + tusCount + wedCount + thuCount + friCount + satCount + sunCount
                textView.text = totalCount.toString()
            } else {
                Log.e("Days", "startDate or endDate is not set")
            }

            return DayCounts(monCount, tusCount, wedCount, thuCount, friCount, satCount, sunCount)
        }

        binding.monday.setOnClickListener {
            mon = setButtonState(binding.monday, mon, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }

        binding.tuesday.setOnClickListener {
            tus = setButtonState(binding.tuesday, tus, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }
        binding.wednesday.setOnClickListener {
            wed = setButtonState(binding.wednesday, wed, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }

        binding.thursday.setOnClickListener {
            thu = setButtonState(binding.thursday, thu, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }

        binding.friday.setOnClickListener {
            fri = setButtonState(binding.friday, fri, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }

        binding.saturday.setOnClickListener {
            sat = setButtonState(binding.saturday, sat, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }

        binding.sunday.setOnClickListener {
            sun = setButtonState(binding.sunday, sun, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
        }

        fun getTime(button: Button, context: Context, onTimeSet: () -> Unit) {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                button.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.time)
                onTimeSet()
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        fun calculateTimeDifferenceInMinutes(startTime: String, endTime: String): Int {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val startDate = timeFormat.parse(startTime)
            val endDate = timeFormat.parse(endTime)

            var difference = endDate.time - startDate.time
            // If the difference is negative, it means endDate is on the next day
            if (difference < 0) {
                val oneDayInMillis = 24 * 60 * 60 * 1000
                difference += oneDayInMillis
            }

            val diffMinutes = (difference / (60 * 1000)).toInt()
            return diffMinutes
        }
        fun updateMinuteDifference() {
            val startTime = binding.startTime.text.toString()
            val endTime = binding.endTime.text.toString()

            if (startTime != "00:00" && endTime != "00:00") {
                val timeDifferenceMinutes = calculateTimeDifferenceInMinutes(startTime, endTime)
                binding.timeDifference.text = timeDifferenceMinutes.toString()
            }
        }
        binding.startTime.setOnClickListener {
            getTime(binding.startTime, this) {
                updateMinuteDifference()
            }
        }

        binding.endTime.setOnClickListener {
            getTime(binding.endTime, this) {
                updateMinuteDifference()
            }
        }
        val breakTimeText = binding.breakTime.text.toString()
        val breakTimeMinutes = breakTimeText.toIntOrNull() ?: 0 // 분 단위로 변환
        binding.salary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val salaryText = s.toString()
                val timeDifferenceText = binding.timeDifference.text.toString()
                val dayCounts = updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
                val totalDayCount = dayCounts.mon + dayCounts.tus + dayCounts.wed + dayCounts.thu + dayCounts.fri + dayCounts.sat + dayCounts.sun
                val salary = salaryText.toIntOrNull() ?: return
                val timeDifference = timeDifferenceText.toIntOrNull() ?: return

                val summarySalary = salary * ((timeDifference - breakTimeMinutes) / 60) * totalDayCount
                binding.summarySalary.text = summarySalary.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}