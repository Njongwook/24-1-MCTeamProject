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
private const val TAG_HOME = "home_fragment"
class JobRegisDetailActivity : AppCompatActivity() {
    private val dbHelper = MyDatabase.MyDBHelper(this)
    private lateinit var binding: PartTimeJobRegisDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // 신유빈 코드 시작--------------------------------------------------------------------------------------------
        binding = PartTimeJobRegisDetailsBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)


        val place_name = intent.getStringExtra("place_name") ?: ""
        val salary_style = intent.getIntExtra("salary_style", 0)
        val sal_day = intent.getStringExtra("salary_day") ?: ""
        val tax = intent.getDoubleExtra("tax", 0.0)
        /*binding.taxexample.text = tax.toString()
        binding.nameexample.text = place_name.toString()
        binding.styleexample.text = salary_style.toString()
        binding.dayexample.text = sal_day.toString()*/


        // WorkTime 타입의 데이터를 조회하여 MutableList<Any>로 변환 - db 연결에 필요
        val getList: MutableList<Any> =
            dbHelper.selectAll("worktime", WorkTime::class.java).toMutableList()

        // 가져온 데이터를 로그로 출력합니다. - 없어도 됨. 디버깅 용
        for (e in getList) {
            Log.d("TAG", e.toString())
        }

        // 어댑터에 데이터를 설정합니다. - db 연결에 필요
        val adapter = MyAdapter(getList)

        var breakTime = 0
        var hourly = 0

//
        // 신유빈 코드 종료--------------------------------------------------------------------------------------------

        super.onCreate(savedInstanceState)
        var startDate: String = ""
        var endDate: String = ""
        var mon = 0
        var tus = 0
        var wed = 0
        var thu = 0
        var fri = 0
        var sat = 0
        var sun = 0

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

        data class DayCounts(
            val mon: Int,
            val tus: Int,
            val wed: Int,
            val thu: Int,
            val fri: Int,
            val sat: Int,
            val sun: Int
        )

        fun updateDayDifference(
            startDate: String,
            endDate: String,
            mon: Int,
            tus: Int,
            wed: Int,
            thu: Int,
            fri: Int,
            sat: Int,
            sun: Int,
            textView: TextView
        ): DayCounts {
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

                val totalCount =
                    monCount + tusCount + wedCount + thuCount + friCount + satCount + sunCount
                textView.text = "총"+totalCount.toString()+"일"
            } else {
                Log.e("Days", "startDate or endDate is not set")
            }

            return DayCounts(monCount, tusCount, wedCount, thuCount, friCount, satCount, sunCount)
        }


        fun getTime(button: Button, context: Context, onTimeSet: () -> Unit) {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                button.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.time)
                onTimeSet()
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
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
                binding.timeDifference.text = timeDifferenceMinutes.toString() // 분 단위로 표시
            }
        }


        var isUpdating = false
        fun updateSummary() {
            val salaryText = binding.salary.text.toString()
            val timeDifferenceText = binding.timeDifference.text.toString()
            val breakTimeText = binding.breakTime.text.toString()

            // Ensure the input values are valid integers
            val salary = salaryText.toIntOrNull() ?: return
            val timeDifference = timeDifferenceText.toIntOrNull() ?: return
            val breakTimeMinutes = breakTimeText.toIntOrNull() ?: 0

            // Calculate the adjusted time difference after deducting break time
            val caltime = timeDifference - breakTimeMinutes
            val adjustedTimeDifference = caltime / 60

            val dayCounts = updateDayDifference(
                startDate,
                endDate,
                mon,
                tus,
                wed,
                thu,
                fri,
                sat,
                sun,
                binding.daysDifference
            )
            val totalDayCount =
                dayCounts.mon + dayCounts.tus + dayCounts.wed + dayCounts.thu + dayCounts.fri + dayCounts.sat + dayCounts.sun

            // Calculate summary salary
            val summarySalary = salary * adjustedTimeDifference * totalDayCount

            // Calculate tax and total income
            val summaryTax: Int
            val totalIncome: Int
            if (tax.toDouble() != 0.0) {
                summaryTax = (summarySalary / tax).toInt()
                totalIncome = summarySalary - summaryTax
            } else {
                // If tax is zero, set summary tax to salary and total income to zero
                summaryTax = 0
                totalIncome = summarySalary - summaryTax
            }

            // Update UI elements with calculated values
            binding.summarySalary.text = summarySalary.toString()
            binding.summaryTax.text = summaryTax.toString()
            binding.totalIncome.text = totalIncome.toString()
        }

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

                // Display the difference in days
                binding.daysDifference.text = "총 근무 일수: $diffInDays 일"

                // Update the summary after calculating the difference in days
                if (isUpdating) return@addOnPositiveButtonClickListener
                isUpdating = true
                updateSummary()
                isUpdating = false
            }
        }
        binding.startTime.setOnClickListener {
            getTime(binding.startTime, this) {
                updateMinuteDifference()
                if (isUpdating) return@getTime
                isUpdating = true
                updateSummary()
                isUpdating = false
            }
        }

        binding.endTime.setOnClickListener {
            getTime(binding.endTime, this) {
                updateMinuteDifference()
                if (isUpdating) return@getTime
                isUpdating = true
                updateSummary()
                isUpdating = false
            }
        }
        binding.monday.setOnClickListener {
            mon = setButtonState(binding.monday, mon, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }
        binding.tuesday.setOnClickListener {
            tus = setButtonState(binding.tuesday, tus, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }

        binding.wednesday.setOnClickListener {
            wed = setButtonState(binding.wednesday, wed, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }

        binding.thursday.setOnClickListener {
            thu = setButtonState(binding.thursday, thu, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }

        binding.friday.setOnClickListener {
            fri = setButtonState(binding.friday, fri, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }

        binding.saturday.setOnClickListener {
            sat = setButtonState(binding.saturday, sat, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }

        binding.sunday.setOnClickListener {
            sun = setButtonState(binding.sunday, sun, R.color.blue4, R.color.blue3)
            updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            if (isUpdating) return@setOnClickListener
            isUpdating = true
            updateSummary()
            isUpdating = false
        }
        binding.salary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                updateSummary()
                isUpdating = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.breakTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                updateSummary()
                isUpdating = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        // 저장 버튼 클릭시
        binding.save.setOnClickListener {
            // Get the text from input fields
            val hourlyText = binding.salary.text.toString().trim()
            val breakTimeText = binding.breakTime.text.toString().trim()
            val dayCounts = updateDayDifference(startDate, endDate, mon, tus, wed, thu, fri, sat, sun, binding.daysDifference)
            val totalDayCount = dayCounts.mon + dayCounts.tus + dayCounts.wed + dayCounts.thu + dayCounts.fri + dayCounts.sat + dayCounts.sun
            val startTime = binding.startTime.text.toString().trim()
            val endTime = binding.endTime.text.toString().trim()

            // Validate if totalDayCount and time fields are set
            if (hourlyText.isNotEmpty() && totalDayCount != 0 && startTime != "00:00" && endTime != "00:00") {
                try {
                    // Validate if break time is a number
                    val breakTime = breakTimeText.toInt()

                    // Validate if hourly wage is a number
                    val hourly = hourlyText.toInt()

                    // Retrieve place name and work time
                    val place_name = intent.getStringExtra("place_name") ?: ""
                    val workTimeInMinutes = binding.timeDifference.text.toString().trim()

                    // Validate if wage is a number
                    val wage = binding.totalIncome.text.toString().trim().toInt()

                    // Create the new WorkTime object
                    val date = "date"
                    val newElement = WorkTime(date, workTimeInMinutes, breakTime.toString(), place_name, hourly, wage)

                    // Insert the new WorkTime object into the database
                    dbHelper.use {
                        val db = dbHelper.writableDatabase
                        val values = ContentValues().apply {
                            put("date", date)
                            put("work_time", workTimeInMinutes)
                            put("break_time", breakTime.toString())
                            put("place_name", place_name)
                            put("hourly", hourly)
                            put("wage", wage)
                        }
                        db.insert("worktime", null, values)
                    }

                    // Add the new element to the adapter
                    adapter.addItem(newElement)

                    // Debugging log
                    Log.d("TAG", newElement.toString())

                    // Return to the main activity
                    val naviIntent = Intent(this, NaviActivity::class.java)
                    naviIntent.putExtra("fragment", TAG_HOME)
                    startActivity(naviIntent)

                } catch (e: NumberFormatException) {
                    // Log and handle the number format exception
                    Log.d("TAG", "Break time, hourly wage, or total wage must be a number. Provided: breakTime='$breakTimeText', hourly='$hourlyText', wage='${binding.totalIncome.text.toString()}'")
                }
            } else {
                Toast.makeText(this, "모든 값을 전부 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}