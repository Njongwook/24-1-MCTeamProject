package com.example.timeconversionapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timeconversionapplication.databinding.ActivityCalenderBinding

class CalenderActivity : AppCompatActivity(){
    lateinit var binding : ActivityCalenderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}