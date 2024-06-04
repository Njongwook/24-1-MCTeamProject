package com.example.timeconversionapplication

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.timeconversionapplication.databinding.PartTimeJobRegisBinding
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding

class JobRegisActivity : AppCompatActivity() {

    private lateinit var binding: PartTimeJobRegisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PartTimeJobRegisBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        var salaryStyle = 3
        binding.daySalary.setOnClickListener{
            salaryStyle = 1
            binding.daySalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue4))
            binding.weekSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.monthSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            Toast.makeText(applicationContext,"select daySalary",Toast.LENGTH_SHORT).show()
        }
        binding.weekSalary.setOnClickListener{
            salaryStyle = 2
            binding.daySalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.weekSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue4))
            binding.monthSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            Toast.makeText(applicationContext,"select weekSalary",Toast.LENGTH_SHORT).show()
        }
        binding.monthSalary.setOnClickListener{
            salaryStyle = 3
            binding.daySalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.weekSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.monthSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue4))
            Toast.makeText(applicationContext,"select monthSalary",Toast.LENGTH_SHORT).show()
        }


        binding.taxText.isEnabled = false
        binding.taxSpinner.isEnabled = false
        var taxStyle = 0
        binding.taxCheck.setOnCheckedChangeListener { _, isChecked ->
            binding.taxSpinner.isEnabled = isChecked
            if (!isChecked) {
                binding.taxText.isEnabled = false
            } else {
                taxStyle = 1
                binding.taxText.isEnabled = true
            }
        }

        binding.taxSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) { // '직접입력'이 선택된 경우
                    binding.tax.isEnabled = true
                } else {
                    binding.tax.isEnabled = false
                    taxStyle = 1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작 (필요한 경우 구현)
            }
        }

        binding.insuranceText.isEnabled = false
        binding.insuranceSpinner.isEnabled = false
        var insuranceStyle = 1
        binding.insuranceCheck.setOnCheckedChangeListener { _, isChecked ->
            binding.insuranceSpinner.isEnabled = isChecked
            if (!isChecked) {
                binding.insuranceText.isEnabled = false
            } else {
                insuranceStyle = 1
                binding.insuranceText.isEnabled = true
            }
        }

        binding.insuranceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) { // '직접입력'이 선택된 경우
                    binding.insurance.isEnabled = true
                } else {
                    binding.insurance.isEnabled = false
                    insuranceStyle = 1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작 (필요한 경우 구현)
            }
        }

        val taxSpinner : Spinner = binding.taxSpinner
        val insuranceSpinner: Spinner = binding.insuranceSpinner // 스피너 참조 추가

        val taxAdpter = ArrayAdapter.createFromResource(this, R.array.tax_array, android.R.layout.simple_spinner_item)
        taxAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taxSpinner.adapter = taxAdpter

        val insuranceAdapter = ArrayAdapter.createFromResource(this, R.array.insurance_array, android.R.layout.simple_spinner_item)
        insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        insuranceSpinner.adapter = insuranceAdapter


        binding.next.setOnClickListener {
            val input = binding.days.text.toString()
            val number = input.toIntOrNull()
            if (number != null) {
                if (number > 31 || number < 1) {
                    Toast.makeText(this, "1부터 31까지의 숫자만 입력하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    intent.putExtra("tax",taxStyle)
                    intent.putExtra("insurance",insuranceStyle)
                    val intent = Intent(this, JobRegisDetailActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "유효한 숫자를 입력하세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}