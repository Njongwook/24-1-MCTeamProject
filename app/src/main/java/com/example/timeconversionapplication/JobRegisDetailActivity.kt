package com.example.timeconversionapplication

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
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
    override fun onCreate(savedInstanceState: Bundle?) {

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