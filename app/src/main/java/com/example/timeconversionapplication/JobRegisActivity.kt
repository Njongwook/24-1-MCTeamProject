package com.example.timeconversionapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timeconversionapplication.databinding.PartTimeJobRegisBinding
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding

class JobRegisActivity : AppCompatActivity() {

    private lateinit var binding: PartTimeJobRegisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PartTimeJobRegisBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        binding.next.setOnClickListener {
            val intent = Intent(this, JobRegisDetailActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}