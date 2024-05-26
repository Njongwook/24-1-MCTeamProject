package com.example.timeconversionapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.timeconversionapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reqLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result : ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                Log.d("TAG", "return")
                val intent = result.data
            }
        }


        binding.btn1.setOnClickListener {
            val intent: Intent = Intent(this,CalenderActivity::class.java).apply{
            }
            try{
                reqLauncher.launch(intent)
                // startActivity(intent)
                Log.d("MYTAG", "move to sub")
            }
            catch (e: ActivityNotFoundException){
                Toast.makeText(applicationContext, "No app for action", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btn2.setOnClickListener {
            val intent: Intent = Intent(this,TipsActivity::class.java).apply{
            }
            try{
                reqLauncher.launch(intent)
                // startActivity(intent)
                Log.d("MYTAG", "move to sub")
            }
            catch (e: ActivityNotFoundException){
                Toast.makeText(applicationContext, "No app for action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}