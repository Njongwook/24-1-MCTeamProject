package com.example.timeconversionapplication

class WorkPlace (val place_name: String,
                 val salary_style: Int,
                 val salary_day: Int,
                 val tax: Int)
{
    override fun toString(): String {
        return "WorkPlace(placeName='$place_name', salaryStyle='$salary_style', salaryDay='$salary_day',tax=$tax)"
    }
}