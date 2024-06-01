package com.example.timeconversionapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timeconversionapplication.databinding.ActivityTipsBinding

class TipsActivity : AppCompatActivity(){
    lateinit var binding : ActivityTipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}