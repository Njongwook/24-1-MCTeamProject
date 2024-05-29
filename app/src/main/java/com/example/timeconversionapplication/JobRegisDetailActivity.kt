package com.example.timeconversionapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding

class JobRegisDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = PartTimeJobRegisDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}